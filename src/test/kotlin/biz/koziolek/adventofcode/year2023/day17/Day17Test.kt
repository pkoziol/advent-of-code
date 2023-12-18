package biz.koziolek.adventofcode.year2023.day17

import biz.koziolek.adventofcode.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2023")
internal class Day17Test {

    private val sampleInput = """
            2413432311323
            3215453535623
            3255245654254
            3446585845452
            4546657867536
            1438598798454
            4457876987766
            3637877979653
            4654967986887
            4564679986453
            1224686865563
            2546548887735
            4322674655533
        """.trimIndent().split("\n")

    @Test
    fun testParseHeatLossMap() {
        val map = parseHeatLossMap(sampleInput)
        assertEquals(13, map.getWidth())
        assertEquals(13, map.getHeight())
    }

    @Test
    fun testIsNotTooLongStraight() {
        assertTrue(PathElement(
            coord = Coord(5, 2),
            currentDirection = Direction.WEST,
            nextDirection = null,
            totalHeatLoss = 123,
            next = emptyList(),
        ).isNotTooLongStraight())

        assertTrue(PathElement(
            coord = Coord(5, 2),
            currentDirection = Direction.WEST,
            nextDirection = null,
            totalHeatLoss = 123,
            next = listOf(
                Coord(4, 2) to Direction.WEST,
            ),
        ).isNotTooLongStraight())

        assertTrue(PathElement(
            coord = Coord(5, 2),
            currentDirection = Direction.WEST,
            nextDirection = null,
            totalHeatLoss = 123,
            next = listOf(
                Coord(4, 2) to Direction.WEST,
                Coord(3, 2) to Direction.WEST,
            ),
        ).isNotTooLongStraight())

        assertFalse(PathElement(
            coord = Coord(5, 2),
            currentDirection = Direction.WEST,
            nextDirection = null,
            totalHeatLoss = 123,
            next = listOf(
                Coord(4, 2) to Direction.WEST,
                Coord(3, 2) to Direction.WEST,
                Coord(2, 2) to Direction.WEST,
            ),
        ).isNotTooLongStraight())

        assertFalse(PathElement(
            coord = Coord(5, 2),
            currentDirection = Direction.WEST,
            nextDirection = null,
            totalHeatLoss = 123,
            next = listOf(
                Coord(4, 2) to Direction.WEST,
                Coord(3, 2) to Direction.WEST,
                Coord(2, 2) to Direction.WEST,
                Coord(1, 2) to Direction.WEST,
            ),
        ).isNotTooLongStraight())

        assertTrue(PathElement(
            coord = Coord(5, 2),
            currentDirection = Direction.WEST,
            nextDirection = null,
            totalHeatLoss = 123,
            next = listOf(
                Coord(4, 2) to Direction.WEST,
                Coord(3, 2) to Direction.WEST,
                Coord(2, 2) to Direction.NORTH,
            ),
        ).isNotTooLongStraight())
    }

    @Test
    fun testSampleAnswer1() {
        val map = parseHeatLossMap(sampleInput)
        val bestPath = findMinimalHeatLossPath(map)
        println(showPath(map, bestPath))
        assertEquals(102, bestPath.sumOf { map[it.first]!! })
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val map = parseHeatLossMap(input)
        val bestPath = findMinimalHeatLossPath(map)
        assertEquals(-1, bestPath.sumOf { map[it.first]!! })
    }
}
