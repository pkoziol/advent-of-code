package biz.koziolek.adventofcode.year2021.day05

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day5Test {

    private val sampleInput = """
            0,9 -> 5,9
            8,0 -> 0,8
            9,4 -> 3,4
            2,2 -> 2,1
            7,0 -> 7,4
            6,4 -> 2,0
            0,9 -> 2,9
            3,4 -> 1,4
            0,0 -> 8,8
            5,5 -> 8,2
        """.trimIndent()

    @Test
    fun testParsingSampleInput() {
        val strLines = sampleInput.split("\n")

        val lines = parseLines(strLines)
        assertEquals(10, lines.size)
    }

    @Test
    fun testParsingFullInput() {
        val strLines = findInput(object {}).readLines()

        val lines = parseLines(strLines)
        assertEquals(500, lines.size)
    }

    @Test
    fun testMappingPart1Sample() {
        val strLines = sampleInput.split("\n")
        val lines = parseLines(strLines).filter { it.isHorizontal || it.isVertical }

        val map = createVentMap(lines)

        val expectedToString = """
            .......1..
            ..1....1..
            ..1....1..
            .......1..
            .112111211
            ..........
            ..........
            ..........
            ..........
            222111....
        """.trimIndent()
        assertEquals(expectedToString, ventMapToString(map))
    }

    @Test
    fun testAnswerPart1Sample() {
        val strLines = sampleInput.split("\n")
        val lines = parseLines(strLines).filter { it.isHorizontal || it.isVertical }
        val map = createVentMap(lines)

        assertEquals(5, countGreaterOrEqual(map, 2))
    }

    @Test
    fun testAnswerPart1() {
        val strLines = findInput(object {}).readLines()
        val lines = parseLines(strLines).filter { it.isHorizontal || it.isVertical }
        val map = createVentMap(lines)

        assertEquals(4993, countGreaterOrEqual(map, 2))
    }

    @Test
    fun testMappingPart2Sample() {
        val strLines = sampleInput.split("\n")
        val lines = parseLines(strLines)
        val map = createVentMap(lines)

        val expectedToString = """
            1.1....11.
            .111...2..
            ..2.1.111.
            ...1.2.2..
            .112313211
            ...1.2....
            ..1...1...
            .1.....1..
            1.......1.
            222111....
        """.trimIndent()
        assertEquals(expectedToString, ventMapToString(map))
    }

    @Test
    fun testAnswerPart2Sample() {
        val strLines = sampleInput.split("\n")
        val lines = parseLines(strLines)
        val map = createVentMap(lines)

        assertEquals(12, countGreaterOrEqual(map, 2))
    }

    @Test
    fun testAnswerPart2() {
        val strLines = findInput(object {}).readLines()
        val lines = parseLines(strLines)
        val map = createVentMap(lines)

        assertEquals(21101, countGreaterOrEqual(map, 2))
    }
}
