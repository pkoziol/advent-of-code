package biz.koziolek.adventofcode

import java.util.*

data class Graph<N : GraphNode, E : GraphEdge<N>>(
    val edges: Set<E> = emptySet(),
    val nodes: Set<N> = emptySet()
) {
    private val isDirectional by lazy {
        edges.any { it is UniDirectionalGraphEdge<*> }
    }

    private val nodeEdges by lazy {
        nodes.associateWith { node ->
            edges.filter { edge -> edge.startsWith(node) }
                .toSet()
        }
    }

    private val nodeNeighbors by lazy {
        nodes.associateWith { node ->
            edges.filter { edge -> edge.startsWith(node) }
                .map { edge -> edge.getOther(node) }
                .toSet()
        }
    }

    fun addEdge(edge: E) =
        copy(
            edges = edges + edge,
            nodes = nodes + edge.node1 + edge.node2,
        )

    fun toGraphvizString() =
        edges.joinToString(
            prefix = """
                ${if (isDirectional) "digraph" else "graph"} G {
                    rankdir=LR
            """.trimIndent() + nodes.joinToString(prefix = "\n    ", postfix = "\n", separator = "\n    ") { it.toGraphvizString() },
            postfix = "\n}",
            separator = "\n"
        ) { "    ${it.toGraphvizString()}" }

    fun getAdjacentNodes(node: N): Set<N> = nodeNeighbors[node] ?: emptySet()
}

sealed interface GraphEdge<N : GraphNode> {
    val node1: N
    val node2: N
    val weight: Int

    fun startsWith(node: N): Boolean

    fun endsWith(node: N): Boolean

    fun contains(node: N): Boolean = node1 == node || node2 == node

    fun getOther(node: N): N =
        when (node) {
            node1 -> node2
            node2 -> node1
            else -> throw IllegalArgumentException("Node: $node is not part of edge: $this")
        }

    fun toGraphvizString(): String
}

class BiDirectionalGraphEdge<N : GraphNode>(
    override val node1: N,
    override val node2: N,
    override val weight: Int = 1
) : GraphEdge<N> {

    override fun startsWith(node: N) = contains(node)

    override fun endsWith(node: N) = contains(node)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GraphEdge<*>

        return (node1 == other.node1 && node2 == other.node2)
                || (node1 == other.node2 && node2 == other.node1)
    }

    override fun hashCode() = Objects.hash(node1, node2)

    override fun toGraphvizString() = "${node1.id} -- ${node2.id}"
}

data class UniDirectionalGraphEdge<N : GraphNode>(
    override val node1: N,
    override val node2: N,
    override val weight: Int = 1
) : GraphEdge<N> {

    override fun startsWith(node: N) = node == node1

    override fun endsWith(node: N) = node == node2

    override fun toGraphvizString() = "${node1.id} -> ${node2.id}"
}

interface GraphNode {
    val id: String
    fun toGraphvizString(): String
}

data class SimpleGraphNode(override val id: String) : GraphNode {
    override fun toGraphvizString() = id
}
