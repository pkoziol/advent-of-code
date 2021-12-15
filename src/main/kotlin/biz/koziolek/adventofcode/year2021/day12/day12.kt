package biz.koziolek.adventofcode.year2021.day12

import biz.koziolek.adventofcode.Graph
import biz.koziolek.adventofcode.GraphEdge
import biz.koziolek.adventofcode.GraphNode
import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val lines = inputFile.bufferedReader().readLines()

    val graph = parseCavesGraph(lines)

    val paths1 = findAllPaths(graph, ::visitSmallCavesOnlyOnce)
    println("There is ${paths1.size} possible paths when visiting small caves only once")

    val paths2 = findAllPaths(graph, ::visitAtMostOneSmallCaveTwice)
    println("There is ${paths2.size} possible paths when visiting at most one small cave twice")
}

sealed interface CaveNode : GraphNode

object StartCave : CaveNode {
    override val id = "start"
    override fun toGraphvizString() = "node [shape=box]; $id;"
    override fun toString() = "${javaClass.simpleName}(id=$id)"
}

object EndCave : CaveNode {
    override val id = "end"
    override fun toGraphvizString() = "node [shape=box]; $id;"
    override fun toString() = "${javaClass.simpleName}(id=$id)"
}

data class BigCave(override val id: String) : CaveNode {
    override fun toGraphvizString() = "node [shape=doublecircle]; $id;"
}

data class SmallCave(override val id: String) : CaveNode {
    override fun toGraphvizString() = "node [shape=circle]; $id;"
}

fun parseCavesGraph(lines: List<String>): Graph<CaveNode> {
    return lines.fold(Graph()) { graph, line ->
        val (node1Id, node2Id) = line.split('-', limit = 2)
        val edge = GraphEdge(createNode(node1Id), createNode(node2Id))
        graph.addEdge(edge)
    }
}

private fun createNode(id: String): CaveNode =
    when (id) {
        "start" -> StartCave
        "end" -> EndCave
        id.uppercase() -> BigCave(id)
        id.lowercase() -> SmallCave(id)
        else -> throw IllegalArgumentException("Unknown cave ID: $id")
    }

fun findAllPaths(graph: Graph<CaveNode>, smallCaveVisitDecider: (SmallCave, List<CaveNode>) -> Boolean): Set<List<CaveNode>> {
    return buildSet {
        var incompletePaths: List<List<CaveNode>> = listOf(listOf(StartCave))

        while (incompletePaths.isNotEmpty()) {
            incompletePaths = incompletePaths.flatMap { path ->
                graph.getAdjacentNodes(path.last())
                    .flatMap { node ->
                        when (node) {
                            is StartCave -> emptySet()
                            is EndCave -> {
                                add(path + node)
                                emptySet()
                            }
                            is BigCave -> setOf(path + node)
                            is SmallCave -> {
                                if (smallCaveVisitDecider(node, path)) {
                                    setOf(path + node)
                                } else {
                                    emptySet()
                                }
                            }
                        }
                    }
            }
        }
    }
}

fun visitSmallCavesOnlyOnce(cave: SmallCave, path: List<CaveNode>) = cave !in path

fun visitAtMostOneSmallCaveTwice(cave: SmallCave, path: List<CaveNode>): Boolean {
    val eachCount = path.filterIsInstance<SmallCave>()
        .groupingBy { it }
        .eachCount()
    val thisCaveVisits = eachCount.getOrDefault(cave, 0)
    val anyCaveVisitedTwice = eachCount.any { it.value == 2 }

    return (thisCaveVisits == 0)
            || (!anyCaveVisitedTwice && thisCaveVisits < 2)
}
