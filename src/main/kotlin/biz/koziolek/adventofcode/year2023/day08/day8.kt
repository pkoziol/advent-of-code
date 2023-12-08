package biz.koziolek.adventofcode.year2023.day08

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})
    val map = parseWastelandMap(inputFile.bufferedReader().readLines())
    println("It takes ${map.followInstructions().size - 1} moves to move from $START_ID to $END_ID")
}

const val START_ID = "AAA"
const val END_ID = "ZZZ"
const val LEFT_WEIGHT = -1
const val RIGHT_WEIGHT = 1

data class WastelandMap(
    val instructions: String,
    val graph: Graph<WastelandNode, UniDirectionalGraphEdge<WastelandNode>>
) {
    @Suppress("MoveVariableDeclarationIntoWhen")
    fun followInstructions(): List<WastelandNode> = buildList {
        var movesCount = 0
        var currentNode = graph.nodes.single { it.id == START_ID }
        add(currentNode)

        while (currentNode.id != END_ID) {
            val move = instructions[movesCount % instructions.length]
            val edge = when (move) {
                'L' -> graph.edges.single { it.node1 == currentNode && it.weight == LEFT_WEIGHT }
                'R' -> graph.edges.single { it.node1 == currentNode && it.weight == RIGHT_WEIGHT }
                else -> throw IllegalArgumentException("Unknown move: $move")
            }

            currentNode = edge.node2
            add(currentNode)

            movesCount++
        }
    }
}

data class WastelandNode(override val id: String) : GraphNode {
    override fun toGraphvizString(): String = id
}

fun parseWastelandMap(lines: List<String>): WastelandMap {
    val instructions = lines.first
    val graph = buildGraph<WastelandNode, UniDirectionalGraphEdge<WastelandNode>> {
        val regex = Regex("(?<start>[A-Z]+) = \\((?<left>[A-Z]+), (?<right>[A-Z]+)\\)")

        val nodes = lines
            .drop(2)
            .mapNotNull { regex.find(it) }
            .map { WastelandNode(it.groups["start"]!!.value) }
            .associateBy { it.id }

        lines
            .drop(2)
            .mapNotNull { regex.find(it) }
            .flatMap { match ->
                val startNode = nodes[match.groups["start"]!!.value]!!
                val leftNode = nodes[match.groups["left"]!!.value]!!
                val rightNode = nodes[match.groups["right"]!!.value]!!

                listOf(
                    UniDirectionalGraphEdge(startNode, leftNode, LEFT_WEIGHT),
                    UniDirectionalGraphEdge(startNode, rightNode, RIGHT_WEIGHT),
                )
            }
            .forEach { add(it) }
    }

    return WastelandMap(instructions, graph)
}
