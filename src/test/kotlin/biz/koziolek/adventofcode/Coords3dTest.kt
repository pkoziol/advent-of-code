package biz.koziolek.adventofcode

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Coords3dTest {

    @Test
    fun testManhattanDistance() {
        assertEquals(3621, Coord3d(1105, -1205, 1229) manhattanDistanceTo Coord3d(-92, -2380, -20))
        assertEquals(20, Coord3d(-5, -5, 0) manhattanDistanceTo Coord3d(5, 5, 0))
        assertEquals(110, Coord3d(-5, 100, 0) manhattanDistanceTo Coord3d(5, 0, 0))
    }
}
