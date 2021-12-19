package biz.koziolek.adventofcode

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

    companion object {
        fun fromString(str: String): Coord3d =
            str.split(',')
                .map { it.toInt() }
                .let { Coord3d(x = it[0], y = it[1], z = it[2]) }
    }
}
