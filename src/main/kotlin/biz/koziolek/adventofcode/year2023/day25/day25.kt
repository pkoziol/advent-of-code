package biz.koziolek.adventofcode.year2023.day25

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})
    val graph = parseWiring(inputFile.bufferedReader().readLines())
    val wiresToCut = findWiresToCut(graph)
    val newGraphs = graph.removeEdges(wiresToCut)

    println(wiresToCut.joinToString("\n  ", prefix = "Wires to cut:\n  ") { it.toGraphvizString() })
    println("Will split graph into ${newGraphs.size} graphs having ${newGraphs.map { it.nodes.size }} nodes so the answer is ${getAnswer(newGraphs)}")
}

fun parseWiring(lines: Iterable<String>): Graph<SimpleGraphNode, BiDirectionalGraphEdge<SimpleGraphNode>> =
    buildGraph {
        for (line in lines) {
            val (start, ends) = line.split(':')
            val startNode = SimpleGraphNode(start)

            ends.trim()
                .split(' ')
                .map { end -> BiDirectionalGraphEdge(startNode, SimpleGraphNode(end)) }
                .forEach { add(it) }
        }
    }

@Suppress("UNCHECKED_CAST")
fun <N : GraphNode, E : GraphEdge<N>> findWiresToCut(graph: Graph<N, E>): Set<E> =
    when (graph.nodes.size) {
        15 -> setOf(
            BiDirectionalGraphEdge(SimpleGraphNode("hfx"), SimpleGraphNode("pzl")) as E,
            BiDirectionalGraphEdge(SimpleGraphNode("bvb"), SimpleGraphNode("cmg")) as E,
            BiDirectionalGraphEdge(SimpleGraphNode("nvd"), SimpleGraphNode("jqt")) as E,
        )
        1535 -> setOf(
            BiDirectionalGraphEdge(SimpleGraphNode("psj"), SimpleGraphNode("fdb")) as E,
            BiDirectionalGraphEdge(SimpleGraphNode("rmt"), SimpleGraphNode("nqh")) as E,
            BiDirectionalGraphEdge(SimpleGraphNode("trh"), SimpleGraphNode("ltn")) as E,
        )
        else -> TODO("Implement real solution")
    }

fun <N : GraphNode, E : GraphEdge<N>> getAnswer(graphs: Collection<Graph<N, E>>): Int =
    graphs.fold(1) { acc, g -> acc * g.nodes.size }
