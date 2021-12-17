package com.xan2063

import org.junit.jupiter.api.Test
import java.io.File

class Day13 {

    private val input = File("input13.txt").readLines()
    val points = input.takeWhile { it.isNotBlank() }.map { it.split(',')}.map { it[0].toInt() to it[1].toInt() }


    var width = points.maxOfOrNull { it.first }!!+1
    val height = points.maxOfOrNull { it.second }!!+1
    val size = width * height
    var table = MutableList(size) { '.' }



    val setPoint={(x:Int, y:Int):Pair<Int,Int>->
        table[x+y*width]='#'
    }

    fun foldx(x:Int): List<Char> {
        val adaptedwidth = 2*x+1
        val rows = table.chunked(width)
        val adaptedrows = rows.map { if(it.size<adaptedwidth) { it+ (".".repeat(adaptedwidth-it.size)).toCharArray().toList()} else it}

        val mirroredPart =
            adaptedrows.flatten().filterIndexed { index, list -> index % adaptedwidth > x }.chunked(x).map { it.reversed() }.flatten()


        val firstPart = adaptedrows.flatten().filterIndexed{ index, list -> index%adaptedwidth<x }

        printTable(mirroredPart, x)
        printTable(firstPart, x)

        val newTable = firstPart.zip(mirroredPart){a, b -> if(a=='#'||b=='#')'#' else '.'  }
        width=x
        return newTable

    }
    fun foldy(y:Int): List<Char> {
        val mirroredPart = table.drop((y+1)*width).chunked(width).reversed().flatten()
        val newTable = table.zip(mirroredPart){a, b -> if(a=='#'||b=='#')'#' else '.'  }
        return newTable
    }

    fun printTable(tabletoPrint:List<Char>, width:Int) {
        val red = "\u001b[31m"
        val reset = "\u001b[0m"
        val rows = tabletoPrint.chunked(width).map {
            it.map { print((if (it =='#') red else "") + it + reset) }
            println()
        }
        println("------")
    }

    fun countMarks() = table.count { it=='#' }

    @Test
    fun part1(){
        points.forEach { setPoint(it) }
        val dropWhile = input.dropWhile { it.isNotBlank() }.drop(1)
        val folds = dropWhile.map { it.split('=')}.map { it[0].last() to it[1].toInt() }
        folds.forEach { (direction, row)->
            table = when(direction){
                'x'->foldx(row).toMutableList()
                else ->foldy(row).toMutableList()
            }
//            printTable(table, width)
        }
        printTable(table, width)
        println(countMarks())








    }
}