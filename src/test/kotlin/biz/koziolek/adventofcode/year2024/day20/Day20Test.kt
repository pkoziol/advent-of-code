package biz.koziolek.adventofcode.year2024.day20

import biz.koziolek.adventofcode.AsciiColor
import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2024")
internal class Day20Test {

    private val sampleInput = """
            ###############
            #...#...#.....#
            #.#.#.#.#.###.#
            #S#...#.#.#...#
            #######.#.#.###
            #######.#.#...#
            #######.#.###.#
            ###..E#...#...#
            ###.#######.###
            #...###...#...#
            #.#####.#.###.#
            #.#...#.#.#...#
            #.#.#.#.#.#.###
            #...#...#...###
            ###############
        """.trimIndent().split("\n")

    @Test
    fun testParse() {
        val racetrack = parseRacetrack(sampleInput)
        assertEquals(sampleInput.joinToString("\n"), AsciiColor.cleanUp(racetrack.toString()))
    }

    @Test
    fun testFindCheats() {
        val racetrack = parseRacetrack(sampleInput)
        val cheats = findCheats(racetrack, duration = 2)
        assertEquals(12, cheats[Coord(7, 1) to Coord(9, 1)])
        assertEquals(20, cheats[Coord(9, 7) to Coord(11, 7)])
        assertEquals(38, cheats[Coord(8, 7) to Coord(8, 9)])
        assertEquals(64, cheats[Coord(7, 7) to Coord(5, 7)])
    }

    @Test
    fun testSampleAnswer1() {
        val racetrack = parseRacetrack(sampleInput)
        val cheats = findCheats(racetrack, duration = 2)
        assertEquals(14 + 14 + 2 + 4 + 2 + 3 + 1 + 1 + 1 + 1 + 1, cheats.size)
        assertEquals(5, cheats.count { it.value >= 20 })
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val racetrack = parseRacetrack(input)
        val cheats = findCheats(racetrack, duration = 2)
        assertEquals(1409, cheats.count { it.value >= 100 })
    }

    @Test
    fun testSampleAnswer2() {
        val racetrack = parseRacetrack(sampleInput)
        val cheats = findCheats(racetrack, duration = 20)
        assertEquals(32 + 31 + 29 + 39 + 25 + 23 + 20 + 19 + 12 + 14 + 12 + 22 + 4 + 3, cheats.count { it.value >= 50 })
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val racetrack = parseRacetrack(input)
        val cheats = findCheats(racetrack, duration = 20)
        assertEquals(1012821, cheats.count { it.value >= 100 })
    }
}
