package biz.koziolek.adventofcode.year2021.day05

import biz.koziolek.adventofcode.Coord
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
    fun testConvertingLinesToStringAndBack() {
        val lineString = "9,4 -> 3,4"
        val line = Line.fromString(lineString)
        
        assertEquals(9, line.x1)
        assertEquals(4, line.y1)
        assertEquals(3, line.x2)
        assertEquals(4, line.y2)
        assertEquals(lineString, line.toString())
    }

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
    fun testCoveredPoints() {
        val strLines = sampleInput.split("\n")
        val lines = parseLines(strLines)

        // 9,4 -> 3,4
        assertTrue(lines[2].isHorizontal)
        val expectedPoints = listOf(Coord(9, 4), Coord(8, 4), Coord(7, 4), Coord(6, 4), Coord(5, 4), Coord(4, 4), Coord(3, 4))
        assertEquals(expectedPoints, lines[2].getCoveredPoints())

        // 7,0 -> 7,4
        assertTrue(lines[4].isVertical)
        val expectedPoints2 = listOf(Coord(7, 0), Coord(7, 1), Coord(7, 2), Coord(7, 3), Coord(7, 4))
        assertEquals(expectedPoints2, lines[4].getCoveredPoints())

        // 6,4 -> 2,0
        assertTrue(lines[5].isDiagonal)
        val expectedPoints3 = listOf(Coord(6, 4), Coord(5, 3), Coord(4, 2), Coord(3, 1), Coord(2, 0))
        assertEquals(expectedPoints3, lines[5].getCoveredPoints())
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
