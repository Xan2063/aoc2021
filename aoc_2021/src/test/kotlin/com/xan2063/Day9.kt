package com.xan2063

import org.junit.jupiter.api.Test
import java.io.File
import java.util.*
import kotlin.collections.ArrayDeque


class lavavents(val rows: List<String>) {
    private val table: List<Int> =
        rows.map { it.map { elem -> elem.digitToInt() } }.flatten()
    private val width = rows.first().length
    private val height = rows.size
    private val size = width * height

    fun getElement(row: Int, column: Int): Int? {
        val index = row * width + column
        return if (index in 0..size) table[row * width + column] else null
    }


    fun getRow(row: Int): List<Int> {
        return table.filterIndexed { index, i -> index < (row + 1) * width && index > row * width }
    }

    fun getColumn(column: Int): List<Int> {
        return table.filterIndexed { index, _ -> index % width == column }
    }

    fun Int.elementAbove() = table.elementAtOrNull(this - width)
    fun Int.elementBelow() = table.elementAtOrNull(this + width)
    fun Int.elementLeft() = if (this % 10 == 0) null else table.elementAtOrNull(this - 1)
    fun Int.elementRight() = if (this % 10 == 9) null else table.elementAtOrNull(this + 1)

    fun Int.indexAbove() = this - width
    fun Int.indexBelow() = this + width
    fun Int.indexLeft() = if (this % width == 0) null else this - 1
    fun Int.indexRight() = if (this % width == width - 1) null else this + 1

    fun getAdjacentsIndices(index: Int): List<Int> {

        return listOfNotNull(
            index.indexAbove(),
            index.indexBelow(),
            index.indexLeft(),
            index.indexRight()
        ).filter { it in 0 until size }
    }

    fun getAdjacentsElements(index: Int): List<Int> {

        return listOfNotNull(index.elementAbove(), index.elementBelow(), index.elementLeft(), index.elementRight())
    }

    fun marklocalLows() {
        table.forEachIndexed { index, i ->
            val adjacentElements = getAdjacentsElements(index)
            if (adjacentElements.all { it > i }) markIndex(index, 1)
        }
    }

    fun fillLocalLows() {
        var basinNr = 1
//        val queue = ArrayDeque<Int>()
//        table.forEachIndexed { index, i ->
//            val adjacentElements = getAdjacentsElements(index)
//            if (adjacentElements.all { it > i }) {
//                markIndex(index, basinNr++)
//                queue.add(index)
//            }
//        }

        val localLows = table.mapIndexed { index, i ->
            val adjacentElements = getAdjacentsElements(index)
            if (adjacentElements.all { it > i }) {
                index
            }
            else 0
        }.filter { it!=0 }

        localLows.forEachIndexed { index, i ->
            if(marks[i]!= 0) return@forEachIndexed
            markIndex(i, index+1)
            fill(listOf(i))
        }
//        mergeIslands()

    }

    private fun mergeIslands() {

        val result = marks.foldIndexed(mutableListOf<Pair<Int, Int>>())


            { index,acc, i ->
            getAdjacentsIndices(index).forEach {
                if (i != 0 && marks[it] != 0 && marks[it] != i) {
                    acc.add(marks[it] to i)
                }
                else
                    acc
            }
                acc
        }.toMap()
        marks.map { if(result.contains(it)) result[it] else it }
    }




tailrec fun fill(nextElements: List<Int>) {
    if (nextElements.isEmpty()) return

    val newElements = nextElements.flatMap { i: Int ->
        val adj = getAdjacentsIndices(i)
        val notBasin = adj.filter { table[it] < 9 && marks[it] == 0 }
        notBasin.also { newElems -> newElems.map { markIndex(it, marks[i]) } }
    }
    fill(newElements)
}

private val marks = MutableList(width * height) { 0 }

fun markIndex(index: Int, basinNr: Int) {

    marks[index] = basinNr
}

fun getValue(): Int {
    return table.filterIndexed { index, i -> marks[index] > 0 }.map { it + 1 }.sum()
}

fun getValue2(): Int {
    val sorted = marks.groupingBy { it }.eachCount().values.sorted().reversed()
    println(sorted)
    return sorted.drop(1).take(3).reduce { acc, i -> acc * i }
}

fun print() {
    val red = "\u001b[31m"
    val reset = "\u001b[0m"
    val rows = table.zip(marks).chunked(width).map {
        it.map { print((if (it.second > 0) red else "") + "${"%4d".format(it.first)}" + reset) }
        println()
    }

    val rows2 = marks.chunked(width).map {
        it.map { print("${"%4d".format(it)}" + reset) }
        println()
    }

    println("------")
}

}

class Day9 {
    @Test
    fun part1() {
        val input = File("testinput.txt").readLines()
        val vents = lavavents(input)
        vents.marklocalLows()
        vents.print()
        println(vents.getValue())

    }

    @Test
    fun part2() {
        val input = File("input9.txt").readLines()
        val vents = lavavents(input)
        vents.fillLocalLows()
        vents.print()
        println(vents.getValue2())

    }
}