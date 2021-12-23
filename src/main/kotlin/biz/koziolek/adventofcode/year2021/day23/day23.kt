package biz.koziolek.adventofcode.year2021.day23

import biz.koziolek.adventofcode.*
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    val inputFile = findInput(object {})
    val burrow = AmphipodBurrow.fromString(inputFile.readText())
    val moves = findCheapestMovesToOrganise(burrow)
    val totalCost = moves.sumOf { it.cost }
    println(toString(moves))
    println("Total cost: $totalCost")
}

data class AmphipodBurrow(val positions: String) {
    val isOrganized = (positions == ".......ABCDABCD")
    val hallway = positions.substring(0, 7)
    val roomA = "" + positions[ROOM_A_1] + positions[ROOM_A_2]
    val roomB = "" + positions[ROOM_B_1] + positions[ROOM_B_2]
    val roomC = "" + positions[ROOM_C_1] + positions[ROOM_C_2]
    val roomD = "" + positions[ROOM_D_1] + positions[ROOM_D_2]

    override fun toString() =
        buildString {
            append("#############\n")
            append("#${positions[HALLWAY_1]}${positions[HALLWAY_2]}.${positions[HALLWAY_3]}.${positions[HALLWAY_4]}.${positions[HALLWAY_5]}.${positions[HALLWAY_6]}${positions[HALLWAY_7]}#\n")
            append("###${positions[ROOM_A_1]}#${positions[ROOM_B_1]}#${positions[ROOM_C_1]}#${positions[ROOM_D_1]}###\n")
            append("  #${positions[ROOM_A_2]}#${positions[ROOM_B_2]}#${positions[ROOM_C_2]}#${positions[ROOM_D_2]}#\n")
            append("  #########")
        }

    companion object {
        private const val EMPTY = '.'

        private const val HALLWAY_1 = 0
        private const val HALLWAY_2 = 1
        private const val HALLWAY_3 = 2
        private const val HALLWAY_4 = 3
        private const val HALLWAY_5 = 4
        private const val HALLWAY_6 = 5
        private const val HALLWAY_7 = 6
        private const val ROOM_A_1 = 7
        private const val ROOM_B_1 = 8
        private const val ROOM_C_1 = 9
        private const val ROOM_D_1 = 10
        private const val ROOM_A_2 = 11
        private const val ROOM_B_2 = 12
        private const val ROOM_C_2 = 13
        private const val ROOM_D_2 = 14

        private val HALLWAYS = listOf(HALLWAY_1, HALLWAY_2, HALLWAY_3, HALLWAY_4, HALLWAY_5, HALLWAY_6, HALLWAY_7)
        private val TOP_ROOMS = listOf(ROOM_A_1, ROOM_B_1, ROOM_C_1, ROOM_D_1)
        private val BOTTOM_ROOMS = listOf(ROOM_A_2, ROOM_B_2, ROOM_C_2, ROOM_D_2)

        private val GRAPH = buildGraph<PositionNode, BiDirectionalGraphEdge<PositionNode>> {
            val h1 = PositionNode(HALLWAY_1)
            val h2 = PositionNode(HALLWAY_2)
            val h3 = PositionNode(HALLWAY_3)
            val h4 = PositionNode(HALLWAY_4)
            val h5 = PositionNode(HALLWAY_5)
            val h6 = PositionNode(HALLWAY_6)
            val h7 = PositionNode(HALLWAY_7)
            val a1 = PositionNode(ROOM_A_1)
            val b1 = PositionNode(ROOM_B_1)
            val c1 = PositionNode(ROOM_C_1)
            val d1 = PositionNode(ROOM_D_1)
            val a2 = PositionNode(ROOM_A_2)
            val b2 = PositionNode(ROOM_B_2)
            val c2 = PositionNode(ROOM_C_2)
            val d2 = PositionNode(ROOM_D_2)

            addAll(
                listOf(
                    h1 to h2,
                    h2 to h3,
                    h3 to h4,
                    h4 to h5,
                    h5 to h6,
                    h6 to h7,

                    a1 to h2,
                    a1 to h3,
                    a2 to a1,

                    b1 to h3,
                    b1 to h4,
                    b2 to b1,

                    c1 to h4,
                    c1 to h5,
                    c2 to c1,

                    d1 to h5,
                    d1 to h6,
                    d2 to d1,
                )
            )
        }

        data class PositionNode(val position: Int) : GraphNode {
            override val id = position.toString()
            override fun toGraphvizString() = id
            infix fun to(other: PositionNode) = BiDirectionalGraphEdge(this, other)
        }

        private val TYPE_COST = mapOf(
            'A' to 1,
            'B' to 10,
            'C' to 100,
            'D' to 1000,
        )

        private val TYPE_ROOM_HORIZONTAL_POS = mapOf(
            'A' to getHorizontalPosition(ROOM_A_1),
            'B' to getHorizontalPosition(ROOM_B_1),
            'C' to getHorizontalPosition(ROOM_C_1),
            'D' to getHorizontalPosition(ROOM_D_1),
        )

        fun fromString(string: String): AmphipodBurrow =
            string.split("\n")
                .let { lines ->
                    AmphipodBurrow(
                        buildString {
                            append(lines[1][1])
                            append(lines[1][2])
                            append(lines[1][4])
                            append(lines[1][6])
                            append(lines[1][8])
                            append(lines[1][10])
                            append(lines[1][11])
                            append(lines[2][3])
                            append(lines[2][5])
                            append(lines[2][7])
                            append(lines[2][9])
                            append(lines[3][3])
                            append(lines[3][5])
                            append(lines[3][7])
                            append(lines[3][9])
                        }
                    )
                }

        private fun isRoom(position: Int) = !isHallway(position)

        private fun isHallway(position: Int) = position <= HALLWAY_7

        private fun getHorizontalPosition(position: Int) =
            when (position) {
                HALLWAY_1 -> 0
                HALLWAY_2 -> 1
                ROOM_A_1 -> 2
                ROOM_A_2 -> 2
                HALLWAY_3 -> 3
                ROOM_B_1 -> 4
                ROOM_B_2 -> 4
                HALLWAY_4 -> 5
                ROOM_C_1 -> 6
                ROOM_C_2 -> 6
                HALLWAY_5 -> 7
                ROOM_D_1 -> 8
                ROOM_D_2 -> 8
                HALLWAY_6 -> 9
                HALLWAY_7 -> 10
                else -> throw IllegalArgumentException("Not a valid position: $position")
            }

        private fun getVerticalPosition(position: Int) =
            when (position) {
                HALLWAY_1 -> 0
                HALLWAY_2 -> 0
                HALLWAY_3 -> 0
                HALLWAY_4 -> 0
                HALLWAY_5 -> 0
                HALLWAY_6 -> 0
                HALLWAY_7 -> 0
                ROOM_A_1 -> 1
                ROOM_B_1 -> 1
                ROOM_C_1 -> 1
                ROOM_D_1 -> 1
                ROOM_A_2 -> 2
                ROOM_B_2 -> 2
                ROOM_C_2 -> 2
                ROOM_D_2 -> 2
                else -> throw IllegalArgumentException("Not a valid position: $position")
            }
    }

    fun generateValidMoves(): List<Move> =
        buildList {
            for (src in TOP_ROOMS + BOTTOM_ROOMS + HALLWAYS) {
                val amphipodType = positions[src]

                if (amphipodType != EMPTY) {
                    for (dst in findAccessiblePositions(src)) {
                        val isDifferentRoom = isHallway(src) || isHallway(dst) || getHorizontalPosition(src) != getHorizontalPosition(dst)
                        val isHallwayToHallway = isHallway(src) && isHallway(dst)
                        val isDesiredRoom = isHallway(dst) || getHorizontalPosition(dst) == TYPE_ROOM_HORIZONTAL_POS[amphipodType]
                        val allowedMove = isDifferentRoom && !isHallwayToHallway && isDesiredRoom

                        if (allowedMove) {
                            add(move(src, dst))
                        }
                    }
                }
            }
        }

    private fun findAccessiblePositions(src: Int): Set<Int> =
        GRAPH.nodes.find { it.position == src }
            ?.let { srcNode ->
                val accessiblePositions = mutableSetOf<Int>()

                visitAll(srcNode) { node ->
                    if (node == srcNode) {
                        GRAPH.getAdjacentNodes(node)
                    } else if (positions[node.position] == EMPTY) {
                        accessiblePositions.add(node.position)
                        GRAPH.getAdjacentNodes(node)
                    } else {
                        emptySet()
                    }
                }

                accessiblePositions
            }
            ?: emptySet()

    private fun move(src: Int, dst: Int): Move =
        Move(
            burrow = swap(src, dst),
            cost = getCost(src, dst),
        )

    private fun swap(position1: Int, position2: Int): AmphipodBurrow =
        copy(positions = buildString {
            val min = min(position1, position2)
            val max = max(position1, position2)

            if (min > 0) {
                append(positions.substring(0, min))
            }
            append(positions[max])
            append(positions.substring(min + 1, max))
            append(positions[min])
            if (max < positions.length - 1) {
                append(positions.substring(max + 1, positions.length))
            }
        })

    private fun getCost(from: Int, to: Int): Int {
        var distance = abs(getHorizontalPosition(from) - getHorizontalPosition(to))

        if (isRoom(from)) {
            distance += getVerticalPosition(from)
        }

        if (isRoom(to)) {
            distance += getVerticalPosition(to)
        }

        return distance * TYPE_COST[positions[from]]!!
    }
}

data class Move(val burrow: AmphipodBurrow, val cost: Int)

fun findCheapestMovesToOrganise(burrow: AmphipodBurrow): List<Move> {
    val cumulativeDistance: MutableMap<Move, Int> = HashMap()
    val predecessors: MutableMap<Move, Move> = HashMap()
    val toVisit: Queue<Pair<Move, Int>> = PriorityQueue(Comparator.comparing { (_, distance) -> distance })
    val start = Move(burrow, cost = 0)
    var current: Move? = start
    cumulativeDistance[start] = 0

    while (current != null) {
//        println("${cumulativeDistance.size} / ${toVisit.size}")

        if (current.burrow.isOrganized) {
            break
        }

        val currentNodeDistance = cumulativeDistance[current] ?: Int.MAX_VALUE

        for (move in current.burrow.generateValidMoves()) {
            val otherNodeDistance = cumulativeDistance[move] ?: Int.MAX_VALUE
            val newDistance = currentNodeDistance + move.cost

            if (newDistance < otherNodeDistance) {
                cumulativeDistance[move] = newDistance
                predecessors[move] = current
                toVisit.add(Pair(move, newDistance))
            }
        }

        current = toVisit.poll()?.first
    }

    return generateSequence(current) { node ->
        if (node == start) {
            null
        } else {
            predecessors[node]
        }
    }.toList().reversed()
}

fun toString(moves: List<Move>) =
    buildString {
        var totalCost = 0
        for (move in moves) {
            totalCost += move.cost
            append("${move.burrow}\ncost=${move.cost} total=$totalCost\n")
        }
    }
