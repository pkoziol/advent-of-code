package biz.koziolek.adventofcode

import java.io.File
import java.util.*
import kotlin.collections.HashMap
import kotlin.io.path.absolutePathString
import kotlin.io.path.deleteIfExists
import kotlin.io.path.writeText

fun <N : GraphNode, E : GraphEdge<N>> buildGraph(code: MutableSet<E>.() -> Unit): Graph<N, E> {
    val edges = mutableSetOf<E>()
    code(edges)
    return buildGraph(edges)
}

fun <N : GraphNode, E : GraphEdge<N>> buildGraph(edges: Collection<E>): Graph<N, E> {
    val nodes = edges.flatMap { it.nodes }.toSet()
    return Graph(edges.toSet(), nodes)
}

data class Graph<N : GraphNode, E : GraphEdge<N>>(
    val edges: Set<E> = emptySet(),
    val nodes: Set<N> = emptySet()
) {
    private val isDirectional by lazy {
        edges.any { it is UniDirectionalGraphEdge<*> }
    }

    private val nodeStartingEdges: Map<N, Set<E>> by lazy {
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

    private val nodeEndingEdges: Map<N, Set<E>> by lazy {
        buildMap {
            for (edge in edges) {
                if (edge.endsWith(edge.node1)) {
                    merge(edge.node1, mutableSetOf(edge)) { a, b -> a + b }
                }
                if (edge.endsWith(edge.node2)) {
                    merge(edge.node2, mutableSetOf(edge)) { a, b -> a + b }
                }
            }
        }
    }

    fun addEdge(edge: E): Graph<N, E> =
        addEdges(listOf(edge))

    fun addEdges(edges: Collection<E>): Graph<N, E> =
        Graph(
            edges = this.edges + edges,
            nodes = this.nodes + edges.map { it.node1 } + edges.map { it.node2 },
        )

    fun removeEdge(edge: E): Collection<Graph<N, E>> =
        removeEdges(listOf(edge))

    fun removeEdges(edges: Collection<E>): Collection<Graph<N, E>> {
        val toCheck = edges.flatMap { it.nodes }.toSet().toMutableList()
        val newGraphs = mutableMapOf<N, Graph<N, E>>()

        while (toCheck.isNotEmpty()) {
            val currentNode = toCheck.removeFirst()
            val reachable = mutableSetOf<E>()

            visitAll(currentNode) { node ->
                val nodeEdges = (nodeStartingEdges[node] ?: emptySet()).filter { it !in edges }
                reachable.addAll(nodeEdges)
                val adjNodes = nodeEdges.flatMap { it.nodes }
                toCheck.removeAll(adjNodes)
                adjNodes
            }

            newGraphs[currentNode] = buildGraph(reachable)
        }

        return newGraphs.values.toList()
    }

    fun toGraphvizString(layout: String = "dot",
                         edgeWeightAsLabel: Boolean = false,
                         exactXYPosition: Boolean = false,
                         xyPositionScale: Float = 1f,
                         exactEdgeLength: Boolean = false,
                         edgeLengthScale: Float = 1f) =
        edges.joinToString(
            prefix = """
                ${if (isDirectional) "digraph" else "graph"} G {
                    rankdir=LR
                    layout=$layout
            """.trimIndent() + nodes.joinToString(prefix = "\n    ", postfix = "\n", separator = "\n    ") { it.toGraphvizString(exactXYPosition, xyPositionScale) },
            postfix = "\n}",
            separator = "\n"
        ) { "    ${it.toGraphvizString(edgeWeightAsLabel, exactEdgeLength, edgeLengthScale)}" }

    fun getAdjacentNodes(node: N): Set<N> =
        nodeStartingEdges[node]
            ?.map { it.getOther(node) }
            ?.toSet()
            ?: emptySet()

    fun getAdjacentEdges(node: N): Set<E> =
        nodeStartingEdges[node] ?: emptySet()

    fun findShortestPath(start: N, end: N): List<N> =
        findShortestPath(end) { it == start }

    fun findShortestPath(starts: Set<N>, end: N): List<N> =
        findShortestPath(end) { it in starts }

    private fun findShortestPath(end: N, isStart: (N) -> Boolean): List<N> {
        val cumulativeDistance: MutableMap<N, Int> = HashMap()
        val toVisit: Queue<Pair<N, Int>> = PriorityQueue(Comparator.comparing { (_, distance) -> distance })
        var current: N? = end
        var start: N? = null
        cumulativeDistance[end] = 0

        while (current != null) {
            if (isStart(current)) {
                start = current
                break
            }

            val currentNodeDistance = cumulativeDistance[current] ?: Int.MAX_VALUE

            for (edge in nodeEndingEdges[current]!!) {
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

    fun simplify(): Graph<N, E> {
        val nodesToCheck = nodes.toMutableList()
        val newEdges = edges.toMutableSet()

        while (nodesToCheck.isNotEmpty()) {
            val node = nodesToCheck.removeFirst()
            val incomingEdges = newEdges.filter { it.endsWith(node) }
            val outgoingEdges = newEdges.filter { it.startsWith(node) }

            if (isDirectional && incomingEdges.size == 1 && outgoingEdges.size == 1) {
                val incoming = incomingEdges.single()
                val outgoing = outgoingEdges.single()
                newEdges.remove(incoming)
                newEdges.remove(outgoing)

                @Suppress("UNCHECKED_CAST")
                newEdges.add(UniDirectionalGraphEdge(
                    node1 = incoming.node1,
                    node2 = outgoing.node2,
                    weight = incoming.weight + outgoing.weight,
                ) as E)
            } else if (!isDirectional && incomingEdges.size == 2 && incomingEdges == outgoingEdges) {
                val otherNode1 = incomingEdges[0].getOther(node)
                val otherNode2 = incomingEdges[1].getOther(node)
                newEdges.remove(incomingEdges[0])
                newEdges.remove(incomingEdges[1])

                @Suppress("UNCHECKED_CAST")
                newEdges.add(BiDirectionalGraphEdge(
                    node1 = otherNode1,
                    node2 = otherNode2,
                    weight = incomingEdges[0].weight + incomingEdges[1].weight,
                ) as E)
            }
        }

        return buildGraph { addAll(newEdges) }
    }

    private data class BFSItem<N : GraphNode, E : GraphEdge<N>>(
        val nodes: List<N>,
        val edges: List<E>
    ) {
        fun extend(node: N, edge: E): BFSItem<N, E> =
            copy(
                nodes = nodes + node,
                edges = edges + edge,
            )

        fun length() = edges.sumOf { it.weight }
    }

    fun findLongestPath(
        start: N,
        end: N,
        debug: Boolean = false,
    ): List<E> {
        val toCheck = LinkedList<BFSItem<N, E>>()
        toCheck.add(BFSItem(
            nodes = listOf(start),
            edges = emptyList(),
        ))
        var competed = 0
        var completedLastPrint = -1
        var longest: Int? = null
        var longestItem: BFSItem<N, E>? = null

        if (debug) println("${edges.size} edges")

        while (toCheck.isNotEmpty()) {
            val item = toCheck.removeFirst()
            val lastNode = item.nodes.last()

            if (debug && (toCheck.size % 100_000 == 0 || (competed % 10_000 == 0 && competed != completedLastPrint))) {
                completedLastPrint = competed
                println("to check: ${toCheck.size} completed: $competed longest: $longest")
            }

            if (lastNode == end) {
                competed++

                val length = item.length()
                if (longest == null || length > longest) {
                    longest = length
                    longestItem = item

                    if (debug) println("to check: ${toCheck.size} completed: $competed longest: $longest NEW!!!")
                }
            } else {
                getAdjacentEdges(lastNode)
                    .filter { edge -> edge !in item.edges && edge.getOther(lastNode) !in item.nodes }
                    .forEach { edge -> toCheck.add(item.extend(edge.getOther(lastNode), edge)) }
            }
        }

        if (debug) println("Found $competed paths")

        return longestItem!!.edges
    }
}

sealed interface GraphEdge<N : GraphNode> {
    val node1: N
    val node2: N
    val weight: Int
    val graphvizSymbol: String
    val color: String?

    val nodes: Set<N>
        get() = setOf(node1, node2)

    fun startsWith(node: N): Boolean

    fun endsWith(node: N): Boolean

    fun contains(node: N): Boolean = node1 == node || node2 == node

    fun getOther(node: N): N =
        when (node) {
            node1 -> node2
            node2 -> node1
            else -> throw IllegalArgumentException("Node: $node is not part of edge: $this")
        }

    fun toGraphvizString(
        weightAsLabel: Boolean = false,
        exactLength: Boolean = false,
        lengthScale: Float = 1f
    ): String {
        val props = mutableListOf<String>()
        if (weightAsLabel) props.add("label=$weight")
        if (exactLength) props.add("len=${weight * lengthScale}")
        if (color != null) props.add("color=$color")

        val propsStr = when {
            props.isNotEmpty() -> " [" + props.joinToString(",") + "]"
            else -> ""
        }

        return "${node1.id} $graphvizSymbol ${node2.id}$propsStr"
    }
}

class BiDirectionalGraphEdge<N : GraphNode>(
    override val node1: N,
    override val node2: N,
    override val weight: Int = 1,
    override val color: String? = null,
) : GraphEdge<N> {
    override val graphvizSymbol = "--"

    override fun startsWith(node: N) = contains(node)

    override fun endsWith(node: N) = contains(node)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GraphEdge<*>

        return (node1 == other.node1 && node2 == other.node2)
                || (node1 == other.node2 && node2 == other.node1)
    }

    override fun hashCode() = node1.hashCode() + node2.hashCode()
}

data class UniDirectionalGraphEdge<N : GraphNode>(
    override val node1: N,
    override val node2: N,
    override val weight: Int = 1,
    override val color: String? = null,
) : GraphEdge<N> {
    override val graphvizSymbol = "->"

    override fun startsWith(node: N) = node == node1

    override fun endsWith(node: N) = node == node2
}

interface GraphNode {
    val id: String
    fun toGraphvizString(exactXYPosition: Boolean = false, xyPositionScale: Float = 1f): String
}

data class SimpleGraphNode(override val id: String) : GraphNode {
    override fun toGraphvizString(exactXYPosition: Boolean, xyPositionScale: Float) = id
}

fun <N : GraphNode, E : GraphEdge<N>> generateGraphvizSvg(graph: Graph<N, E>, outFile: File) {
    generateGraphvizSvg(graph.toGraphvizString(), outFile)
}

fun generateGraphvizSvg(graphvizString: String, outFile: File) {
    val tempFile = kotlin.io.path.createTempFile("aoc-graphviz-")
    try {
        tempFile.writeText(graphvizString)

        ProcessBuilder(listOf(
            "/opt/homebrew/bin/dot",
            "-Tsvg",
            tempFile.absolutePathString()
        ))
            .redirectOutput(outFile)
            .start()
            .waitFor()
    } finally {
        try {
            tempFile.deleteIfExists()
        } catch (_: Exception) {}
    }
}
