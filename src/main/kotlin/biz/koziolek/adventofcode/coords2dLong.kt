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

    fun getAdjacentCoords(includeDiagonal: Boolean = false): Set<LongCoord> =
        if (includeDiagonal) {
            setOf(
                this + LongCoord(-1, 0),
                this + LongCoord(0, -1),
                this + LongCoord(1, 0),
                this + LongCoord(0, 1),
                this + LongCoord(-1, -1),
                this + LongCoord(1, -1),
                this + LongCoord(-1, 1),
                this + LongCoord(1, 1),
            )
        } else {
            setOf(
                this + LongCoord(-1, 0),
                this + LongCoord(0, -1),
                this + LongCoord(1, 0),
                this + LongCoord(0, 1),
            )
        }

    fun move(direction: Direction, count: Long = 1): LongCoord =
        when (direction) {
            Direction.NORTH -> copy(y = y - count)
            Direction.SOUTH -> copy(y = y + count)
            Direction.WEST -> copy(x = x - count)
            Direction.EAST -> copy(x = x + count)
        }

    companion object {
        val yComparator: Comparator<LongCoord> = Comparator.comparing { it.y }
        val xComparator: Comparator<LongCoord> = Comparator.comparing { it.x }

        fun fromString(str: String): LongCoord =
            str.split(',')
                .map { it.trim().toInt() }
                .let { LongCoord(x = it[0], y = it[1]) }
    }
}

fun LongProgression.zipAsCoord(ys: LongProgression) = zip(ys) { x, y -> LongCoord(x, y) }

fun <T> Map<LongCoord, T>.getWidth() = keys.maxOfOrNull { it.x }?.plus(1) ?: 0

fun <T> Map<LongCoord, T>.getHeight() = keys.maxOfOrNull { it.y }?.plus(1) ?: 0

fun Iterable<LongCoord>.sortByYX() =
    sortedWith(
        LongCoord.yComparator.thenComparing(LongCoord.xComparator)
    )

fun Map<LongCoord, Char>.to2DString(default: Char): String =
    to2DString { _, c -> c ?: default }

fun <T> Map<LongCoord, T>.to2DString(formatter: (LongCoord, T?) -> Char): String =
    to2DStringOfStrings { coord, t -> formatter(coord, t).toString() }

fun <T> Map<LongCoord, T>.to2DStringOfStrings(formatter: (LongCoord, T?) -> String): String =
    buildString {
        val (minX, maxX) = keys.minAndMaxOrNull { it.x }!!
        val (minY, maxY) = keys.minAndMaxOrNull { it.y }!!
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                val coord = LongCoord(x, y)
                val value = get(coord)
                append(formatter(coord, value))
            }
            if (y != maxY) {
                append('\n')
            }
        }
    }

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

    override fun toGraphvizString(exactXYPosition: Boolean, xyPositionScale: Float) = id
}
