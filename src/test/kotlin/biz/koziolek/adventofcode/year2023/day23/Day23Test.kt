package biz.koziolek.adventofcode.year2023.day23

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2023")
internal class Day23Test {

    private val sampleInput = """
            #.#####################
            #.......#########...###
            #######.#########.#.###
            ###.....#.>.>.###.#.###
            ###v#####.#v#.###.#.###
            ###.>...#.#.#.....#...#
            ###v###.#.#.#########.#
            ###...#.#.#.......#...#
            #####.#.#.#######.#.###
            #.....#.#.#.......#...#
            #.#####.#.#.#########v#
            #.#...#...#...###...>.#
            #.#.#v#######v###.###v#
            #...#.>.#...>.>.#.###.#
            #####v#.#.###v#.#.###.#
            #.....#...#...#.#.#...#
            #.#########.###.#.#.###
            #...###...#...#...#.###
            ###.###.#.###v#####v###
            #...#...#.#.>.>.#.>.###
            #.###.###.#.###.#.#v###
            #.....###...###...#...#
            #####################.#
        """.trimIndent().split("\n")

    @Test
    fun testParse() {
        val hikingTrails = parseHikingTrails(sampleInput)
        assertEquals(213, hikingTrails.size)
    }

    @Test
    fun testSampleAnswer1() {
        val hikingTrails = parseHikingTrails(sampleInput)
        assertEquals(94, findLongestPathLen(hikingTrails))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val hikingTrails = parseHikingTrails(input)
        assertEquals(2502, findLongestPathLen(hikingTrails))
    }
}
