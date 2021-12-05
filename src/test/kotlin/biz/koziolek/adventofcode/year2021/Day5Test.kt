package biz.koziolek.adventofcode.year2021

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

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

        assertEquals(0, lines[0].x1)
        assertEquals(9, lines[0].y1)
        assertEquals(5, lines[0].x2)
        assertEquals(9, lines[0].y2)
        assertTrue(lines[0].isHorizontal)
        assertFalse(lines[0].isVertical)

        assertEquals(7, lines[4].x1)
        assertEquals(0, lines[4].y1)
        assertEquals(7, lines[4].x2)
        assertEquals(4, lines[4].y2)
        assertFalse(lines[4].isHorizontal)
        assertTrue(lines[4].isVertical)

        assertEquals(0, lines[8].x1)
        assertEquals(0, lines[8].y1)
        assertEquals(8, lines[8].x2)
        assertEquals(8, lines[8].y2)
        assertFalse(lines[8].isHorizontal)
        assertFalse(lines[8].isVertical)
    }

    @Test
    fun testParsingFullInput() {
        val strLines = File("src/main/resources/year2021/day5/input").readLines()

        val lines = parseLines(strLines)
        assertEquals(500, lines.size)
    }

    @Test
    fun testMappingPart1Sample() {
        val strLines = sampleInput.split("\n")
        val lines = parseLines(strLines).filter { it.isHorizontal || it.isVertical }

        val map = createMap(lines)

        assertEquals(10, map.width)
        assertEquals(10, map.height)

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
        assertEquals(expectedToString, map.toString())
    }

    @Test
    fun testAnswerPart1Sample() {
        val strLines = sampleInput.split("\n")
        val lines = parseLines(strLines).filter { it.isHorizontal || it.isVertical }
        val map = createMap(lines)

        assertEquals(5, map.countGreaterOrEqual(2))
    }

    @Test
    fun testAnswerPart1() {
        val strLines = File("src/main/resources/year2021/day5/input").readLines()
        val lines = parseLines(strLines).filter { it.isHorizontal || it.isVertical }
        val map = createMap(lines)

        assertEquals(4993, map.countGreaterOrEqual(2))
    }

    @Test
    fun testMappingPart2Sample() {
        val strLines = sampleInput.split("\n")
        val lines = parseLines(strLines)
        val map = createMap(lines)

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
        assertEquals(expectedToString, map.toString())
    }

    @Test
    fun testAnswerPart2Sample() {
        val strLines = sampleInput.split("\n")
        val lines = parseLines(strLines)
        val map = createMap(lines)

        assertEquals(12, map.countGreaterOrEqual(2))
    }

    @Test
    fun testAnswerPart2() {
        val strLines = File("src/main/resources/year2021/day5/input").readLines()
        val lines = parseLines(strLines)
        val map = createMap(lines)

        assertEquals(21101, map.countGreaterOrEqual(2))
    }
}
