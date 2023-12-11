package biz.koziolek.adventofcode

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

data class Coord(val x: Int, val y: Int) {
    operator fun plus(other: Coord) = Coord(x + other.x, y + other.y)
    operator fun minus(other: Coord) = Coord(x - other.x, y - other.y)
    operator fun unaryMinus() = Coord(-x, -y)
    override fun toString() = "$x,$y"

    fun toLong() = LongCoord(x, y)

    fun distanceTo(other: Coord) = sqrt(
        (x - other.x).toDouble().pow(2)
                + (y - other.y).toDouble().pow(2)
    )

    infix fun manhattanDistanceTo(other: Coord): Int =
        abs(x - other.x) + abs(y - other.y)

    companion object {
        fun fromString(str: String): Coord =
            str.split(',')
                .map { it.toInt() }
                .let { Coord(x = it[0], y = it[1]) }
    }
}

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
                .map { it.toLong() }
                .let { LongCoord(x = it[0], y = it[1]) }
    }
}

fun IntProgression.zipAsCoord(ys: IntProgression) = zip(ys) { x, y -> Coord(x, y) }

fun <T> Map<Coord, T>.getWidth() = keys.maxOfOrNull { it.x }?.plus(1) ?: 0

fun <T> Map<Coord, T>.getHeight() = keys.maxOfOrNull { it.y }?.plus(1) ?: 0

fun <T> Map<Coord, T>.getAdjacentCoords(coord: Coord, includeDiagonal: Boolean): Set<Coord> =
    keys.getAdjacentCoords(coord, includeDiagonal)

fun Iterable<Coord>.getAdjacentCoords(coord: Coord, includeDiagonal: Boolean): Set<Coord> {
    return sequence {
        yield(Coord(-1, 0))
        yield(Coord(0, -1))
        yield(Coord(1, 0))
        yield(Coord(0, 1))

        if (includeDiagonal) {
            yield(Coord(-1, -1))
            yield(Coord(1, -1))
            yield(Coord(-1, 1))
            yield(Coord(1, 1))
        }
    }
        .map { coord + it }
        .filter { contains(it) }
        .toSet()
}

fun Map<Coord, Int>.toGraph(includeDiagonal: Boolean): Graph<CoordNode, UniDirectionalGraphEdge<CoordNode>> =
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

fun Coord.toGraphNode() = CoordNode(this)

data class CoordNode(val coord: Coord) : GraphNode {

    override val id = "x${coord.x}_y${coord.y}"

    override fun toGraphvizString() = id
}
