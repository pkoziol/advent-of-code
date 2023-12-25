package biz.koziolek.adventofcode.year2023.day25

import biz.koziolek.adventofcode.BiDirectionalGraphEdge
import biz.koziolek.adventofcode.SimpleGraphNode
import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2023")
internal class Day25Test {

    private val sampleInput = """
            jqt: rhn xhk nvd
            rsh: frs pzl lsr
            xhk: hfx
            cmg: qnr nvd lhk bvb
            rhn: xhk bvb hfx
            bvb: xhk hfx
            pzl: lsr hfx nvd
            qnr: nvd
            ntq: jqt hfx bvb xhk
            nvd: lhk
            lsr: lhk
            rzs: qnr cmg lsr rsh
            frs: qnr lhk lsr
        """.trimIndent().split("\n")

    @Test
    fun testParse() {
        val graph = parseWiring(sampleInput)
        assertEquals(33, graph.edges.size)
        assertEquals(15, graph.nodes.size)
    }

    @Test
    fun testFindWiresToCutInSample() {
        val graph = parseWiring(sampleInput)
        val wires = findWiresToCut(graph)
        assertEquals(
            setOf(
                BiDirectionalGraphEdge(SimpleGraphNode("hfx"), SimpleGraphNode("pzl")),
                BiDirectionalGraphEdge(SimpleGraphNode("bvb"), SimpleGraphNode("cmg")),
                BiDirectionalGraphEdge(SimpleGraphNode("nvd"), SimpleGraphNode("jqt")),
            ),
            wires
        )
    }

    @Test
    fun testFindWiresToCutInReal() {
        val input = findInput(object {}).bufferedReader().readLines()
        val graph = parseWiring(input)
        val wires = findWiresToCut(graph)
        assertEquals(
            setOf(
                BiDirectionalGraphEdge(SimpleGraphNode("psj"), SimpleGraphNode("fdb")),
                BiDirectionalGraphEdge(SimpleGraphNode("rmt"), SimpleGraphNode("nqh")),
                BiDirectionalGraphEdge(SimpleGraphNode("trh"), SimpleGraphNode("ltn")),
            ),
            wires
        )
    }

    @Test
    fun testSampleAnswer1() {
        val graph = parseWiring(sampleInput)
//        generateGraphvizSvg(
//            graph.toGraphvizString(layout = "sfdp"),
//            File("src/test/kotlin/biz/koziolek/adventofcode/year2023/day25/sample-graph.svg")
//        )

        val wires = findWiresToCut(graph)
        val newGraphs = graph.removeEdges(wires)
        assertEquals(2, newGraphs.size)
        assertTrue(newGraphs.any { it.nodes.size == 9 })
        assertTrue(newGraphs.any { it.nodes.size == 6 })
        assertEquals(54, getAnswer(newGraphs))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val graph = parseWiring(input)
//        generateGraphvizSvg(
//            graph.toGraphvizString(layout = "sfdp"),
//            File("src/test/kotlin/biz/koziolek/adventofcode/year2023/day25/real-graph.svg")
//        )

        val wires = findWiresToCut(graph)
        val newGraphs = graph.removeEdges(wires)
        assertEquals(2, newGraphs.size)
        assertEquals(589036, getAnswer(newGraphs))
    }
}
