package biz.koziolek.adventofcode.year2023.day11

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import kotlin.test.assertContains

@Tag("2023")
internal class Day11Test {

    private val sampleInput = """
            ...#......
            .......#..
            #.........
            ..........
            ......#...
            .#........
            .........#
            ..........
            .......#..
            #...#.....
        """.trimIndent().split("\n")

    @Test
    fun testParseGalaxyMap() {
        val map = parseGalaxyMap(sampleInput)
        assertEquals(
            GalaxyMap(
                galaxies = setOf(
                    Coord(3, 0),
                    Coord(7, 1),
                    Coord(0, 2),
                    Coord(6, 4),
                    Coord(1, 5),
                    Coord(9, 6),
                    Coord(7, 8),
                    Coord(0, 9),
                    Coord(4, 9),
                )
            ),
            map
        )
        assertEquals(sampleInput.joinToString("\n"), map.toString())
    }

    @Test
    fun testExpand() {
        val map = parseGalaxyMap(sampleInput)
        assertEquals("""
            ....#........
            .........#...
            #............
            .............
            .............
            ........#....
            .#...........
            ............#
            .............
            .............
            .........#...
            #....#.......
        """.trimIndent(), map.expand().toString())
    }

    @Test
    fun testSampleAnswer1() {
        val map = parseGalaxyMap(sampleInput)
        val expanded = map.expand()
        assertEquals(36, expanded.distances.size)
        assertContains(expanded.distances, 9)
        assertContains(expanded.distances, 15)
        assertContains(expanded.distances, 17)
        assertContains(expanded.distances, 5)
        assertEquals(374, expanded.distances.sum())
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val map = parseGalaxyMap(input)
        assertEquals(9556896, map.expand().distances.sum())
    }
}
