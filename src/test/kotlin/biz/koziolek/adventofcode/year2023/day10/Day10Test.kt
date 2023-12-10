package biz.koziolek.adventofcode.year2023.day10

import biz.koziolek.adventofcode.AsciiColor.*
import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@Tag("2023")
internal class Day10Test {

    private val inputs = mapOf(
        "1a" to """
            .....
            .S-7.
            .|.|.
            .L-J.
            .....
        """.trimIndent().split("\n"),
        "1b" to """
            -L|F7
            7S-7|
            L|7||
            -L-J|
            L|-JF
        """.trimIndent().split("\n"),
        "2a" to """
            ..F7.
            .FJ|.
            SJ.L7
            |F--J
            LJ...
        """.trimIndent().split("\n"),
        "2b" to """
            7-F7-
            .FJ|7
            SJLL7
            |F--J
            LJ.LJ
        """.trimIndent().split("\n"),
        "3" to """
            ..........
            .S------7.
            .|F----7|.
            .||....||.
            .||....||.
            .|L-7F-J|.
            .|..||..|.
            .L--JL--J.
            ..........
        """.trimIndent().split("\n"),
        "4" to """
            .F----7F7F7F7F-7....
            .|F--7||||||||FJ....
            .||.FJ||||||||L7....
            FJL7L7LJLJ||LJ.L-7..
            L--J.L7...LJS7F-7L7.
            ....F-J..F7FJ|L7L7L7
            ....L7.F7||L7|.L7L7|
            .....|FJLJ|FJ|F7|.LJ
            ....FJL-7.||.||||...
            ....L---J.LJ.LJLJ...
        """.trimIndent().split("\n"),
        "5" to """
            FF7FSF7F7F7F7F7F---7
            L|LJ||||||||||||F--J
            FL-7LJLJ||||||LJL-77
            F--JF--7||LJLJ7F7FJ-
            L---JF-JLJ.||-FJLJJ7
            |F|F-JF---7F7-L7L|7|
            |FFJF7L7F-JF7|JL---7
            7-L-JL7||F7|L7F-7F7|
            L.L7LFJ|||||FJL7||LJ
            L7JLJL-JLJLJL--JLJ.L
        """.trimIndent().split("\n"),
    )

    @ParameterizedTest(name = "input={0}")
    @CsvSource(
        "1a,25,1,1",
        "1b,25,1,1",
        "2a,25,0,2",
        "2b,25,0,2",
    )
    fun testParsePipeMaze(inputId: String, expectedSize: Int, expectedStartX: Int, expectedStartY: Int) {
        val maze = parsePipeMaze(inputs[inputId]!!)
        assertEquals(expectedSize, maze.contents.size)
        assertEquals(Coord(expectedStartX, expectedStartY), maze.startPos)
    }

    @ParameterizedTest(name = "input={0}")
    @CsvSource(
        "1a,8",
        "1b,8",
        "2a,16",
        "2b,16",
    )
    fun testFindLoop(inputId: String, expectedSize: Int) {
        val maze = parsePipeMaze(inputs[inputId]!!)
        assertEquals(expectedSize, maze.theLoop.size)
    }

    @ParameterizedTest(name = "input={0}")
    @CsvSource(
        "1a,4",
        "1b,4",
        "2a,8",
        "2b,8",
    )
    fun testSampleAnswer1(inputId: String, expectedDistance: Int) {
        val maze = parsePipeMaze(inputs[inputId]!!)
        assertEquals(expectedDistance, maze.theLoopFarthestDistanceFromStart)
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val maze = parsePipeMaze(input)
        assertEquals(6846, maze.theLoopFarthestDistanceFromStart)
    }

    @ParameterizedTest(name = "input={0}")
    @CsvSource(
        "1a,1,true",
        "1b,1,true",
        "2a,1,true",
        "2b,1,true",
        "3,4,true",
        "4,8,true",
        "5,10,true",
    )
    fun testSampleAnswer2(inputId: String, expectedSize: Int, debug: Boolean) {
        val maze = parsePipeMaze(inputs[inputId]!!)

        if (debug) {
            debugPrintMaze(maze)
        }

        assertEquals(expectedSize, maze.insideTheLoop.size)
    }

    private fun debugPrintMaze(maze: PipeMaze) {
        println("Maze with loop:\n${mazeToString(maze, loopColor = WHITE)}")
        println("Maze with marked inside:\n${mazeToString(maze, loopColor = WHITE, insideColor = BRIGHT_WHITE)}")
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val maze = parsePipeMaze(input)
        assertEquals(325, maze.insideTheLoop.size)
    }
}
