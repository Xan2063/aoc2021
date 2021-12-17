package com.xan2063

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Day12 {

    fun String.IsStartOrEnd()  = this =="start"||this=="end"
    fun String.isEnd()  = this=="end"
    fun String.isStart()  = this=="start"

    val input = File("input12.txt").readLines()
    val connectionmap = input.map { it.split('-')}
        .flatMap {
            listOfNotNull(it[0] to it [1],
                it[1] to it[0]  )
        }.groupBy({it.first}, {it.second})


tailrec fun findPathes(currentPath:List<String>): List<List<String>> {
        val currentCave= currentPath.last()
        if(currentCave.isEnd()) return listOf(currentPath)
        val alternatives = connectionmap.getValue(currentCave).filter { it.all(Char::isUpperCase) || it !in currentPath }
        return alternatives.flatMap { findPathes(currentPath+it) }

    }

    fun List<String>.containsLowerCaseDupe() = this.filter {it.all(Char::isLowerCase) }.groupingBy { it }.eachCount().any { it.value ==2 }


    @Test
    fun lowerCaseDupeTest(){
        assertTrue { listOf("ab","cd","ab").containsLowerCaseDupe() }

        assertFalse { listOf("ab","cd","AB").containsLowerCaseDupe() }
        assertTrue { listOf("ab","cd","AB","ab").containsLowerCaseDupe() }
        assertTrue { listOf("ab","cd","cd","DE").containsLowerCaseDupe() }
    }

    @Test
    fun part1() {
        val pathes = findPathes(listOf("start"))
        pathes.forEach { it.joinToString(",").also { println(it) } }
        println(pathes.count())
    }

    tailrec fun findPathes2(currentPath:List<String>): List<List<String>> {
        val currentCave= currentPath.last()
        if(currentCave.isEnd()) return listOf(currentPath)
        val alternatives = connectionmap.getValue(currentCave).filter { (it.all(Char::isUpperCase) || it !in currentPath || !currentPath.containsLowerCaseDupe()) && !it.isStart() }
        return alternatives.flatMap { findPathes2(currentPath+it) }

    }


    @Test
    fun part2() {
        val pathes = findPathes2(listOf("start"))
        pathes.forEach { it.joinToString(",").also { println(it) } }
        println(pathes.count())
    }

}