package biz.koziolek.adventofcode.year2022.day14

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.Line
import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val rockLines = parseRockLines(inputFile.bufferedReader().readLines())
    val rockMap = buildRockMap(rockLines)
    println("Units of sand that come to rest before sand starts flowing into the abyss: ${countSandNotInAbyss(rockMap)}")
    println("Units of sand that come to rest before sand blocks the source: ${countSandWithInfiniteFloor(rockMap)}")
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

interface RockMap {
    val source: Coord?
    val minX: Int
    val maxX: Int
    val minY: Int
    val maxY: Int
    val maxRockY: Int
    operator fun get(coord: Coord): Char?
    fun isFree(coord: Coord): Boolean
    fun addSand(coord: Coord): RockMap
}

data class RockMapWithAbyss(private val map: Map<Coord, Char>) : RockMap {
    override val source = map.entries.singleOrNull { it.value == SOURCE }?.key
    override val minX = map.keys.minOf { it.x }
    override val maxX = map.keys.maxOf { it.x }
    override val minY = map.keys.minOf { it.y }
    override val maxY = map.keys.maxOf { it.y }
    override val maxRockY = map.filter { it.value == ROCK }.maxOf { it.key.y }

    override operator fun get(coord: Coord) = map[coord]

    override fun isFree(coord: Coord) =
        coord !in map || map[coord] == AIR

    override fun addSand(coord: Coord) =
        RockMapWithAbyss(map = map + (coord to SAND))
}

data class RockMapWithFloor(
    val map: RockMap,
    val floorDistance: Int = 2
) : RockMap {
    private val floorY = map.maxRockY + floorDistance
    override val source = map.source
    override val minX = map.minX
    override val maxX = map.maxX
    override val minY = map.minY
    override val maxY = floorY
    override val maxRockY = floorY

    override fun get(coord: Coord) =
        if (map[coord] != null) {
            map[coord]
        } else if (coord.y == floorY) {
            ROCK
        } else {
            null
        }

    override fun isFree(coord: Coord) =
        map.isFree(coord) && coord.y != floorY

    override fun addSand(coord: Coord) =
        copy(map = map.addSand(coord))
}

fun buildRockMap(rockLines: Set<Line>, source: Coord = Coord(500, 0)): RockMap =
    rockLines
        .flatMap { line -> line.getCoveredPoints() }
        .associateWith { ROCK }
        .plus(source to SOURCE)
        .let { RockMapWithAbyss(it) }

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
object SourceBlocked : Exception()

fun dropSand(map: RockMap, count: Int = 1): RockMap =
    (1..count).fold(map) { currentMap, _ ->
        dropSand(currentMap)
    }

fun dropSand(map: RockMap): RockMap {
    val abyssY = map.maxY
    var sand = map.source ?: throw SourceBlocked
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
            } catch (e: SourceBlocked) {
                return@sequence
            }
        }
    }.count()

fun countSandWithInfiniteFloor(rockMap: RockMap): Int =
    countSandNotInAbyss(RockMapWithFloor(rockMap))
