package biz.koziolek.adventofcode.year2023.day10

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput
import biz.koziolek.adventofcode.getAdjacentCoords

fun main() {
    val inputFile = findInput(object {})
    val maze = parsePipeMaze(inputFile.bufferedReader().readLines())
    println("Farthest distance from the start on the loop: ${maze.theLoopFarthestDistanceFromStart}")
}

const val NORTH_SOUTH = '|'
const val EAST_WEST = '-' // is a horizontal pipe connecting east and west.
const val NORTH_EAST = 'L' // is a 90-degree bend connecting north and east.
const val NORTH_WEST = 'J' // is a 90-degree bend connecting north and west.
const val SOUTH_WEST = '7' // is a 90-degree bend connecting south and west.
const val SOUTH_EAST = 'F' // is a 90-degree bend connecting south and east.
const val GROUND = '.' // is ground; there is no pipe in this tile.
const val START = 'S'

data class PipeMaze(val contents: Map<Coord, Char>) {
    val startPos = contents.entries.single { it.value == START }.key

    val theLoop by lazy {
        buildMap<Coord, Char> {
            val coordsToCheck = mutableListOf(startPos)

            while (coordsToCheck.isNotEmpty()) {
                val coord = coordsToCheck.removeFirst()
                val pipe = contents[coord]
                if (pipe != null) {
                    put(coord, pipe)
                    findConnectedTo(coord)
                        .filter { !contains(it) }
                        .forEach { coordsToCheck.add(it) }
                }
            }
        }
    }

    val theLoopFarthestDistanceFromStart: Int by lazy { theLoop.size / 2 }

    fun findConnectedTo(coord: Coord): Set<Coord> =
        when (contents[coord]) {
            NORTH_SOUTH -> setOf(coord + Coord(0, -1), coord + Coord(0, 1))
            EAST_WEST -> setOf(coord + Coord(-1, 0), coord + Coord(1, 0))
            NORTH_EAST -> setOf(coord + Coord(0, -1), coord + Coord(1, 0))
            NORTH_WEST -> setOf(coord + Coord(0, -1), coord + Coord(-1, 0))
            SOUTH_WEST -> setOf(coord + Coord(0, 1), coord + Coord(-1, 0))
            SOUTH_EAST -> setOf(coord + Coord(0, 1), coord + Coord(1, 0))
            START -> contents
                .getAdjacentCoords(coord, includeDiagonal = false)
                .filter { findConnectedTo(it).contains(coord) }
                .toSet()
            else -> emptySet()
        }
}

fun parsePipeMaze(lines: Iterable<String>): PipeMaze =
    lines
        .flatMapIndexed { y, line ->
            line.mapIndexed { x, char ->
                Coord(x, y) to char
            }
        }
        .toMap()
        .let { PipeMaze(it) }
