package com.xan2063

import org.junit.jupiter.api.Test
import java.io.File

class Day15 {
//    val input = File("testinput.txt").readLines()
    val input = File("input15.txt").readLines()
    private val table: List<Int> =
        input.map { it.map { elem -> elem.digitToInt() } }.flatten()
    private val width = input.first().length
    private val height = input.size
    private val size = width * height

    @Test
    fun part1(){

        val result = dijkstra().toSortedMap()
        println(result)


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


    data class Graph<T>(
        val vertices: Set<T>,
        val edges: Map<T, Set<T>>,
        val weights: Map<Pair<T, T>, Int>
    )


    fun dijkstra(): Map<Int, Int?> {
        val S: MutableSet<Int> = mutableSetOf() // a subset of vertices, for which we know the true distance

        val delta = table.mapIndexed { index, i -> index to Int.MAX_VALUE }.toMap().toMutableMap()
        delta[0] = 0
        val vertices = (0 until size).toSet()
        val previous: MutableMap<Int, Int?> = table.associateWith { null }.toMutableMap()

        while (S != vertices) {
            val v: Int = delta
                .filter { !S.contains(it.key) }
                .minByOrNull { it.value }!!
                .key

            v.getAdjacentsIndices().minus(S).forEach { neighbor ->
                val newPath = delta.getValue(v) + table[neighbor]

                if (newPath < delta.getValue(neighbor)) {
                    delta[neighbor] = newPath
                    previous[neighbor] = v
                }
            }

            S.add(v)
        }
        println(delta)
        println("last element ${delta[size-1]}")
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


