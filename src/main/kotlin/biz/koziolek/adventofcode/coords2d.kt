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

    fun walkNorthTo(dstY: Int, includeCurrent: Boolean) = sequence {
        val startY = if (includeCurrent) y else y - 1
        for (y in startY downTo dstY) {
            yield(Coord(x, y))
        }
    }

    fun walkWestTo(dstX: Int, includeCurrent: Boolean) = sequence {
        val startX = if (includeCurrent) x else x - 1
        for (x in startX downTo dstX) {
            yield(Coord(x, y))
        }
    }

    fun walkSouthTo(dstY: Int, includeCurrent: Boolean) = sequence {
        val startY = if (includeCurrent) y else y + 1
        for (y in startY..dstY) {
            yield(Coord(x, y))
        }
    }

    fun walkEastTo(dstX: Int, includeCurrent: Boolean) = sequence {
        val startX = if (includeCurrent) x else x + 1
        for (x in startX..dstX) {
            yield(Coord(x, y))
        }
    }

    companion object {
        fun fromString(str: String): Coord =
            str.split(',')
                .map { it.toInt() }
                .let { Coord(x = it[0], y = it[1]) }
    }
}

fun Iterable<String>.parse2DMap(): Sequence<Pair<Coord, Char>> =
    parse2DMap { it }

fun <T> Iterable<String>.parse2DMap(valueMapper: (Char) -> T?): Sequence<Pair<Coord, T>> =
    sequence {
        forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                val value = valueMapper(char)
                if (value != null) {
                    yield(Coord(x, y) to value)
                }
            }
        }
    }

fun IntProgression.zipAsCoord(ys: IntProgression) = zip(ys) { x, y -> Coord(x, y) }

fun <T> Map<Coord, T>.getWidth() = keys.maxOfOrNull { it.x }?.plus(1) ?: 0

fun <T> Map<Coord, T>.getHeight() = keys.maxOfOrNull { it.y }?.plus(1) ?: 0

/**
 * Walk north -> south, west -> east.
 *
 *     |---> 1
 *     |---> 2
 *     |---> 3
 *     V
 */
fun <T> Map<Coord, T>.walkSouth() = sequence {
    val width = getWidth()
    val height = getHeight()
    for (y in 0..<height) {
        for (x in 0..<width) {
            yield(Coord(x, y))
        }
    }
}

/**
 * Walk west -> east, south -> north.
 *
 *     1 2 3
 *     ^ ^ ^
 *     | | |
 *     | | |
 *     ----->
 */
fun <T> Map<Coord, T>.walkEast() = sequence {
    val width = getWidth()
    val height = getHeight()
    for (x in 0..<width) {
        for (y in height-1 downTo 0) {
            yield(Coord(x, y))
        }
    }
}

/**
 * Walk south -> north -> south, east -> west.
 *
 *           ^
 *     3 <---|
 *     2 <---|
 *     1 <---|
 */
fun <T> Map<Coord, T>.walkNorth() = sequence {
    val width = getWidth()
    val height = getHeight()
    for (y in height-1 downTo 0) {
        for (x in width-1 downTo 0) {
            yield(Coord(x, y))
        }
    }
}

/**
 * Walk east -> west, north -> south.
 *
 *     <-----
 *      | | |
 *      | | |
 *      V V V
 *      3 2 1
 */
fun <T> Map<Coord, T>.walkWest() = sequence {
    val width = getWidth()
    val height = getHeight()
    for (x in width-1 downTo 0) {
        for (y in 0..<height) {
            yield(Coord(x, y))
        }
    }
}

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
