package com.xan2063

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.math.abs
import kotlin.math.floor

class Day7 {
    @Test
    fun part1(){
        val positions = File("input7.txt").readLines()[0].split(',').map (String::toInt)
        val average = positions.average()
        val averageInt = floor(average).toInt()
        val fuelConsumption = ((averageInt-200)..(averageInt+10)).map { avg-> positions.map { abs(avg - it) }.sum() to avg }
        fuelConsumption.forEach{println(it)}

        println(fuelConsumption)


        println(average)
        println(positions.maxOrNull())
        println(positions.minOrNull())
    }

    @Test
    fun part2(){
        val positions = File("input7.txt").readLines()[0].split(',').map (String::toInt)
        val average = positions.average()
        val averageInt = floor(average).toInt()
        println(average)
        println(positions.maxOrNull())
        println(positions.minOrNull())
        val fuelConsumption = ((averageInt-200)..(averageInt+10)).map {
                avg-> positions.map {
            val difference = abs(avg - it)
            difference*(difference+1)/2

                }.sum() to avg
        }
        println()
        fuelConsumption.forEach{println(it)}

        println(average)
        println(positions.maxOrNull())
        println(positions.minOrNull())
    }
}