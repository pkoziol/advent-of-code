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
                DigPlanEntry(direction = Direction.EAST, distance = 6, color = "#70c710"),
                DigPlanEntry(direction = Direction.SOUTH, distance = 5, color = "#0dc571"),
                DigPlanEntry(direction = Direction.WEST, distance = 2, color = "#5713f0"),
                DigPlanEntry(direction = Direction.SOUTH, distance = 2, color = "#d2c081"),
                DigPlanEntry(direction = Direction.EAST, distance = 2, color = "#59c680"),
                DigPlanEntry(direction = Direction.SOUTH, distance = 2, color = "#411b91"),
                DigPlanEntry(direction = Direction.WEST, distance = 5, color = "#8ceee2"),
                DigPlanEntry(direction = Direction.NORTH, distance = 2, color = "#caa173"),
                DigPlanEntry(direction = Direction.WEST, distance = 1, color = "#1b58a2"),
                DigPlanEntry(direction = Direction.NORTH, distance = 2, color = "#caa171"),
                DigPlanEntry(direction = Direction.EAST, distance = 2, color = "#7807d2"),
                DigPlanEntry(direction = Direction.NORTH, distance = 3, color = "#a77fa3"),
                DigPlanEntry(direction = Direction.WEST, distance = 2, color = "#015232"),
                DigPlanEntry(direction = Direction.NORTH, distance = 2, color = "#7a21e3"),
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
                #######
                #.....#
                ###...#
                ..#...#
                ..#...#
                ###.###
                #...#..
                ##..###
                .#....#
                .######
            """.trimIndent(),
            trenchEdge.to2DString { _, c ->
                if (c != null) TRENCH else LEVEL_TERRAIN
            }
        )
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
