package biz.koziolek.adventofcode

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class LineTest {

    private val horizontalLine = Line(Coord(9, 4), Coord(3, 4))
    private val verticalLine = Line(Coord(7, 0), Coord(7, 4))
    private val diagonalLine = Line(Coord(6, 4), Coord(2, 0))
    private val randomAngleLine = Line(Coord(4, 40), Coord(10, 99))

    @Test
    fun testConvertingLinesToStringAndBack() {
        val lineString = "1,2 -> 3,4"
        val line = Line.fromString(lineString)

        assertEquals(Coord(1, 2), line.from)
        assertEquals(Coord(3, 4), line.to)
        assertEquals(lineString, line.toString())
    }

    @Test
    fun testGetOpposite() {
        assertEquals(Coord(3, 4), horizontalLine.getOpposite().from)
        assertEquals(Coord(9, 4), horizontalLine.getOpposite().to)

        assertEquals(Coord(7, 4), verticalLine.getOpposite().from)
        assertEquals(Coord(7, 0), verticalLine.getOpposite().to)

        assertEquals(Coord(2, 0), diagonalLine.getOpposite().from)
        assertEquals(Coord(6, 4), diagonalLine.getOpposite().to)

        assertEquals(Coord(10, 99), randomAngleLine.getOpposite().from)
        assertEquals(Coord(4, 40), randomAngleLine.getOpposite().to)
    }

    @Test
    fun testIsHorizontal() {
        assertTrue(horizontalLine.isHorizontal)
        assertTrue(horizontalLine.getOpposite().isHorizontal)

        assertFalse(verticalLine.isHorizontal)
        assertFalse(verticalLine.getOpposite().isHorizontal)

        assertFalse(diagonalLine.isHorizontal)
        assertFalse(diagonalLine.getOpposite().isHorizontal)

        assertFalse(randomAngleLine.isHorizontal)
        assertFalse(randomAngleLine.getOpposite().isHorizontal)
    }

    @Test
    fun testIsVertical() {
        assertFalse(horizontalLine.isVertical)
        assertFalse(horizontalLine.getOpposite().isVertical)

        assertTrue(verticalLine.isVertical)
        assertTrue(verticalLine.getOpposite().isVertical)

        assertFalse(diagonalLine.isVertical)
        assertFalse(diagonalLine.getOpposite().isVertical)

        assertFalse(randomAngleLine.isVertical)
        assertFalse(randomAngleLine.getOpposite().isVertical)
    }

    @Test
    fun testIsDiagonal() {
        assertFalse(horizontalLine.isDiagonal)
        assertFalse(horizontalLine.getOpposite().isDiagonal)

        assertFalse(verticalLine.isDiagonal)
        assertFalse(verticalLine.getOpposite().isDiagonal)

        assertTrue(diagonalLine.isDiagonal)
        assertTrue(diagonalLine.getOpposite().isDiagonal)

        assertFalse(randomAngleLine.isDiagonal)
        assertFalse(randomAngleLine.getOpposite().isDiagonal)
    }

    @Test
    fun testCoveredPoints() {
        assertEquals(
            listOf(
                Coord(9, 4),
                Coord(8, 4),
                Coord(7, 4),
                Coord(6, 4),
                Coord(5, 4),
                Coord(4, 4),
                Coord(3, 4),
            ),
            horizontalLine.getCoveredPoints()
        )

        assertEquals(
            listOf(
                Coord(7, 0),
                Coord(7, 1),
                Coord(7, 2),
                Coord(7, 3),
                Coord(7, 4),
            ),
            verticalLine.getCoveredPoints()
        )

        assertEquals(
            listOf(
                Coord(6, 4),
                Coord(5, 3),
                Coord(4, 2),
                Coord(3, 1),
                Coord(2, 0),
            ),
            diagonalLine.getCoveredPoints()
        )
        
        assertThrowsExactly(IllegalArgumentException::class.java) {
            randomAngleLine.getCoveredPoints()
        }
    }
}
