package biz.koziolek.adventofcode.year2022.day09

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

fun main() {
    val inputFile = findInput(object {})
    val rope = Rope()
    val moves = parseMoves(inputFile.bufferedReader().readLines())
    val allMovedRopes = moveRopeInSteps(rope, moves)
    println("Positions visited by tail at least once: ${countPositionsVisitedByTail(allMovedRopes)}")
}

data class Rope(
    val head: Coord = Coord(0, 0),
    val tail: Coord = Coord(0, 0),
)

enum class Move {
    UP,
    DOWN,
    LEFT,
    RIGHT,
}

fun moveRopeInSteps(rope: Rope, moves: List<Move>): List<Rope> =
    moves.fold(listOf(rope)) { ropes, move ->
        ropes + moveRope(ropes.last(), move)
    }

fun moveRope(rope: Rope, move: Move, times: Int): Rope =
    moveRope(rope, List(times) { move })

fun moveRope(rope: Rope, moves: List<Move>): Rope =
    moves.fold(rope) { movedRope, direction -> moveRope(movedRope, direction) }

fun moveRope(rope: Rope, move: Move): Rope {
    val (headMoveX, headMoveY) = when (move) {
        Move.UP -> 0 to 1
        Move.DOWN -> 0 to -1
        Move.LEFT -> -1 to 0
        Move.RIGHT -> 1 to 0
    }

    val newHead = Coord(
        x = rope.head.x + headMoveX,
        y = rope.head.y + headMoveY,
    )

    val tailDistanceX = newHead.x - rope.tail.x
    val tailDistanceY = newHead.y - rope.tail.y

    var tailMoveX = 0
    var tailMoveY = 0
    if (abs(tailDistanceX) > 1) {
        tailMoveX = tailDistanceX.sign

        if (rope.tail.y != newHead.y) {
            tailMoveY = tailDistanceY.sign
        }
    } else if (abs(tailDistanceY) > 1) {
        tailMoveY = tailDistanceY.sign

        if (rope.tail.x != newHead.x) {
            tailMoveX = tailDistanceX.sign
        }
    }

    val newTail = Coord(
        x = rope.tail.x + tailMoveX,
        y = rope.tail.y + tailMoveY,
    )

    return Rope(
        head = newHead,
        tail = newTail,
    )
}

fun visualizeRope(rope: Rope, corners: Pair<Coord, Coord>, markStart: Boolean = false): String {
    val minX = min(corners.first.x, corners.second.x)
    val maxX = max(corners.first.x, corners.second.x)
    val minY = min(corners.first.y, corners.second.y)
    val maxY = max(corners.first.y, corners.second.y)

    return buildString {
        for (y in maxY downTo minY) {
            for (x in minX..maxX) {
                if (x == rope.head.x && y == rope.head.y) {
                    append('H')
                } else if (x == rope.tail.x && y == rope.tail.y) {
                    append('T')
                } else if (markStart && x == 0 && y == 0) {
                    append('s')
                } else {
                    append('.')
                }
            }

            if (y > minY) {
                append('\n')
            }
        }
    }
}

fun parseMoves(lines: Iterable<String>): List<Move> =
    lines.flatMap { line ->
        val (dir, count) = line.split(' ')
        val move = when (dir) {
            "U" -> Move.UP
            "D" -> Move.DOWN
            "L" -> Move.LEFT
            "R" -> Move.RIGHT
            else -> throw IllegalArgumentException("Unknown direction: '$dir'")
        }

        List(count.toInt()) { move }
    }

fun countPositionsVisitedByTail(ropes: List<Rope>): Int =
    ropes.map { it.tail }.distinct().count()
