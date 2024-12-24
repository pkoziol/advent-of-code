package biz.koziolek.adventofcode.year2024.day18

import biz.koziolek.adventofcode.AsciiColor
import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2024")
internal class Day18Test {

    private val sampleInput = """
            5,4
            4,2
            4,5
            3,0
            2,1
            6,3
            2,4
            1,5
            0,6
            3,3
            2,6
            5,1
            1,2
            5,5
            2,5
            6,5
            1,4
            0,4
            6,4
            1,1
            6,1
            1,0
            0,5
            1,6
            2,0
        """.trimIndent().split("\n")

    @Test
    fun testParseFallingBytes() {
        val bytes = parseFallingBytes(sampleInput)
        assertEquals(25, bytes.size)
    }

    @Test
    fun testSampleAnswer1() {
        val bytes = parseFallingBytes(sampleInput)

        val memory = Memory.fromBytes(bytes.take(12), size = 6)
        assertEquals("""
            ...#...
            ..#..#.
            ....#..
            ...#..#
            ..#..#.
            .#..#..
            #.#....
        """.trimIndent(), AsciiColor.cleanUp(memory.toString()))

        val path = findExitPath(memory)
        assertEquals("""
            OO.#OOO
            .O#OO#O
            .OOO#OO
            ...#OO#
            ..#OO#.
            .#.O#..
            #.#OOOO
        """.trimIndent(), AsciiColor.cleanUp(memory.toString(path)))
        assertEquals(22, path.size - 1)
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val bytes = parseFallingBytes(input)
        val memory = Memory.fromBytes(bytes.take(1024))
        val path = findExitPath(memory)
        assertEquals(338, path.size - 1)
    }

    @Test
    fun testSampleAnswer2() {
        val bytes = parseFallingBytes(sampleInput)
        val firstBlockingByte = findFirstBlockingByte(bytes, size = 6)
        assertEquals(Coord(6, 1), firstBlockingByte)
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val bytes = parseFallingBytes(input)
        val firstBlockingByte = findFirstBlockingByte(bytes)
        assertEquals(Coord(20, 44), firstBlockingByte)
    }
}
