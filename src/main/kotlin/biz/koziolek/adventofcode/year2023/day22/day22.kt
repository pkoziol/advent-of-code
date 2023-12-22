package biz.koziolek.adventofcode.year2023.day22

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.Coord3d
import biz.koziolek.adventofcode.findInput
import kotlin.math.max
import kotlin.math.min

fun main() {
    val inputFile = findInput(object {})
    val bricks = parseBricks(inputFile.bufferedReader().readLines())
    println("${findSafeToDisintegrate(bricks).size} are safe to disintegrate")
}

data class Brick(val from: Coord3d, val to: Coord3d) {
    val bottomZ = min(from.z, to.z)
    val topZ = max(from.z, to.z)
    val crossSection = CrossSection(
        from = Coord(
            x = min(from.x, to.x),
            y = min(from.y, to.y),
        ),
        to = Coord(
            x = max(from.x, to.x),
            y = max(from.y, to.y),
        ),
    )

    fun moveToBottomZ(z: Int): Brick =
        copy(
            from = from.copy(z = z),
            to = to.copy(z = z + (to.z - from.z))
        )
}

data class CrossSection(val from: Coord, val to: Coord) {
    fun overlaps(other: CrossSection): Boolean =
        this.from.x <= other.to.x
                && this.to.x >= other.from.x
                && this.from.y <= other.to.y
                && this.to.y >= other.from.y
}

data class StationaryBrick(val original: Brick, val fallen: Brick)

fun parseBricks(lines: Iterable<String>): List<Brick> =
    lines.map { line ->
        val (coord1, coord2) = line.split('~')
        Brick(
            from = Coord3d.fromString(coord1),
            to = Coord3d.fromString(coord2),
        )
    }

fun findSafeToDisintegrate(bricks: Collection<Brick>, debug: Boolean = false): Set<StationaryBrick> {
    val fallen = fallAll(bricks)
    val supportingMap = fallen.associateWith { getSupportedBricks(it, fallen) }
    val supportedByMap = supportingMap.entries
        .flatMap { supporting ->
            supporting.value.map { supported ->
                supported to supporting.key
            }
        }
        .groupBy { it.first }
        .mapValues { entry -> entry.value.map { it.second } }

    if (debug) {
        println("Supporting map:")
        supportingMap.forEach { (supporting, supportedList) ->
            println("  $supporting a.k.a. ${getFriendlyName(supporting)} supports:")
            supportedList.forEach { supported ->
                println("    $supported a.k.a. ${getFriendlyName(supported)}")
            }
            println()
        }

        println("Supported by map:")
        supportedByMap.forEach { (supported, supportingList) ->
            println("  $supported a.k.a. ${getFriendlyName(supported)} is supported by:")
            supportingList.forEach { supporting ->
                println("    $supporting a.k.a. ${getFriendlyName(supporting)}")
            }
            println()
        }
    }

    return supportingMap
        .filter { supporting ->
            supporting.value.all { supported ->
                supportedByMap[supported]!!.size > 1
            }
        }
        .keys
}

fun fallAll(bricks: Collection<Brick>): List<StationaryBrick> {
    val sortedByZ = bricks.sortedBy { it.bottomZ }
    val stationaryBricks = mutableListOf<StationaryBrick>()

    for (brick in sortedByZ) {
        var newZ = brick.bottomZ
        while (newZ > 1) {
            val anyOverlaps = stationaryBricks
                .filter { it.fallen.topZ == newZ - 1 }
                .any { it.fallen.crossSection.overlaps(brick.crossSection) }

            if (anyOverlaps) {
                break
            }

            newZ--
        }

        stationaryBricks.add(
            StationaryBrick(
                original = brick,
                fallen = brick.moveToBottomZ(newZ)
            )
        )
    }

    return stationaryBricks
}

fun getSupportedBricks(brick: StationaryBrick, bricks: Collection<StationaryBrick>): List<StationaryBrick> =
    bricks.filter { it.fallen.bottomZ == brick.fallen.topZ + 1 && it.fallen.crossSection.overlaps(brick.fallen.crossSection) }

internal fun getFriendlyName(stationaryBrick: StationaryBrick): String =
    when (stationaryBrick.original) {
        Brick(from = Coord3d(1, 0, 1), to = Coord3d(1, 2, 1)) -> "A"
        Brick(from = Coord3d(0, 0, 2), to = Coord3d(2, 0, 2)) -> "B"
        Brick(from = Coord3d(0, 2, 3), to = Coord3d(2, 2, 3)) -> "C"
        Brick(from = Coord3d(0, 0, 4), to = Coord3d(0, 2, 4)) -> "D"
        Brick(from = Coord3d(2, 0, 5), to = Coord3d(2, 2, 5)) -> "E"
        Brick(from = Coord3d(0, 1, 6), to = Coord3d(2, 1, 6)) -> "F"
        Brick(from = Coord3d(1, 1, 8), to = Coord3d(1, 1, 9)) -> "G"
        else -> "UNKNOWN"
    }
