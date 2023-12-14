package biz.koziolek.adventofcode.year2023.day08

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})
    val map = parseWastelandMap(inputFile.bufferedReader().readLines())
    println("It takes ${map.followInstructions().size - 1} moves to move from $START_ID to $END_ID")
    println("It takes ${map.followGhostInstructions()} moves to move from $START_ID to $END_ID")
}

const val START_ID = "AAA"
const val END_ID = "ZZZ"

data class WastelandMap(
    val instructions: String,
    val nodes: Map<String, WastelandNode>
) {
    val ghostStartNodes = nodes.values.filter { it.isGhostStart }.sortedBy { it.id }
    val ghostEndNodes = nodes.values.filter { it.isGhostEnd }.sortedBy { it.id }

    fun followInstructions(): List<WastelandNode> =
        findPath(START_ID) { node -> node.id == END_ID }

    private fun findPath(startId: String, isEnd: (WastelandNode) -> Boolean): List<WastelandNode> = buildList {
        var movesCount = 0
        var currentNode = nodes.values.single { it.id == startId }
        add(currentNode)

        while (!isEnd(currentNode)) {
            val move = instructions[movesCount % instructions.length]
            currentNode = findNext(currentNode, move)
            add(currentNode)

            movesCount++
        }
    }

    @Deprecated("Should work I think, but toList() will eat ALL your memory. will take FOREVER and there's sequence overhead.")
    fun followGhostInstructions1(): Sequence<List<WastelandNode>> = sequence {
        var movesCount = 0
        var currentNodes = ghostStartNodes
        yield(currentNodes)

        while (!currentNodes.all { it.isGhostEnd }) {
            val move = instructions[movesCount % instructions.length]
            currentNodes = currentNodes.map { findNext(it, move) }
            yield(currentNodes)

            movesCount++
        }
    }

    @Deprecated("Should work I think, with no memory issues, but takes FOREVER.")
    fun followGhostInstructions2(): Long {
        var movesCount = 0L
        var currentNodes = ghostStartNodes
        val found = mutableSetOf<Int>()

        while (!currentNodes.all { it.isGhostEnd }) {
            val move = instructions[(movesCount % instructions.length).toInt()]
            currentNodes = currentNodes.map { findNext(it, move) }

            movesCount++

            currentNodes.forEachIndexed { index, node ->
                if (node.isGhostEnd) {
                    found.add(index)
                }
            }
        }

        return movesCount
    }

    fun followGhostInstructions(): Long {
        val counts = ghostStartNodes
            .map { node ->
                findPath(node.id) { possibleEndNode ->
                    possibleEndNode.isGhostEnd
                }.size - 1
            }
            .map { it.toLong() }

        if (counts.any { it % instructions.length != 0L }) {
            throw AssertionError("Counts are not multiply of instructions length - I don't know what to do")
        }

        return lcm(counts)
    }

    private fun findNext(node: WastelandNode, move: Char) =
        when (move) {
            'L' -> node.left!!
            'R' -> node.right!!
            else -> throw IllegalArgumentException("Unknown move: $move")
        }
}

data class WastelandNode(val id: String) {
    var left: WastelandNode? = null
    var right: WastelandNode? = null
    val isGhostStart = id[2] == 'A'
    val isGhostEnd = id[2] == 'Z'
}

fun parseWastelandMap(lines: List<String>): WastelandMap {
    val instructions = lines.first()
    val regex = Regex("(?<start>[A-Z0-9]+) = \\((?<left>[A-Z0-9]+), (?<right>[A-Z0-9]+)\\)")

    val nodes = lines
        .drop(2)
        .mapNotNull { regex.find(it) }
        .map { WastelandNode(it.groups["start"]!!.value) }
        .associateBy { it.id }

    lines
        .drop(2)
        .mapNotNull { regex.find(it) }
        .forEach { match ->
            val startNode = nodes[match.groups["start"]!!.value]!!
            val leftNode = nodes[match.groups["left"]!!.value]!!
            val rightNode = nodes[match.groups["right"]!!.value]!!

            startNode.left = leftNode
            startNode.right = rightNode
        }

    return WastelandMap(instructions, nodes)
}
