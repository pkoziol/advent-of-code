package biz.koziolek.adventofcode

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Coords2dLongTest {

    private val map = mapOf(
        LongCoord(0, 0) to 1,
        LongCoord(1, 0) to 2,
        LongCoord(2, 0) to 3,
        LongCoord(0, 1) to 4,
        LongCoord(1, 1) to 5,
        LongCoord(2, 1) to 6,
        LongCoord(0, 2) to 7,
        LongCoord(1, 2) to 8,
        LongCoord(2, 2) to 9,
        LongCoord(0, 3) to 10,
        LongCoord(1, 3) to 11,
        LongCoord(2, 3) to 12,
    )

    @Test
    fun testCoordPlus() {
        assertEquals(LongCoord(10, 20), LongCoord(3, 11) + LongCoord(7, 9))
    }

    @Test
    fun testCoordMinus() {
        assertEquals(LongCoord(-4, 2), LongCoord(3, 11) - LongCoord(7, 9))
    }

    @Test
    fun testCoordUnaryMinus() {
        assertEquals(LongCoord(5, -6), -LongCoord(-5, 6))
    }

    @Test
    fun testZipAsCoord() {
        val coords = (1L..4L).zipAsCoord(7L downTo 4L)

        assertEquals(4, coords.size)
        assertEquals(LongCoord(1, 7), coords[0])
        assertEquals(LongCoord(2, 6), coords[1])
        assertEquals(LongCoord(3, 5), coords[2])
        assertEquals(LongCoord(4, 4), coords[3])
    }

    @Test
    fun testMapGetWidth() {
        assertEquals(3, map.getWidth())
    }

    @Test
    fun testMapGetHeight() {
        assertEquals(4, map.getHeight())
    }

    @Test
    fun testMapGetAdjacentCoords() {
        val corner = LongCoord(0, 0)

        val expectedAdjacentCorner = setOf(LongCoord(1, 0), LongCoord(0, 1))
        assertEquals(expectedAdjacentCorner, map.getAdjacentCoords(corner, includeDiagonal = false))

        val expectedAdjacentCornerDiagonal = expectedAdjacentCorner + setOf(LongCoord(1, 1))
        assertEquals(expectedAdjacentCornerDiagonal, map.getAdjacentCoords(corner, includeDiagonal = true))

        val edge = LongCoord(2, 2)

        val expectedAdjacentEdge = setOf(LongCoord(2, 1), LongCoord(1, 2), LongCoord(2, 3))
        assertEquals(expectedAdjacentEdge, map.getAdjacentCoords(edge, includeDiagonal = false))

        val expectedAdjacentEdgeDiagonal = expectedAdjacentEdge + setOf(LongCoord(1, 1), LongCoord(1, 3))
        assertEquals(expectedAdjacentEdgeDiagonal, map.getAdjacentCoords(edge, includeDiagonal = true))

        val middle = LongCoord(1, 1)

        val expectedAdjacentMiddle = setOf(LongCoord(1, 0), LongCoord(2, 1), LongCoord(1, 2), LongCoord(0, 1))
        assertEquals(expectedAdjacentMiddle, map.getAdjacentCoords(middle, includeDiagonal = false))

        val expectedAdjacentMiddleDiagonal = expectedAdjacentMiddle + setOf(LongCoord(0, 0), LongCoord(2, 0), LongCoord(2, 2), LongCoord(0, 2))
        assertEquals(expectedAdjacentMiddleDiagonal, map.getAdjacentCoords(middle, includeDiagonal = true))
    }
}
