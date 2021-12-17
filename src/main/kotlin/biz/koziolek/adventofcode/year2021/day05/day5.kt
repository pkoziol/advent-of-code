package biz.koziolek.adventofcode.year2021.day05

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})
    val strLines = inputFile.bufferedReader().readLines()
    val lines = parseLines(strLines)

    val map1 = createVentMap(lines.filter { it.isHorizontal || it.isVertical })
    println(map1)
    println(">= 2: ${countGreaterOrEqual(map1, 2)}")

    val map2 = createVentMap(lines)
    println(map2)
    println(">= 2: ${countGreaterOrEqual(map2, 2)}")
}

fun parseLines(strLines: List<String>): List<Line> =
        strLines
                .filter { it.isNotBlank() }
                .map { Line.fromString(it) }

fun createVentMap(lines: List<Line>): Map<Coord, Int> =
    lines
        .filter { it.isHorizontal || it.isVertical || it.isDiagonal }
        .flatMap { it.getCoveredPoints() }
        .groupingBy { it }
        .eachCount()

fun countGreaterOrEqual(ventMap: Map<Coord, Int>, value: Int): Int =
    ventMap.values.count { it >= value }

fun ventMapToString(ventMap: Map<Coord, Int>): String {
    val maxValue = ventMap.values.maxOf { it }
    val strLen = maxValue.toString().length

    return (0 until ventMap.getHeight()).joinToString(separator = "\n") { y ->
        (0 until ventMap.getWidth()).joinToString(separator = "") { x ->
            ventMap[Coord(x, y)]
                ?.toString()
                ?.padStart(strLen, ' ')
                ?: ".".repeat(strLen)
        }
    }
}
