package biz.koziolek.adventofcode.year2022.day09

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

fun main() {
    val inputFile = findInput(object {})
    val rope = PlanckRope()
    val moves = parseMoves(inputFile.bufferedReader().readLines())
    val allMovedRopes = moveRopeInSteps(rope, moves)
    println("Positions visited by tail at least once: ${countPositionsVisitedByTail(allMovedRopes)}")
}

interface Rope {

    val head: Coord
    val tail: Coord

    fun move(move: Move): Rope

    fun getSymbol(coord: Coord): Char?
}

data class PlanckRope(
    override val head: Coord = Coord(0, 0),
    override val tail: Coord = Coord(0, 0),
) : Rope {

    override fun move(move: Move): PlanckRope {
        val (headMoveX, headMoveY) = when (move) {
            Move.UP -> 0 to 1
            Move.DOWN -> 0 to -1
            Move.LEFT -> -1 to 0
            Move.RIGHT -> 1 to 0
        }

        val newHead = Coord(
            x = head.x + headMoveX,
            y = head.y + headMoveY,
        )

        val tailDistanceX = newHead.x - tail.x
        val tailDistanceY = newHead.y - tail.y

        var tailMoveX = 0
        var tailMoveY = 0
        if (abs(tailDistanceX) > 1) {
            tailMoveX = tailDistanceX.sign

            if (tail.y != newHead.y) {
                tailMoveY = tailDistanceY.sign
            }
        } else if (abs(tailDistanceY) > 1) {
            tailMoveY = tailDistanceY.sign

            if (tail.x != newHead.x) {
                tailMoveX = tailDistanceX.sign
            }
        }

        val newTail = Coord(
            x = tail.x + tailMoveX,
            y = tail.y + tailMoveY,
        )

        return PlanckRope(
            head = newHead,
            tail = newTail,
        )
    }

    override fun getSymbol(coord: Coord): Char? =
        when (coord) {
            head -> 'H'
            tail -> 'T'
            else -> null
        }
}

enum class Move {
    UP,
    DOWN,
    LEFT,
    RIGHT,
}

fun <T : Rope> moveRopeInSteps(rope: T, moves: List<Move>): List<T> =
    moves.fold(listOf(rope)) { ropes, move ->
        @Suppress("UNCHECKED_CAST")
        ropes + ropes.last().move(move) as T
    }

fun visualizeRope(rope: Rope, corners: Pair<Coord, Coord>, markStart: Boolean = false): String {
    val minX = min(corners.first.x, corners.second.x)
    val maxX = max(corners.first.x, corners.second.x)
    val minY = min(corners.first.y, corners.second.y)
    val maxY = max(corners.first.y, corners.second.y)

    return buildString {
        for (y in maxY downTo minY) {
            for (x in minX..maxX) {
                val coord = Coord(x, y)
                val symbol = rope.getSymbol(coord)

                if (symbol != null) {
                    append(symbol)
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
