package com.xan2063

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.test.assertEquals

class Day15_b {
//    val input = File("testinput.txt").readLines()
    val input = File("input15.txt").readLines()
    private val table: List<Int> =
        input.map { it.map { elem -> elem.digitToInt() } }.flatten()

//    private val bigtable: List<Int> =
//        table.chunked(width).
    private val origwidth = input.first().length
    private val origheight = input.size
    private val origsize = origheight*origwidth


    private val width = input.first().length*5
    private val height = input.size*5
    private val size = width * height

    @Test
    fun part2(){

        val result = dijkstra().toSortedMap()
//        val path = shortestPath(result,0,size-1)
//        println(path)


//        println(result)


    }

    val rightValues = arrayOf(1)
    val leftValues = arrayOf(-1)

    fun Int.addLeft() = if (this % width != 0) leftValues.map { it + this }.toTypedArray() else emptyArray()
    fun Int.addRight() =
        if (this % width != width - 1) rightValues.map { it + this }.toTypedArray() else emptyArray()

    fun Int.addAbove() = this - width
    fun Int.addBelow() = this + width

    fun Int.getAdjacentsIndices(): List<Int> {
        with(this) {
            return listOf(*addLeft(), *addRight(), addAbove(), addBelow()).filter { it in 0 until size }
        }
    }

    @Test
    fun part(){
        val list = listOf<Int>(8,9,9,8)
        val result = (0..99).map { list.getWeight2(it,10,10,2,2) }.chunked(10).forEach { println(it.joinToString()) }


    }

    fun List<Int>.getWeight2(index:Int,widht:Int, height:Int, orighe:Int,origwe:Int ): Int {
        val row = index / widht
        val column = index%widht
        val rowfactor = floor(row.toDouble() / origwe)
        val columnfactor = floor(column.toDouble() /orighe)

        val origRow = row %2
        val origColumn = column %2

        val origIndex = origRow * origwe + origColumn
        val value = this[origIndex] + rowfactor.toInt() + columnfactor.toInt()
        return if(value>9) value-9 else value
    }

    fun List<Int>.getWeight(index:Int): Int {
        val row = index / width
        val column = index%width
        val rowfactor = floor(row.toDouble() /origheight)
        val columnfactor = floor(column.toDouble() /origwidth)

        val origRow = row %origheight
        val origColumn = column %origwidth

        val value = table[origRow*origwidth+ origColumn] + rowfactor.toInt() + columnfactor.toInt()
        return if(value>9) value-9 else value
    }


    fun dijkstra(): Map<Int, Int?> {
        val S: MutableSet<Int> = mutableSetOf() // a subset of vertices, for which we know the true distance

        val delta = (0 until size).map { i -> i to Int.MAX_VALUE }.toMap().toMutableMap()
        delta[0] = 0
        val vertices = (0 until size).toSet()
        val previous: MutableMap<Int, Int?> =  (0 until size).associateWith { null }.toMutableMap()

        while (S != vertices) {
            val v: Int = delta
                .filter { !S.contains(it.key) }
                .minByOrNull { it.value }!!
                .key

            v.getAdjacentsIndices().filter { it !in S }.forEach { neighbor ->
                val newPath = delta.getValue(v) + table.getWeight(neighbor)

                if (newPath < delta.getValue(neighbor)) {
                    delta[neighbor] = newPath
                    previous[neighbor] = v
                }
            }
            println(v)
            S.add(v)
        }
//        println(delta)
        println("last element ${delta.toList().takeLast(5)}")
        println("last element ${delta[size-1]}")
        println("last element ${delta[size-5]}")
        return previous.toMap()
    }

    fun <T> shortestPath(shortestPathTree: Map<T, T?>, start: T, end: T): List<T> {
        fun pathTo(start: T, end: T): List<T> {
            if (shortestPathTree[end] == null) return listOf(end)
            return listOf(pathTo(start, shortestPathTree[end]!!), listOf(end)).flatten()
        }

        return pathTo(start, end)
    }


}


