package biz.koziolek.adventofcode.year2021

import java.io.File

fun main() {
    val inputFile = File("src/main/resources/year2021/day5/input")
    val strLines = inputFile.bufferedReader().readLines()
    val lines = parseLines(strLines)
    val map = createMap(lines)
    
    println(map)
    println(">= 2: ${map.countGreaterOrEqual(2)}")
}

data class Line(val x1: Int, val y1: Int, val x2: Int, val y2: Int) {
    val isHorizontal = y1 == y2
    val isVertical = x1 == x2

    fun getCoveredPoints(): List<Pair<Int, Int>> {
        val minX = minOf(x1, x2)
        val maxX = maxOf(x1, x2)
        val minY = minOf(y1, y2)
        val maxY = maxOf(y1, y2)

        return when {
            isHorizontal -> (minX..maxX).map { Pair(it, y1) }
            isVertical -> (minY..maxY).map { Pair(x1, it) }
            else -> throw IllegalArgumentException("Only horizontal and vertical lines are supported")
        }
    }
}

fun parseLines(strLines: List<String>): List<Line> =
        strLines
                .filter { it.isNotBlank() }
                .map { it.split(',', ' ') }
                .map {
                    Line(
                            x1 = it[0].toInt(),
                            y1 = it[1].toInt(),
                            x2 = it[3].toInt(),
                            y2 = it[4].toInt()
                    )
                }

data class Map(val width: Int = 0,
               val height: Int = 0,
               private val points: List<List<Int>> = emptyList()) {

    fun addLine(line: Line): Map {
        if (!line.isHorizontal && !line.isVertical) {
            return this
        }

        val coveredPoints = line.getCoveredPoints()
        if (coveredPoints.isEmpty()) {
            return this
        }
        
        println("Adding line: $line")

        val newWidth = maxOf(width, line.x1 + 1, line.x2 + 1)
        val newHeight = maxOf(height, line.y1 + 1, line.y2 + 1)

        return copy(
                width = newWidth,
                height = newHeight,
                points = (0 until newHeight).map { i ->
                    (0 until newWidth).map { j ->
                        points.getOrElse(i) { emptyList() }.getOrElse(j) { 0 } + coveredPoints.count { it.first == j && it.second == i }
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

        return (0 until height).joinToString(separator = "\n") { i ->
            (0 until width).joinToString(separator = "") { j ->
                when (points[i][j]) {
                    0 -> ".".repeat(strLen)
                    else -> points[i][j].toString().padStart(strLen, ' ')
                }
            }
        }
    }
}

fun createMap(lines: List<Line>): Map = lines.fold(Map(), Map::addLine)
