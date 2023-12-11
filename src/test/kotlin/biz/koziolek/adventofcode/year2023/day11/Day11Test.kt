package biz.koziolek.adventofcode.year2023.day11

import biz.koziolek.adventofcode.LongCoord
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
                    LongCoord(3, 0),
                    LongCoord(7, 1),
                    LongCoord(0, 2),
                    LongCoord(6, 4),
                    LongCoord(1, 5),
                    LongCoord(9, 6),
                    LongCoord(7, 8),
                    LongCoord(0, 9),
                    LongCoord(4, 9),
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
        """.trimIndent(), map.scale(1).toString())
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
        """.trimIndent(), map.scale(2).toString())
        assertEquals("""
            .....#..........
            ...........#....
            #...............
            ................
            ................
            ................
            ..........#.....
            .#..............
            ...............#
            ................
            ................
            ................
            ...........#....
            #.....#.........
        """.trimIndent(), map.scale(3).toString())
    }

    @Test
    fun testSampleAnswer1() {
        val map = parseGalaxyMap(sampleInput)
        val expanded = map.scale()
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
        assertEquals(9556896, map.scale().distances.sum())
    }

    @Test
    fun testSampleAnswer2() {
        val map = parseGalaxyMap(sampleInput)
        assertEquals(1030, map.scale(scale = 10).distances.sum())
        assertEquals(8410, map.scale(scale = 100).distances.sum())
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val map = parseGalaxyMap(input)
        assertEquals(685038186836, map.scale(scale = 1_000_000).distances.sum())
    }
}
