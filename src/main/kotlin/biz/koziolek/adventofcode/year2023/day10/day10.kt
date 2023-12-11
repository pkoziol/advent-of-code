package biz.koziolek.adventofcode.year2023.day10

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})
    val maze = parsePipeMaze(inputFile.bufferedReader().readLines())
    println("Farthest distance from the start on the loop: ${maze.theLoopFarthestDistanceFromStart}")
    println("Tiles are enclosed by the loop: ${maze.insideTheLoop.size}")
}

const val NORTH_SOUTH = '|'
const val EAST_WEST = '-'
const val NORTH_EAST = 'L'
const val NORTH_WEST = 'J'
const val SOUTH_WEST = '7'
const val SOUTH_EAST = 'F'
@Suppress("unused")
const val GROUND = '.'
const val START = 'S'

data class PipeMaze(val contents: Map<Coord, Char>, val startPos: Coord) {
    val theLoop by lazy {
        buildMap {
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

    /**
     * Find coordinates inside the loop using [Ray casting algorithm](https://en.wikipedia.org/wiki/Point_in_polygon#Ray_casting_algorithm)
     */
    val insideTheLoop: Set<Coord> by lazy {
        contents.keys
            .filter { !theLoop.keys.contains(it) }
            .filter { findEdgeIntersections(it) % 2 == 1 }
            .map { it }
            .toSet()
    }

    /**
     * Finds edges that are crossed once.
     *
     *         *
     *     ----|----
     *         | F--
     *         |/
     *         |
     *        /|
     *     --J |
     *     --7 |
     *        \|
     *         |
     *         |\
     *         | L--
     *         v
     */
    private val singleCrossingRegex: Regex =
        listOf(
            Regex.escape(EAST_WEST.toString()),
            buildVerticalLineRegex(SOUTH_EAST, NORTH_WEST),
            buildVerticalLineRegex(SOUTH_WEST, NORTH_EAST),
        )
            .joinToString("|")
            .let { Regex(it) }

    /**
     * Finds edges that are crossed twice.
     *
     *         *
     *         | F--
     *         |/
     *         |
     *        /|
     *        \|
     *         |
     *     --7 |\
     *        \| L--
     *         |
     *         |\
     *         |/
     *         |
     *        /|
     *     --J |
     *         v
     */
    private val doubleCrossingRegex: Regex =
        listOf(
            buildVerticalLineRegex(SOUTH_EAST, NORTH_EAST),
            buildVerticalLineRegex(SOUTH_WEST, NORTH_WEST),
        )
            .joinToString("|")
            .let { Regex(it) }

    private fun buildVerticalLineRegex(corner1: Char, corner2: Char) =
        Regex.escape(corner1.toString()) + Regex.escape(NORTH_SOUTH.toString()) + "*" + Regex.escape(corner2.toString())

    /**
     * Cast a ray starting at the north edge going down to [coord]
     * and count how many loop edges it crosses.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun findEdgeIntersections(coord: Coord): Int {
        val rayStr = (0 until coord.y)
            .map { y -> Coord(coord.x, y) }
            .mapNotNull { theLoop[it] }.joinToString("")
        val singleCrossing = singleCrossingRegex.findAll(rayStr)
            .map { it.value }
            .toList()
        val doubleCrossing = doubleCrossingRegex.findAll(rayStr)
            .map { it.value }
            .toList()

        return 0 + singleCrossing.size + doubleCrossing.size * 2
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
        throw IllegalArgumentException("Expected exactly 2 pipes connected start, but got ${connectedTo.size}: $connectedTo")
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
        lines.parse2DMap().forEach { (coord, char) ->
            put(coord, char)

            if (char == START) {
                startPos = coord
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
