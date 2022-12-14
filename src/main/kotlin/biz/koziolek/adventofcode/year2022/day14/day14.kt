package biz.koziolek.adventofcode.year2022.day14

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.Line
import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val rockLines = parseRockLines(inputFile.bufferedReader().readLines())
    val rockMap = buildRockMap(rockLines)
    println("Inits of sand that come to rest before sand starts flowing into the abyss: ${countSandNotInAbyss(rockMap)}")
}

const val ROCK = '#'
const val AIR = '.'
const val SOURCE = '+'
const val SAND = 'o'

fun parseRockLines(lines: Iterable<String>): Set<Line> =
    lines.flatMap { line ->
        line.split(" -> ")
            .zipWithNext { a, b ->
                Line(
                    from = Coord.fromString(a),
                    to = Coord.fromString(b),
                )
            }
    }.toSet()

fun buildRockMap(rockLines: Set<Line>, source: Coord = Coord(500, 0)): Map<Coord, Char> =
    rockLines
        .flatMap { line -> line.getCoveredPoints() }
        .associateWith { ROCK }
        .plus(source to SOURCE)

fun visualizeRockMap(rockMap: Map<Coord, Char>): String =
    buildString {
        val minX = rockMap.keys.minOf { it.x }
        val maxX = rockMap.keys.maxOf { it.x }
        val minY = rockMap.keys.minOf { it.y }
        val maxY = rockMap.keys.maxOf { it.y }

        for (y in minY..maxY) {
            for (x in minX..maxX) {
                append(rockMap[Coord(x, y)] ?: AIR)
            }

            if (y < maxY) {
                append("\n")
            }
        }
    }

object FallenIntoAbyss : Exception()

fun dropSand(map: Map<Coord, Char>, count: Int = 1): Map<Coord, Char> =
    (1..count).fold(map) { currentMap, _ ->
        dropSand(currentMap)
    }

fun dropSand(map: Map<Coord, Char>): Map<Coord, Char> {
    val abyssY = map.keys.maxOf { it.y }
    var sand = map.entries.single { it.value == SOURCE }.key
    var canMove = true

    while (canMove) {
        val coordsToTry = listOf(
            sand.copy(y = sand.y + 1),
            sand.copy(y = sand.y + 1, x = sand.x - 1),
            sand.copy(y = sand.y + 1, x = sand.x + 1),
        )

        canMove = false
        for (coord in coordsToTry) {
            if (coord !in map || map[coord] == AIR) {
                canMove = true
                sand = coord
                break
            }
        }
        
        if (sand.y > abyssY) {
            throw FallenIntoAbyss
        }
    }

    return map + (sand to SAND)
}

fun countSandNotInAbyss(rockMap: Map<Coord, Char>): Int =
    sequence {
        var map = rockMap
        while (true) {
            try {
                map = dropSand(map)
                yield(map)
            } catch (e: FallenIntoAbyss) {
                return@sequence
            }
        }
    }.count()
