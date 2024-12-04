package biz.koziolek.adventofcode.year2024.day04

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})
    val letterGrid = parseLetterGrid(inputFile.bufferedReader().readLines())
    println("XMAS count: ${countXMAS(letterGrid)}")
    println("X-MAS count: ${countX_MAS(letterGrid)}")
}

fun parseLetterGrid(lines: Iterable<String>): Map<Coord, Char> =
    lines.parse2DMap().toMap()

fun countXMAS(letterGrid: Map<Coord, Char>): Int =
    findText(letterGrid, "XMAS").size

fun countX_MAS(letterGrid: Map<Coord, Char>): Int {
    val allMas = findText(letterGrid, "MAS")
    var count = 0
    for (mas1 in allMas) {
        for (mas2 in allMas) {
            if (mas1 == mas2) {
                continue
            }

            val coords1 = mas1.map { it.first }
            val coords2 = mas2.map { it.first }

            if (coords1[1] != coords2[1]) {
                continue
            }

            val diagonalAdj = coords1[1].getAdjacentCoords(includeDiagonal = true) - coords1[1].getAdjacentCoords()
            if (coords1[0] in diagonalAdj && coords1[2] in diagonalAdj && coords2[0] in diagonalAdj && coords2[2] in diagonalAdj) {
                count++
            }
        }
    }

    return count / 2
}

private fun findText(letterGrid: Map<Coord, Char>, text: String): List<List<Pair<Coord, Char>>> =
    letterGrid.walkSouth()
        .filter { letterGrid[it] == text.first() }
        .flatMap { startingCoord ->
            listOf(
                startingCoord.walk(Direction.NORTH, text.length - 1, true),
                startingCoord.walk(Direction.EAST, text.length - 1, true),
                startingCoord.walk(Direction.SOUTH, text.length - 1, true),
                startingCoord.walk(Direction.WEST, text.length - 1, true),
                startingCoord.walk(Direction.NORTH, Direction.EAST, text.length - 1, true),
                startingCoord.walk(Direction.SOUTH, Direction.EAST, text.length - 1, true),
                startingCoord.walk(Direction.SOUTH, Direction.WEST, text.length - 1, true),
                startingCoord.walk(Direction.NORTH, Direction.WEST, text.length - 1, true)
            )
        }
        .filter { it.all { coord -> coord in letterGrid } }
        .map { it.map { coord -> coord to letterGrid[coord]!! }.toList() }
        .filter { it.joinToString("") { it.second.toString() } == text }
        .toList()
