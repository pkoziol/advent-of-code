package biz.koziolek.adventofcode.year2023.day11

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val map = parseGalaxyMap(inputFile.bufferedReader().readLines())
    println("Sum of shortest paths between every pair in expanded universe is: ${map.expand().distances.sum()}")
}

const val GALAXY = '#'
const val VOID = '.'

data class GalaxyMap(val galaxies: Set<Coord>) {
    val width = galaxies.maxOf { it.x } + 1
    val height = galaxies.maxOf { it.y } + 1
    val distances: List<Int> by lazy {
        val galaxiesList = galaxies.toList()
        galaxiesList
            .flatMapIndexed { firstIdx, firstCoord ->
                galaxiesList
                    .filterIndexed { secondIndex, _ -> secondIndex > firstIdx }
                    .map { secondCoord -> firstCoord to secondCoord }
            }
            .map { it.first.manhattanDistanceTo(it.second) }
    }

    fun expand(): GalaxyMap {
        val emptyColumns = (0..width) - galaxies.map { it.x }.toSet()
        val emptyRows = (0..height) - galaxies.map { it.y }.toSet()

        return GalaxyMap(
            galaxies = galaxies.map { oldCoord ->
                Coord(
                    x = oldCoord.x + emptyColumns.count { it < oldCoord.x },
                    y = oldCoord.y + emptyRows.count { it < oldCoord.y },
                )
            }.toSet(),
        )
    }

    override fun toString() =
        buildString {
            for (y in 0 until height) {
                for (x in 0 until width) {
                    val coord = Coord(x, y)

                    if (coord in galaxies) {
                        append(GALAXY)
                    } else {
                        append(VOID)
                    }
                }

                if (y != height - 1) {
                    append("\n")
                }
            }
        }
}

fun parseGalaxyMap(lines: Iterable<String>): GalaxyMap =
    lines.flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, char ->
            if (char == GALAXY) {
                Coord(x, y)
            } else {
                null
            }
        }
    }.let { GalaxyMap(it.toSet()) }
