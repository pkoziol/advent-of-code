package biz.koziolek.adventofcode

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

data class LongCoord(val x: Long, val y: Long) {
    constructor(x: Int, y: Int) : this(x.toLong(), y.toLong())
    operator fun plus(other: LongCoord) = LongCoord(x + other.x, y + other.y)
    operator fun minus(other: LongCoord) = LongCoord(x - other.x, y - other.y)
    operator fun unaryMinus() = LongCoord(-x, -y)
    override fun toString() = "$x,$y"

    fun distanceTo(other: LongCoord) = sqrt(
        (x - other.x).toDouble().pow(2)
                + (y - other.y).toDouble().pow(2)
    )

    infix fun manhattanDistanceTo(other: LongCoord): Long =
        abs(x - other.x) + abs(y - other.y)

    companion object {
        fun fromString(str: String): LongCoord =
            str.split(',')
                .map { it.toInt() }
                .let { LongCoord(x = it[0], y = it[1]) }
    }
}

fun LongProgression.zipAsCoord(ys: LongProgression) = zip(ys) { x, y -> LongCoord(x, y) }

fun <T> Map<LongCoord, T>.getWidth() = keys.maxOfOrNull { it.x }?.plus(1) ?: 0

fun <T> Map<LongCoord, T>.getHeight() = keys.maxOfOrNull { it.y }?.plus(1) ?: 0

fun <T> Map<LongCoord, T>.getAdjacentCoords(coord: LongCoord, includeDiagonal: Boolean): Set<LongCoord> =
    keys.getAdjacentCoords(coord, includeDiagonal)

fun Iterable<LongCoord>.getAdjacentCoords(coord: LongCoord, includeDiagonal: Boolean): Set<LongCoord> {
    return sequence {
        yield(LongCoord(-1, 0))
        yield(LongCoord(0, -1))
        yield(LongCoord(1, 0))
        yield(LongCoord(0, 1))

        if (includeDiagonal) {
            yield(LongCoord(-1, -1))
            yield(LongCoord(1, -1))
            yield(LongCoord(-1, 1))
            yield(LongCoord(1, 1))
        }
    }
        .map { coord + it }
        .filter { contains(it) }
        .toSet()
}

fun Map<LongCoord, Int>.toGraph(includeDiagonal: Boolean): Graph<LongCoordNode, UniDirectionalGraphEdge<LongCoordNode>> =
    buildGraph {
        this@toGraph.forEach { (coord, value) ->
            val node2 = coord.toGraphNode()

            getAdjacentCoords(coord, includeDiagonal)
                .forEach { adjCoord ->
                    add(UniDirectionalGraphEdge(
                        node1 = adjCoord.toGraphNode(),
                        node2 = node2,
                        weight = value,
                    ))
                }
        }
    }

fun LongCoord.toGraphNode() = LongCoordNode(this)

data class LongCoordNode(val coord: LongCoord) : GraphNode {

    override val id = "x${coord.x}_y${coord.y}"

    override fun toGraphvizString() = id
}
