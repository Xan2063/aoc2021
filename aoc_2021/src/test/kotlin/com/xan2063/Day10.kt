package com.xan2063

import org.junit.jupiter.api.Test
import java.io.File
import java.util.*

class Day10 {

    fun Char.isMatching(other:Char): Boolean {
        return (this=='('&&other==')')||
                (this=='['&&other==']')||
        (this=='{'&&other=='}')||
        (this=='<'&&other=='>')
    }

    @Test
    fun part1(){
        val input = File("input10.txt").readLines()
        val searched = input.map {
            val stack = Stack<Char>()
            val res = it.dropWhile {
                when (it) {
                    '(', '[', '{', '<' -> stack.push(it).let { true }
                    else -> {
                        val lastopening = stack.pop()
                        lastopening.isMatching(it)
                    }

                }

            }
            res

        }
        val result = searched.filter { it.isNotEmpty() }
        val result2 = result.map { it[0] }.map {
            when(it){
                ')'->3
                ']'->57
                '}'->1197
                '>'->25137
                else -> 0
            }
        }.sum()
        println(searched)
        println(result)
        println(result2)


    }

    @Test
    fun part2(){
        val input = File("input10.txt").readLines()
        val searched = input.map {
            val stack = Stack<Char>()
            val res = it.dropWhile {
                when (it) {
                    '(', '[', '{', '<' -> stack.push(it).let { true }
                    else -> {
                        val lastopening = stack.pop()
                        if(!lastopening.isMatching(it)){
                            stack.clear()
                        }
                        lastopening.isMatching(it)

                    }

                }
            }
            if(res.isNotEmpty())
                Stack<Char>()
            stack

        }
        val result = searched.filter { it.isNotEmpty() }
        val rep = result.map { it.fold(""){acc, c ->acc+c  }.reversed()}
        rep.forEach { println(it) }
//        println("rep $rep")

        val sorted = rep.map {
            it.fold(0L) { acc, c ->
                acc * 5 + when (c) {
                    '(' -> 1
                    '[' -> 2
                    '{' -> 3
                    '<' -> 4
                    else -> 0
                }
            }
        }.sorted()

        val i = rep.size / 2
        val result2= sorted[i]
        println(sorted)
        println(searched)
        println(result)
        println(result2)


    }
}