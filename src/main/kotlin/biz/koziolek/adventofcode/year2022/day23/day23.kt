package biz.koziolek.adventofcode.year2022.day23

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput
import biz.koziolek.adventofcode.getAdjacentCoords
import kotlin.math.max
import kotlin.math.min

fun main() {
    val inputFile = findInput(object {})
    val elves = parseElves(inputFile.bufferedReader().readLines())
    val rounds = 10
    val movedElves = moveElves(elves, rounds = rounds)

    println("== End of Round $rounds ==")
    println(visualizeElves(movedElves))
    println("Empty ground: ${countEmptyGround(movedElves)}")
}

const val ELF = '#'
const val EMPTY = '.'

fun parseElves(lines: Iterable<String>): Set<Coord> =
    lines
        .flatMapIndexed { y, line ->
            line.mapIndexed { x, char ->
                if (char == ELF) {
                    Coord(x, y)
                } else {
                    null
                }
            }
        }
        .filterNotNull()
        .toSet()

fun visualizeElves(elves: Set<Coord>, corners: Pair<Coord, Coord>? = null): String {
    @Suppress("DuplicatedCode")
    val minX = corners?.let { min(it.first.x, it.second.x) } ?: elves.minOf { it.x }
    val maxX = corners?.let { max(it.first.x, it.second.x) } ?: elves.maxOf { it.x }
    val minY = corners?.let { min(it.first.y, it.second.y) } ?: elves.minOf { it.y }
    val maxY = corners?.let { max(it.first.y, it.second.y) } ?: elves.maxOf { it.y }

    return buildString {
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                val coord = Coord(x, y)
                
                if (coord in elves) {
                    append(ELF)
                } else {
                    append(EMPTY)
                }
            }

            if (y < maxY) {
                append('\n')
            }
        }
    }
}

enum class Direction {
    NORTH,
    SOUTH,
    WEST,
    EAST;

    fun getPosition(startPos: Coord): Coord =
        when (this) {
            NORTH -> startPos + Coord(0, -1)
            SOUTH -> startPos + Coord(0, +1)
            WEST -> startPos + Coord(-1, 0)
            EAST -> startPos + Coord(+1, 0)
        }

    fun getAdjacentPositions(startPos: Coord): Set<Coord> =
        when (this) {
            NORTH -> setOf(
                startPos + Coord(-1, -1),
                startPos + Coord(+0, -1),
                startPos + Coord(+1, -1),
            )
            SOUTH -> setOf(
                startPos + Coord(-1, +1),
                startPos + Coord(+0, +1),
                startPos + Coord(+1, +1),
            )
            WEST -> setOf(
                startPos + Coord(-1, +1),
                startPos + Coord(-1, +0),
                startPos + Coord(-1, -1),
            )
            EAST -> setOf(
                startPos + Coord(+1, +1),
                startPos + Coord(+1, +0),
                startPos + Coord(+1, -1),
            )
        }
}

fun moveElves(elves: Set<Coord>, rounds: Int): Set<Coord> =
    directionsToConsider()
        .take(rounds)
        .fold(elves, ::processRound)

fun moveElvesUntilNoMovements(elves: Set<Coord>): Pair<Int, Set<Coord>> {
    var currentElves = elves
    var round = 0

    for (directions in directionsToConsider()) {
        val newElves = processRound(currentElves, directions)
        round++

        if (newElves == currentElves) {
            break
        }

        currentElves = newElves
    }

    return round to currentElves
}

private fun directionsToConsider(): Sequence<List<Direction>> =
    sequence {
        val initialOrder = listOf(Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST)
        var offset = 0

        while (true) {
            val currentOrder = initialOrder.indices.map {
                val index = (it + offset) % initialOrder.size
                initialOrder[index]
            }
            yield(currentOrder)
            offset++
        }
    }

private fun processRound(currentElves: Set<Coord>, directions: List<Direction>): Set<Coord> {
    val proposedMoves: Collection<Pair<Coord, Coord?>> = proposeMoves(currentElves, directions)

    return proposedMoves.groupBy { it.second }
        .entries
        .flatMap { it ->
            if (it.key == null) {
                // Doesn't want to move
                it.value.map { it.first }
            } else if (it.value.size != 1) {
                // Conflicting propositions
                it.value.map { it.first }
            } else {
                // Typical move
                it.value.map { it.second!! }
            }
        }
        .toSet()
}

private fun proposeMoves(elves: Set<Coord>, directions: List<Direction>): Collection<Pair<Coord, Coord?>> =
    elves.map { elf ->
        val adjElves = elves.getAdjacentCoords(elf, includeDiagonal = true)

        val newPos = if (adjElves.isEmpty()) {
            null
        } else {
            directions
                .firstOrNull { direction ->
                    direction.getAdjacentPositions(elf).all { it !in elves }
                }
                ?.getPosition(elf)
        }
        elf to newPos
    }

fun countEmptyGround(elves: Set<Coord>): Int {
    @Suppress("DuplicatedCode")
    val minX = elves.minOf { it.x }
    val maxX = elves.maxOf { it.x }
    val minY = elves.minOf { it.y }
    val maxY = elves.maxOf { it.y }

    return (maxX - minX + 1) * (maxY - minY + 1) - elves.size
}
