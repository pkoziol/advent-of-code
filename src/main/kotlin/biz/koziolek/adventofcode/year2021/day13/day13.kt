package biz.koziolek.adventofcode.year2021.day13

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})
    val lines = inputFile.bufferedReader().readLines()

    val dotsMap = parseTransparentPaper(lines)
    val foldInstructions = parseFoldInstructions(lines)

    val foldedOnce = fold(dotsMap, foldInstructions.take(1))
    println("Visible dots after folding once: ${foldedOnce.size}")

    val fullyFolded = fold(dotsMap, foldInstructions)
    println("Paper after fully folding:")
    println(toString(fullyFolded))
}

fun parseTransparentPaper(lines: List<String>): Map<Coord, Boolean> =
    lines.takeWhile { it.isNotEmpty() }
        .map { it.split(',', limit = 2) }
        .associate { Coord(
            x = it[0].toInt(),
            y = it[1].toInt()
        ) to true }

fun parseFoldInstructions(lines: List<String>): List<Pair<String, Int>> =
    lines.dropWhile { it.isNotEmpty() }
        .drop(1)
        .map { it.split('=', limit = 2) }
        .map { (prefix, num) ->
            when (prefix) {
                "fold along y" -> "up" to num.toInt()
                "fold along x" -> "left" to num.toInt()
                else -> throw IllegalArgumentException("Unknown fold command: $prefix=$num")
            }
        }

fun toString(map: Map<Coord, Boolean>): String {
    val width = map.getWidth()
    val height = map.getHeight()

    return buildString {
        for (y in 0 until height) {
            for (x in 0 until width) {
                val isDot = map[Coord(x, y)] ?: false
                if (isDot) {
                    append('#')
                } else {
                    append('.')
                }
            }

            if (y < height - 1) {
                append("\n")
            }
        }
    }
}

fun fold(map: Map<Coord, Boolean>, instructions: List<Pair<String, Int>>): Map<Coord, Boolean> =
    instructions.fold(map) { acc, (direction, value) ->
        when (direction) {
            "up" -> foldUp(acc, y = value)
            "left" -> foldLeft(acc, x = value)
            else -> throw IllegalArgumentException("Unknown fold direction: $direction")
        }
    }

fun foldUp(map: Map<Coord, Boolean>, y: Int): Map<Coord, Boolean> =
    map.mapKeys { (coord, _) ->
        if (coord.y > y) {
            coord.copy(y = y - (coord.y - y))
        } else {
            coord
        }
    }

fun foldLeft(map: Map<Coord, Boolean>, x: Int): Map<Coord, Boolean> =
    map.mapKeys { (coord, _) ->
        if (coord.x > x) {
            coord.copy(x = x - (coord.x - x))
        } else {
            coord
        }
    }
