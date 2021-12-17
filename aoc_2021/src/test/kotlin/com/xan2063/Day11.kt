package com.xan2063

import org.junit.jupiter.api.Test
import java.io.File
import java.util.*

class Day11 {

    class octopusses(val rows: List<String>) {
        private var table: MutableList<Int> =
            rows.flatMap { it.map { elem -> elem.digitToInt() } }.toMutableList()
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

        val rightValues = arrayOf(1, 1 + width, 1 - width)
        val leftValues = arrayOf(-1, -1 + width, -1 - width)

        fun Int.addLeft() = if (this % width != 0) leftValues.map { it + this }.toTypedArray() else emptyArray()
        fun Int.addRight() =
            if (this % width != width - 1) rightValues.map { it + this }.toTypedArray() else emptyArray()

        fun Int.addAbove() = this - width
        fun Int.addBelow() = this + width

        fun Int.getAdjacentsIndicesWithDiagonals(): List<Int> {
            with(this) {
                return listOf(*addLeft(), *addRight(), addAbove(), addBelow()).filter { it in 0 until size }
            }
        }

        fun increaseEnergyLevel() {
            table = table.map { it + 1 }.toMutableList()
        }

        fun flash():Int{

            val flashStack = Stack<Int>()
            val flashing = table.foldIndexed(emptyList<Int>()) { index, acc, i -> if (i > 9) acc + index else acc }.toMutableList()
            flashStack.addAll(flashing)

            while (flashStack.isNotEmpty()) {
                val adjacentsIndicesWithDiagonals = flashStack.pop().getAdjacentsIndicesWithDiagonals()
                val newFlashers = adjacentsIndicesWithDiagonals.onEach { table[it]=table[it] + 1 }
                    .filter { table[it] > 9 && it !in flashing }.also { flashing.addAll(it) }
                flashStack.addAll(newFlashers)

            }
            return flashing.count()
        }

        fun resetFlashers(){
            table = table.map { if (it>9) 0 else it }.toMutableList()
        }

        fun print() {
            val red = "\u001b[31m"
            val reset = "\u001b[0m"
            val rows = table.chunked(width).map {
                it.map { print((if (it > 9) red else "") + "${"%4d".format(it)}" + reset) }
                println()
            }

            println("------")
        }

        fun allFlashin()=
            table.all { it==0 }

    }


        @Test
        fun part1() {
            val input = File("input11.txt").readLines()


            val octopusses = octopusses(input)

            val result = (1..100).fold(0) { acc,i->
                octopusses.increaseEnergyLevel()
                val flashers = octopusses.flash()
                octopusses.print()
                octopusses.resetFlashers()
                acc+flashers




            }
            println(result)


        }
    @Test
    fun part2() {
        val input = File("input11.txt").readLines()


        val octopusses = octopusses(input)
        var i = 0
        val result = generateSequence { i++





        }.map {
            octopusses.increaseEnergyLevel()
            val flashers = octopusses.flash()
//            octopusses.print()
            octopusses.resetFlashers()
             }.takeWhile { !octopusses.allFlashin() }

        octopusses.print()
        println(result.last())
        println(i)


    }

    }