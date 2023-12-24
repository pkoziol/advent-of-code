package biz.koziolek.adventofcode.year2023.day23

import biz.koziolek.adventofcode.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2023")
internal class Day23Test {

    private val sampleInput = """
            #.#####################
            #.......#########...###
            #######.#########.#.###
            ###.....#.>.>.###.#.###
            ###v#####.#v#.###.#.###
            ###.>...#.#.#.....#...#
            ###v###.#.#.#########.#
            ###...#.#.#.......#...#
            #####.#.#.#######.#.###
            #.....#.#.#.......#...#
            #.#####.#.#.#########v#
            #.#...#...#...###...>.#
            #.#.#v#######v###.###v#
            #...#.>.#...>.>.#.###.#
            #####v#.#.###v#.#.###.#
            #.....#...#...#.#.#...#
            #.#########.###.#.#.###
            #...###...#...#...#.###
            ###.###.#.###v#####v###
            #...#...#.#.>.>.#.>.###
            #.###.###.#.###.#.#v###
            #.....###...###...#...#
            #####################.#
        """.trimIndent().split("\n")

    @Test
    fun testParse() {
        val hikingTrails = parseHikingTrails(sampleInput)
        assertEquals(213, hikingTrails.size)
    }

    @Test
    fun testSampleAnswer1() {
        val hikingTrails = parseHikingTrails(sampleInput)
        assertEquals(94, findLongestPathLen(hikingTrails))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val hikingTrails = parseHikingTrails(input)
        assertEquals(2502, findLongestPathLen(hikingTrails))
    }

    @Test
    fun testSampleAnswer2() {
        val graph = parseHikingTrailsAsGraph(sampleInput)
//        generateGraphvizSvg(
//            graph.simplify().toGraphvizString(layout = "neato", exactXYPosition = true, xyPositionScale = .2f, edgeWeightAsLabel = true, exactEdgeLength = true, edgeLengthScale = .2f),
//            File("src/test/kotlin/biz/koziolek/adventofcode/year2023/day23/graph-2-sample.svg")
//        )

        assertEquals(154, findLongestPathLen(graph))
    }

    @Test
    @Disabled("Takes 33s to finish on my machine")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val graph = parseHikingTrailsAsGraph(input)
//        generateGraphvizSvg(
//            graph.simplify().toGraphvizString(layout = "neato", exactXYPosition = true, xyPositionScale = .1f, edgeWeightAsLabel = true, exactEdgeLength = true, edgeLengthScale = .1f),
//            File("src/test/kotlin/biz/koziolek/adventofcode/year2023/day23/graph-2.svg")
//        )

        assertEquals(6726, findLongestPathLen(graph))
    }
}
