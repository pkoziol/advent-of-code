package biz.koziolek.adventofcode

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class GraphTest {

    private val a = SimpleGraphNode("a")
    private val b = SimpleGraphNode("b")
    private val c = SimpleGraphNode("c")
    private val d = SimpleGraphNode("d")
    private val e = SimpleGraphNode("e")

    @Test
    fun testEdgeEquality() {
        assertEquals(GraphEdge(b, a), GraphEdge(a, b))
    }

    @Test
    fun testEdgeContains() {
        val edge = GraphEdge(a, b)
        assertTrue(edge.contains(a))
        assertTrue(edge.contains(b))
        assertFalse(edge.contains(c))
    }

    @Test
    fun testEdgeGetOther() {
        val edge = GraphEdge(a, b)
        assertEquals(b, edge.getOther(a))
        assertEquals(a, edge.getOther(b))
        assertThrowsExactly(IllegalArgumentException::class.java) {
            edge.getOther(c)
        }
    }

    @Test
    fun testGetAdjacentNodes() {
        val graph = Graph<SimpleGraphNode>()
            .addEdge(GraphEdge(a, b))
            .addEdge(GraphEdge(b, c))
            .addEdge(GraphEdge(b, d))
            .addEdge(GraphEdge(d, e))
            .addEdge(GraphEdge(d, c))
            .addEdge(GraphEdge(e, a))
        println(graph.toGraphvizString())

        assertEquals(5, graph.nodes.size)
        assertEquals(6, graph.edges.size)
        assertEquals(setOf(b, e), graph.getAdjacentNodes(a))
        assertEquals(setOf(a, c, d), graph.getAdjacentNodes(b))
        assertEquals(setOf(b, d), graph.getAdjacentNodes(c))
        assertEquals(setOf(b, c, e), graph.getAdjacentNodes(d))
        assertEquals(setOf(a, d), graph.getAdjacentNodes(e))
    }

    @Test
    fun testToGraphvizString() {
        val graph = Graph<SimpleGraphNode>()
            .addEdge(GraphEdge(a, b))
            .addEdge(GraphEdge(b, c))
            .addEdge(GraphEdge(b, d))
            .addEdge(GraphEdge(d, e))
            .addEdge(GraphEdge(d, c))
            .addEdge(GraphEdge(e, a))
        println(graph.toGraphvizString())

        assertEquals("""
            graph G {
                rankdir=LR
                a
                b
                c
                d
                e
                a -- b
                b -- c
                b -- d
                d -- e
                d -- c
                e -- a
            }
        """.trimIndent(), graph.toGraphvizString())
    }
}
