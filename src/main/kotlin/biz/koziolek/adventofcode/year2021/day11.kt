package biz.koziolek.adventofcode.year2021

import biz.koziolek.adventofcode.*
import java.io.File
import java.util.*

fun main() {
    val inputFile = File("src/main/resources/year2021/day11/input")
    val lines = inputFile.bufferedReader().readLines()
    val map = parseOctopusMap(lines)
    
    println("Flashes after 100 steps: ${countFlashes(map, maxStep = 100)}")
    println("First step all flash: ${nextTimeAllFlash(map)}")
}

data class Coord(val x: Int, val y: Int) {
    operator fun plus(other: Coord) = Coord(x + other.x, y + other.y)
    operator fun minus(other: Coord) = Coord(x - other.x, y - other.y)
    operator fun unaryMinus() = Coord(-x, -y)
}

data class Octopus(val energy: Int, val flashed: Boolean = false) {
    fun increaseEnergy(): Octopus {
        if (flashed) {
            return this
        }

        val newEnergy = energy.inc()
        val flashed = (newEnergy > 9)

        return copy(
            energy = if (flashed) 0 else newEnergy,
            flashed = flashed
        )
    }
}

fun parseOctopusMap(lines: List<String>): Map<Coord, Octopus> =
    lines.flatMapIndexed { y, line ->
        line.mapIndexed { x, char ->
            Pair(Coord(x, y), Octopus(char.digitToInt()))
        }
    }.toMap()

fun countFlashes(map0: Map<Coord, Octopus>, maxStep: Int): Int =
    generateSequence(map0) { map -> calculateNextStep(map) }
        .take(maxStep + 1)
        .sumOf { map -> map.values.count { octopus -> octopus.flashed } }

fun nextTimeAllFlash(map0: Map<Coord, Octopus>): Int =
    generateSequence(map0) { map -> calculateNextStep(map) }
        .takeWhile { map -> !map.values.all { octopus -> octopus.flashed } }
        .count()

fun calculateNextStep(map: Map<Coord, Octopus>): Map<Coord, Octopus> {
    return buildMap {
        map.forEach { (coord, octopus) ->
            put(coord, octopus.copy(flashed = false))
        }

        val toVisit: Queue<Coord> = ArrayDeque(map.keys)
        var currentCoord: Coord? = toVisit.remove()

        while (currentCoord != null) {
            computeIfPresent(currentCoord) { coord, octopus ->
                val newOctopus = octopus.increaseEnergy()
                if (!octopus.flashed && newOctopus.flashed) {
                    toVisit.addAll(getAdjacentCoords(coord, this))
                }
                newOctopus
            }

            currentCoord = toVisit.poll()
        }
    }
}

private fun getAdjacentCoords(coord: Coord, map: Map<Coord, Octopus>): Set<Coord> {
    return listOf(
        Coord(-1, -1),
        Coord(0, -1),
        Coord(1, -1),
        Coord(-1, 0),
        Coord(1, 0),
        Coord(-1, 1),
        Coord(0, 1),
        Coord(1, 1),
    )
        .map { coord + it }
        .filter { map[it] != null }
        .toSet()
}

fun getWidth(map: Map<Coord, Octopus>) = map.keys.maxOfOrNull { it.x }?.plus(1) ?: 0

fun getHeight(map: Map<Coord, Octopus>) = map.keys.maxOfOrNull { it.y }?.plus(1) ?: 0

fun toString(map: Map<Coord, Octopus>, color: Boolean = true): String {
    val width = getWidth(map)
    val height = getHeight(map)

    return buildString {
        for (y in 0 until height) {
            for (x in 0 until width) {
                val octopus = map[Coord(x, y)]
                if (octopus != null) {
                    if (octopus.flashed) {
                        if (color) {
                            append(AsciiColor.BRIGHT_WHITE.format(octopus.energy))
                        } else {
                            append(octopus.energy)
                        }
                    } else {
                        append(octopus.energy.toString())
                    }
                } else {
                    if (color) {
                        append(AsciiColor.BRIGHT_BLACK.format("?"))
                    } else {
                        append("?")
                    }
                }
            }

            if (y < height - 1) {
                append("\n")
            }
        }
    }
}
