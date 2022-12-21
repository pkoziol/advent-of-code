package biz.koziolek.adventofcode.year2022.day21

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2022")
internal class Day21Test {

    private val sampleInput = """
            root: pppw + sjmn
            dbpl: 5
            cczh: sllz + lgvd
            zczc: 2
            ptdq: humn - dvpt
            dvpt: 3
            lfqf: 4
            humn: 5
            ljgn: 2
            sjmn: drzm * dbpl
            sllz: 4
            pppw: cczh / lfqf
            lgvd: ljgn * ptdq
            drzm: hmdt - zczc
            hmdt: 32
        """.trimIndent().split("\n")

    @Test
    fun testParseYellingMonkeys() {
        val yellingMonkeys = parseYellingMonkeys(sampleInput)
        assertEquals(15, yellingMonkeys.size)
    }

    @Test
    fun testFindYelledNumber() {
        val yellingMonkeys = parseYellingMonkeys(sampleInput)
        assertEquals(32, findYelledNumber("hmdt", yellingMonkeys))
        assertEquals(2, findYelledNumber("zczc", yellingMonkeys))
        assertEquals(30, findYelledNumber("drzm", yellingMonkeys))
        assertEquals(150, findYelledNumber("sjmn", yellingMonkeys))
        assertEquals(152, findYelledNumber("root", yellingMonkeys))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val yellingMonkeys = parseYellingMonkeys(input)
        assertEquals(24947355373338, findYelledNumber("root", yellingMonkeys))
    }

    @Test
    fun testFindNumberToYell() {
        val yellingMonkeys = parseYellingMonkeys(sampleInput)
        assertEquals(301, findNumberToYell(yellingMonkeys))
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val yellingMonkeys = parseYellingMonkeys(input)
        assertEquals(3876907167495, findNumberToYell(yellingMonkeys))
    }
}
