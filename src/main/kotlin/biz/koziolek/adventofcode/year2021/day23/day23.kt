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

data class AmphipodBurrow(val positions: String, val height: Int = 2) {
    val isOrganized = when (height) {
        2 -> positions == ".......ABCDABCD"
        4 -> positions == ".......ABCDABCDABCDABCD"
        else -> throw IllegalArgumentException("Height $height is not supported")
    }

    val hallway by lazy { positions.substring(0, 7) }

    override fun toString() =
        buildString {
            append("#############\n")
            append("#${positions[HALLWAY_1]}${positions[HALLWAY_2]}.${positions[HALLWAY_3]}.${positions[HALLWAY_4]}.${positions[HALLWAY_5]}.${positions[HALLWAY_6]}${positions[HALLWAY_7]}#\n")
            append("###${positions[ROOM_A_1]}#${positions[ROOM_B_1]}#${positions[ROOM_C_1]}#${positions[ROOM_D_1]}###\n")
            append("  #${positions[ROOM_A_2]}#${positions[ROOM_B_2]}#${positions[ROOM_C_2]}#${positions[ROOM_D_2]}#\n")
            if (height >= 4) {
                append("  #${positions[ROOM_A_3]}#${positions[ROOM_B_3]}#${positions[ROOM_C_3]}#${positions[ROOM_D_3]}#\n")
                append("  #${positions[ROOM_A_4]}#${positions[ROOM_B_4]}#${positions[ROOM_C_4]}#${positions[ROOM_D_4]}#\n")
            }
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
        private const val ROOM_A_3 = 15
        private const val ROOM_B_3 = 16
        private const val ROOM_C_3 = 17
        private const val ROOM_D_3 = 18
        private const val ROOM_A_4 = 19
        private const val ROOM_B_4 = 20
        private const val ROOM_C_4 = 21
        private const val ROOM_D_4 = 22

        private val HALLWAYS = HALLWAY_1..HALLWAY_7

        // height -> room positions
        private val ROOMS = mapOf(
            2 to ROOM_A_1..ROOM_D_2,
            4 to ROOM_A_1..ROOM_D_4,
        )

        private val HEIGHTS = ROOMS.keys

        private val NODES = (HALLWAYS + ROOMS[HEIGHTS.maxOf { it }]!!).associateWith { PositionNode(it) }

        private val GRAPH_HEIGHT_2 = buildGraph<PositionNode, BiDirectionalGraphEdge<PositionNode>> {
            addAll(
                listOf(
                    NODES[HALLWAY_1]!! to NODES[HALLWAY_2]!!,
                    NODES[HALLWAY_2]!! to NODES[HALLWAY_3]!!,
                    NODES[HALLWAY_3]!! to NODES[HALLWAY_4]!!,
                    NODES[HALLWAY_4]!! to NODES[HALLWAY_5]!!,
                    NODES[HALLWAY_5]!! to NODES[HALLWAY_6]!!,
                    NODES[HALLWAY_6]!! to NODES[HALLWAY_7]!!,

                    NODES[ROOM_A_1]!! to NODES[HALLWAY_2]!!,
                    NODES[ROOM_A_1]!! to NODES[HALLWAY_3]!!,
                    NODES[ROOM_A_2]!! to NODES[ROOM_A_1]!!,

                    NODES[ROOM_B_1]!! to NODES[HALLWAY_3]!!,
                    NODES[ROOM_B_1]!! to NODES[HALLWAY_4]!!,
                    NODES[ROOM_B_2]!! to NODES[ROOM_B_1]!!,

                    NODES[ROOM_C_1]!! to NODES[HALLWAY_4]!!,
                    NODES[ROOM_C_1]!! to NODES[HALLWAY_5]!!,
                    NODES[ROOM_C_2]!! to NODES[ROOM_C_1]!!,

                    NODES[ROOM_D_1]!! to NODES[HALLWAY_5]!!,
                    NODES[ROOM_D_1]!! to NODES[HALLWAY_6]!!,
                    NODES[ROOM_D_2]!! to NODES[ROOM_D_1]!!,
                )
            )
        }

        private val GRAPH_HEIGHT_4 = buildGraph<PositionNode, BiDirectionalGraphEdge<PositionNode>> {
            addAll(GRAPH_HEIGHT_2.edges)

            addAll(
                listOf(
                    NODES[ROOM_A_3]!! to NODES[ROOM_A_2]!!,
                    NODES[ROOM_A_4]!! to NODES[ROOM_A_3]!!,

                    NODES[ROOM_B_3]!! to NODES[ROOM_B_2]!!,
                    NODES[ROOM_B_4]!! to NODES[ROOM_B_3]!!,

                    NODES[ROOM_C_3]!! to NODES[ROOM_C_2]!!,
                    NODES[ROOM_C_4]!! to NODES[ROOM_C_3]!!,

                    NODES[ROOM_D_3]!! to NODES[ROOM_D_2]!!,
                    NODES[ROOM_D_4]!! to NODES[ROOM_D_3]!!,
                )
            )
        }

        // height -> graph
        private val GRAPHS = mapOf(
            2 to GRAPH_HEIGHT_2,
            4 to GRAPH_HEIGHT_4,
        )

        // height -> source -> destination -> path
        private val ALL_PATHS = GRAPHS.mapValues { (_, graph) ->
            graph.nodes.associate { srcNode ->
                srcNode.position to graph.nodes.filter { it != srcNode }.associate { dstNode ->
                    dstNode.position to graph.findShortestPath(srcNode, dstNode).map { it.position }
                }
            }
        }

        data class PositionNode(val position: Int) : GraphNode {
            override val id = position.toString()
            override fun toGraphvizString(exactXYPosition: Boolean, xyPositionScale: Float) = id
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

        // height -> type -> positions
        private val ROOMS_BY_TYPE = ROOMS.map { (height, rooms) ->
            height to TYPE_ROOM_HORIZONTAL_POS.map { (type, desiredRoomPos) ->
                type to rooms
                    .filter { getHorizontalPosition(it) == desiredRoomPos }
                    .sortedBy { getVerticalPosition(it) }
            }.toMap()
        }.toMap()

        fun fromString(string: String): AmphipodBurrow =
            string.trim().split("\n")
                .let { lines ->
                    AmphipodBurrow(
                        buildString {
                            // Hallways
                            append(lines[1][1])
                            append(lines[1][2])
                            append(lines[1][4])
                            append(lines[1][6])
                            append(lines[1][8])
                            append(lines[1][10])
                            append(lines[1][11])

                            // Rooms
                            for (i in 2 until lines.size - 1) {
                                append(lines[i][3])
                                append(lines[i][5])
                                append(lines[i][7])
                                append(lines[i][9])
                            }
                        },
                        height = lines.size - 3
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
                ROOM_A_3 -> 2
                ROOM_A_4 -> 2
                HALLWAY_3 -> 3
                ROOM_B_1 -> 4
                ROOM_B_2 -> 4
                ROOM_B_3 -> 4
                ROOM_B_4 -> 4
                HALLWAY_4 -> 5
                ROOM_C_1 -> 6
                ROOM_C_2 -> 6
                ROOM_C_3 -> 6
                ROOM_C_4 -> 6
                HALLWAY_5 -> 7
                ROOM_D_1 -> 8
                ROOM_D_2 -> 8
                ROOM_D_3 -> 8
                ROOM_D_4 -> 8
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
                ROOM_A_3 -> 3
                ROOM_B_3 -> 3
                ROOM_C_3 -> 3
                ROOM_D_3 -> 3
                ROOM_A_4 -> 4
                ROOM_B_4 -> 4
                ROOM_C_4 -> 4
                ROOM_D_4 -> 4
                else -> throw IllegalArgumentException("Not a valid position: $position")
            }
    }

    fun generateValidMoves(): List<Move> =
        (ROOMS[height]!! + HALLWAYS)
            .filter { src -> positions[src] != EMPTY }
            .flatMap { src ->
                findAccessiblePositions(src)
                    .map { dst -> move(src, dst) }
            }

    private fun findAccessiblePositions(src: Int): Set<Int> =
        ALL_PATHS[height]
            ?.get(src)
            ?.filterValues { path -> path.all { it == src || positions[it] == EMPTY } }
            ?.filterKeys { dst -> isMoveAllowed(src, dst) }
            ?.keys
            ?: emptySet()

    private fun isMoveAllowed(src: Int, dst: Int): Boolean {
        val srcIsRoom = isRoom(src)
        val dstIsRoom = isRoom(dst)
        val isRoomToHall = srcIsRoom && !dstIsRoom
        val isHallToRoom = !srcIsRoom && dstIsRoom
        val amphipodType = positions[src]

        return (isRoomToHall
                || (isHallToRoom && isDesiredRoom(dst, amphipodType) && isRoomEmptyOrMyTypeOnly(amphipodType)))
    }

    private fun isDesiredRoom(destRoomPos: Int, amphipodType: Char) =
        getHorizontalPosition(destRoomPos) == TYPE_ROOM_HORIZONTAL_POS[amphipodType]

    private fun isRoomEmptyOrMyTypeOnly(amphipodType: Char) =
        roomContents(amphipodType).all { it == EMPTY || it == amphipodType }

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

    fun roomContents(type: Char) =
        ROOMS_BY_TYPE[height]
            ?.get(type)
            ?.joinToString(separator = "") { positions[it].toString() }
            ?: ""
}

data class Move(val burrow: AmphipodBurrow, val cost: Int)

fun findCheapestMovesToOrganise(burrow: AmphipodBurrow): List<Move> {
    val cumulativeDistance: MutableMap<AmphipodBurrow, Int> = HashMap()
    val predecessors: MutableMap<Move, Move> = HashMap()
    val toVisit: Queue<Pair<Move, Int>> = PriorityQueue(Comparator.comparing { (_, distance) -> distance })
    val start = Move(burrow, cost = 0)
    var current: Move? = start
    cumulativeDistance[start.burrow] = 0

    while (current != null) {
        if (current.burrow.isOrganized) {
            break
        }

        val currentNodeDistance = cumulativeDistance[current.burrow] ?: Int.MAX_VALUE

        for (move in current.burrow.generateValidMoves()) {
            val otherNodeDistance = cumulativeDistance[move.burrow] ?: Int.MAX_VALUE
            val newDistance = currentNodeDistance + move.cost

            if (newDistance < otherNodeDistance) {
                cumulativeDistance[move.burrow] = newDistance
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
            append("${move.burrow}\ncost=${move.cost} total=$totalCost\n\n")
        }
    }

fun unfold(input: String): String =
    input.split("\n")
        .let { lines ->
            buildString {
                appendLine(lines[0])
                appendLine(lines[1])
                appendLine(lines[2])
                appendLine("  #D#C#B#A#")
                appendLine("  #D#B#A#C#")
                appendLine(lines[3])
                append(lines[4])
            }
        }
