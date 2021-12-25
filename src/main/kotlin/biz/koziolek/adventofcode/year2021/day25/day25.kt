package biz.koziolek.adventofcode.year2021.day25

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput
import biz.koziolek.adventofcode.getHeight
import biz.koziolek.adventofcode.getWidth

fun main() {
    val inputFile = findInput(object {})
    val lines = inputFile.bufferedReader().readLines()
    val map = parseSeaCucumberMap(lines)
    val (stepNumber, _) = moveUntilStop(map)
    println("Sea cucumbers stop moving after $stepNumber steps")
}

fun parseSeaCucumberMap(lines: List<String>): Map<Coord, Char> =
    lines.flatMapIndexed { y, line ->
        line.mapIndexed { x, char ->
            Coord(x, y) to char
        }
    }.toMap()

fun seaCucumberMapToString(seaCucumberMap: Map<Coord, Char>): String {
    val width = seaCucumberMap.getWidth()
    val height = seaCucumberMap.getHeight()

    return buildString {
        for (y in 0 until height) {
            for (x in 0 until width) {
                append(seaCucumberMap[Coord(x, y)])
            }

            if (y < height - 1) {
                append("\n")
            }
        }
    }
}

fun moveUntilStop(seaCucumberMap: Map<Coord, Char>): Pair<Int, Map<Coord, Char>> {
    var stepNumber = 0
    var currentMap = seaCucumberMap

    while (true) {
        stepNumber++
        val nextMap = moveSeaCucumbers(currentMap)

        if (nextMap == currentMap) {
            break
        }

        currentMap = nextMap
    }

    return stepNumber to currentMap
}

fun moveSeaCucumbers(seaCucumberMap: Map<Coord, Char>): Map<Coord, Char> =
    moveAll(moveAll(seaCucumberMap, '>'), 'v')

private fun moveAll(seaCucumberMap: Map<Coord, Char>, typeToMove: Char): Map<Coord, Char> {
    val width = seaCucumberMap.getWidth()
    val height = seaCucumberMap.getHeight()

    return buildMap {
        for (y in 0 until height) {
            for (x in 0 until width) {
                val coord = Coord(x, y)
                put(coord, '.')
            }
        }

        for (y in 0 until height) {
            for (x in 0 until width) {
                val coord = Coord(x, y)
                val type = seaCucumberMap[coord]!!

                if (type == typeToMove) {
                    val forwardCoord = getForwardCoord(coord, type, width, height)
                    if (seaCucumberMap[forwardCoord] == '.') {
                        put(forwardCoord, type)
                    } else {
                        put(coord, type)
                    }
                } else if (type != '.'){
                    put(coord, type)
                }
            }
        }
    }
}

fun getForwardCoord(coord: Coord, type: Char, width: Int, height: Int): Coord =
    when (type) {
        '>' -> coord + Coord(1, 0)
        'v' -> coord + Coord(0, 1)
        else -> throw IllegalArgumentException("Unsupported type to move: $type")
    }.let {
        if (it.x >= width) {
            it.copy(x = 0)
        } else {
            it
        }
    }.let {
        if (it.y >= height) {
            it.copy(y = 0)
        } else {
            it
        }
    }
