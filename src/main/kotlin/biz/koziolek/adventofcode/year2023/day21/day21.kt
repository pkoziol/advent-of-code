package biz.koziolek.adventofcode.year2023.day21

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput
import biz.koziolek.adventofcode.getAdjacentCoords
import biz.koziolek.adventofcode.parse2DMap

fun main() {
    val inputFile = findInput(object {})
    val map = parseGardenMap(inputFile.bufferedReader().readLines())
    val steps = 64
    println("${walk(map, steps).size} plots can be reached in $steps steps")
}

const val START = 'S'
const val GARDEN_PLOT = '.'
const val ROCK = '#'
const val REACHED_POS = 'O'

fun parseGardenMap(lines: Iterable<String>): Map<Coord, Char> =
    lines.parse2DMap().toMap()

fun findStart(map: Map<Coord, Char>) =
    map.entries.single { it.value == START }.key

fun walk(map: Map<Coord, Char>, steps: Int): Set<Coord> =
    (1..steps).fold(setOf(findStart(map))) { positions, _ ->
        positions
            .flatMap { map.getAdjacentCoords(it, includeDiagonal = false) }
            .filter { map[it] != ROCK }
            .toSet()
    }
