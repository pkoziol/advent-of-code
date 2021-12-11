package biz.koziolek.adventofcode.year2021.day09

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput
import biz.koziolek.adventofcode.getAdjacentCoords
import biz.koziolek.adventofcode.visitAll

fun main() {
    val inputFile = findInput(object {})
    val lines = inputFile.bufferedReader().readLines()
    val smokeMap = parseSmokeMap(lines)

    println("Low points risk sum is: ${getLowPointsRiskSum(smokeMap)}")

    val basins = findSmokeBasins(smokeMap)
    println("Product of sizes of the three largest basins is: ${getLargestBasinsSizeProduct(basins, n = 3)}")
}

fun getLowPointsRiskSum(smokeMap: Map<Coord, Int>): Int =
        findSmokeLowPoints(smokeMap).let { lowPoints ->
            lowPoints.sumOf { getRiskLevel(it, smokeMap, lowPoints) }
        }

fun getLargestBasinsSizeProduct(basins: Collection<Set<Coord>>, n: Int) =
        basins.map { it.size }
                .sorted()
                .reversed()
                .take(n)
                .ifEmpty { return 0 }
                .fold(1) { acc, size -> acc * size }

fun findSmokeBasins(smokeMap: Map<Coord, Int>): Collection<Set<Coord>> {
    return findSmokeLowPoints(smokeMap)
            .map { lowPoint ->
                buildSet {
                    visitAll(start = lowPoint) { currentCoord ->
                        val value = smokeMap[currentCoord] ?: 0
                        if (value != 9) {
                            add(currentCoord)
                            smokeMap.getAdjacentCoords(currentCoord, includeDiagonal = false)
                        } else {
                            emptySet()
                        }
                    }
                }
            }
            .toSet()
}

fun parseSmokeMap(lines: List<String>): Map<Coord, Int> =
    lines.flatMapIndexed { y, line ->
        line.mapIndexed { x, char ->
            Coord(x, y) to char.digitToInt()
        }
    }.toMap()

fun findSmokeLowPoints(smokeMap: Map<Coord, Int>): Set<Coord> =
        smokeMap.flatMap { (coord, currentHeight) ->
            smokeMap.getAdjacentCoords(coord, includeDiagonal = false)
                    .map { smokeMap[it] ?: 0 }
                    .all { it > currentHeight }
                    .let {
                        when (it) {
                            true -> setOf(coord)
                            false -> emptySet()
                        }
                    }
        }.toSet()

fun getRiskLevel(coord: Coord, smokeMap: Map<Coord, Int>, lowPoints: Set<Coord>) =
        when (coord) {
            in lowPoints -> (smokeMap[coord] ?: 0) + 1
            else -> throw IllegalArgumentException("Unsupported coordinates: $coord")
        }
