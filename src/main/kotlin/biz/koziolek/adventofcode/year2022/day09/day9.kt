package biz.koziolek.adventofcode.year2022.day09

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

fun main() {
    val inputFile = findInput(object {})
    val moves = parseMoves(inputFile.bufferedReader().readLines())

    val rope = PlanckRope()
    val allMovedRopes = moveRopeInSteps(rope, moves)
    println("Positions visited by tail at least once: ${countPositionsVisitedByTail(allMovedRopes)}")

    val longRope = LongRope.ofLength(10)
    val allMovedLongRopes = moveRopeInSteps(longRope, moves)
    println("Positions visited by tail at least once: ${countPositionsVisitedByTail(allMovedLongRopes)}")
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
            Move.UP_LEFT -> -1 to 1
            Move.UP_RIGHT -> 1 to 1
            Move.DOWN_LEFT -> -1 to -1
            Move.DOWN_RIGHT -> 1 to -1
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

data class LongRope(
    val planckRopes: List<PlanckRope>
) : Rope {

    companion object {

        fun ofLength(length: Int) =
            LongRope(List(length - 1) { PlanckRope() })
    }

    override val head: Coord
        get() = planckRopes.first().head

    override val tail: Coord
        get() = planckRopes.last().tail

    override fun move(move: Move): Rope {
        val movedPlanckRopes = mutableListOf<PlanckRope>()
        var nextMove: Move? = move

        for (rope in planckRopes) {
            val movedRope = if (nextMove != null) {
                rope.move(nextMove)
            } else {
                rope
            }

            nextMove = if (movedRope.tail == rope.tail) {
                null
            } else {
                val tailDistanceX = movedRope.tail.x - rope.tail.x
                val tailDistanceY = movedRope.tail.y - rope.tail.y

                when (tailDistanceX to tailDistanceY) {
                    0 to 1 -> Move.UP
                    0 to -1 -> Move.DOWN
                    -1 to 0 -> Move.LEFT
                    1 to 0 -> Move.RIGHT
                    -1 to 1 -> Move.UP_LEFT
                    1 to 1 -> Move.UP_RIGHT
                    -1 to -1 -> Move.DOWN_LEFT
                    1 to -1 -> Move.DOWN_RIGHT
                    else -> throw IllegalStateException("Unexpected tail distance: $tailDistanceX,$tailDistanceY}")
                }
            }

            movedPlanckRopes.add(movedRope)
        }

        return LongRope(movedPlanckRopes)
    }

    private val symbols = ('1'..'9') + ('a'..'z')

    override fun getSymbol(coord: Coord): Char? =
        if (coord == planckRopes.first().head) {
            'H'
        } else {
            planckRopes.withIndex()
                .find { (_, planckRope) -> coord == planckRope.tail }
                ?.let { (index, _) -> symbols[index] }
        }
}

enum class Move {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    UP_LEFT,
    UP_RIGHT,
    DOWN_LEFT,
    DOWN_RIGHT,
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
