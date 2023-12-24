package biz.koziolek.adventofcode.year2023.day24

import biz.koziolek.adventofcode.Coord3d
import biz.koziolek.adventofcode.LongCoord
import biz.koziolek.adventofcode.LongCoord3d
import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
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
                Hail(position = LongCoord3d(19, 13, 30), velocity = Coord3d(-2,  1, -2)),
                Hail(position = LongCoord3d(18, 19, 22), velocity = Coord3d(-1, -1, -2)),
                Hail(position = LongCoord3d(20, 25, 34), velocity = Coord3d(-2, -2, -4)),
                Hail(position = LongCoord3d(12, 31, 28), velocity = Coord3d(-1, -2, -1)),
                Hail(position = LongCoord3d(20, 19, 15), velocity = Coord3d(1, -5, -3)),
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

    @Test
    @Disabled("Not finished - doesn't actually find the answer")
    fun testSampleAnswer2() {
        val hails = parseHails(sampleInput)
        val rockPosition = findRockPosition(hails, x = -10..10, y = -10..10, z = -10..0)
        assertEquals(LongCoord3d(24, 13, 10), rockPosition)
        assertEquals(47, rockPosition.x + rockPosition.y + rockPosition.z)
    }

    @Test
    @Disabled("Not finished - doesn't actually find the answer")
    fun testAnswer2b() {
        val input = findInput(object {}).bufferedReader().readLines()
        val hails = parseHails(input)

//        val executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
//
//        for (x in listOf(-2000, -1500, -1000, -500, 0, 500, 1000, 1500)) {
//            for (y in listOf(-2000, -1500, -1000, -500, 0, 500, 1000, 1500)) {
//                for (z in listOf(-2000, -1500, -1000, -500, 0, 500, 1000, 1500)) {
//                    executor.execute {
//                        findRockPosition(
//                            hails,
//                            logPrefix = "%+5d/%+5d/%+5d: ".format(x, y, z),
//                            x = x..(x + 500),
//                            y = y..(y + 500),
//                            z = z..(z + 500),
//                        )
//                    }
//                }
//            }
//        }
//        executor.shutdown()
//        executor.awaitTermination(1, TimeUnit.DAYS)

        val rockPosition = findRockPosition(hails, x = -117..-117, y = -69..-69, z = 281..281)
        assertEquals(786617045860267L, rockPosition.x + rockPosition.y + rockPosition.z)
    }
}
