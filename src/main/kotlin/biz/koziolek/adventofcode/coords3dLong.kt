package biz.koziolek.adventofcode

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

data class LongCoord3d(val x: Long, val y: Long, val z: Long) {
    operator fun plus(other: LongCoord3d) = LongCoord3d(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: LongCoord3d) = LongCoord3d(x - other.x, y - other.y, z - other.z)
    operator fun unaryMinus() = LongCoord3d(-x, -y, -z)
    override fun toString() = "$x,$y,$z"

    fun distanceTo(other: LongCoord3d) = sqrt(
        (x - other.x).toDouble().pow(2)
            + (y - other.y).toDouble().pow(2)
            + (z - other.z).toDouble().pow(2)
    )

    infix fun manhattanDistanceTo(other: LongCoord3d): Long =
        abs(x - other.x) + abs(y - other.y) + abs(z - other.z)

    companion object {
        fun fromString(str: String): LongCoord3d =
            str.split(',')
                .map { it.trim().toLong() }
                .let { LongCoord3d(x = it[0], y = it[1], z = it[2]) }
    }

    fun getAdjacentCoords(includeDiagonal: Boolean = false): Set<LongCoord3d> =
        sequence {
            yield(LongCoord3d(-1, 0, 0))
            yield(LongCoord3d(0, -1, 0))
            yield(LongCoord3d(0, 0, -1))
            yield(LongCoord3d(1, 0, 0))
            yield(LongCoord3d(0, 1, 0))
            yield(LongCoord3d(0, 0, 1))

            if (includeDiagonal) {
                yield(LongCoord3d(-1, -1, -1))
                yield(LongCoord3d(0, -1, -1))
                yield(LongCoord3d(1, -1, -1))
                yield(LongCoord3d(-1, 0, -1))
                yield(LongCoord3d(1, 0, -1))
                yield(LongCoord3d(-1, 1, -1))
                yield(LongCoord3d(0, 1, -1))
                yield(LongCoord3d(1, 1, -1))

                yield(LongCoord3d(-1, -1, 0))
                yield(LongCoord3d(1, -1, 0))
                yield(LongCoord3d(-1, 1, 0))
                yield(LongCoord3d(1, 1, 0))

                yield(LongCoord3d(-1, -1, 1))
                yield(LongCoord3d(0, -1, 1))
                yield(LongCoord3d(1, -1, 1))
                yield(LongCoord3d(-1, 0, 1))
                yield(LongCoord3d(1, 0, 1))
                yield(LongCoord3d(-1, 1, 1))
                yield(LongCoord3d(0, 1, 1))
                yield(LongCoord3d(1, 1, 1))
            }
        }
            .map { this + it }
            .toSet()
}

fun Collection<LongCoord3d>.getAdjacentCoords(coord: LongCoord3d, includeDiagonal: Boolean = false): Set<LongCoord3d> =
    coord.getAdjacentCoords(includeDiagonal)
        .filter { contains(it) }
        .toSet()
