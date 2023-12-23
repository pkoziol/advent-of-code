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
        assertEquals(BiDirectionalGraphEdge(b, a), BiDirectionalGraphEdge(a, b))
        assertNotEquals(UniDirectionalGraphEdge(b, a), UniDirectionalGraphEdge(a, b))
    }

    @Test
    fun testEdgeHashCode() {
        assertEquals(BiDirectionalGraphEdge(b, a).hashCode(), BiDirectionalGraphEdge(a, b).hashCode())
    }

    @Test
    fun testEdgeContains() {
        val edge = BiDirectionalGraphEdge(a, b)
        assertTrue(edge.contains(a))
        assertTrue(edge.contains(b))
        assertFalse(edge.contains(c))

        val edge2 = UniDirectionalGraphEdge(a, b)
        assertTrue(edge2.contains(a))
        assertTrue(edge2.contains(b))
        assertFalse(edge2.contains(c))
    }

    @Test
    fun testEdgeGetOther() {
        val edge = BiDirectionalGraphEdge(a, b)
        assertEquals(b, edge.getOther(a))
        assertEquals(a, edge.getOther(b))
        assertThrowsExactly(IllegalArgumentException::class.java) {
            edge.getOther(c)
        }

        val edge2 = UniDirectionalGraphEdge(a, b)
        assertEquals(b, edge2.getOther(a))
        assertEquals(a, edge2.getOther(b))
        assertThrowsExactly(IllegalArgumentException::class.java) {
            edge2.getOther(c)
        }
    }

    @Test
    fun testGetAdjacentNodesWhenBiDirectional() {
        val graph = Graph<SimpleGraphNode, BiDirectionalGraphEdge<SimpleGraphNode>>()
            .addEdge(BiDirectionalGraphEdge(a, b))
            .addEdge(BiDirectionalGraphEdge(b, c))
            .addEdge(BiDirectionalGraphEdge(b, d))
            .addEdge(BiDirectionalGraphEdge(d, e))
            .addEdge(BiDirectionalGraphEdge(d, c))
            .addEdge(BiDirectionalGraphEdge(e, a))
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
    fun testGetAdjacentNodesWhenUniDirectional() {
        val graph = Graph<SimpleGraphNode, UniDirectionalGraphEdge<SimpleGraphNode>>()
            .addEdge(UniDirectionalGraphEdge(a, b))
            .addEdge(UniDirectionalGraphEdge(b, c))
            .addEdge(UniDirectionalGraphEdge(b, d))
            .addEdge(UniDirectionalGraphEdge(d, e))
            .addEdge(UniDirectionalGraphEdge(d, c))
            .addEdge(UniDirectionalGraphEdge(e, a))
        println(graph.toGraphvizString())

        assertEquals(5, graph.nodes.size)
        assertEquals(6, graph.edges.size)
        assertEquals(setOf(b), graph.getAdjacentNodes(a))
        assertEquals(setOf(c, d), graph.getAdjacentNodes(b))
        assertEquals(emptySet<SimpleGraphNode>(), graph.getAdjacentNodes(c))
        assertEquals(setOf(c, e), graph.getAdjacentNodes(d))
        assertEquals(setOf(a), graph.getAdjacentNodes(e))
    }

    @Test
    fun testToGraphvizStringWhenBiDirectional() {
        val graph = Graph<SimpleGraphNode, BiDirectionalGraphEdge<SimpleGraphNode>>()
            .addEdge(BiDirectionalGraphEdge(a, b))
            .addEdge(BiDirectionalGraphEdge(b, c))
            .addEdge(BiDirectionalGraphEdge(b, d))
            .addEdge(BiDirectionalGraphEdge(d, e))
            .addEdge(BiDirectionalGraphEdge(d, c))
            .addEdge(BiDirectionalGraphEdge(e, a))
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

    @Test
    fun testToGraphvizStringWhenUniDirectional() {
        val graph = Graph<SimpleGraphNode, UniDirectionalGraphEdge<SimpleGraphNode>>()
            .addEdge(UniDirectionalGraphEdge(a, b))
            .addEdge(UniDirectionalGraphEdge(b, c))
            .addEdge(UniDirectionalGraphEdge(b, d))
            .addEdge(UniDirectionalGraphEdge(d, e))
            .addEdge(UniDirectionalGraphEdge(d, c))
            .addEdge(UniDirectionalGraphEdge(e, a))
        println(graph.toGraphvizString())

        assertEquals("""
            digraph G {
                rankdir=LR
                a
                b
                c
                d
                e
                a -> b
                b -> c
                b -> d
                d -> e
                d -> c
                e -> a
            }
        """.trimIndent(), graph.toGraphvizString())
    }
}
