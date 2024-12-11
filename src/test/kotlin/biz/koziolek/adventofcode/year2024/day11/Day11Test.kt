package biz.koziolek.adventofcode.year2024.day11

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2024")
internal class Day11Test {

    private val sampleInput = """
            0 1 10 99 999
        """.trimIndent().split("\n")

    @Test
    fun testParseAndUpdateOnce() {
        val stones0 = parseStones(sampleInput)
        assertEquals(5, stones0.size)
        val stones1 = updateStones(stones0)
        assertEquals(listOf(1L, 2024L, 1L, 0L, 9L, 9L, 2021976L), stones1)
    }

    @Test
    fun testSampleAnswer1() {
        val stones0 = parseStones("125 17".split("\n"))
        assertEquals(listOf(125L, 17L), stones0)

        val stones6 = updateStones(stones0, times = 6)
        assertEquals(listOf(2097446912L, 14168L, 4048L, 2L, 0L, 2L, 4L, 40L, 48L, 2024L, 40L, 48L, 80L, 96L, 2L, 8L, 6L, 7L, 6L, 0L, 3L, 2L), stones6)
        assertEquals(22, stones6.size)

        val stones25 = updateStones(stones0, times = 25)
        assertEquals(55312, stones25.size)
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val stones0 = parseStones(input)
        val stones25 = updateStones(stones0, times = 25)
        assertEquals(218079, stones25.size)
    }
}
