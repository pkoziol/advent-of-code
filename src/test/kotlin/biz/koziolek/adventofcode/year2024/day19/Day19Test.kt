package biz.koziolek.adventofcode.year2024.day19

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2024")
internal class Day19Test {

    private val sampleInput = """
            r, wr, b, g, bwu, rb, gb, br

            brwrr
            bggr
            gbbr
            rrbgbr
            ubwu
            bwurrg
            brgr
            bbrgwb
        """.trimIndent().split("\n")

    @Test
    fun testParseTowels() {
        val (patterns, designs) = parseTowels(sampleInput)
        assertEquals(8, patterns.size)
        assertEquals(8, designs.size)
    }

    @Test
    fun testIsPossible() {
        val (patterns, designs) = parseTowels(sampleInput)
        assertTrue(isPossible(patterns, designs[0]), designs[0])
        assertTrue(isPossible(patterns, designs[1]), designs[1])
        assertTrue(isPossible(patterns, designs[2]), designs[2])
        assertTrue(isPossible(patterns, designs[3]), designs[3])
        assertFalse(isPossible(patterns, designs[4]), designs[4])
        assertTrue(isPossible(patterns, designs[5]), designs[5])
        assertTrue(isPossible(patterns, designs[6]), designs[6])
        assertFalse(isPossible(patterns, designs[7]), designs[7])
    }

    @Test
    fun testSampleAnswer1() {
        val (patterns, designs) = parseTowels(sampleInput)
        assertEquals(6, designs.count { isPossible(patterns, it) })
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val (patterns, designs) = parseTowels(input)
        assertEquals(220, designs.count { isPossible(patterns, it) })
    }
}
