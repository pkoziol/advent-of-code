package biz.koziolek.adventofcode.year2024.day20

import biz.koziolek.adventofcode.*
import java.util.*
import kotlin.collections.HashMap

fun main() {
    val inputFile = findInput(object {})
    val racetrack = parseRacetrack(inputFile.bufferedReader().readLines())
    val cheats = findCheats(racetrack)
    println("There is ${cheats.count { it.value >= 100 }} saving at least 100 picoseconds")
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

fun findCheats(racetrack: Racetrack): Map<Pair<Coord, Coord>, Int> {
    val distanceMap = buildDistanceMap(racetrack)

    return racetrack.map
        .filterValues { it == WALL }
        .keys
        .flatMap { wallCoord ->
            listOf(
                Triple(wallCoord + Coord(-1, 0), wallCoord, wallCoord + Coord(1, 0)),
                Triple(wallCoord + Coord(1, 0), wallCoord, wallCoord + Coord(-1, 0)),
                Triple(wallCoord + Coord(0, -1), wallCoord, wallCoord + Coord(0, 1)),
                Triple(wallCoord + Coord(0, 1), wallCoord, wallCoord + Coord(0, -1)),
            )
        }
        .filter { (beforeWall, wall, afterWall) ->
            beforeWall in racetrack.map && racetrack.map[beforeWall] != WALL
                    && wall in racetrack.map && racetrack.map[wall] == WALL
                    && afterWall in racetrack.map && racetrack.map[afterWall] != WALL
        }
        .map { (beforeWall, wall, afterWall) ->
            val distanceBeforeWall = distanceMap[beforeWall]!!
            val distanceAfterWall = distanceMap[afterWall]!!
            val moveCost = 2
            val savedDistance = distanceAfterWall - distanceBeforeWall - moveCost
            Pair(wall to afterWall, savedDistance)
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
