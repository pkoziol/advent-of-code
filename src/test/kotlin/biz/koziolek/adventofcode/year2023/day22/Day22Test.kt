package biz.koziolek.adventofcode.year2023.day22

import biz.koziolek.adventofcode.Coord3d
import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2023")
internal class Day22Test {

    private val sampleInput = """
            1,0,1~1,2,1
            0,0,2~2,0,2
            0,2,3~2,2,3
            0,0,4~0,2,4
            2,0,5~2,2,5
            0,1,6~2,1,6
            1,1,8~1,1,9
        """.trimIndent().split("\n")

    @Test
    fun testParse() {
        val bricks = parseBricks(sampleInput)
        assertEquals(
            listOf(
                Brick(from = Coord3d(1, 0, 1), to = Coord3d(1, 2, 1)),
                Brick(from = Coord3d(0, 0, 2), to = Coord3d(2, 0, 2)),
                Brick(from = Coord3d(0, 2, 3), to = Coord3d(2, 2, 3)),
                Brick(from = Coord3d(0, 0, 4), to = Coord3d(0, 2, 4)),
                Brick(from = Coord3d(2, 0, 5), to = Coord3d(2, 2, 5)),
                Brick(from = Coord3d(0, 1, 6), to = Coord3d(2, 1, 6)),
                Brick(from = Coord3d(1, 1, 8), to = Coord3d(1, 1, 9)),
            ),
            bricks
        )
    }

    @Test
    fun testSampleAnswer1() {
        val bricks = parseBricks(sampleInput)
        val safeToDisintegrate = findSafeToDisintegrate(bricks)
//        println(safeToDisintegrate.joinToString("\n") { getFriendlyName(it) })
        assertEquals(5, safeToDisintegrate.size)
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val bricks = parseBricks(input)
        assertEquals(465, findSafeToDisintegrate(bricks).size)
    }
}

