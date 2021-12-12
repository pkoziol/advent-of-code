package biz.koziolek.adventofcode.year2021.day12

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class Day12Test {

    private val sampleInput1 = """
        start-A
        start-b
        A-c
        A-b
        b-d
        A-end
        b-end
    """.trimIndent().split("\n")

    private val sampleInput2 = """
        dc-end
        HN-start
        start-kj
        dc-start
        dc-HN
        LN-dc
        HN-end
        kj-sa
        kj-HN
        kj-dc
    """.trimIndent().split("\n")

    private val sampleInput3 = """
        fs-end
        he-DX
        fs-he
        start-DX
        pj-DX
        end-zg
        zg-sl
        zg-pj
        pj-he
        RW-he
        fs-DX
        pj-RW
        zg-RW
        start-pj
        he-WI
        zg-he
        pj-fs
        start-RW
    """.trimIndent().split("\n")

    @Test
    fun testEdgeEquality() {
        val a = SmallCave("a")
        val b = SmallCave("b")
        assertEquals(GraphEdge(a, b), GraphEdge(b, a))
    }

    @Test
    fun testParseGraphs() {
        val graph1 = parseCavesGraph(sampleInput1)
        println(graph1.toGraphvizString())

        assertEquals(7, graph1.edges.size)
        assertEquals(6, graph1.nodes.size)

        val graph2 = parseCavesGraph(sampleInput2)
        println(graph2.toGraphvizString())

        assertEquals(10, graph2.edges.size)
        assertEquals(7, graph2.nodes.size)

        val graph3 = parseCavesGraph(sampleInput3)
        println(graph3.toGraphvizString())

        assertEquals(18, graph3.edges.size)
        assertEquals(10, graph3.nodes.size)

        val fullInput = findInput(object {}).readLines()
        val graph4 = parseCavesGraph(fullInput)
        println(graph4.toGraphvizString())

        assertEquals(21, graph4.edges.size)
        assertEquals(12, graph4.nodes.size)
    }

    @Test
    fun testGetAdjacentNodes() {
        val graph = parseCavesGraph(sampleInput1)

        assertEquals(setOf(
            BigCave("A"),
            SmallCave("b"),
        ), graph.getAdjacentNodes(StartCave))

        assertEquals(setOf(
            StartCave,
            SmallCave("b"),
            SmallCave("c"),
            EndCave,
        ), graph.getAdjacentNodes(BigCave("A")))

        assertEquals(setOf(
            StartCave,
            BigCave("A"),
            SmallCave("d"),
            EndCave,
        ), graph.getAdjacentNodes(SmallCave("b")))

        assertEquals(setOf(
            BigCave("A"),
        ), graph.getAdjacentNodes(SmallCave("c")))

        assertEquals(setOf(
            SmallCave("b"),
        ), graph.getAdjacentNodes(SmallCave("d")))

        assertEquals(setOf(
            BigCave("A"),
            SmallCave("b"),
        ), graph.getAdjacentNodes(EndCave))
    }

    @Test
    fun testFindAllPathsVisitingSmallCavesOnlyOnce() {
        val graph1 = parseCavesGraph(sampleInput1)
        val paths1 = findAllPaths(graph1, ::visitSmallCavesOnlyOnce)
        val expectedPaths1 = setOf(
            "start,A,b,A,c,A,end",
            "start,A,b,A,end",
            "start,A,b,end",
            "start,A,c,A,b,A,end",
            "start,A,c,A,b,end",
            "start,A,c,A,end",
            "start,A,end",
            "start,b,A,c,A,end",
            "start,b,A,end",
            "start,b,end"
        )
        assertEquals(expectedPaths1, paths1.map { path -> path.joinToString(separator = ",") { node -> node.id } }.toSet())

        val graph2 = parseCavesGraph(sampleInput2)
        val paths2 = findAllPaths(graph2, ::visitSmallCavesOnlyOnce)
        val expectedPaths2 = setOf(
            "start,HN,dc,HN,end",
            "start,HN,dc,HN,kj,HN,end",
            "start,HN,dc,end",
            "start,HN,dc,kj,HN,end",
            "start,HN,end",
            "start,HN,kj,HN,dc,HN,end",
            "start,HN,kj,HN,dc,end",
            "start,HN,kj,HN,end",
            "start,HN,kj,dc,HN,end",
            "start,HN,kj,dc,end",
            "start,dc,HN,end",
            "start,dc,HN,kj,HN,end",
            "start,dc,end",
            "start,dc,kj,HN,end",
            "start,kj,HN,dc,HN,end",
            "start,kj,HN,dc,end",
            "start,kj,HN,end",
            "start,kj,dc,HN,end",
            "start,kj,dc,end",
        )
        assertEquals(expectedPaths2, paths2.map { path -> path.joinToString(separator = ",") { node -> node.id } }.toSet())

        val graph3 = parseCavesGraph(sampleInput3)
        val paths3 = findAllPaths(graph3, ::visitSmallCavesOnlyOnce)
        assertEquals(226, paths3.size)
    }

    @Test
    fun testAnswer1() {
        val fullInput = findInput(object {}).readLines()
        val graph = parseCavesGraph(fullInput)
        val paths = findAllPaths(graph, ::visitSmallCavesOnlyOnce)
        assertEquals(4749, paths.size)
    }

    @Test
    fun testFindAllPathsVisitingAtMostOneSmallCaveTwice() {
        val graph1 = parseCavesGraph(sampleInput1)
        val paths1 = findAllPaths(graph1, ::visitAtMostOneSmallCaveTwice)
        val expectedPaths1 = setOf(
            "start,A,b,A,b,A,c,A,end",
            "start,A,b,A,b,A,end",
            "start,A,b,A,b,end",
            "start,A,b,A,c,A,b,A,end",
            "start,A,b,A,c,A,b,end",
            "start,A,b,A,c,A,c,A,end",
            "start,A,b,A,c,A,end",
            "start,A,b,A,end",
            "start,A,b,d,b,A,c,A,end",
            "start,A,b,d,b,A,end",
            "start,A,b,d,b,end",
            "start,A,b,end",
            "start,A,c,A,b,A,b,A,end",
            "start,A,c,A,b,A,b,end",
            "start,A,c,A,b,A,c,A,end",
            "start,A,c,A,b,A,end",
            "start,A,c,A,b,d,b,A,end",
            "start,A,c,A,b,d,b,end",
            "start,A,c,A,b,end",
            "start,A,c,A,c,A,b,A,end",
            "start,A,c,A,c,A,b,end",
            "start,A,c,A,c,A,end",
            "start,A,c,A,end",
            "start,A,end",
            "start,b,A,b,A,c,A,end",
            "start,b,A,b,A,end",
            "start,b,A,b,end",
            "start,b,A,c,A,b,A,end",
            "start,b,A,c,A,b,end",
            "start,b,A,c,A,c,A,end",
            "start,b,A,c,A,end",
            "start,b,A,end",
            "start,b,d,b,A,c,A,end",
            "start,b,d,b,A,end",
            "start,b,d,b,end",
            "start,b,end",
        )
        assertEquals(expectedPaths1, paths1.map { path -> path.joinToString(separator = ",") { node -> node.id } }.toSet())

        val graph2 = parseCavesGraph(sampleInput2)
        val paths2 = findAllPaths(graph2, ::visitAtMostOneSmallCaveTwice)
        assertEquals(103, paths2.size)

        val graph3 = parseCavesGraph(sampleInput3)
        val paths3 = findAllPaths(graph3, ::visitAtMostOneSmallCaveTwice)
        assertEquals(3509, paths3.size)
    }

    @Test
    fun testAnswer2() {
        val fullInput = findInput(object {}).readLines()
        val graph = parseCavesGraph(fullInput)
        val paths = findAllPaths(graph, ::visitAtMostOneSmallCaveTwice)
        assertEquals(123054, paths.size)
    }
}
