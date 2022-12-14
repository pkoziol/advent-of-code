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

data class RockMap(private val map: Map<Coord, Char>) {
    val source = map.entries.single { it.value == SOURCE }.key
    val minX = map.keys.minOf { it.x }
    val maxX = map.keys.maxOf { it.x }
    val minY = map.keys.minOf { it.y }
    val maxY = map.keys.maxOf { it.y }

    operator fun get(coord: Coord) = map[coord]

    fun isFree(coord: Coord) =
        coord !in map || map[coord] == AIR

    fun addSand(coord: Coord) =
        copy(map = map + (coord to SAND))
}

fun buildRockMap(rockLines: Set<Line>, source: Coord = Coord(500, 0)): RockMap =
    rockLines
        .flatMap { line -> line.getCoveredPoints() }
        .associateWith { ROCK }
        .plus(source to SOURCE)
        .let { RockMap(it) }

fun visualizeRockMap(rockMap: RockMap): String =
    buildString {
        for (y in rockMap.minY..rockMap.maxY) {
            for (x in rockMap.minX..rockMap.maxX) {
                append(rockMap[Coord(x, y)] ?: AIR)
            }

            if (y < rockMap.maxY) {
                append("\n")
            }
        }
    }

object FallenIntoAbyss : Exception()

fun dropSand(map: RockMap, count: Int = 1): RockMap =
    (1..count).fold(map) { currentMap, _ ->
        dropSand(currentMap)
    }

fun dropSand(map: RockMap): RockMap {
    val abyssY = map.maxY
    var sand = map.source
    var canMove = true

    while (canMove) {
        val coordsToTry = listOf(
            sand.copy(y = sand.y + 1),
            sand.copy(y = sand.y + 1, x = sand.x - 1),
            sand.copy(y = sand.y + 1, x = sand.x + 1),
        )

        canMove = false
        for (coord in coordsToTry) {
            if (map.isFree(coord)) {
                canMove = true
                sand = coord
                break
            }
        }
        
        if (sand.y > abyssY) {
            throw FallenIntoAbyss
        }
    }

    return map.addSand(sand)
}

fun countSandNotInAbyss(rockMap: RockMap): Int =
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
