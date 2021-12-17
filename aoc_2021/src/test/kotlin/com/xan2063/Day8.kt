package com.xan2063

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.math.pow
import kotlin.test.assertEquals

class Day8 {

    @Test
    fun part1(){
        val input = File("input8.txt").readLines().map { it.split('|') }
        val outputValues = input.map { it[1].split(' ').map { it.trim() } }
        val result = outputValues.flatten().groupingBy { it.length }.eachCount().filterKeys { it in listOf(2,3,4,7) }
        println(result)
        println(result.values.sum())
    }

    fun Map<Int, List<String>>.getOne() = this.getValue(2).first()
    fun Map<Int, List<String>>.getSeven() = this.getValue(3).first()
    fun Map<Int, List<String>>.getFour() = this.getValue(4).first()
    fun Map<Int, List<String>>.getEight() = this.getValue(7).first()
    fun Map<Int, List<String>>.getSix() = this.getValue(6).first{ !this.getOne().all{c->c in it} }
    fun Map<Int, List<String>>.getNine() = this.getValue(6).first{ this.getFour().all{c->c in it } }
    fun Map<Int, List<String>>.getZero() = this.getValue(6).first{!this.getFour().all{c->c in it}&&this.getOne().all{c->c in it} }
    fun Map<Int, List<String>>.getThree() = this.getValue(5).first{ this.getOne().all{c-> c in it} }
    fun Map<Int, List<String>>.getFive() = this.getValue(5).first{
        this.getFour().filter { it !in this.getOne() }.all{c->c in it} }
    fun Map<Int, List<String>>.getTwo() = this.getValue(5).first{

        val b = this.getFour().filter { it !in this.getOne() }
        !b.all { c -> c in it }&& !this.getOne().all{c->c in it}
    }

    @Test
    fun TestSix(){
        val table = mapOf(6 to listOf("cdfgeb"), 2 to listOf("ab")).getSix()
        val test =!"ab".all{c->c in "acde"}
        assertEquals("cdfgeb", table)
    }

    @Test
    fun part2(){
        val input = File("input8.txt").readLines().map { it.replace("| ","").split(' ').map { it.trim().toCharArray().sorted().joinToString("") } }
        val outputValues = input.map { it.drop(10).take(4) }
        val sequence = input.map { it.take(10) }

        val test = "abc"
        val res = test.filter { it !in "ab" }
        println(res)




        val decodetables = sequence.map {
            val lettersbyLength = it.groupBy { it.length }
            with(lettersbyLength){
                mapOf(
                    getOne() to 1,
                    getSeven() to 7,
                    getFour() to 4,
                    getEight() to 8,
                    getSix() to 6,
                    getNine() to 9,
                    getZero() to 0,
                    getThree() to 3,
                    getFive() to 5,
                    getTwo() to 2
                )
            }
            mapOf(
                lettersbyLength.getOne() to 1,
                lettersbyLength.getSeven() to 7,
                lettersbyLength.getFour() to 4,
                lettersbyLength.getEight() to 8,
                lettersbyLength.getSix() to 6,
                lettersbyLength.getNine() to 9,
                lettersbyLength.getZero() to 0,
                lettersbyLength.getThree() to 3,
                lettersbyLength.getFive() to 5,
                lettersbyLength.getTwo() to 2
            )

        }
        val output = outputValues.zip(decodetables).map { (values, table)-> values.map {
            table.getValue(it) }.foldIndexed(0L){index, acc, i -> acc+(i*10.0.pow(3-index)).toLong() } }
        println(output)
        println(output.sum())

    }
}