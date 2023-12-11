package biz.koziolek.adventofcode.year2023.day11

import biz.koziolek.adventofcode.LongCoord
import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val map = parseGalaxyMap(inputFile.bufferedReader().readLines())
    println("Sum of shortest paths between every pair of galaxies in universe scaled:")
    println("- by 2 is: ${map.scale(2).distances.sum()}")
    println("- by 1 000 000: ${map.scale(1_000_000).distances.sum()}")
}

const val GALAXY = '#'
const val VOID = '.'

data class GalaxyMap(val galaxies: Set<LongCoord>) {
    val width = galaxies.maxOf { it.x } + 1
    val height = galaxies.maxOf { it.y } + 1
    val distances: List<Long> by lazy {
        val galaxiesList = galaxies.toList()
        galaxiesList
            .flatMapIndexed { firstIdx, firstCoord ->
                galaxiesList
                    .filterIndexed { secondIndex, _ -> secondIndex > firstIdx }
                    .map { secondCoord -> firstCoord to secondCoord }
            }
            .map { it.first.manhattanDistanceTo(it.second) }
    }

    fun scale(scale: Int = 2): GalaxyMap {
        val emptyColumns = (0..width) - galaxies.map { it.x }.toSet()
        val emptyRows = (0..height) - galaxies.map { it.y }.toSet()

        return GalaxyMap(
            galaxies = galaxies.map { oldCoord ->
                LongCoord(
                    x = oldCoord.x + (scale - 1) * emptyColumns.count { it < oldCoord.x },
                    y = oldCoord.y + (scale - 1) * emptyRows.count { it < oldCoord.y },
                )
            }.toSet(),
        )
    }

    override fun toString() =
        buildString {
            for (y in 0 until height) {
                for (x in 0 until width) {
                    val coord = LongCoord(x, y)

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
                LongCoord(x, y)
            } else {
                null
            }
        }
    }.let { GalaxyMap(it.toSet()) }
