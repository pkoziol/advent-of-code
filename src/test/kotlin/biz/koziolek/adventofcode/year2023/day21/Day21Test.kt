package biz.koziolek.adventofcode.year2023.day21

import biz.koziolek.adventofcode.findInput
import biz.koziolek.adventofcode.getHeight
import biz.koziolek.adventofcode.getWidth
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2023")
internal class Day21Test {

    private val sampleInput = """
            ...........
            .....###.#.
            .###.##..#.
            ..#.#...#..
            ....#.#....
            .##..S####.
            .##..#...#.
            .......##..
            .##.#.####.
            .##..##.##.
            ...........
        """.trimIndent().split("\n")

    @Test
    fun testParse() {
        val map = parseGardenMap(sampleInput)
        assertEquals(11, map.getWidth())
        assertEquals(11, map.getHeight())
        assertEquals(40, map.count { it.value == ROCK })
    }

    @Test
    fun testSampleAnswer1() {
        val map = parseGardenMap(sampleInput)
        assertEquals(2, walk(map, steps = 1).size)
        assertEquals(4, walk(map, steps = 2).size)
        assertEquals(6, walk(map, steps = 3).size)
        assertEquals(16, walk(map, steps = 6).size)
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val map = parseGardenMap(input)
        assertEquals(3594, walk(map, steps = 64).size)
    }
}
