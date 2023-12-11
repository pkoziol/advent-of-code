package biz.koziolek.adventofcode.year2021.day15

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})
    val lines = inputFile.bufferedReader().readLines()
    val riskMap = parseRiskMap(lines)

    val lowestRiskPath = findLowestRiskPath(riskMap,
        start = Coord(0, 0),
        end = Coord(riskMap.getWidth() - 1, riskMap.getHeight() - 1)
    )
    println(toString(riskMap, lowestRiskPath))
    println("Lowest total risk of any path: ${getTotalRisk(riskMap, lowestRiskPath)}")

    val expandedMap = expandMap(riskMap, expandWidth = 4, expandHeight = 4)
    val lowestRiskPath5x5 = findLowestRiskPath(expandedMap,
        start = Coord(0, 0),
        end = Coord(expandedMap.getWidth() - 1, expandedMap.getHeight() - 1)
    )
    println(toString(expandedMap, lowestRiskPath5x5))
    println("Lowest total risk of any path: ${getTotalRisk(expandedMap, lowestRiskPath5x5)}")
}

fun parseRiskMap(lines: List<String>): Map<Coord, Int> =
    lines.parse2DMap { it.digitToInt() }.toMap()

fun expandMap(riskMap: Map<Coord, Int>, expandWidth: Int, expandHeight: Int): Map<Coord, Int> {
    val width = riskMap.getWidth()
    val height = riskMap.getHeight()
    
    return buildMap {
        for (j in 0..expandHeight) {
            for (i in 0..expandWidth) {
                val delta = Coord(i * width, j * height)
                for ((coord, risk) in riskMap) {
                    val newCoord = coord + delta
                    var newRisk = risk + i + j
                    while (newRisk > 9) {
                        newRisk -= 9
                    }

                    put(newCoord, newRisk)
                }
            }
        }
    }
}

fun findLowestRiskPath(riskMap: Map<Coord, Int>, start: Coord, end: Coord): List<Coord> =
    riskMap.toGraph(includeDiagonal = false)
        .findShortestPath(start.toGraphNode(), end.toGraphNode())
        .map { it.coord }

fun getTotalRisk(riskMap: Map<Coord, Int>, path: List<Coord>): Int =
    path.drop(1).sumOf { riskMap[it] ?: 0 }

fun toString(riskMap: Map<Coord, Int>, path: List<Coord>, color: Boolean = true): String {
    val width = riskMap.getWidth()
    val height = riskMap.getHeight()

    return buildString {
        for (y in 0 until height) {
            for (x in 0 until width) {
                val coord = Coord(x, y)
                if (coord in path) {
                    if (color) {
                        append(AsciiColor.BRIGHT_WHITE.format(riskMap[coord]))
                    } else {
                        append(riskMap[coord])
                    }
                } else {
                    append(riskMap[coord])
                }
            }

            if (y < height - 1) {
                append("\n")
            }
        }
    }
}
