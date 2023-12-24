package biz.koziolek.adventofcode

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

data class Coord3d(val x: Int, val y: Int, val z: Int) {
    operator fun plus(other: Coord3d) = Coord3d(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: Coord3d) = Coord3d(x - other.x, y - other.y, z - other.z)
    operator fun unaryMinus() = Coord3d(-x, -y, -z)
    override fun toString() = "$x,$y,$z"

    fun distanceTo(other: Coord3d) = sqrt(
        (x - other.x).toDouble().pow(2)
            + (y - other.y).toDouble().pow(2)
            + (z - other.z).toDouble().pow(2)
    )

    infix fun manhattanDistanceTo(other: Coord3d): Int =
        abs(x - other.x) + abs(y - other.y) + abs(z - other.z)

    companion object {
        fun fromString(str: String): Coord3d =
            str.split(',')
                .map { it.trim().toInt() }
                .let { Coord3d(x = it[0], y = it[1], z = it[2]) }
    }

    fun getAdjacentCoords(includeDiagonal: Boolean = false): Set<Coord3d> =
        sequence {
            yield(Coord3d(-1, 0, 0))
            yield(Coord3d(0, -1, 0))
            yield(Coord3d(0, 0, -1))
            yield(Coord3d(1, 0, 0))
            yield(Coord3d(0, 1, 0))
            yield(Coord3d(0, 0, 1))

            if (includeDiagonal) {
                yield(Coord3d(-1, -1, -1))
                yield(Coord3d(0, -1, -1))
                yield(Coord3d(1, -1, -1))
                yield(Coord3d(-1, 0, -1))
                yield(Coord3d(1, 0, -1))
                yield(Coord3d(-1, 1, -1))
                yield(Coord3d(0, 1, -1))
                yield(Coord3d(1, 1, -1))

                yield(Coord3d(-1, -1, 0))
                yield(Coord3d(1, -1, 0))
                yield(Coord3d(-1, 1, 0))
                yield(Coord3d(1, 1, 0))

                yield(Coord3d(-1, -1, 1))
                yield(Coord3d(0, -1, 1))
                yield(Coord3d(1, -1, 1))
                yield(Coord3d(-1, 0, 1))
                yield(Coord3d(1, 0, 1))
                yield(Coord3d(-1, 1, 1))
                yield(Coord3d(0, 1, 1))
                yield(Coord3d(1, 1, 1))
            }
        }
            .map { this + it }
            .toSet()
}

fun Collection<Coord3d>.getAdjacentCoords(coord: Coord3d, includeDiagonal: Boolean = false): Set<Coord3d> =
    coord.getAdjacentCoords(includeDiagonal)
        .filter { contains(it) }
        .toSet()
