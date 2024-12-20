package biz.koziolek.adventofcode.year2024.day20

import biz.koziolek.adventofcode.*
import java.util.*
import kotlin.collections.HashMap

fun main() {
    val inputFile = findInput(object {})
    val racetrack = parseRacetrack(inputFile.bufferedReader().readLines())

    val save = 100
    for (duration in listOf(2, 20)) {
        val cheats = findCheats(racetrack, duration)
        println("There is ${cheats.count { it.value >= save }} $duration-picoseconds cheats saving at least $save picoseconds")
    }
}

const val WALL = '#'
const val EMPTY = '.'
const val START = 'S'
const val END = 'E'

data class Racetrack(val map: Map<Coord, Char>) {
    val start: Coord = map.entries.single { it.value == START }.key
    val end: Coord = map.entries.single { it.value == END }.key

    override fun toString(): String =
        map.to2DStringOfStrings { _, c ->
            when (c) {
                WALL -> AsciiColor.BRIGHT_BLACK.format(c)
                EMPTY -> AsciiColor.WHITE.format(c)
                START -> AsciiColor.GREEN.format(c)
                END -> AsciiColor.RED.format(c)
                else -> throw IllegalArgumentException("Unknown character $c")
            }
        }
}

fun parseRacetrack(lines: Iterable<String>): Racetrack =
    lines.parse2DMap()
        .toMap()
        .let { Racetrack(it) }

fun findCheats(racetrack: Racetrack, duration: Int): Map<Pair<Coord, Coord>, Int> {
    val distanceMap = buildDistanceMap(racetrack)
    val nonWallCoords = racetrack.map.filterValues { it != WALL }.keys

    return nonWallCoords
        .flatMap { startCoord ->
            nonWallCoords
                .filter { endCoord -> startCoord.manhattanDistanceTo(endCoord) <= duration }
                .map { endCoord -> startCoord to endCoord }
        }
        .map { (beforeWall, afterWall) ->
            val distanceBeforeWall = distanceMap[beforeWall]!!
            val distanceAfterWall = distanceMap[afterWall]!!
            val savedDistance = distanceAfterWall - distanceBeforeWall - beforeWall.manhattanDistanceTo(afterWall)
            Pair(beforeWall to afterWall, savedDistance)
        }
        .filter { it.second > 0 }
        .toMap()
}

private fun buildDistanceMap(racetrack: Racetrack): Map<Coord, Int> {
    val cumulativeDistance: MutableMap<Coord, Int> = HashMap()
    val prev: MutableMap<Coord, Coord> = HashMap()
    val toVisit: MutableSet<Coord> = HashSet()

    for (node in racetrack.map.keys) {
        cumulativeDistance[node] = Int.MAX_VALUE
        toVisit.add(node)
    }
    cumulativeDistance[racetrack.start] = 0

    while (toVisit.isNotEmpty()) {
        val current = toVisit.minBy { node -> cumulativeDistance[node]!! }
        toVisit.remove(current)

        val currentNodeDistance = cumulativeDistance[current]!!

        for (otherNode in racetrack.map.getAdjacentCoords(current, includeDiagonal = false)) {
            if (racetrack.map[otherNode] == WALL) {
                continue
            }
            if (otherNode in toVisit) {
                val otherNodeDistance = cumulativeDistance[otherNode]!!
                val newDistance = currentNodeDistance + 1

                if (newDistance < otherNodeDistance) {
                    cumulativeDistance[otherNode] = newDistance
                    prev[otherNode] = current
                }
            }
        }
    }

    return cumulativeDistance
}
