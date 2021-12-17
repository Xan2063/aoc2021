package com.xan2063


import org.junit.jupiter.api.DisplayNameGeneration
import org.junit.jupiter.api.Test
import java.io.Console
import java.io.File
import kotlin.math.*

class HelloTest {
    @Test
    fun test() {
        val zipWithNext = File("input.txt").readLines().map { it.toInt() }.zipWithNext()
        val count = zipWithNext.count { it.first < it.second }
        println(count)
    }

    @Test
    fun test2() {
        val zipWithNext = File("input.txt").readLines().map { it.toInt() }.windowed(3, 1).map { it.sum() }.zipWithNext()
        val count = zipWithNext.count { it.first < it.second }
        println(count)
    }

    data class Command(val command: String, val ammount: Int) {

    }

    data class Postion(val depth: Int, val position: Int)

    @Test
    fun test3() {
        val finalPosition = File("input2.txt").readLines().map { it.split(' ') }.map { Command(it[0], it[1].toInt()) }
            .fold(Postion(0, 0)) { acc, command ->
                when (command.command) {
                    "forward" -> Postion(acc.depth, acc.position + command.ammount)
                    "up" -> Postion(acc.depth - command.ammount, acc.position)
                    else -> Postion(acc.depth + command.ammount, acc.position)
                }
            }
        println(finalPosition)
        println(finalPosition.position * finalPosition.depth)
    }

    data class Postion2(val depth: Int, val position: Int, val aim: Int)

    @Test
    fun test4() {
        val finalPosition = File("input2.txt").readLines().map { it.split(' ') }.map { Command(it[0], it[1].toInt()) }
            .fold(Postion2(0, 0, 0)) { acc, command ->
                when (command.command) {
                    "forward" -> Postion2(
                        acc.depth + acc.aim * command.ammount,
                        acc.position + command.ammount,
                        acc.aim
                    )
                    "up" -> Postion2(acc.depth, acc.position, acc.aim - command.ammount)
                    else -> Postion2(acc.depth, acc.position, acc.aim + command.ammount)
                }
            }
        println(finalPosition)
        println(finalPosition.position * finalPosition.depth)
    }

    @Test
    fun test5() {

        val lines = File("testinput.txt").readLines()

//        val colums3 = lines.(initial){acc, s -> acc.mapIndexed{index, i -> i+s[index].digitToInt() }}
        val initial = List<Int>(lines.first().length) { 0 }
        val colums = lines.fold(initial) { acc, s -> acc.mapIndexed { index, i -> i + s[index].digitToInt() } }
        val gamma = colums.map { if (it > lines.size / 2) 1 else 0 }.joinToString("").toInt(2)
        val eps = colums.map { if (it > lines.size / 2) 0 else 1 }.joinToString("").toInt(2)
        println(colums)
        println(gamma)
        println(eps)
        println(gamma * eps)
    }

    @Test
    fun test6() {

        val lines = File("input3.txt").readLines()
        val oxygen_generator_rating = filterValues(lines, 0).toInt(2)
        val CO2_scrubber_rating = filterValues2(lines, 0).toInt(2)
        println(oxygen_generator_rating)
        println(CO2_scrubber_rating)

        println(oxygen_generator_rating * CO2_scrubber_rating)
    }

    tailrec fun filterValues(lines: List<String>, idx: Int): String {
        if (lines.size == 1) return lines.first()
        val mcb = getColumnMostCommonBit(lines, idx)

        println(lines)
        println(mcb)
        return filterValues(lines.filter { Character.getNumericValue(it[idx]) == mcb }, idx + 1)
    }

    tailrec fun filterValues2(lines: List<String>, idx: Int): String {
        if (lines.size == 1) return lines.first()
        val mcb = getLEastCommonBit(lines, idx)
        return filterValues2(lines.filter { Character.getNumericValue(it[idx]) == mcb }, idx + 1)
    }

    fun getColumnMostCommonBit(lines: List<String>, idx: Int): Int {
        val columValue = lines.fold(0) { acc, s -> acc + s[idx].toInt() - 48 }
        return if (columValue >= (lines.size + 1) / 2) 1 else 0
    }

    fun getLEastCommonBit(lines: List<String>, idx: Int): Int {

        val columValue = lines.fold(0) { acc, s -> acc + s[idx].toInt() - 48 }
        return if (columValue >= (lines.size + 1) / 2) 0 else 1
    }


    class BingoBoard(val rows: List<String>) {
        private val table: List<Int> =
            rows.map { it.split("\\s+".toRegex()).filter { !it.isNullOrEmpty() }.map { elem -> elem.toInt() } }
                .flatten()

        fun getElement(row: Int, column: Int): Int {
            return table[row * size + column]
        }

        fun getRow(row: Int): List<Int> {
            return table.filterIndexed { index, i -> index < (row + 1) * size && index > row * size }
        }

        fun getColumn(column: Int): List<Int> {
            return table.filterIndexed { index, _ -> index % size == column }
        }

        private val size = 5

        private val marks = MutableList<Boolean>(size * size) { false }

        fun markNumber(number: Int) {
            val index = table.indexOf(number)
            if (index != -1) {
                marks[index] = true
            }
        }

        fun isSolved(): Boolean {
            val rows = marks.chunked(size)
            val hasFullRows = rows.any { it.all { it } }
            val columns = marks.withIndex().groupBy { it.index % size }.map { it.value.map { it.value } }
            val hasFullColumns = columns.any { it.all { it } }
            return hasFullRows || hasFullColumns
        }

        fun getValue(): Int {
            return table.filterIndexed { index, i -> !marks[index] }.sum()
        }

        fun print() {
            val red = "\u001b[31m"
            val reset = "\u001b[0m"
            val rows = table.zip(marks).chunked(size).map {
                it.map { print((if (it.second) red else "")+ "${ "%4d".format(it.first)}"+reset) }
                println()
            }


// Resets previous color codes



            println("------")
        }

    }

    @Test
    fun test7() {

        val lines = File("input4.txt").readLines()
        val bingoNumbers = lines.first().split(',').map { it.toInt() }

        val boards = lines.drop(1).chunked(6).map { it.drop(1) }.map { BingoBoard(it) }

        val number = bingoNumbers.first { newnumber ->
            val red = "\u001b[32m"
            val reset = "\u001b[0m"
            println(red + newnumber + reset)
            boards.any {

                it.also { it.markNumber(newnumber) }.also { it.print() }.isSolved()

            }
        }

        val solvedBoard = boards.single { it.isSolved() }.getValue()

        println(solvedBoard)
        println(number)
        println(number * solvedBoard)


    }

    @Test
    fun test8() {

        val lines = File("input4.txt").readLines()
        val bingoNumbers = lines.first().split(',').map { it.toInt() }

        val boards = lines.drop(1).chunked(6).map { it.drop(1) }.map { BingoBoard(it) }
        val solvedBoard =solveBoards(bingoNumbers, boards)
        println(solvedBoard)
        println( solvedBoard.first.getValue()*solvedBoard.second)


    }

    tailrec fun solveBoards(bingoNumbers:List<Int>, bingoBoards: List<BingoBoard>): Pair<BingoBoard, Int> {
        val newnumber = bingoNumbers.first()
        val red = "\u001b[32m"
        val reset = "\u001b[0m"
        println(red + newnumber + reset)

        bingoBoards.forEach() { it.markNumber(newnumber) }
        return if (bingoBoards.all { it.isSolved() }) bingoBoards.first() to newnumber
        else solveBoards(bingoNumbers.drop(1), bingoBoards.filter { !it.isSolved() })

    }



    class Ventfield(val size:Int) {

        private val table: MutableList<Int> = MutableList<Int>(size*size) { 0 }


        fun getElement(row: Int, column: Int): Int {
            return table[row * size + column]
        }

        fun getRow(row: Int): List<Int> {
            return table.filterIndexed { index, i -> index < (row + 1) * size && index > row * size }
        }

        fun getColumn(column: Int): List<Int> {
            return table.filterIndexed { index, _ -> index % size == column }
        }



        private val marks = MutableList<Boolean>(size * size) { false }

        fun incElement(row: Int, column: Int) {
             table[row * size + column]+=1
        }


        fun allPairs(n:Int, xRange:List<Int>) = xRange.map { Pair( it, n) }
        fun markLine( from:Pair<Int,Int>,to : Pair<Int, Int>){
            val vector  = to.first-from.first to to.second-from.second
            val distance = max(abs(vector.first),vector.second.absoluteValue)
            val steigung = vector.first/distance to vector.second/distance
            val points = (0..distance).map { from+steigung*it }
            println(points)
            points.map { incElement(it.second, it.first) }
        }

        fun getValueGreaterOne(): Int {
            return table.filter { it>1 }.count()
        }

        fun print() {
            val red = "\u001b[31m"
            val reset = "\u001b[0m"
            val rows = table.chunked(size).map {
                it.map { print("${ "%4d".format(it)}") }
                println()
            }


// Resets previous color codes



            println("------")
        }

    }

    @Test
    fun test9() {

        val input = File("input5.txt").readLines()
        fun String.coordStringToCoord():Pair<Int, Int> {
            val coords = this.split(',').map (String::toInt)
            return coords[0] to coords[1]
        }

        val map = input.map { it.split("->").map(String::trim).map { it.coordStringToCoord() } }
        val lines = map.map { it[0] to it[1] }
        val ventfield = Ventfield(10)
        val vertOrHorizLines = lines.filter { it.first.first ==it.second.first || it.first.second == it.second.second }
        vertOrHorizLines.forEach{line->
            ventfield.markLine(line.first, line.second)
            ventfield.print()
        }


        println(ventfield.getValueGreaterOne())



    }

    @Test
    fun test10() {

        val input = File("input5.txt").readLines()
        fun String.coordStringToCoord():Pair<Int, Int> {
            val coords = this.split(',').map (String::toInt)
            return coords[0] to coords[1]
        }

        val map = input.map { it.split("->").map(String::trim).map { it.coordStringToCoord() } }
        val lines = map.map { it[0] to it[1] }
        val ventfield = Ventfield(1000)
//        val vertOrHorizLines = lines.filter { it.first.first ==it.second.first || it.first.second == it.second.second }
        lines.forEach{line->
            ventfield.markLine(line.first, line.second)
//            ventfield.print()
        }


        println(ventfield.getValueGreaterOne())



    }

    @Test
    fun test11() {

        val input = File("input6.txt").readLines()[0].split(',').map(String::toInt).toMutableList()
        val fishlist = nextGen4(input, 0)
        println(fishlist.count())
    }
    @Test
    fun test12() {

        val input = File("testinput.txt").readLines()[0].split(',').map(String::toInt).toMutableList()
        val fishlist = nextGen4(input, 0)
        println(fishlist.count())
    }

    @Test
    fun test13() {

        val input = File("testinput.txt").readLines()[0].split(',').map(String::toInt)
        val fishesByReproDate = input.groupingBy { it }.eachCount().toMutableMap()
        fishesByReproDate[7]=0
        fishesByReproDate[6]=0
        fishesByReproDate[5]=0
        fishesByReproDate[8]=0

        val fishes=(0..16).fold(fishesByReproDate) { acc, i ->
            println(acc.toSortedMap())
            var spawnday = i % 6 +1
            val newfish = acc.getValue(spawnday)
            acc[spawnday] = acc.getValue(spawnday) + acc.getValue(7)
            acc[7] = acc.getValue(8)
            acc[8] =newfish
            acc
        }
        println(fishes)
        println(fishes.values.sum())
    }

    @Test
    fun test14() {

        val input = File("input6.txt").readLines()[0].split(',').map(String::toLong)
        val fishesByReproDate = input.groupingBy { it }.eachCount().mapValues { it.value.toLong() }.toMutableMap()


        val fishes=(0..255).fold(Pair(fishesByReproDate, mutableMapOf<Long, Long>())) { (oldfishes, newfishes), i ->
            val spawnday:Long = (i%7).toLong()
            val spawnedFish = oldfishes.getOrDefault(spawnday,0)
            newfishes[(spawnday+2)%7]= spawnedFish
            oldfishes[spawnday]=oldfishes.getOrDefault(spawnday,0)+newfishes.getOrDefault(spawnday,0)
            newfishes[spawnday]=0
            println("day = ${i+1} old: ${oldfishes.values.sum()} new: ${newfishes.values.sum()} spawnded: $spawnedFish")
            oldfishes to newfishes
        }
        println(fishes.first.values.sum()+fishes.second.values.sum())

    }

    tailrec fun nextGen4(fishes:List<Int>, generation: Int): List<Int> {
//        println("generation $generation $fishes")
        println("generation $generation")
        val newfish= fishes.filter { it==0 }.map { 8 }
        val oldfishes = fishes.map { if (it==0) 6 else it-1 }

        if(generation==255) return oldfishes + newfish else return nextGen4(oldfishes + newfish, generation+1)
    }

    @Test
    fun Day_7() {
        val input = File("testinput.txt").readLines()
    }

}

private operator fun  Pair<Int, Int>.times(it: Int): Pair<Int, Int> =
    this.first*it to this.second*it


private operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) =
    this.first+other.first to this.second+other.second



