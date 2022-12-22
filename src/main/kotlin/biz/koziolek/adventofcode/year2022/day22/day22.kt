package biz.koziolek.adventofcode.year2022.day22

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val forceFieldNotes = parseForceFieldNotes(inputFile.bufferedReader().readLines())
    val (position, facing) = followPath(forceFieldNotes.board, forceFieldNotes.path)
    val password = getPassword(position, facing)
    println("Password for $position and $facing is: $password")
}

enum class TurnDirection {
    RIGHT,
    LEFT,
}

enum class Facing(val value: Int, val symbol: Char) {
    RIGHT(0, '>'),
    DOWN(1, 'v'),
    LEFT(2, '<'),
    TOP(3, '^');

    fun turn(turnDirection: TurnDirection): Facing =
        when (turnDirection) {
            TurnDirection.RIGHT -> when (this) {
                RIGHT -> DOWN
                DOWN -> LEFT
                LEFT -> TOP
                TOP -> RIGHT
            }
            TurnDirection.LEFT -> when (this) {
                RIGHT -> TOP
                DOWN -> RIGHT
                LEFT -> DOWN
                TOP -> LEFT
            }
        }
}

const val VOID = ' '
const val OPEN = '.'
const val WALL = '#'

data class ForceFieldNotes(
    val board: Map<Coord, Char>,
    val path: List<Pair<Int, TurnDirection?>>,
)

fun parseForceFieldNotes(lines: Iterable<String>): ForceFieldNotes {
    val board = lines
        .takeWhile { it.isNotEmpty() }
        .flatMapIndexed { y: Int, line: String ->
            line.mapIndexed { x, c ->
                if (c != VOID) {
                    Coord(x + 1, y + 1) to c
                } else {
                    null
                }
            }.filterNotNull()
        }
        .toMap()

    val path = Regex("([0-9]+)([RL])?")
        .findAll(lines.last())
        .map { result ->
            val steps = result.groups[1]!!.value.toInt()
            val turn = result.groups[2]?.value.let {
                when (it) {
                    null -> null
                    "R" -> TurnDirection.RIGHT
                    "L" -> TurnDirection.LEFT
                    else -> throw IllegalArgumentException("Unsupported turn direction: '$it'")
                }
            }

            steps to turn
        }
        .toList()

    return ForceFieldNotes(board, path)
}

fun visualizeForceShieldBoard(board: Map<Coord, Char>, highlight: Coord? = null): String {
    val minX = board.keys.minOf { it.x }
    val maxX = board.keys.maxOf { it.x }
    val minY = board.keys.minOf { it.y }
    val maxY = board.keys.maxOf { it.y }

    return buildString {
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                val coord = Coord(x, y)
                val symbol = board[coord]

                if (coord == highlight) {
                    append('X')
                } else if (symbol != null) {
                    append(symbol)
                } else {
                    append(VOID)
                }
            }

            if (y < maxY) {
                append('\n')
            }
        }
    }
}

fun followPath(
    board: Map<Coord, Char>,
    path: List<Pair<Int, TurnDirection?>>,
): Pair<Coord, Facing> {
    val startPos = Coord(
        y = 1,
        x = board.keys.filter { it.y == 1 }.minOf { it.x }
    )

    return path.fold(Pair(startPos, Facing.RIGHT)) { (currentPos, currentFacing), (steps, turn) ->
        var newPos = currentPos
        val (dx, dy) = when (currentFacing) {
            Facing.RIGHT -> 1 to 0
            Facing.DOWN -> 0 to 1
            Facing.LEFT -> -1 to 0
            Facing.TOP -> 0 to -1
        }

//        println("$steps$turn")

        for (step in 1..steps) {
            var potentialNewPos = newPos + Coord(dx, dy)
//            println("$newPos -> $potentialNewPos")
//            println(visualizeForceShieldBoard(board, highlight = potentialNewPos))

            if (board[potentialNewPos] == WALL) {
                break
            }

            if (potentialNewPos !in board) {
                potentialNewPos = when (currentFacing) {
                    Facing.RIGHT -> potentialNewPos.copy(
                        x = board.keys.filter { it.y == potentialNewPos.y }.minOf { it.x }
                    )
                    Facing.DOWN -> potentialNewPos.copy(
                        y = board.keys.filter { it.x == potentialNewPos.x }.minOf { it.y }
                    )
                    Facing.LEFT -> potentialNewPos.copy(
                        x = board.keys.filter { it.y == potentialNewPos.y }.maxOf { it.x }
                    )
                    Facing.TOP -> potentialNewPos.copy(
                        y = board.keys.filter { it.x == potentialNewPos.x }.maxOf { it.y }
                    )
                }
            }

            if (board[potentialNewPos] == WALL) {
                break
            }

            newPos = potentialNewPos
        }

        val newFacing = turn?.let { currentFacing.turn(it) } ?: currentFacing

        Pair(newPos, newFacing)
    }
}

fun getPassword(position: Coord, facing: Facing): Int =
    1000 * position.y + 4 * position.x + facing.value
