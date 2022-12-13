package biz.koziolek.adventofcode

import java.util.*
import kotlin.collections.HashMap

fun <N : GraphNode, E : GraphEdge<N>> buildGraph(code: MutableSet<E>.() -> Unit): Graph<N, E> {
    val edges = mutableSetOf<E>()
    code(edges)
    val nodes = edges.flatMap { setOf(it.node1, it.node2) }.toSet()
    return Graph(edges, nodes)
}

data class Graph<N : GraphNode, E : GraphEdge<N>>(
    val edges: Set<E> = emptySet(),
    val nodes: Set<N> = emptySet()
) {
    private val isDirectional by lazy {
        edges.any { it is UniDirectionalGraphEdge<*> }
    }

    private val nodeEdges: Map<N, Set<E>> by lazy {
        buildMap {
            for (edge in edges) {
                if (edge.startsWith(edge.node1)) {
                    merge(edge.node1, mutableSetOf(edge)) { a, b -> a + b }
                }
                if (edge.startsWith(edge.node2)) {
                    merge(edge.node2, mutableSetOf(edge)) { a, b -> a + b }
                }
            }
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

    fun getAdjacentNodes(node: N): Set<N> =
        nodeEdges[node]
            ?.map { it.getOther(node) }
            ?.toSet()
            ?: emptySet()

    fun findShortestPath(start: N, end: N): List<N> {
        val cumulativeDistance: MutableMap<N, Int> = HashMap()
        val toVisit: Queue<Pair<N, Int>> = PriorityQueue(Comparator.comparing { (_, distance) -> distance })
        var current: N? = end
        cumulativeDistance[end] = 0

        while (current != null) {
            if (current == start) {
                break
            }

            val currentNodeDistance = cumulativeDistance[current] ?: Int.MAX_VALUE

            for (edge in edges) {
                if (edge.endsWith(current)) {
                    val otherNode = edge.getOther(current)
                    val otherNodeDistance = cumulativeDistance[otherNode] ?: Int.MAX_VALUE
                    val newDistance = currentNodeDistance + edge.weight

                    if (newDistance < otherNodeDistance) {
                        cumulativeDistance[otherNode] = newDistance
                        toVisit.add(Pair(otherNode, newDistance))
                    }
                }
            }
            
            current = toVisit.poll()?.first
        }

        return generateSequence(start) { node ->
            if (node == end) {
                null
            } else {
                getAdjacentNodes(node)
                    .minByOrNull { adjNode -> cumulativeDistance[adjNode] ?: Int.MAX_VALUE }
            }
        }.toList()
    }
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
