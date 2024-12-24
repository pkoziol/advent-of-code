package biz.koziolek.adventofcode

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class GraphTest {

    private val a = SimpleGraphNode("a")
    private val b = SimpleGraphNode("b")
    private val c = SimpleGraphNode("c")
    private val d = SimpleGraphNode("d")
    private val e = SimpleGraphNode("e")

    private val coordNode13 = CoordNode(Coord(1, 3))
    private val coordNode72 = CoordNode(Coord(7, 2))
    private val coordNode55 = CoordNode(Coord(5, 5))

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
                layout=dot
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
                layout=dot
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

    @Test
    fun testToGraphvizStringWithAdditionalProps() {
        val graph = Graph<CoordNode, BiDirectionalGraphEdge<CoordNode>>()
            .addEdge(BiDirectionalGraphEdge(coordNode13, coordNode55, 10))
            .addEdge(BiDirectionalGraphEdge(coordNode13, coordNode72, 20, color = "red"))
            .addEdge(BiDirectionalGraphEdge(coordNode55, coordNode72, 15))
        println(graph.toGraphvizString())

        assertEquals("""
            graph G {
                rankdir=LR
                layout=dot
                x1_y3 [pos="0.1,0.3!"]
                x5_y5 [pos="0.5,0.5!"]
                x7_y2 [pos="0.7,0.2!"]
                x1_y3 -- x5_y5 [label=10,len=5.0]
                x1_y3 -- x7_y2 [label=20,len=10.0,color=red]
                x5_y5 -- x7_y2 [label=15,len=7.5]
            }
        """.trimIndent(), graph.toGraphvizString(
            exactXYPosition = true,
            xyPositionScale = .1f,
            edgeWeightAsLabel = true,
            exactEdgeLength = true,
            edgeLengthScale = .5f,
        ))
    }
}
