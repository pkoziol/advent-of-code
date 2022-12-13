package biz.koziolek.adventofcode.year2022.day12

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput
import biz.koziolek.adventofcode.getHeight
import biz.koziolek.adventofcode.getWidth
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2022")
internal class Day12Test {

    private val sampleInput = """
            Sabqponm
            abcryxxl
            accszExk
            acctuvwj
            abdefghi
        """.trimIndent().split("\n")

    @Test
    fun testParseHeightMap() {
        val heightMap = parseHeightMap(sampleInput)
        assertEquals(8, heightMap.getWidth())
        assertEquals(5, heightMap.getHeight())
    }

    @Test
    fun testLabelToHeight() {
        assertEquals(0, labelToHeight('S'))
        assertEquals(25, labelToHeight('E'))
        assertEquals(0, labelToHeight('a'))
        assertEquals(1, labelToHeight('b'))
        assertEquals(2, labelToHeight('c'))
    }

    @Test
    fun testBuildElevationGraph() {
        val heightMap = parseHeightMap(sampleInput)
        val elevationGraph = buildElevationGraph(heightMap)
        assertEquals(40, elevationGraph.nodes.size)

        val start = ElevationNode(
            label = 'S',
            height = 0,
            coord = Coord(0, 0),
        )
        assertEquals(2, elevationGraph.getAdjacentNodes(start).size)

        val t = ElevationNode(
            label = 't',
            height = 19,
            coord = Coord(3, 3),
        )
        val tAdjacentNodes = elevationGraph.getAdjacentNodes(t)
        assertEquals(
            setOf(
                ElevationNode(
                    label = 's',
                    height = 18,
                    coord = Coord(3, 2),
                ),
                ElevationNode(
                    label = 'u',
                    height = 20,
                    coord = Coord(4, 3),
                ),
                ElevationNode(
                    label = 'e',
                    height = 4,
                    coord = Coord(3, 4),
                ),
                ElevationNode(
                    label = 'c',
                    height = 2,
                    coord = Coord(2, 3),
                ),
            ),
            tAdjacentNodes
        )
        
        println(elevationGraph.toGraphvizString())
    }

    @Test
    fun testSamplePath() {
        val heightMap = parseHeightMap(sampleInput)
        val elevationGraph = buildElevationGraph(heightMap)
        assertEquals(31, findFewestStepsFromStartToEnd(elevationGraph))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val heightMap = parseHeightMap(input)
        val elevationGraph = buildElevationGraph(heightMap)
        assertEquals(528, findFewestStepsFromStartToEnd(elevationGraph))
    }
}
