package biz.koziolek.adventofcode.year2021

import java.io.File
import kotlin.math.abs

fun main() {
    val inputFile = File("src/main/resources/year2021/day5/input")
    val strLines = inputFile.bufferedReader().readLines()
    val lines = parseLines(strLines)

    val map1 = createMap(lines.filter { it.isHorizontal || it.isVertical })
    println(map1)
    println(">= 2: ${map1.countGreaterOrEqual(2)}")

    val map2 = createMap(lines)
    println(map2)
    println(">= 2: ${map2.countGreaterOrEqual(2)}")
}

data class Line(val x1: Int, val y1: Int, val x2: Int, val y2: Int) {
    val isHorizontal = y1 == y2
    val isVertical = x1 == x2
    val isDiagonal = abs(x1 - x2) == abs(y1 - y2)

    fun getCoveredPoints(): List<Pair<Int, Int>> {
        return when {
            isHorizontal -> range(x1, x2).map { Pair(it, y1) }
            isVertical -> range(y1, y2).map { Pair(x1, it) }
            isDiagonal -> range(x1, x2) zip range(y1, y2)
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

data class Map(val width: Int = 0,
               val height: Int = 0,
               private val points: List<List<Int>> = emptyList()) {

    fun addLine(line: Line): Map {
        if (!line.isHorizontal && !line.isVertical && !line.isDiagonal) {
            return this
        }

        val coveredPoints = line.getCoveredPoints()
        if (coveredPoints.isEmpty()) {
            return this
        }

        val newWidth = maxOf(width, line.x1 + 1, line.x2 + 1)
        val newHeight = maxOf(height, line.y1 + 1, line.y2 + 1)

        return copy(
                width = newWidth,
                height = newHeight,
                points = (0 until newHeight).map { y ->
                    val pointsForRow = coveredPoints.filter { it.second == y }

                    if (pointsForRow.isEmpty()) {
                        if (y >= height) {
                            // Add new row
                            List(newWidth) { 0 }
                        } else if (newWidth == width) {
                            // Reuse existing row
                            points[y]
                        } else {
                            // Add new columns
                            points[y] + List(newWidth - width) { 0 }
                        }
                    } else {
                        (0 until newWidth).map { x ->
                            points.getOrElse(y) { emptyList() }.getOrElse(x) { 0 } + pointsForRow.count { it.first == x }
                        }
                    }
                }
        )
    }

    fun countGreaterOrEqual(value: Int): Int {
        return points.sumOf { row -> row.count { it >= value } }
    }

    override fun toString(): String {
        val maxValue = points.maxOf { row -> row.maxOf { it } }
        val strLen = maxValue.toString().length

        return (0 until height).joinToString(separator = "\n") { y ->
            (0 until width).joinToString(separator = "") { x ->
                when (points[y][x]) {
                    0 -> ".".repeat(strLen)
                    else -> points[y][x].toString().padStart(strLen, ' ')
                }
            }
        }
    }
}

fun createMap(lines: List<Line>): Map = lines.fold(Map(), Map::addLine)
