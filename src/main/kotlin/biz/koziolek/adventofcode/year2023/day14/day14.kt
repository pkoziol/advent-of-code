package biz.koziolek.adventofcode.year2023.day14

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})
    val platform = parsePlatform(inputFile.bufferedReader().readLines())
    println("Total load after sliding north: ${platform.slideNorth().totalLoad}")
}

const val ROUND_ROCK = 'O'
const val CUBE_ROCK = '#'
const val EMPTY_SPACE = '.'

data class Platform(val rocks: Map<Coord, Char>) {

    val totalLoad: Int =
        rocks
            .filter { it.value == ROUND_ROCK }
            .map { rocks.getHeight() - it.key.y }
            .sum()

    override fun toString() =
        buildString {
            val width = rocks.getWidth()
            val height = rocks.getHeight()

            for (y in 0 until height) {
                for (x in 0 until width) {
                    val coord = Coord(x, y)
                    val symbol = rocks[coord] ?: EMPTY_SPACE
                    append(symbol)
                }

                if (y != height - 1) {
                    append("\n")
                }
            }
        }

    fun slideNorth(): Platform {
        val newRocks = rocks.toMutableMap()

        for (y in 0..rocks.getHeight()) {
            for (x in 0..rocks.getWidth()) {
                val coord = Coord(x, y)
                if (newRocks[coord] == ROUND_ROCK) {
                    var lastFreeY = y
                    for (yy in y-1 downTo 0) {
                        if (newRocks[Coord(x, yy)] == null) {
                            lastFreeY = yy
                        } else {
                            break
                        }
                    }

                    newRocks.remove(coord)
                    newRocks[Coord(x, lastFreeY)] = ROUND_ROCK
                }
            }
        }

        return Platform(newRocks)
    }
}

fun parsePlatform(lines: Iterable<String>): Platform =
    Platform(lines.parse2DMap { if (it != EMPTY_SPACE) it else null }.toMap())
