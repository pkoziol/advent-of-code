package biz.koziolek.adventofcode.year2024.day06

import biz.koziolek.adventofcode.findInput
import biz.koziolek.adventofcode.getHeight
import biz.koziolek.adventofcode.getWidth
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2024")
internal class Day6Test {

    private val sampleInput = """
            ....#.....
            .........#
            ..........
            ..#.......
            .......#..
            ..........
            .#..^.....
            ........#.
            #.........
            ......#...
        """.trimIndent().split("\n")

    @Test
    fun testParse() {
        val map = parseGuardMap(sampleInput)
        assertEquals(10, map.getWidth())
        assertEquals(10, map.getHeight())
    }

    @Test
    fun testSampleAnswer1() {
        val map = parseGuardMap(sampleInput)
        val finalMap = walkUntilGuardLeaves(map)
        val visitedPosCount = countVisitedPositions(finalMap)
        assertEquals(41, visitedPosCount)
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val map = parseGuardMap(input)
        val finalMap = walkUntilGuardLeaves(map)
        val visitedPosCount = countVisitedPositions(finalMap)
        assertEquals(4967, visitedPosCount)
    }
}
