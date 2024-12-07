package biz.koziolek.adventofcode.year2024.day06

import biz.koziolek.adventofcode.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import kotlin.test.assertContains

@Tag("2024")
internal class Day6Test {

    private val sampleInput = """
            ....#.....
            .........#
            ..........
            ..#.......
            .......#..
            ..........
            .#..^.....
            ........#.
            #.........
            ......#...
        """.trimIndent().split("\n")

    private val expectedSampleObstacles = """
            ....#.....
            .........#
            ..........
            ..#.......
            .......#..
            ..........
            .#.O^.....
            ......OO#.
            #O.O......
            ......#O..

        """.trimIndent()

    @Test
    fun testParse() {
        val map = parseGuardMap(sampleInput)
        assertEquals(10, map.width)
        assertEquals(10, map.height)
    }

    @Test
    fun testSampleAnswer1() {
        val map = parseGuardMap(sampleInput)
        val finalMap = walkUntilGuardLeaves(map).last()
        val visitedPosCount = countVisitedPositions(finalMap)
        assertEquals(41, visitedPosCount)
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val map = parseGuardMap(input)
        val finalMap = walkUntilGuardLeaves(map).last()
        val visitedPosCount = countVisitedPositions(finalMap)
        assertEquals(4967, visitedPosCount)
    }

    @Test
    fun testSampleAnswer2() {
        val map = parseGuardMap(sampleInput)
        val newObstacles = putAllObstacles(map)
//        println(map.toStringWithObstacles(newObstacles))

        assertEquals(expectedSampleObstacles, AsciiColor.cleanUp(map.toStringWithObstacles(newObstacles).replace(" ", "")))
        assertContains(newObstacles, Coord(3, 6), "Option one")
        assertContains(newObstacles, Coord(6, 7), "Option two")
        assertContains(newObstacles, Coord(7, 7), "Option three")
        assertContains(newObstacles, Coord(1, 8), "Option four")
        assertContains(newObstacles, Coord(3, 8), "Option five")
        assertContains(newObstacles, Coord(7, 9), "Option six")
        assertEquals(6, newObstacles.size)
    }

    // Basic rectangle
    private val sampleInput2 = """
            .#.....
            ...>..#
            .......
            .....#.
        """.trimIndent().split("\n")

    @Test
    fun testSampleAnswer2b() {
        val map = parseGuardMap(sampleInput2)
        val intermediateMaps = walkUntilGuardLeaves(map).take(100)
//        intermediateMaps.forEach { println(it) }
        val visitedPosCount = countVisitedPositions(intermediateMaps.last())

        val newObstacles = putAllObstacles(map)
//        println("Obstacles to place: $newObstacles")
//        println(map.toStringWithObstacles(newObstacles))

        assertEquals(9, visitedPosCount)
        assertContains(newObstacles, Coord(0, 2))
        assertEquals(1, newObstacles.size)
    }

    // Horizontal loop created by obstacle
    private val sampleInput3 = """
            .#.....
            ...>..#
            .....#.
        """.trimIndent().split("\n")

    @Test
    fun testSampleAnswer2c() {
        val map = parseGuardMap(sampleInput3)
        val intermediateMaps = walkUntilGuardLeaves(map).take(100)
//        intermediateMaps.forEach { println(it) }
        val visitedPosCount = countVisitedPositions(intermediateMaps.last())

        val newObstacles = putAllObstacles(map)
//        println("Obstacles to place: $newObstacles")
//        println(map.toStringWithObstacles(newObstacles))

        assertEquals(6, visitedPosCount)
        assertContains(newObstacles, Coord(0, 1))
        assertEquals(1, newObstacles.size)
    }

    // 1x1 space - will not be able to go there when obstacle is placed first
    private val sampleInput4 = """
            ..#.
            >..#
            ..#.
        """.trimIndent().split("\n")

    @Test
    fun testSampleAnswer2d() {
        val map = parseGuardMap(sampleInput4)
        val intermediateMaps = walkUntilGuardLeaves(map).take(100)
//        intermediateMaps.forEach { println(it) }
        val visitedPosCount = countVisitedPositions(intermediateMaps.last())

        val newObstacles = putAllObstacles(map)
//        println("Obstacles to place: $newObstacles")
//        println(map.toStringWithObstacles(newObstacles))

        assertEquals(3, visitedPosCount)
        assertEquals(0, newObstacles.size)
    }

    // Vertical loop on the right side not using new obstacle
    private val sampleInput5 = """
            ....v.....
            .#......#.
            #........#
            .......#..
            ........#.
        """.trimIndent().split("\n")

    @Test
    @Timeout(5, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    fun testSampleAnswer2e() {
        val map = parseGuardMap(sampleInput5)

//        walkUntilGuardLeaves(map).take(100).forEach { println(it) }

        val newObstacles = putAllObstacles(map)
//        println("Obstacles to place: $newObstacles")
//        println(map.toStringWithObstacles(newObstacles))

        assertContains(newObstacles, Coord(4, 3))
        assertEquals(1, newObstacles.size)
    }

    // Cannot be at guardian's starting position
    private val sampleInput6 = """
            ....##....
            ......#..#
            ..........
            ....^.....
            .....#....
        """.trimIndent().split("\n")

    @Test
    fun testSampleAnswer2f() {
        val map = parseGuardMap(sampleInput6)
        assertEquals(map.guardPos, Coord(4, 3))

//        walkUntilGuardLeaves(map).take(100).forEach { println(it) }

        val newObstacles = putAllObstacles(map)
//        println("Obstacles to place: $newObstacles")
//        println(map.toStringWithObstacles(newObstacles))

        assertContains(newObstacles, Coord(3, 3))
        assertEquals(1, newObstacles.size)
    }

    // Smaller version of input6 that causes problems somehow?
    private val sampleInput7 = """
            .##.
            ...#
            ....
            .^..
            ..#.
        """.trimIndent().split("\n")

    @Test
    fun testSampleAnswer2g() {
        val map = parseGuardMap(sampleInput7)
        assertEquals(map.guardPos, Coord(1, 3))

//        walkUntilGuardLeaves(map).take(100).forEach { println(it) }

        val newObstacles = putAllObstacles(map)
//        println("Obstacles to place: $newObstacles")
//        println(map.toStringWithObstacles(newObstacles))

        assertContains(newObstacles, Coord(0, 3))
        assertEquals(1, newObstacles.size)
    }

    // Even smaller input6 that causes needs obstacle on negative X coordinate
    private val sampleInput8 = """
            ##.
            ..#
            ...
            ^..
            .#.
        """.trimIndent().split("\n")

    @Test
    fun testSampleAnswer2h() {
        val map = parseGuardMap(sampleInput8)
        assertEquals(map.guardPos, Coord(0, 3))

//        walkUntilGuardLeaves(map).take(100).forEach { println(it) }

        val newObstacles = putAllObstacles(map)
//        println("Obstacles to place: $newObstacles")
//        println(map.toStringWithObstacles(newObstacles))

        assertContains(newObstacles, Coord(-1, 3))
        assertEquals(1, newObstacles.size)
    }

    // Obstacle blocking old path
    private val sampleInput9 = """
            .#......
            .......#
            >.......
            ........
            #.......
            .....##.
        """.trimIndent().split("\n")

    @Test
    fun testSampleAnswer2i() {
        val map = parseGuardMap(sampleInput9)

//        walkUntilGuardLeaves(map).take(100).forEach { println(it) }

        val newObstacles = putAllObstacles(map)
//        println("Obstacles to place: $newObstacles")
//        println(map.toStringWithObstacles(newObstacles))

        assertContains(newObstacles, Coord(7, 2))
        assertEquals(1, newObstacles.size)
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val map = parseGuardMap(input)
//        println("Guard starting position: ${map.guardPos}")

//        val finalMap = walkUntilGuardLeaves(map).last()
//        println("Final map: $finalMap")

        val newObstacles = putAllObstacles(map)
//        println("Obstacles to place: $newObstacles")

//        println(finalMap.toStringWithObstacles(newObstacles))
        assertNotEquals(412, newObstacles.size)
        assertNotEquals(645, newObstacles.size)
        assertNotEquals(2169, newObstacles.size)
        assertNotEquals(1867, newObstacles.size)
        assertNotEquals(1871, newObstacles.size)
        assertTrue(newObstacles.size > 412)
        assertTrue(newObstacles.size > 645)
        assertTrue(newObstacles.size < 2169)
        assertEquals(1789, newObstacles.size)
    }
}
