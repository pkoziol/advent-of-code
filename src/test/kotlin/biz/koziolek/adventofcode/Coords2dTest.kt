package biz.koziolek.adventofcode

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Coords2dTest {

    private val map = mapOf(
        Coord(0, 0) to 1,
        Coord(1, 0) to 2,
        Coord(2, 0) to 3,
        Coord(0, 1) to 4,
        Coord(1, 1) to 5,
        Coord(2, 1) to 6,
        Coord(0, 2) to 7,
        Coord(1, 2) to 8,
        Coord(2, 2) to 9,
        Coord(0, 3) to 10,
        Coord(1, 3) to 11,
        Coord(2, 3) to 12,
    )

    @Test
    fun testCoordPlus() {
        assertEquals(Coord(10, 20), Coord(3, 11) + Coord(7, 9))
    }

    @Test
    fun testCoordMinus() {
        assertEquals(Coord(-4, 2), Coord(3, 11) - Coord(7, 9))
    }

    @Test
    fun testCoordUnaryMinus() {
        assertEquals(Coord(5, -6), -Coord(-5, 6))
    }

    @Test
    fun testZipAsCoord() {
        val coords = (1..4).zipAsCoord(7 downTo 4)

        assertEquals(4, coords.size)
        assertEquals(Coord(1, 7), coords[0])
        assertEquals(Coord(2, 6), coords[1])
        assertEquals(Coord(3, 5), coords[2])
        assertEquals(Coord(4, 4), coords[3])
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
        val corner = Coord(0, 0)

        val expectedAdjacentCorner = setOf(Coord(1, 0), Coord(0, 1))
        assertEquals(expectedAdjacentCorner, map.getAdjacentCoords(corner, includeDiagonal = false))

        val expectedAdjacentCornerDiagonal = expectedAdjacentCorner + setOf(Coord(1, 1))
        assertEquals(expectedAdjacentCornerDiagonal, map.getAdjacentCoords(corner, includeDiagonal = true))

        val edge = Coord(2, 2)

        val expectedAdjacentEdge = setOf(Coord(2, 1), Coord(1, 2), Coord(2, 3))
        assertEquals(expectedAdjacentEdge, map.getAdjacentCoords(edge, includeDiagonal = false))

        val expectedAdjacentEdgeDiagonal = expectedAdjacentEdge + setOf(Coord(1, 1), Coord(1, 3))
        assertEquals(expectedAdjacentEdgeDiagonal, map.getAdjacentCoords(edge, includeDiagonal = true))

        val middle = Coord(1, 1)

        val expectedAdjacentMiddle = setOf(Coord(1, 0), Coord(2, 1), Coord(1, 2), Coord(0, 1))
        assertEquals(expectedAdjacentMiddle, map.getAdjacentCoords(middle, includeDiagonal = false))

        val expectedAdjacentMiddleDiagonal = expectedAdjacentMiddle + setOf(Coord(0, 0), Coord(2, 0), Coord(2, 2), Coord(0, 2))
        assertEquals(expectedAdjacentMiddleDiagonal, map.getAdjacentCoords(middle, includeDiagonal = true))
    }

    @Test
    fun testWalkNorthTo() {
        assertEquals(
            listOf(Coord(10, 19), Coord(10, 18), Coord(10, 17)),
            Coord(10, 20).walkNorthTo(dstY = 17, includeCurrent = false).toList()
        )
        assertEquals(
            listOf(Coord(10, 20), Coord(10, 19), Coord(10, 18), Coord(10, 17)),
            Coord(10, 20).walkNorthTo(dstY = 17, includeCurrent = true).toList()
        )
    }

    @Test
    fun testWalkWestTo() {
        assertEquals(
            listOf(Coord(9, 20), Coord(8, 20), Coord(7, 20)),
            Coord(10, 20).walkWestTo(dstX = 7, includeCurrent = false).toList()
        )
        assertEquals(
            listOf(Coord(10, 20), Coord(9, 20), Coord(8, 20), Coord(7, 20)),
            Coord(10, 20).walkWestTo(dstX = 7, includeCurrent = true).toList()
        )
    }

    @Test
    fun testWalkSouthTo() {
        assertEquals(
            listOf(Coord(10, 21), Coord(10, 22), Coord(10, 23)),
            Coord(10, 20).walkSouthTo(dstY = 23, includeCurrent = false).toList()
        )
        assertEquals(
            listOf(Coord(10, 20), Coord(10, 21), Coord(10, 22), Coord(10, 23)),
            Coord(10, 20).walkSouthTo(dstY = 23, includeCurrent = true).toList()
        )
    }

    @Test
    fun testWalkEastTo() {
        assertEquals(
            listOf(Coord(11, 20), Coord(12, 20), Coord(13, 20)),
            Coord(10, 20).walkEastTo(dstX = 13, includeCurrent = false).toList()
        )
        assertEquals(
            listOf(Coord(10, 20), Coord(11, 20), Coord(12, 20), Coord(13, 20)),
            Coord(10, 20).walkEastTo(dstX = 13, includeCurrent = true).toList()
        )
    }

    @Test
    fun testMapWalkSouth() {
        val map = mapOf(
            Coord(0, 0) to 'X',
            Coord(3, 1) to 'X',
        )
        assertEquals(4, map.getWidth())
        assertEquals(2, map.getHeight())
        assertEquals(
            listOf(
                Coord(0, 0),
                Coord(1, 0),
                Coord(2, 0),
                Coord(3, 0),
                Coord(0, 1),
                Coord(1, 1),
                Coord(2, 1),
                Coord(3, 1),
            ),
            map.walkSouth().toList()
        )
    }

    @Test
    fun testMapWalkEast() {
        val map = mapOf(
            Coord(0, 0) to 'X',
            Coord(3, 1) to 'X',
        )
        assertEquals(4, map.getWidth())
        assertEquals(2, map.getHeight())
        assertEquals(
            listOf(
                Coord(0, 1),
                Coord(0, 0),
                Coord(1, 1),
                Coord(1, 0),
                Coord(2, 1),
                Coord(2, 0),
                Coord(3, 1),
                Coord(3, 0),
            ),
            map.walkEast().toList()
        )
    }

    @Test
    fun testMapWalkNorth() {
        val map = mapOf(
            Coord(0, 0) to 'X',
            Coord(3, 1) to 'X',
        )
        assertEquals(4, map.getWidth())
        assertEquals(2, map.getHeight())
        assertEquals(
            listOf(
                Coord(3, 1),
                Coord(2, 1),
                Coord(1, 1),
                Coord(0, 1),
                Coord(3, 0),
                Coord(2, 0),
                Coord(1, 0),
                Coord(0, 0),
            ),
            map.walkNorth().toList()
        )
    }

    @Test
    fun testMapWalkWest() {
        val map = mapOf(
            Coord(0, 0) to 'X',
            Coord(3, 1) to 'X',
        )
        assertEquals(4, map.getWidth())
        assertEquals(2, map.getHeight())
        assertEquals(
            listOf(
                Coord(3, 0),
                Coord(3, 1),
                Coord(2, 0),
                Coord(2, 1),
                Coord(1, 0),
                Coord(1, 1),
                Coord(0, 0),
                Coord(0, 1),
            ),
            map.walkWest().toList()
        )
    }
}
