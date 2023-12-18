package biz.koziolek.adventofcode.year2023.day18

import biz.koziolek.adventofcode.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2023")
internal class Day18Test {

    private val sampleInput = """
            R 6 (#70c710)
            D 5 (#0dc571)
            L 2 (#5713f0)
            D 2 (#d2c081)
            R 2 (#59c680)
            D 2 (#411b91)
            L 5 (#8ceee2)
            U 2 (#caa173)
            L 1 (#1b58a2)
            U 2 (#caa171)
            R 2 (#7807d2)
            U 3 (#a77fa3)
            L 2 (#015232)
            U 2 (#7a21e3)
        """.trimIndent().split("\n")

    @Test
    fun testParse() {
        val digPlan = parseDigPlan(sampleInput)
        assertEquals(
            listOf(
                DigPlanEntry(direction = Direction.EAST, distance = 6),
                DigPlanEntry(direction = Direction.SOUTH, distance = 5),
                DigPlanEntry(direction = Direction.WEST, distance = 2),
                DigPlanEntry(direction = Direction.SOUTH, distance = 2),
                DigPlanEntry(direction = Direction.EAST, distance = 2),
                DigPlanEntry(direction = Direction.SOUTH, distance = 2),
                DigPlanEntry(direction = Direction.WEST, distance = 5),
                DigPlanEntry(direction = Direction.NORTH, distance = 2),
                DigPlanEntry(direction = Direction.WEST, distance = 1),
                DigPlanEntry(direction = Direction.NORTH, distance = 2),
                DigPlanEntry(direction = Direction.EAST, distance = 2),
                DigPlanEntry(direction = Direction.NORTH, distance = 3),
                DigPlanEntry(direction = Direction.WEST, distance = 2),
                DigPlanEntry(direction = Direction.NORTH, distance = 2),
            ),
            digPlan
        )
    }

    @Test
    fun testBuildTrenchEdge() {
        val digPlan = parseDigPlan(sampleInput)
        val trenchEdge = buildTrenchEdge(digPlan)
        assertEquals(
            """
                F.....7
                .......
                L.7....
                .......
                .......
                F.J.F.J
                .......
                L7..L.7
                .......
                .L....J
            """.trimIndent(),
            trenchEdge.to2DString { _, c ->
                c ?: LEVEL_TERRAIN
            }
        )
    }

    @Test
    fun testSumEvenPairDifferences() {
        assertEquals(102033, sumEvenPairDifferences(
            setOf(50, 20, 800000, 900000, 102000, 100000)
        ))
    }

    @Test
    fun testSampleAnswer1() {
        val digPlan = parseDigPlan(sampleInput)
        val trenchEdge = buildTrenchEdge(digPlan)
//        printTrenchEdge(trenchEdge)
        assertEquals(62, calculateTrenchVolume(trenchEdge))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val digPlan = parseDigPlan(input)
        val trenchEdge = buildTrenchEdge(digPlan)
//        printTrenchEdge(trenchEdge)
        assertEquals(40131, calculateTrenchVolume(trenchEdge))
    }

    @Test
    fun testParseV2() {
        val digPlan = parseDigPlanV2(sampleInput)
        assertEquals(
            listOf(
                DigPlanEntry(direction = Direction.EAST, distance = 461937),
                DigPlanEntry(direction = Direction.SOUTH, distance = 56407),
                DigPlanEntry(direction = Direction.EAST, distance = 356671),
                DigPlanEntry(direction = Direction.SOUTH, distance = 863240),
                DigPlanEntry(direction = Direction.EAST, distance = 367720),
                DigPlanEntry(direction = Direction.SOUTH, distance = 266681),
                DigPlanEntry(direction = Direction.WEST, distance = 577262),
                DigPlanEntry(direction = Direction.NORTH, distance = 829975),
                DigPlanEntry(direction = Direction.WEST, distance = 112010),
                DigPlanEntry(direction = Direction.SOUTH, distance = 829975),
                DigPlanEntry(direction = Direction.WEST, distance = 491645),
                DigPlanEntry(direction = Direction.NORTH, distance = 686074),
                DigPlanEntry(direction = Direction.WEST, distance = 5411),
                DigPlanEntry(direction = Direction.NORTH, distance = 500254),
            ),
            digPlan
        )
    }

    @Test
    fun testSampleAnswer2() {
        val digPlan = parseDigPlanV2(sampleInput)
        val trenchEdge = buildTrenchEdge(digPlan)
        assertEquals(952_408_144_115, calculateTrenchVolume(trenchEdge))
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val digPlan = parseDigPlanV2(input)
        val trenchEdge = buildTrenchEdge(digPlan)
        assertEquals(104_454_050_898_331, calculateTrenchVolume(trenchEdge))
    }

    private fun printTrenchEdge(trenchEdge: Map<Coord, Char>) {
        val str2d = trenchEdge.to2DStringOfStrings { coord, c ->
            val s = (c ?: LEVEL_TERRAIN).toString()

            if (coord.x == 0 || coord.y == 0) {
                AsciiColor.BRIGHT_WHITE.format(s)
            } else {
                s
            }
        }
        println(str2d)
    }
}
