package biz.koziolek.adventofcode.year2021.day05

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput
import biz.koziolek.adventofcode.getHeight
import biz.koziolek.adventofcode.getWidth
import kotlin.math.abs

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

data class Line(val x1: Int, val y1: Int, val x2: Int, val y2: Int) {
    val isHorizontal = y1 == y2
    val isVertical = x1 == x2
    val isDiagonal = abs(x1 - x2) == abs(y1 - y2)

    fun getCoveredPoints(): List<Coord> {
        return when {
            isHorizontal -> range(x1, x2).map { Coord(it, y1) }
            isVertical -> range(y1, y2).map { Coord(x1, it) }
            isDiagonal -> range(x1, x2).zip(range(y1, y2), ::Coord)
            else -> throw IllegalArgumentException("Only horizontal, vertical and diagonal lines are supported")
        }
    }

    private fun range(start: Int, end: Int): IntProgression =
            if (start < end) {
                start..end
            } else {
                start downTo end
            }

    override fun toString(): String {
        return "$x1,$y1 -> $x2,$y2"
    }

    companion object {
        fun fromString(str: String): Line {
            return str.split(',', ' ').let {
                Line(
                        x1 = it[0].toInt(),
                        y1 = it[1].toInt(),
                        x2 = it[3].toInt(),
                        y2 = it[4].toInt()
                )
            }
        }
    }
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
