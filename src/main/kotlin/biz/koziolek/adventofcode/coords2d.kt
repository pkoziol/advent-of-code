package biz.koziolek.adventofcode

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

enum class Direction(val char: Char) {
    NORTH('^'),
    SOUTH('v'),
    WEST('<'),
    EAST('>'),
}

data class Coord(val x: Int, val y: Int) {
    operator fun plus(other: Coord) = Coord(x + other.x, y + other.y)
    operator fun minus(other: Coord) = Coord(x - other.x, y - other.y)
    operator fun unaryMinus() = Coord(-x, -y)
    operator fun times(num: Int): Coord = Coord(x * num, y * num)
    operator fun times(num: Long): LongCoord = LongCoord(x * num, y * num)
    override fun toString() = "$x,$y"

    fun toLong() = LongCoord(x, y)

    fun distanceTo(other: Coord) = sqrt(
        (x - other.x).toDouble().pow(2)
                + (y - other.y).toDouble().pow(2)
    )

    infix fun manhattanDistanceTo(other: Coord): Int =
        abs(x - other.x) + abs(y - other.y)

    fun getAdjacentCoords(includeDiagonal: Boolean = false): Set<Coord> =
        if (includeDiagonal) {
            setOf(
                this + Coord(-1, 0),
                this + Coord(0, -1),
                this + Coord(1, 0),
                this + Coord(0, 1),
                this + Coord(-1, -1),
                this + Coord(1, -1),
                this + Coord(-1, 1),
                this + Coord(1, 1),
            )
        } else {
            setOf(
                this + Coord(-1, 0),
                this + Coord(0, -1),
                this + Coord(1, 0),
                this + Coord(0, 1),
            )
        }

    fun move(direction: Direction, count: Int = 1): Coord =
        when (direction) {
            Direction.NORTH -> copy(y = y - count)
            Direction.SOUTH -> copy(y = y + count)
            Direction.WEST -> copy(x = x - count)
            Direction.EAST -> copy(x = x + count)
        }

    fun walk(direction: Direction, distance: Int, includeCurrent: Boolean) =
        when (direction) {
            Direction.NORTH -> walkNorthTo(y - distance, includeCurrent)
            Direction.SOUTH -> walkSouthTo(y + distance, includeCurrent)
            Direction.WEST -> walkWestTo(x - distance, includeCurrent)
            Direction.EAST -> walkEastTo(x + distance, includeCurrent)
        }

    fun walk(direction1: Direction, direction2: Direction, distance: Int, includeCurrent: Boolean) =
        when (direction1 to direction2) {
            Direction.NORTH to Direction.EAST -> walkNorthEastTo(x + distance, y - distance, includeCurrent)
            Direction.SOUTH to Direction.EAST -> walkSouthEastTo(x + distance, y + distance, includeCurrent)
            Direction.SOUTH to Direction.WEST -> walkSouthWestTo(x - distance, y + distance, includeCurrent)
            Direction.NORTH to Direction.WEST -> walkNorthWestTo(x - distance, y - distance, includeCurrent)
            else -> throw IllegalArgumentException("Invalid directions: $direction1, $direction2")
        }

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

    private fun walkNorthEastTo(dstX: Int, dstY: Int, includeCurrent: Boolean) = sequence {
        val startX = if (includeCurrent) x else x + 1
        val startY = if (includeCurrent) y else y - 1
        var x = startX
        var y = startY
        while (x <= dstX && y >= dstY) {
            yield(Coord(x, y))
            x++
            y--
        }
    }

    private fun walkSouthEastTo(dstX: Int, dstY: Int, includeCurrent: Boolean) = sequence {
        val startX = if (includeCurrent) x else x + 1
        val startY = if (includeCurrent) y else y + 1
        var x = startX
        var y = startY
        while (x <= dstX && y <= dstY) {
            yield(Coord(x, y))
            x++
            y++
        }
    }

    private fun walkSouthWestTo(dstX: Int, dstY: Int, includeCurrent: Boolean) = sequence {
        val startX = if (includeCurrent) x else x - 1
        val startY = if (includeCurrent) y else y + 1
        var x = startX
        var y = startY
        while (x >= dstX && y <= dstY) {
            yield(Coord(x, y))
            x--
            y++
        }
    }

    private fun walkNorthWestTo(dstX: Int, dstY: Int, includeCurrent: Boolean) = sequence {
        val startX = if (includeCurrent) x else x - 1
        val startY = if (includeCurrent) y else y - 1
        var x = startX
        var y = startY
        while (x >= dstX && y >= dstY) {
            yield(Coord(x, y))
            x--
            y--
        }
    }

    companion object {
        val yComparator: Comparator<Coord> = Comparator.comparing { it.y }
        val xComparator: Comparator<Coord> = Comparator.comparing { it.x }

        fun fromString(str: String): Coord =
            str.split(',')
                .map { it.trim().toInt() }
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

fun <T> Map<Coord, T>.getWidth() = getHorizontalRange().count()

fun <T> Map<Coord, T>.getHeight() = getVerticalRange().count()

fun <T> Map<Coord, T>.getHorizontalRange() =
    keys.minAndMaxOrNull { it.x }?.let { it.first..it.second } ?: IntRange.EMPTY

fun <T> Map<Coord, T>.getVerticalRange() =
    keys.minAndMaxOrNull { it.y }?.let { it.first..it.second } ?: IntRange.EMPTY

fun Iterable<Coord>.sortByYX() =
    sortedWith(
        Coord.yComparator.thenComparing(Coord.xComparator)
    )

/**
 * Walk north -> south, west -> east.
 *
 *     |---> 1
 *     |---> 2
 *     |---> 3
 *     V
 */
fun <T> Map<Coord, T>.walkSouth() = sequence {
    val xRange = getHorizontalRange()
    val yRange = getVerticalRange()
    for (y in yRange) {
        for (x in xRange) {
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
    val xRange = getHorizontalRange()
    val yRange = getVerticalRange()
    for (x in xRange) {
        for (y in yRange.reversed()) {
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
    val xRange = getHorizontalRange()
    val yRange = getVerticalRange()
    for (y in yRange.reversed()) {
        for (x in xRange.reversed()) {
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
    val xRange = getHorizontalRange()
    val yRange = getVerticalRange()
    for (x in xRange.reversed()) {
        for (y in yRange) {
            yield(Coord(x, y))
        }
    }
}

fun Map<Coord, Char>.to2DString(default: Char): String =
    to2DString { _, c -> c ?: default }

fun <T> Map<Coord, T>.to2DString(formatter: (Coord, T?) -> Char): String =
    to2DStringOfStrings { coord, t -> formatter(coord, t).toString() }

fun <T> Map<Coord, T>.to2DStringOfStrings(from: Coord? = null,
                                          to: Coord? = null,
                                          formatter: (Coord, T?) -> String): String =
    buildString {
        val minX = from?.x ?: keys.minOf { it.x }
        val maxX = to?.x ?: keys.maxOf { it.x }
        val minY = from?.y ?: keys.minOf { it.y }
        val maxY = to?.y ?: keys.maxOf { it.y }
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                val coord = Coord(x, y)
                val value = get(coord)
                append(formatter(coord, value))
            }
            if (y != maxY) {
                append('\n')
            }
        }
    }

fun <T> Map<Coord, T>.to2DRainbowString(formatter: (Coord, T?) -> Pair<String, Float?>): String =
    to2DStringOfStrings { coord, value ->
        val (str, colorPercentage) = formatter(coord, value)

        if (colorPercentage != null) {
            AsciiColor.rainbow(colorPercentage, str)
        } else {
            str
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

fun Map<Coord, Int>.toBiDirectionalGraph(includeDiagonal: Boolean): Graph<CoordNode, BiDirectionalGraphEdge<CoordNode>> =
    buildGraph {
        this@toBiDirectionalGraph.forEach { (coord, value) ->
            val node2 = coord.toGraphNode()

            getAdjacentCoords(coord, includeDiagonal)
                .forEach { adjCoord ->
                    add(BiDirectionalGraphEdge(
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

    override fun toGraphvizString(exactXYPosition: Boolean, xyPositionScale: Float): String {
        val props = mutableListOf<String>()
        if (exactXYPosition) props.add("pos=\"${coord.x * xyPositionScale},${coord.y * xyPositionScale}!\"")

        val propsStr = when {
            props.isNotEmpty() -> " [" + props.joinToString(",") + "]"
            else -> ""
        }

        return "$id$propsStr"
    }
}
