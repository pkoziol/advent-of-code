package biz.koziolek.adventofcode.year2023.day14

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2023")
internal class Day14Test {

    private val sampleInput = """
            O....#....
            O.OO#....#
            .....##...
            OO.#O....O
            .O.....O#.
            O.#..O.#.#
            ..O..#O..O
            .......O..
            #....###..
            #OO..#....
        """.trimIndent().split("\n")

    @Test
    fun testParsePlatform() {
        val platform = parsePlatform(sampleInput)
        assertEquals(18, platform.rocks.count { it.value == ROUND_ROCK })
        assertEquals(17, platform.rocks.count { it.value == CUBE_ROCK })
        assertEquals(sampleInput.joinToString("\n"), platform.toString())
    }

    @Test
    fun testSlideNorth() {
        val platform = parsePlatform(sampleInput)
        assertEquals("""
            OOOO.#.O..
            OO..#....#
            OO..O##..O
            O..#.OO...
            ........#.
            ..#....#.#
            ..O..#.O.O
            ..O.......
            #....###..
            #....#....
        """.trimIndent(), platform.slideNorth().toString())
    }

    @Test
    fun testSampleAnswer1() {
        val platform = parsePlatform(sampleInput)
        assertEquals(136, platform.slideNorth().totalLoad)
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val platform = parsePlatform(input)
        assertEquals(108918, platform.slideNorth().totalLoad)
    }
}
