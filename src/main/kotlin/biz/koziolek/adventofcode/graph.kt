package biz.koziolek.adventofcode

import java.util.*

data class Graph<T : GraphNode>(val edges: Set<GraphEdge<T>> = emptySet(),
                                val nodes: Set<T> = emptySet()) {
    private val nodeNeighbors by lazy {
        nodes.associateWith { node ->
            edges.filter { edge -> edge.contains(node) }
                .map { edge -> edge.getOther(node) }
                .toSet()
        }
    }

    fun addEdge(edge: GraphEdge<T>) =
        copy(
            edges = edges + edge,
            nodes = nodes + edge.node1 + edge.node2,
        )

    fun toGraphvizString() =
        edges.joinToString(
            prefix = """
                graph G {
                    rankdir=LR
            """.trimIndent() + nodes.joinToString(prefix = "\n    ", postfix = "\n", separator = "\n    ") { it.toGraphvizString() },
            postfix = "\n}",
            separator = "\n"
        ) { "    ${it.node1.id} -- ${it.node2.id}" }

    fun getAdjacentNodes(node: T): Set<T> = nodeNeighbors[node] ?: emptySet()
}

data class GraphEdge<T>(val node1: T, val node2: T) {
    fun contains(node: T) = node1 == node || node2 == node

    fun getOther(node: T) =
        when (node) {
            node1 -> node2
            node2 -> node1
            else -> throw IllegalArgumentException("Node: $node is not part of edge: $this")
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GraphEdge<*>

        return (node1 == other.node1 && node2 == other.node2)
                || (node1 == other.node2 && node2 == other.node1)
    }

    override fun hashCode() = Objects.hash(node1, node2)
}

interface GraphNode {
    val id: String
    fun toGraphvizString(): String
}

data class SimpleGraphNode(override val id: String) : GraphNode {
    override fun toGraphvizString() = id
}
