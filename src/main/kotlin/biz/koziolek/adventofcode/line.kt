package biz.koziolek.adventofcode

import kotlin.math.abs
import kotlin.math.sign

data class Line(val from: Coord, val to: Coord) {
    val isHorizontal = from.y == to.y
    val isVertical = from.x == to.x
    val isDiagonal = abs(from.x - to.x) == abs(from.y - to.y)

    fun getOpposite() = Line(to, from)

    fun getCoveredPoints(): List<Coord> {
        return when {
            isHorizontal -> range(from.x, to.x).map { Coord(it, from.y) }
            isVertical -> range(from.y, to.y).map { Coord(from.x, it) }
            isDiagonal -> range(from.x, to.x).zipAsCoord(range(from.y, to.y))
            else -> throw IllegalArgumentException("Only horizontal, vertical and diagonal lines are supported")
        }
    }

    override fun toString(): String {
        return "$from -> $to"
    }

    companion object {
        fun fromString(str: String): Line =
            str.split(" -> ")
                .map { Coord.fromString(it) }
                .let { Line(from = it[0], to = it[1]) }
    }
}

private fun range(start: Int, end: Int): IntProgression =
    IntProgression.fromClosedRange(start, end, (end - start).sign)
