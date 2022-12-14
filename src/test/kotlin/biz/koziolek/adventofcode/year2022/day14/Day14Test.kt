package biz.koziolek.adventofcode.year2022.day14

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.Line
import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2022")
internal class Day14Test {

    private val sampleInput = """
            498,4 -> 498,6 -> 496,6
            503,4 -> 502,4 -> 502,9 -> 494,9
        """.trimIndent().split("\n")

    @Test
    fun testParseRockStructures() {
        val rockLines = parseRockLines(sampleInput)
        assertEquals(
            setOf(
                Line(Coord(498, 4), Coord(498, 6)),
                Line(Coord(498, 6), Coord(496, 6)),
                Line(Coord(503, 4), Coord(502, 4)),
                Line(Coord(502, 4), Coord(502, 9)),
                Line(Coord(502, 9), Coord(494, 9)),
            ),
            rockLines
        )
    }

    @Test
    fun testBuildRockMap() {
        val rockLines = parseRockLines(sampleInput)
        val rockMap = buildRockMap(rockLines)

        assertEquals(SOURCE, rockMap[Coord(500, 0)])

        assertEquals(ROCK, rockMap[Coord(498, 4)])
        assertEquals(ROCK, rockMap[Coord(498, 5)])
        assertEquals(ROCK, rockMap[Coord(498, 6)])
        assertEquals(ROCK, rockMap[Coord(497, 6)])
        assertEquals(ROCK, rockMap[Coord(496, 6)])

        assertEquals(ROCK, rockMap[Coord(503, 4)])
        assertEquals(ROCK, rockMap[Coord(502, 4)])
        assertEquals(ROCK, rockMap[Coord(502, 5)])
        assertEquals(ROCK, rockMap[Coord(502, 6)])
        assertEquals(ROCK, rockMap[Coord(502, 7)])
        assertEquals(ROCK, rockMap[Coord(502, 8)])
        assertEquals(ROCK, rockMap[Coord(502, 9)])
        assertEquals(ROCK, rockMap[Coord(501, 9)])
        assertEquals(ROCK, rockMap[Coord(500, 9)])
        assertEquals(ROCK, rockMap[Coord(499, 9)])
        assertEquals(ROCK, rockMap[Coord(498, 9)])
        assertEquals(ROCK, rockMap[Coord(497, 9)])
        assertEquals(ROCK, rockMap[Coord(496, 9)])
        assertEquals(ROCK, rockMap[Coord(495, 9)])
        assertEquals(ROCK, rockMap[Coord(494, 9)])
        
        assertEquals(
            """
                ......+...
                ..........
                ..........
                ..........
                ....#...##
                ....#...#.
                ..###...#.
                ........#.
                ........#.
                #########.
            """.trimIndent(),
            visualizeRockMap(rockMap)
        )
    }

    @Test
    fun testDropSand() {
        val rockLines = parseRockLines(sampleInput)
        val rockMap = buildRockMap(rockLines)

        val map1 = dropSand(rockMap)
        assertEquals(
            """
                ......+...
                ..........
                ..........
                ..........
                ....#...##
                ....#...#.
                ..###...#.
                ........#.
                ......o.#.
                #########.
            """.trimIndent(),
            visualizeRockMap(map1)
        )

        val map2 = dropSand(map1)
        assertEquals(
            """
                ......+...
                ..........
                ..........
                ..........
                ....#...##
                ....#...#.
                ..###...#.
                ........#.
                .....oo.#.
                #########.
            """.trimIndent(),
            visualizeRockMap(map2)
        )

        val map5 = dropSand(map2, count = 3)
        assertEquals(
            """
                ......+...
                ..........
                ..........
                ..........
                ....#...##
                ....#...#.
                ..###...#.
                ......o.#.
                ....oooo#.
                #########.
            """.trimIndent(),
            visualizeRockMap(map5)
        )

        val map22 = dropSand(map5, count = 17)
        assertEquals(
            """
                ......+...
                ..........
                ......o...
                .....ooo..
                ....#ooo##
                ....#ooo#.
                ..###ooo#.
                ....oooo#.
                ...ooooo#.
                #########.
            """.trimIndent(),
            visualizeRockMap(map22)
        )

        val map24 = dropSand(map22, count = 2)
        assertEquals(
            """
                ......+...
                ..........
                ......o...
                .....ooo..
                ....#ooo##
                ...o#ooo#.
                ..###ooo#.
                ....oooo#.
                .o.ooooo#.
                #########.
            """.trimIndent(),
            visualizeRockMap(map24)
        )
    }

    @Test
    fun testDropSandIntoAbyss() {
        val rockLines = parseRockLines(sampleInput)
        val rockMap = buildRockMap(rockLines)

        assertThrows(FallenIntoAbyss.javaClass) {
            dropSand(rockMap, count = 25)
        }
    }

    @Test
    fun testCountSandNotInAbyss() {
        val rockLines = parseRockLines(sampleInput)
        val rockMap = buildRockMap(rockLines)
        assertEquals(24, countSandNotInAbyss(rockMap))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val rockLines = parseRockLines(input)
        val rockMap = buildRockMap(rockLines)
        assertEquals(763, countSandNotInAbyss(rockMap))
    }

    @Test
    fun testCountSandWithInfiniteFloor() {
        val rockLines = parseRockLines(sampleInput)
        val rockMap = buildRockMap(rockLines)
        assertEquals(93, countSandWithInfiniteFloor(rockMap))
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val rockLines = parseRockLines(input)
        val rockMap = buildRockMap(rockLines)
        assertEquals(23921, countSandWithInfiniteFloor(rockMap))
    }
}
