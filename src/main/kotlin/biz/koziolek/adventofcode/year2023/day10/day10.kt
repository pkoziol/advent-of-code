package biz.koziolek.adventofcode.year2023.day10

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})
    val maze = parsePipeMaze(inputFile.bufferedReader().readLines())
    println("Farthest distance from the start on the loop: ${maze.theLoopFarthestDistanceFromStart}")
    println("Tiles are enclosed by the loop: ${maze.insideTheLoop.size}")
}

const val NORTH_SOUTH = '|'
const val EAST_WEST = '-' // is a horizontal pipe connecting east and west.
const val NORTH_EAST = 'L' // is a 90-degree bend connecting north and east.
const val NORTH_WEST = 'J' // is a 90-degree bend connecting north and west.
const val SOUTH_WEST = '7' // is a 90-degree bend connecting south and west.
const val SOUTH_EAST = 'F' // is a 90-degree bend connecting south and east.
const val GROUND = '.' // is ground; there is no pipe in this tile.
const val START = 'S'

data class PipeMaze(val contents: Map<Coord, Char>, val startPos: Coord) {
    val theLoop by lazy {
        buildMap<Coord, Char> {
            val coordsToCheck = mutableListOf(startPos)

            while (coordsToCheck.isNotEmpty()) {
                val coord = coordsToCheck.removeFirst()
                val pipe = contents[coord]
                if (pipe != null) {
                    put(coord, pipe)
                    findConnectedTo(contents, coord)
                        .filter { !contains(it) }
                        .forEach { coordsToCheck.add(it) }
                }
            }
        }
    }

    val theLoopFarthestDistanceFromStart: Int by lazy { theLoop.size / 2 }

    val insideTheLoop: Set<Coord> by lazy {
        val filter = contents.keys
            .filter { !theLoop.keys.contains(it) }
        filter
            .filter { findEdgeIntersections(it) % 2 == 1 }
            .map { it }
            .toSet()
    }

    fun findEdgeIntersections(coord: Coord): Int {
        val map = (0 until coord.y)
            .map { y -> Coord(coord.x, y) }
        val ray = map
            .mapNotNull { theLoop[it] }
        val rayStr = ray.joinToString("")

//        val countEW = ray.count { it == EAST_WEST }
//        val countNE = ray.count { it == NORTH_EAST }
//        val countNW = ray.count { it == NORTH_WEST }
//        val countSW = ray.count { it == SOUTH_WEST }
//        val countSE = ray.count { it == SOUTH_EAST }
//        val a = countSE + countNW
//        val aa = if (a > 0) { a - 1 } else { 0 }
//        val b = countSW + countNE
//        val bb = if (b > 0) { b - 1 } else { 0 }
//        val intersections = countEW + aa + bb

        val singleCrossing = Regex("-|F\\|*J|7\\|*L")
            .findAll(rayStr)
            .map { it.value }
            .toList()
        val doubleCrossing = Regex("F\\|*L|7\\|*J")
            .findAll(rayStr)
            .map { it.value }
            .toList()
        val intersections = 0 + singleCrossing.size + doubleCrossing.size * 2

        return intersections
    }
}

fun findConnectedTo(pipes: Map<Coord, Char>, coord: Coord): Set<Coord> =
    when (pipes[coord]) {
        NORTH_SOUTH -> setOf(coord + Coord(0, -1), coord + Coord(0, 1))
        EAST_WEST -> setOf(coord + Coord(-1, 0), coord + Coord(1, 0))
        NORTH_EAST -> setOf(coord + Coord(0, -1), coord + Coord(1, 0))
        NORTH_WEST -> setOf(coord + Coord(0, -1), coord + Coord(-1, 0))
        SOUTH_WEST -> setOf(coord + Coord(0, 1), coord + Coord(-1, 0))
        SOUTH_EAST -> setOf(coord + Coord(0, 1), coord + Coord(1, 0))
        START -> pipes
            .getAdjacentCoords(coord, includeDiagonal = false)
            .filter { findConnectedTo(pipes, it).contains(coord) }
            .toSet()

        else -> emptySet()
    }

fun findStartReplacement(pipes: Map<Coord, Char>, startCoord: Coord): Char {
    val connectedTo = findConnectedTo(pipes, startCoord)
    if (connectedTo.size != 2) {
        throw IllegalArgumentException("Not 2?!")
    }

    val leftOrRight = connectedTo.filter { it.y == startCoord.y }
    val upOrDown = connectedTo.filter { it.x == startCoord.x }
    val replacement = if (upOrDown.size == 2) {
        NORTH_SOUTH
    } else if (leftOrRight.size == 2) {
        EAST_WEST
    } else {
        val diff = Coord(
            x = leftOrRight.single().x - startCoord.x,
            y = upOrDown.single().y - startCoord.y,
        )

        when (diff) {
            Coord(1, -1) -> NORTH_EAST
            Coord(-1, -1) -> NORTH_WEST
            Coord(-1, 1) -> SOUTH_WEST
            Coord(1, 1) -> SOUTH_EAST
            else -> throw IllegalArgumentException("Unexpected diff: $diff")
        }
    }

    return replacement
}

fun parsePipeMaze(lines: Iterable<String>): PipeMaze {
    var startPos: Coord? = null
    val pipes = buildMap {
        lines
            .flatMapIndexed { y, line ->
                line.mapIndexed { x, char ->
                    val coord = Coord(x, y)
                    put(coord, char)

                    if (char == START) {
                        startPos = coord
                    }
                }
            }

        val startReplacement = findStartReplacement(this, startPos!!)
        put(startPos!!, startReplacement)
    }

    return PipeMaze(pipes, startPos!!)
}

fun mazeToString(maze: PipeMaze,
                 defaultColor: AsciiColor = AsciiColor.BLACK,
                 startColor: AsciiColor = AsciiColor.BRIGHT_YELLOW,
                 loopColor: AsciiColor? = null,
                 insideColor: AsciiColor? = null) =
    pipesToString(
        maze.contents,
        highlights = maze.contents.keys.associateWith { defaultColor }
                + (if (loopColor != null ) maze.theLoop.keys.associateWith { loopColor } else emptyMap())
                + mapOf(maze.startPos to startColor)
                + (if (insideColor != null ) maze.insideTheLoop.associateWith { insideColor } else emptyMap())
    )

fun pipesToString(pipes: Map<Coord, Char>,
                  highlights: Map<Coord, AsciiColor> = emptyMap()) =
    buildString {
        val width = pipes.getWidth()
        val height = pipes.getHeight()

        for (y in 0 until height) {
            for (x in 0 until width) {
                val coord = Coord(x, y)
                val symbol = pipes[coord] ?: ' '
                append(highlights[coord]?.format(symbol) ?: symbol)
            }

            if (y != height - 1) {
                append("\n")
            }
        }
    }
