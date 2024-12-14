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

//        for (i in 0..200) {
//            val mapI = simulate(map, seconds = i)
//            println("After $i second${if (i > 1) "s" else ""}:")
//            println(mapI.toString() + "\n")
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

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val map = parseRobotMap(input)

//        gatherStats(map)
//        printMatrix(map)

        val secondsToATree = findTree(map)
        assertEquals(6285, secondsToATree)
    }

    private fun gatherStats(map: RobotMap) {
        val stats = mutableMapOf<Int, Int>()
        fun getRegex(count: Int) =
            Regex(".*[0-9*]" + "\\.[0-9*]".repeat(count - 1) + ".*")

        for (i in 1..10403) {
            val mapI = simulate(map, seconds = i)
            val mapString = mapI.toString()

//            for (j in 20 downTo 4) {
//                if (toString.lines().any { it.matches(getRegex(j)) }) {
//                    stats[j] = stats[j]?.plus(1) ?: 1
//                    break
//                }
//            }

            val emptyLinesCount = mapString.lines().count { it.all { c -> c == '.' } }
            val fullLinesCount = mapString.lines().count { it.all { c -> c != '.' } }
            val longestConsecutive = mapString.lines().map { line -> line.split('.').maxOfOrNull { it.length } ?: 0 }.maxOrNull() ?: 0
            stats[longestConsecutive] = stats[longestConsecutive]?.plus(1) ?: 1

            if (longestConsecutive == 31) {
                println("After $i second${if (i > 1) "s" else ""}:")
                println(mapString)
                println()
            }

//            if (toString.lines().any { line -> line.matches(getRegex(6)) }) {
//                println("After $i second${if (i > 1) "s" else ""}:")
//                println(toString + "\n")
//            }

//            if (mapI == map) {
//                println("After $i seconds the map is the same as at the beginning")
//                break
//            }
        }

        stats.entries
            .sortedByDescending { it.key }
            .forEach { println("Pattern ${it.key} appears ${it.value} times") }
    }

    private fun printMatrix(map: RobotMap, cols: Int, rows: Int, sleepMs: Long) {
        var i = 1
        var printedLinesCount = 0
        val ESC = Char(27)

        print("$ESC[H$ESC[2J")
        System.out.flush()

        while (i <= 11403) {
            print("$ESC[${printedLinesCount}A")
            println("Seconds $i - ${i + cols * rows}")
            printedLinesCount += 1

            repeat(rows) {
                val lines = mutableListOf<String>()
                repeat(cols) {
                    val mapI = simulate(map, seconds = i)
                    val mapString = mapI.toString()
                    val mapLines = mapString.lines()

                    if (lines.isEmpty()) {
                        lines.addAll(mapLines)
                    } else {
                        for (j in lines.indices) {
                            lines[j] += "  " + mapLines[j]
                        }
                    }

                    i++
                }
                lines.forEach { println(it) }
                println()
                printedLinesCount += lines.size + 1
            }

            System.out.flush()
            Thread.sleep(sleepMs)
        }
    }
}
