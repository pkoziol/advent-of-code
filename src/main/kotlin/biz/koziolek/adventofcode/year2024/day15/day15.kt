package biz.koziolek.adventofcode.year2024.day15

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})

    val (warehouse, moves) = parseWarehouse(inputFile.bufferedReader().readLines())
    val finalWarehouse = warehouse.move(moves)
    println("Sum of boxes coords: ${finalWarehouse.sumBoxesCoords()}")

    val scaledWarehouse = warehouse.scale()
    val finalScaledWarehouse = scaledWarehouse.move(moves)
    println("Sum of boxes coords in scaled warehouse: ${finalScaledWarehouse.sumBoxesCoords()}")
}

const val ROBOT = '@'
const val BOX = 'O'
const val SCALED_BOX_L = '['
const val SCALED_BOX_R = ']'
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
            true -> copy(map = newMap)
            false -> this
        }
    }

    private fun move(
        direction: Direction,
        coord: Coord,
        newMap: MutableMap<Coord, Char>
    ): Boolean {
        val newCoord = coord.move(direction)
        return when (newMap[coord]) {
            null, EMPTY -> true
            WALL -> false
            BOX, ROBOT -> {
                moveOne(direction, coord, newCoord, newMap)
            }
            SCALED_BOX_L -> {
                if (direction == Direction.WEST || direction == Direction.EAST) {
                    moveOne(direction, coord, newCoord, newMap)
                } else {
                    val coordR = coord + Coord(1, 0)
                    val newCoordR = newCoord + Coord(1, 0)
                    moveTwo(direction, coord, newCoord, coordR, newCoordR, newMap)
                }
            }
            SCALED_BOX_R -> {
                if (direction == Direction.WEST || direction == Direction.EAST) {
                    moveOne(direction, coord, newCoord, newMap)
                } else {
                    val coordL = coord + Coord(-1, 0)
                    val newCoordL = newCoord + Coord(-1, 0)
                    moveTwo(direction, coordL, newCoordL, coord, newCoord, newMap)
                }
            }
            else -> {
                throw IllegalArgumentException("Unknown char: ${newMap[coord]}")
            }
        }
    }

    private fun moveOne(
        direction: Direction,
        coord: Coord,
        newCoord: Coord,
        newMap: MutableMap<Coord, Char>
    ): Boolean {
        val moved = move(direction, newCoord, newMap)
        if (moved) {
            newMap[newCoord] = newMap[coord]!!
            newMap.remove(coord)
        }
        return moved
    }

    private fun moveTwo(
        direction: Direction,
        coordL: Coord,
        newCoordL: Coord,
        coordR: Coord,
        newCoordR: Coord,
        newMap: MutableMap<Coord, Char>
    ): Boolean {
        val movedL = move(direction, newCoordL, newMap)
        val movedR = move(direction, newCoordR, newMap)
        return if (movedL && movedR) {
            newMap[newCoordL] = newMap[coordL]!!
            newMap[newCoordR] = newMap[coordR]!!
            newMap.remove(coordL)
            newMap.remove(coordR)
            true
        } else {
            false
        }
    }

    fun scale(): Warehouse {
        val newMap = buildMap {
            val secondCoord = Coord(1, 0)
            map.entries.forEach { (coord, c) ->
                val newCoord = Coord(coord.x * 2, coord.y)
                when (c) {
                    ROBOT -> {
                        set(newCoord, ROBOT)
                    }
                    BOX -> {
                        set(newCoord, SCALED_BOX_L)
                        set(newCoord + secondCoord, SCALED_BOX_R)
                    }
                    WALL -> {
                        set(newCoord, WALL)
                        set(newCoord + secondCoord, WALL)
                    }
                    EMPTY -> {}
                    else -> throw IllegalArgumentException("Unknown char: $c")
                }
            }
        }
        return copy(map = newMap)
    }

    fun sumBoxesCoords(): Int =
        map.entries
            .filter { it.value == BOX || it.value == SCALED_BOX_L }
            .sumOf { it.key.x + 100 * it.key.y }

    override fun toString() =
        map.to2DStringOfStrings { _, c ->
            when (c) {
                ROBOT -> AsciiColor.BRIGHT_YELLOW.format(c)
                BOX -> AsciiColor.BRIGHT_WHITE.format(c)
                SCALED_BOX_L -> AsciiColor.BRIGHT_WHITE.format(c)
                SCALED_BOX_R -> AsciiColor.BRIGHT_WHITE.format(c)
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
