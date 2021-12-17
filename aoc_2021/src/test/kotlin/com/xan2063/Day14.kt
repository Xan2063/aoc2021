package com.xan2063

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.system.measureTimeMillis

class Day14 {

    //    val input = File("testinput.txt").readLines()
    val input = File("input14.txt").readLines()

    val polymerTemplate = input[0]
    val insertionRules = input.drop(2).map { it.split("->").map { it.trim() } }.map { it[0] to it[1] }.toMap()


    @Test
    fun part1() {
        val result = (1..40).fold(polymerTemplate) { acc, _ ->
            val result = acc.windowed(2).fold("") { acc, s ->
                acc + s[0] + insertionRules.getValue(s)
            } + acc.last()
//                println(result)
            println(result.length)
            result
        }

        val countbyLetter = result.groupingBy { it }.eachCount()
        val sorted = countbyLetter.entries.sortedBy { it.value }
        println(sorted)
        println(sorted.last().value - sorted.first().value)


    }

    @Test
    fun part2() {
//        println(insertionRules)
        val time = measureTimeMillis {
        val initialGroups = polymerTemplate.windowed(2).groupingBy { it }.eachCount().mapValues { it.value.toLong() }
//        println(initialGroups)
        val groupMapping = insertionRules.map { (key, value) -> key to listOf(key[0] + value, value + key[1]) }.toMap()

        val letters =
            polymerTemplate.groupingBy { it.toString() }.eachCount().mapValues { it.value.toLong() }.toMutableMap()

            (1..40).fold(initialGroups) { acc, _ ->
                acc.map { (key, value) -> insertionRules[key]!! to value }
                    .forEach { letters[it.first] = letters.getOrDefault(it.first, 0L) + it.second }
                acc.flatMap { (atom, count) -> groupMapping[atom]!!.map { it to count } }
                    .groupBy { it.first }
                    .mapValues { (_, l) -> l.sumOf { it.second } }
            }


        val map = letters.map { it.value }
        }
        println("${time} ms")
//        println(map.maxOrNull()!! - map.minOrNull()!!)
//        println(letters)

    }
}