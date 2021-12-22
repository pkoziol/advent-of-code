package biz.koziolek.adventofcode.year2021.day22

import biz.koziolek.adventofcode.*
import kotlin.math.max
import kotlin.math.min

fun main() {
    val inputFile = findInput(object {})
    val lines = inputFile.bufferedReader().readLines()

    val rebootSteps = parseReactorRebootSteps(lines)

    val onCubes = executeRebootSteps(rebootSteps, min = -50, max = 50)
    println("On cubes in -50..50: $onCubes")

    val onCubesAll = executeRebootStepsV2(rebootSteps, min = Int.MIN_VALUE, max = Int.MAX_VALUE)
    println("On cubes everywhere: $onCubesAll")
}

data class RebootStep(val turnOn: Boolean, val cuboid: Cuboid)

sealed interface ICuboid {
    fun exclude(cuboid: ICuboid): ICuboid
    fun getCubes(): Sequence<Coord3d>
    fun intersects(other: ICuboid): Boolean
    fun intersection(other: ICuboid): ICuboid
    fun countContained(): Long
    operator fun contains(coord3d: Coord3d): Boolean
}

object EmptyCuboid : ICuboid {
    override fun exclude(cuboid: ICuboid) = EmptyCuboid
    override fun getCubes() = emptySequence<Coord3d>()
    override fun intersects(other: ICuboid) = false
    override fun intersection(other: ICuboid) = EmptyCuboid
    override fun countContained() = 0L
    override fun contains(coord3d: Coord3d) = false
}

data class Cuboid(val from: Coord3d, val to: Coord3d, private val excluded: List<ICuboid> = emptyList()) : ICuboid {

    override fun exclude(cuboid: ICuboid): Cuboid {
        return when (val excludedIntersection = cuboid.intersection(this)) {
            is Cuboid -> copy(excluded = excluded.map { it.exclude(excludedIntersection) } + excludedIntersection)
            is EmptyCuboid -> this
        }
    }

    override fun getCubes(): Sequence<Coord3d> =
        (from.x..to.x).asSequence().flatMap { x ->
            (from.y..to.y).asSequence().flatMap { y ->
                (from.z..to.z).asSequence().map { z ->
                    Coord3d(x, y, z)
                }
            }
        }.filter { coord -> excluded.none { it.contains(coord) } }

    override fun intersects(other: ICuboid) =
        when (other) {
            is Cuboid ->
                this.from.x <= other.to.x && other.from.x <= this.to.x
                        && this.from.y <= other.to.y && other.from.y <= this.to.y
                        && this.from.z <= other.to.z && other.from.z <= this.to.z
            is EmptyCuboid -> false
        }

    override fun intersection(other: ICuboid): ICuboid =
        if (intersects(other) && other is Cuboid) {
            Cuboid(
                from = Coord3d(
                    x = max(this.from.x, other.from.x),
                    y = max(this.from.y, other.from.y),
                    z = max(this.from.z, other.from.z),
                ),
                to = Coord3d(
                    x = min(this.to.x, other.to.x),
                    y = min(this.to.y, other.to.y),
                    z = min(this.to.z, other.to.z),
                ),
            )
        } else {
            EmptyCuboid
        }

    override fun countContained(): Long =
        (to.x - from.x + 1L) * (to.y - from.y + 1L) * (to.z - from.z + 1L) - excluded.sumOf { it.countContained() }

    override operator fun contains(coord3d: Coord3d) =
        from.x <= coord3d.x && coord3d.x <= to.x
                && from.y <= coord3d.y && coord3d.y <= to.y
                && from.z <= coord3d.z && coord3d.z <= to.z

    fun cutOut(other: Cuboid): Set<Cuboid> =
        buildSet {
            val xSplits = split(from.x, to.x, other.from.x, other.to.x)
            val ySplits = split(from.y, to.y, other.from.y, other.to.y)
            val zSplits = split(from.z, to.z, other.from.z, other.to.z)
            for ((x1, x2) in xSplits) {
                for ((y1, y2) in ySplits) {
                    for ((z1, z2) in zSplits) {
                        val cuboid = Cuboid(
                            from = Coord3d(x = x1, y = y1, z = z1),
                            to = Coord3d(x = x2, y = y2, z = z2),
                        )

                        if (!cuboid.intersects(other)) {
                            add(cuboid)
                        }
                    }
                }          
            }
        }

    private fun split(start: Int, end: Int, cut1: Int, cut2: Int): List<Pair<Int, Int>> {
        return buildList {
            if (start < cut1 && cut2 < end) {
                add(start to cut1 - 1)
                add(cut1 to cut2)
                add(cut2 + 1 to end)
            } else if (start < cut1 && cut1 <= end) {
                add(start to cut1 - 1)
                add(cut1 to end)
            } else if (start <= cut2 && cut2 < end) {
                add(start to cut2)
                add(cut2 + 1 to end)
            } else {
                add(start to end)
            }
        }
    }

    override fun toString(): String {
        return toString(indent = 0)
    }

    fun toString(indent: Int = 0): String {
        val separator = "\n" + " ".repeat(indent + 2)
        return "${from.x},${from.y},${from.z} -> ${to.x},${to.y},${to.z}" +
                excluded.joinToString(separator = separator, prefix = separator) {
                    when (it) {
                        is Cuboid -> it.toString(indent + 2)
                        else -> it.toString()
                    }
                }
    }
}

fun parseReactorRebootSteps(lines: List<String>): List<RebootStep> =
    lines.map { line ->
        Regex("(on|off) x=(-?[0-9]+)\\.\\.(-?[0-9]+),y=(-?[0-9]+)\\.\\.(-?[0-9]+),z=(-?[0-9]+)\\.\\.(-?[0-9]+)")
            .find(line)
            ?.let { result ->
                RebootStep(
                    turnOn = result.groupValues[1] == "on",
                    cuboid = Cuboid(
                        from = Coord3d(
                            x = result.groupValues[2].toInt(),
                            y = result.groupValues[4].toInt(),
                            z = result.groupValues[6].toInt(),
                        ),
                        to = Coord3d(
                            x = result.groupValues[3].toInt(),
                            y = result.groupValues[5].toInt(),
                            z = result.groupValues[7].toInt(),
                        ),
                    ),
                )
            }
            ?: throw IllegalArgumentException("Could not parse reboot step: $line")
    }

fun executeRebootSteps(rebootSteps: List<RebootStep>, min: Int, max: Int): Int {
    val region = Cuboid(Coord3d(min, min, min), Coord3d(max, max, max))
    return rebootSteps
        .filter { region.intersects(it.cuboid) }
        .fold(mutableMapOf<Coord3d, Boolean>()) { map, rebootStep ->
            rebootStep.cuboid.getCubes()
                .filter { region.contains(it) }
                .fold(map) { map2, coord3d ->
                    map2[coord3d] = rebootStep.turnOn
                    map2
                }
        }
        .count { it.value }
}

fun executeRebootStepsV2(rebootSteps: List<RebootStep>, min: Int, max: Int): Long {
    val region = Cuboid(Coord3d(min, min, min), Coord3d(max, max, max))
    return rebootSteps
        .filter { region.intersects(it.cuboid) }
        .fold(listOf<Cuboid>()) { list, rebootStep ->
            if (rebootStep.turnOn) {
                list.map { it.exclude(rebootStep.cuboid) } + rebootStep.cuboid
            } else {
                list.map { it.exclude(rebootStep.cuboid) }
            }
        }
        .sumOf { it.countContained() }
}
