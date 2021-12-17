package com.xan2063

import org.junit.jupiter.api.Test
import java.io.File

class Day17 {

    val regex = Regex("target area: x=(\\d*)\\.\\.(\\d*), y=(-?\\d*)\\.\\.(-?\\d*)")

    val input = File("testinput.txt").readLines()[0]
    val matches = regex.find(input)

    @Test
    fun part1() {
        val x = matches!!.groups[1]!!.value.toInt()..matches!!.groups[2]!!.value.toInt()
        val y = matches!!.groups[3]!!.value.toInt()..matches!!.groups[4]!!.value.toInt()
        println(x)
        println(y)





        val values = generateSequence(1) {
            it + 1

        }.map {
            val results = (it downTo 0 step 1).fold(0 to 0) { acc, i ->
                if (acc.second + i < x.last) {
                    acc.first + 1 to acc.second + i
                } else acc
            }
            Triple(results.first, results.second, it)
        }.filter { it.second > x.first && it.second < x.last }.take(5).toList()

        val candidate = values.first()


        val speeds = generateSequence(0) { it + 1 }.map { initspeed -> (0..candidate.first).fold(initspeed to 0){
                (speed,height), _-> speed-1 to height+speed } to initspeed}.dropWhile { pair -> pair.first.second>y.first }.take(15).toList()
        println(candidate)
        println(speeds)



    }
}