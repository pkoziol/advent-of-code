package biz.koziolek.adventofcode.year2024.day03

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2024")
internal class Day3Test {

    private val sampleInput = """
            xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))
        """.trimIndent().split("\n")

    private val sampleInput2 = """
            xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))
        """.trimIndent().split("\n")

    @Test
    fun testParse() {
        val instructions = parseMulInstructions(sampleInput)
        assertEquals(4, instructions.size)
    }

    @Test
    fun testSampleAnswer1() {
        val instructions = parseMulInstructions(sampleInput)
        assertEquals(161, sumMul(instructions))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val instructions = parseMulInstructions(input)
        assertEquals(165225049, sumMul(instructions))
    }

    @Test
    fun testParse2() {
        val instructions = parseMulInstructions2(sampleInput2)
        assertEquals(6, instructions.size)
    }

    @Test
    fun testSampleAnswer2() {
        val instructions = parseMulInstructions2(sampleInput2)
        assertEquals(48, sumMul(instructions))
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val instructions = parseMulInstructions2(input)
        assertEquals(108830766, sumMul(instructions))
    }
}
