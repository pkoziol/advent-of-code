package biz.koziolek.adventofcode.year2024.day15

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})
    val (warehouse, moves) = parseWarehouse(inputFile.bufferedReader().readLines())
    val finalWarehouse = warehouse.move(moves)
    println("Sum of boxes coords: ${finalWarehouse.sumBoxesCoords()}")
}

const val ROBOT = '@'
const val BOX = 'O'
const val WALL = '#'
const val EMPTY = '.'

data class Warehouse(val map: Map<Coord, Char>) {

    fun move(moves: List<Direction>): Warehouse =
        moves.fold(this) { acc, direction ->
            acc.move(direction)
        }

    fun move(direction: Direction): Warehouse {
        val robotCoord = map.entries.single { it.value == ROBOT }.key
        val newMap = map.toMutableMap()
        val moved = move(direction, robotCoord, newMap)

        return when (moved) {
            true -> Warehouse(newMap)
            false -> this
        }
    }

    private fun move(direction: Direction,
                     coord: Coord,
                     newMap: MutableMap<Coord, Char>): Boolean {
        val newCoord = coord.move(direction)
        return when(newMap[newCoord]) {
            WALL -> false
            BOX -> {
                val moved = move(direction, newCoord, newMap)
                if (moved) {
                    newMap[newCoord] = newMap[coord]!!
                    newMap.remove(coord)
                }
                moved
            }
            else -> {
                newMap[newCoord] = newMap[coord]!!
                newMap.remove(coord)
                true
            }
        }
    }

    fun sumBoxesCoords(): Int =
        map.entries
            .filter { it.value == BOX }
            .sumOf { it.key.x + 100 * it.key.y }

    override fun toString() =
        map.to2DStringOfStrings { _, c ->
            when (c) {
                ROBOT -> AsciiColor.BRIGHT_YELLOW.format(c)
                BOX -> AsciiColor.BRIGHT_WHITE.format(c)
                WALL -> AsciiColor.BRIGHT_BLACK.format(c)
                EMPTY -> AsciiColor.BLACK.format(EMPTY)
                null -> AsciiColor.BLACK.format(EMPTY)
                else -> throw IllegalArgumentException("Unknown char: $c")
            }
        }
}

fun parseWarehouse(lines: Iterable<String>): Pair<Warehouse, List<Direction>> {
    val warehouse = lines.takeWhile { it.isNotBlank() }
        .parse2DMap()
        .filter { it.second != EMPTY }
        .toMap()
        .let { Warehouse(it) }

    val moves = lines.dropWhile { it.isNotBlank() }
        .drop(1)
        .flatMap { line ->
            line.map { c -> Direction.fromChar(c) }
        }

    return warehouse to moves
}
