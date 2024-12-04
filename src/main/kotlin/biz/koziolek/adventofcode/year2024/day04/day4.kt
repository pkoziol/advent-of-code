package biz.koziolek.adventofcode.year2024.day04

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})
    val letterGrid = parseLetterGrid(inputFile.bufferedReader().readLines())
    val xmas = findXMAS(letterGrid)
    println("XMAS count: ${xmas.size}")
}

fun parseLetterGrid(lines: Iterable<String>): Map<Coord, Char> =
    lines.parse2DMap().toMap()

fun findXMAS(letterGrid: Map<Coord, Char>): List<List<Pair<Coord, Char>>> =
    letterGrid.walkSouth()
        .filter { letterGrid[it] == 'X' }
        .flatMap { startingCoord ->
            listOf(
                startingCoord.walk(Direction.NORTH, 3, true),
                startingCoord.walk(Direction.EAST, 3, true),
                startingCoord.walk(Direction.SOUTH, 3, true),
                startingCoord.walk(Direction.WEST, 3, true),
                startingCoord.walk(Direction.NORTH, Direction.EAST, 3, true),
                startingCoord.walk(Direction.SOUTH, Direction.EAST, 3, true),
                startingCoord.walk(Direction.SOUTH, Direction.WEST, 3, true),
                startingCoord.walk(Direction.NORTH, Direction.WEST, 3, true)
            )
        }
        .filter { it.all { coord -> coord in letterGrid } }
        .map { it.map { coord -> coord to letterGrid[coord]!! }.toList() }
        .filter { it.joinToString("") { it.second.toString() } == "XMAS" }
        .toList()
