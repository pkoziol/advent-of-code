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
    return findText(letterGrid, "MAS")
        .map { it.map { (coord, _) -> coord } }
        .groupBy { it[1] }
        .values
        .sumOf { masList ->
            productWithItself(masList, true)
                .count { (mas1, mas2) -> isX(mas1, mas2) }
        }
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

private fun isX(mas1: List<Coord>, mas2: List<Coord>): Boolean {
    if (mas1[0] == mas2[0] || mas1[1] != mas2[1]) {
        return false
    }

    val diagonalAdj = mas1[1].getAdjacentCoords(includeDiagonal = true) - mas1[1].getAdjacentCoords()
    return (mas1[0] in diagonalAdj && mas1[2] in diagonalAdj && mas2[0] in diagonalAdj && mas2[2] in diagonalAdj)
}
