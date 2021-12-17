package com.xan2063


import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class Pack(val version: Int, val typeId: Int) {
    abstract fun getLength(): Int
    fun getWorth():Long{
        return when(this){
            is Literal->{
                println(this.getValue())
                this.getValue()
            }
            else ->when(typeId){
                0-> getKids().sumOf {it.getWorth() }
                1->getKids().fold(1){acc, pack -> acc*pack.getWorth() }
                2->getKids().minOf { it.getWorth() }
                3->getKids().maxOf { it.getWorth() }
                5->{
                    println("val 1 ${getKids()[0].getWorth()}")
                    println("val 2 ${getKids()[1].getWorth()}")

                    if(getKids()[0].getWorth()>getKids()[1].getWorth())1 else 0}
                6->{
                    println("val 1 ${getKids()[0].getWorth()}")
                    println("val 2 ${getKids()[1].getWorth()}")
                    if(getKids()[0].getWorth()<getKids()[1].getWorth())1 else 0
                }
                7->if(getKids()[0].getWorth()==getKids()[1].getWorth())1 else 0
                else -> 2
            }
        }
    }


    open fun getKids()= emptyList<Pack>()

}





class Literal(version: Int, typeId: Int, val bits: List<String>) : Pack(version, typeId) {

    fun getValue(): Long {
        val joinToString = bits.joinToString("") { it.drop(1) }
        return joinToString.toLong(2)
    }

    override fun getLength() = 3 + 3 + bits.count() * 5

    override fun toString(): String {
        return "literal ${bits.count()}"
    }

}



class LengthOperatorPAck(version: Int, typeId: Int, val subpackages: List<Pack>) : Pack(version, typeId) {

    override fun getLength() = 3 + 3 + 1 + 15 + subpackages.sumOf { it.getLength() }
    override fun toString(): String {
        return "length ${subpackages.count()}"
    }

    override fun getKids()= subpackages.reversed()

}

class AmmountOperatorPAck(version: Int, typeId: Int, val subpackages: List<Pack>) : Pack(version, typeId) {

    override fun getLength() = 3 + 3 + 1 + 11 + subpackages.sumOf { it.getLength() }

    override fun getKids()= subpackages

    override fun toString(): String {
        return "ammount ${subpackages.count()}"
    }

}

class Day16 {

    @Test
    fun helper(){
        assertEquals(getWorth("C200B40A82"),3)//finds the sum of 1 and 2, resulting in the value 3.
        assertEquals(getWorth("04005AC33890"),54) //finds the product of 6 and 9, resulting in the value 54.
        assertEquals(getWorth("880086C3E88112"),7) //finds the minimum of 7, 8, and 9, resulting in the value 7.
        assertEquals(getWorth("CE00C43D881120"),9) //finds the maximum of 7, 8, and 9, resulting in the value 9.
        assertEquals(getWorth("D8005AC2A8F0"),1) //produces 1, because 5 is less than 15.
        assertEquals(getWorth("F600BC2D8F"),0) //produces 0, because 5 is not greater than 15.
        assertEquals(getWorth("9C005AC2F8F0"),0) //produces 0, because 5 is not equal to 15.
        assertEquals(getWorth("9C0141080250320F1802104A08"),1)// produces 1, because 1 + 3 = 2 * 2.
    }

    @Test
    fun part2_real() {
        val input = File("input16.txt").readLines()[0]
        val  count = getWorth(input)

        println(count)

    }

    private fun getWorth(input: String): Long{
        val string = extractBinary(input)

        val literal = decodNextPack(string.asSequence(), 0).first()

        val count = literal.getWorth()
        return  count
    }

    @Test
    fun part1_real() {
        val input = File("input16.txt").readLines()[0]
        val string = extractBinary(input)

        val literal = decodNextPack(string.asSequence(),0).first()

        val count = getVersionCount(literal)
        assertEquals(16, count)
        println(count)
        println(literal)
    }
    @Test
    fun part1() {
        val string = extractBinary("8A004A801A8002F478")

        val literal = decodNextPack(string.asSequence(),0)

        val count = getVersionCount(literal.first())
        assertEquals(16, count)
        println(count)
        println(literal)
    }

    @Test
    fun part1_a() {
        val string = extractBinary("620080001611562C8802118E34")

        val literal = decodNextPack(string.asSequence(),0)

        val count = getVersionCount(literal.first())
        assertEquals(12, count)
        println(count)
        println(literal)
    }
    @Test
    fun part1_b() {
        val string = extractBinary("C0015000016115A2E0802F182340")

        val literal = decodNextPack(string.asSequence(),0)

        val count = getVersionCount(literal.first())
        assertEquals(23, count)
        println(count)
        println(literal)
    }
    @Test
    fun part1_c() {
        val string = extractBinary("A0016C880162017C3686B18A3D4780")

        val literal = decodNextPack(string.asSequence(),0)

        val count = getVersionCount(literal.first())
        assertEquals(31, count)
        println(count)
        println(literal)
    }

    fun getVersionCount(pack:Pack):Int{
        return pack.version + when(pack){
            is Literal -> 0
            is AmmountOperatorPAck ->pack.subpackages.sumOf { getVersionCount(it) }
            is LengthOperatorPAck ->pack.subpackages.sumOf { getVersionCount(it) }
            else ->0
        }
    }


    fun getHeader(input: Sequence<String>): Pair<Int, Int> {
        val joinToString = input.take(3).joinToString("")
        val version = joinToString.toInt(2)
        val typeId = input.drop(3).take(3).joinToString("").toInt(2)
        return version to typeId

    }




    tailrec fun decodNextPack(input: Sequence<String>, indentLevel:Int): Sequence<Pack> {

        if (input.count() <= 0||input.all { it=="0" }) return emptySequence()
        val (version, typeId) = getHeader(input)
        print("\t".repeat(indentLevel))
        val newpack = when (typeId) {
            4 -> createLiteral(input.drop(6), version, typeId)
            else ->
                when(input.drop(6).take(1).first()) {
                    "0"->decodeLenghtPackage(input.drop(7), version, typeId, indentLevel)
                    else -> decodeammountPackage(input.drop(7), version, typeId, indentLevel)

                }
        }


        return  decodNextPack(input.drop(newpack.getLength()), indentLevel)+newpack

    }

    tailrec fun decodXPack(input: Sequence<String>, ammount:Int, indentLevel:Int): Sequence<Pack> {

        if (input.count() <= 0||input.all { it=="0" }) return emptySequence()
        val packs = mutableListOf<Pack>()
         (0 until ammount).fold(input) {
            acc,_->
                val (version, typeId) = getHeader(acc)
                print("\t".repeat(indentLevel))
                val newpack =when (typeId) {
                    4 -> createLiteral(acc.drop(6), version, typeId)
                    else ->
                        when(acc.drop(6).take(1).first()) {
                            "0"->decodeLenghtPackage(acc.drop(7), version, typeId, indentLevel)
                            else -> decodeammountPackage(acc.drop(7), version, typeId, indentLevel)

                        }
                }
             packs.add(newpack)
             acc.drop(newpack.getLength())
        }



        return  packs.asSequence()

    }


    private fun decodeammountPackage(input: Sequence<String>, version: Int, typeId: Int, indentLevel: Int): Pack {

        val length = input.take(11).joinToString("").toInt(2)
        print("ammount ${length}")
        println("  input "+input.joinToString (""))
        val subpackages = decodXPack(input.drop(11), length,indentLevel+1)
        assertEquals(length,subpackages.count())
        val newpack = AmmountOperatorPAck(version, typeId, subpackages.toList())
        return newpack
    }

    private fun decodeLenghtPackage(input: Sequence<String>, version: Int, typeId: Int, indentLevel: Int): LengthOperatorPAck {
        val length = input.take(15).joinToString("").toInt(2)

        print("length ${length}")
        println("  input "+input.joinToString (""))
        val subpackages = decodNextPack(input.drop(15).take(length), indentLevel+1)

        val newpack = LengthOperatorPAck(version, typeId, subpackages.toList())


        return newpack
    }

    fun createLiteral(input: Sequence<String>, version: Int, typeId: Int): Literal {
        val literal = Literal(version, typeId, getLiteralValue(input).toList())
        print(literal)
        println("  input "+input.joinToString (""))
        return literal
    }

    @Test
    fun literalTest() {
        val string =
            "D2FE28".toCharArray().joinToString("") { it.digitToInt(16).toString(2).padStart(4, '0') }.toCharArray()
                .map { it.toString() }
        val literal = decodNextPack(string.asSequence(),0).first()
        assertEquals(2021, (literal as Literal).getValue())
    }

    @Test
    fun lengthoperatorPackage() {
        val string = extractBinary("38006F45291200")

        val literal = decodNextPack(string.asSequence(),0).first()

        assertIs<LengthOperatorPAck>(literal)
        literal.subpackages.forEach { assertIs<Literal>(it) }
        val ub1 = literal.subpackages[0] as Literal
        assertEquals(20, ub1.getValue())
        val ub2 = literal.subpackages[1] as Literal
        assertEquals(10, ub2.getValue())

        assertEquals(2, literal.subpackages.count())

    }

    private fun extractBinary(input:String) =
        input.toCharArray().joinToString("") { it.digitToInt(16).toString(2).padStart(4, '0') }.toCharArray().map { it.toString() }

    @Test
    fun ammountoperatorPackage() {
        val string = "EE00D40C823060".toCharArray().joinToString("") { it.digitToInt(16).toString(2).padStart(4, '0') }
            .toCharArray().map { it.toString() }
        val literal = decodNextPack(string.asSequence(),0).first()

        assertIs<AmmountOperatorPAck>(literal)
        literal.subpackages.forEach { assertIs<Literal>(it) }
        val ub1 = literal.subpackages[0] as Literal
        assertEquals(1, ub1.getValue())
        val ub2 = literal.subpackages[1] as Literal
        assertEquals(2, ub2.getValue())

        assertEquals(3, literal.subpackages.count())

    }

    fun getLiteralValue(input: Sequence<String>): Sequence<String> {
        val takeWhile = input.chunked(5).takeWhile { it.first() == "1" }.toList()
        val bits = takeWhile.map { it.joinToString("") }.asSequence()
        return bits + input.drop(bits.count() * 5).take(5).joinToString("")
    }
}