package biz.koziolek.adventofcode.year2024.day14

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2024")
internal class Day14Test {

    private val sampleInput = """
            p=0,4 v=3,-3
            p=6,3 v=-1,-3
            p=10,3 v=-1,2
            p=2,0 v=2,-1
            p=0,0 v=1,3
            p=3,0 v=-2,-2
            p=7,6 v=-1,-3
            p=3,0 v=-1,-2
            p=9,3 v=2,3
            p=7,3 v=-1,2
            p=2,4 v=2,-3
            p=9,5 v=-3,-3
        """.trimIndent().split("\n")

    @Test
    fun testParseRobotsMap() {
        val map = parseRobotMap(sampleInput, width = 11, height = 7)
        assertEquals(
            """
                1.12.......
                ...........
                ...........
                ......11.11
                1.1........
                .........1.
                .......1...
            """.trimIndent(),
            map.toString()
        )
    }

    @Test
    fun testTeleport() {
        val map = RobotMap(emptyList(), width = 5, height = 3)
        assertEquals(Coord(2, 0), Robot(Coord(2, 0), Coord(0, 0)).teleport(map).position)
        assertEquals(Coord(1, 0), Robot(Coord(1, 0), Coord(0, 0)).teleport(map).position)
        assertEquals(Coord(0, 0), Robot(Coord(0, 0), Coord(0, 0)).teleport(map).position)
        assertEquals(Coord(4, 0), Robot(Coord(-1, 0), Coord(0, 0)).teleport(map).position)
        assertEquals(Coord(3, 0), Robot(Coord(-2, 0), Coord(0, 0)).teleport(map).position)
        assertEquals(Coord(2, 0), Robot(Coord(-3, 0), Coord(0, 0)).teleport(map).position)
        assertEquals(Coord(1, 0), Robot(Coord(-4, 0), Coord(0, 0)).teleport(map).position)
        assertEquals(Coord(0, 0), Robot(Coord(-5, 0), Coord(0, 0)).teleport(map).position)
        assertEquals(Coord(4, 0), Robot(Coord(-6, 0), Coord(0, 0)).teleport(map).position)
        assertEquals(Coord(3, 0), Robot(Coord(-7, 0), Coord(0, 0)).teleport(map).position)
        assertEquals(Coord(2, 0), Robot(Coord(-8, 0), Coord(0, 0)).teleport(map).position)
        assertEquals(Coord(1, 0), Robot(Coord(-9, 0), Coord(0, 0)).teleport(map).position)
        assertEquals(Coord(0, 0), Robot(Coord(-10, 0), Coord(0, 0)).teleport(map).position)
    }

    @Test
    fun testSimulate() {
        val map = parseRobotMap(sampleInput, width = 11, height = 7)

//        for (i in 100..100) {
//            val mapI = simulate(map, seconds = i)
//            println("After $i second${if (i > 1) "s" else ""}:")
//            println(mapI.toString() + "\n")
//            mapI.robots.forEach { robot ->
//                println(robot.position)
//                val x = robot.position.x
//                val y = robot.position.y
//                assertTrue(x in 0 until mapI.width)
//                assertTrue(y in 0 until mapI.height)
//            }
//        }

        val map100 = simulate(map, seconds = 100)
        assertEquals(
            """
                ......2..1.
                ...........
                1..........
                .11........
                .....1.....
                ...12......
                .1....1....
            """.trimIndent(),
            map100.toString()
        )
    }

    @Test
    fun testSafetyFactor() {
        val map = parseRobotMap(sampleInput, width = 11, height = 7)
        val map100 = simulate(map, seconds = 100)
        assertEquals(12, map100.calculateSafetyFactor())
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val map = parseRobotMap(input)
        val map100 = simulate(map, seconds = 100)
        assertEquals(215476074, map100.calculateSafetyFactor())
    }
}
