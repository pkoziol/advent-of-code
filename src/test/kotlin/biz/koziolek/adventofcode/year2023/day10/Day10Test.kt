package biz.koziolek.adventofcode.year2023.day10

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2023")
internal class Day10Test {

    private val sampleInput1a = """
            .....
            .S-7.
            .|.|.
            .L-J.
            .....
        """.trimIndent().split("\n")

    private val sampleInput1b = """
            -L|F7
            7S-7|
            L|7||
            -L-J|
            L|-JF
        """.trimIndent().split("\n")

    private val sampleInput2a = """
            ..F7.
            .FJ|.
            SJ.L7
            |F--J
            LJ...
        """.trimIndent().split("\n")

    private val sampleInput2b = """
            7-F7-
            .FJ|7
            SJLL7
            |F--J
            LJ.LJ
        """.trimIndent().split("\n")

    @Test
    fun testParsePipeMaze() {
        val maze1a = parsePipeMaze(sampleInput1a)
        assertEquals(25, maze1a.contents.size)
        assertEquals(Coord(1, 1), maze1a.startPos)

        val maze1b = parsePipeMaze(sampleInput1b)
        assertEquals(25, maze1b.contents.size)
        assertEquals(Coord(1, 1), maze1b.startPos)

        val maze2a = parsePipeMaze(sampleInput2a)
        assertEquals(25, maze2a.contents.size)
        assertEquals(Coord(0, 2), maze2a.startPos)

        val maze2b = parsePipeMaze(sampleInput2b)
        assertEquals(25, maze2b.contents.size)
        assertEquals(Coord(0, 2), maze2b.startPos)
    }

    @Test
    fun testFindLoop() {
        val maze1a = parsePipeMaze(sampleInput1a)
        assertEquals(8, maze1a.theLoop.size)

        val maze1b = parsePipeMaze(sampleInput1b)
        assertEquals(8, maze1b.theLoop.size)

        val maze2a = parsePipeMaze(sampleInput2a)
        assertEquals(16, maze2a.theLoop.size)

        val maze2b = parsePipeMaze(sampleInput2b)
        assertEquals(16, maze2b.theLoop.size)
    }

    @Test
    fun testSampleAnswer1() {
        val maze1a = parsePipeMaze(sampleInput1a)
        assertEquals(4, maze1a.theLoopFarthestDistanceFromStart)

        val maze1b = parsePipeMaze(sampleInput1b)
        assertEquals(4, maze1b.theLoopFarthestDistanceFromStart)

        val maze2a = parsePipeMaze(sampleInput2a)
        assertEquals(8, maze2a.theLoopFarthestDistanceFromStart)

        val maze2b = parsePipeMaze(sampleInput2b)
        assertEquals(8, maze2b.theLoopFarthestDistanceFromStart)
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val maze = parsePipeMaze(input)
        assertEquals(6846, maze.theLoopFarthestDistanceFromStart)
    }
}
