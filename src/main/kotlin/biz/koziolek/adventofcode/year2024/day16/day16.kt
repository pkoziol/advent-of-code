package biz.koziolek.adventofcode.year2024.day16

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})
    val maze = parseMaze(inputFile.bufferedReader().readLines())
    val lowestScorePath = maze.findLowestScorePath()
    println("Lowest score is: ${lowestScorePath.score}")
}

const val WALL = '#'
const val EMPTY = '.'
const val START = 'S'
const val END = 'E'

const val POINTS_PER_MOVE = 1
const val POINTS_PER_TURN = 1000

fun parseMaze(lines: Iterable<String>): Maze =
    lines.parse2DMap()
        .toMap()
        .let { map ->
            val start = map.entries.single { it.value == START }.key
            val end = map.entries.single { it.value == END }.key
            Maze(map, start, end)
        }

data class Maze(
    val map: Map<Coord, Char>,
    val start: Coord,
    val end: Coord,
) {
    fun findLowestScorePath(): MazePath =
        findPaths().minByOrNull { it.score }!!

    fun findPaths(): List<MazePath> {
        val graph = buildGraph {
            val rotations = mapOf(
                Direction.NORTH to listOf(Direction.WEST, Direction.EAST),
                Direction.EAST to listOf(Direction.NORTH, Direction.SOUTH),
                Direction.SOUTH to listOf(Direction.EAST, Direction.WEST),
                Direction.WEST to listOf(Direction.SOUTH, Direction.NORTH),
            )
            for ((coord, char) in map) {
                if (char == WALL) {
                    continue
                }

                for (direction in Direction.entries) {
                    val node = CoordWithDirectionNode(coord, direction)

                    for (newDirection in rotations[direction]!!) {
                        add(
                            UniDirectionalGraphEdge(
                                node1 = node,
                                node2 = CoordWithDirectionNode(coord, newDirection),
                                weight = scoreMove(coord, direction, coord, newDirection)
                            )
                        )
                    }

                    val nextPos = coord.move(direction)
                    if (map[nextPos] != WALL) {
                        add(
                            UniDirectionalGraphEdge(
                                node1 = node,
                                node2 = CoordWithDirectionNode(nextPos, direction),
                                weight = scoreMove(coord, direction, nextPos)
                            )
                        )
                    }
                }
            }
        }

        val paths = graph.findShortestPaths(
            start = CoordWithDirectionNode(start, Direction.EAST),
            end = CoordWithDirectionNode(end, Direction.EAST),
        ).map { path ->
            MazePath(
                maze = this,
                coords = path.map { it.coord }.distinct(),
            )
        }

        return paths
    }

    override fun toString(): String {
        return map.to2DStringOfStrings { _, c ->
            when (c) {
                WALL -> AsciiColor.BRIGHT_BLACK.format(WALL)
                EMPTY, null -> AsciiColor.BLACK.format(EMPTY)
                START -> AsciiColor.BRIGHT_RED.format(START)
                END -> AsciiColor.BRIGHT_GREEN.format(END)
                else -> throw IllegalArgumentException("Unknown character: $c")
            }
        }
    }
}

data class MazePath(
    val maze: Maze,
    val coords: List<Coord>,
) {
    val score: Int by lazy {
        coords
            .fold(Triple(0, maze.start, Direction.EAST)) { (score, currentPos, currentDir), nextPos ->
                val nextDir = when (nextPos) {
                    currentPos -> Direction.fromCoord(coords[1] - coords[0])
                    else -> Direction.fromCoord(nextPos - currentPos)
                }
                Triple(
                    score + scoreMove(currentPos, currentDir, nextPos, nextDir),
                    nextPos,
                    nextDir
                )
            }
            .first
    }

    fun toDirections(): List<Direction> =
        coords.zipWithNext { current, next ->
            Direction.fromCoord(next - current)
        }.let { it + it.last() }

    override fun toString(): String {
        val directions = toDirections()
        return maze.map.to2DStringOfStrings { coord, c ->
            val index = coords.indexOf(coord)
            if (index != -1) {
                val direction = directions[index]
                AsciiColor.BRIGHT_WHITE.format(direction.char)
            } else {
                when (c) {
                    WALL -> AsciiColor.BRIGHT_BLACK.format(WALL)
                    EMPTY, null -> AsciiColor.BLACK.format(EMPTY)
                    START -> AsciiColor.BRIGHT_RED.format(START)
                    END -> AsciiColor.BRIGHT_GREEN.format(END)
                    else -> throw IllegalArgumentException("Unknown character: $c")
                }
            }
        }
    }
}

fun scoreMove(currentPos: Coord,
              currentDir: Direction,
              nextPos: Coord): Int {
    val nextDirection = Direction.fromCoord(nextPos - currentPos)
    return scoreMove(currentPos, currentDir, nextPos, nextDirection)
}

fun scoreMove(currentPos: Coord,
              currentDir: Direction,
              nextPos: Coord,
              nextDir: Direction): Int {
    val pointsPerTurn = if (currentDir != nextDir) POINTS_PER_TURN else 0
    val pointsPerDistance = if (currentPos != nextPos) POINTS_PER_MOVE else 0
    return pointsPerDistance + pointsPerTurn
}
