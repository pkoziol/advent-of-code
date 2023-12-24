package biz.koziolek.adventofcode

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

data class DoubleCoord(val x: Double, val y: Double) {
    operator fun plus(other: DoubleCoord) = DoubleCoord(x + other.x, y + other.y)
    operator fun minus(other: DoubleCoord) = DoubleCoord(x - other.x, y - other.y)
    operator fun unaryMinus() = DoubleCoord(-x, -y)
    override fun toString() = "$x,$y"

    fun distanceTo(other: DoubleCoord) = sqrt(
        (x - other.x).pow(2)
                + (y - other.y).pow(2)
    )

    infix fun manhattanDistanceTo(other: DoubleCoord): Double =
        abs(x - other.x) + abs(y - other.y)

    fun move(direction: Direction, count: Int = 1): DoubleCoord =
        when (direction) {
            Direction.NORTH -> copy(y = y - count)
            Direction.SOUTH -> copy(y = y + count)
            Direction.WEST -> copy(x = x - count)
            Direction.EAST -> copy(x = x + count)
        }

    companion object {
        val yComparator: Comparator<DoubleCoord> = Comparator.comparing { it.y }
        val xComparator: Comparator<DoubleCoord> = Comparator.comparing { it.x }

        fun fromString(str: String): DoubleCoord =
            str.split(',')
                .map { it.trim().toDouble() }
                .let { DoubleCoord(x = it[0], y = it[1]) }
    }
}
