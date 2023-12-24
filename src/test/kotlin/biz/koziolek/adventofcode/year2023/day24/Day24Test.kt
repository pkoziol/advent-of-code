package biz.koziolek.adventofcode.year2023.day24

import biz.koziolek.adventofcode.Coord3d
import biz.koziolek.adventofcode.LongCoord
import biz.koziolek.adventofcode.LongCoord3d
import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2023")
internal class Day24Test {

    private val sampleInput = """
            19, 13, 30 @ -2,  1, -2
            18, 19, 22 @ -1, -1, -2
            20, 25, 34 @ -2, -2, -4
            12, 31, 28 @ -1, -2, -1
            20, 19, 15 @  1, -5, -3
        """.trimIndent().split("\n")

    private val sampleTestArea = LongCoord(7, 7) to LongCoord(27, 27)

    @Test
    fun testParse() {
        val hails = parseHails(sampleInput)
        assertEquals(
            listOf(
                Hail(position = LongCoord3d(19, 13, 30), velocity = Coord3d(-2,  1, -2), time = 1),
                Hail(position = LongCoord3d(18, 19, 22), velocity = Coord3d(-1, -1, -2), time = 1),
                Hail(position = LongCoord3d(20, 25, 34), velocity = Coord3d(-2, -2, -4), time = 1),
                Hail(position = LongCoord3d(12, 31, 28), velocity = Coord3d(-1, -2, -1), time = 1),
                Hail(position = LongCoord3d(20, 19, 15), velocity = Coord3d(1, -5, -3), time = 1),
            ),
            hails
        )
    }

    @Test
    fun testSampleAnswer1() {
        val hails = parseHails(sampleInput)
        assertEquals(2, countFutureIntersections(hails, testArea = sampleTestArea))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val hails = parseHails(input)
        assertEquals(28266, countFutureIntersections(hails, testArea = testArea))
    }
}
