package biz.koziolek.adventofcode.year2021

import java.io.File
import java.util.*
import kotlin.collections.HashSet

fun main() {
    val inputFile = File("src/main/resources/year2021/day9/input")
    val lines = inputFile.bufferedReader().readLines()
    val smokeMap = parseSmokeMap(lines)

    println("Low points risk sum is: ${getLowPointsRiskSum(smokeMap)}")

    val basins = findSmokeBasins(smokeMap)
    println("Product of sizes of the three largest basins is: ${getLargestBasinsSizeProduct(basins, n = 3)}")
}

fun getLowPointsRiskSum(smokeMap: Array<IntArray>): Int =
        findSmokeLowPoints(smokeMap).let { lowPoints ->
            lowPoints.sumOf { getRiskLevel(it, smokeMap, lowPoints) }
        }

fun getLargestBasinsSizeProduct(basins: Set<Set<Pair<Int, Int>>>, n: Int) =
        basins.map { it.size }
                .sorted()
                .reversed()
                .take(n)
                .ifEmpty { return 0 }
                .fold(1) { acc, size -> acc * size }

fun findSmokeBasins(smokeMap: Array<IntArray>): Set<Set<Pair<Int, Int>>> {
    return findSmokeLowPoints(smokeMap)
            .map { lowPoint ->
                buildSet {
                    val coordsToCheck: Queue<Pair<Int, Int>> = ArrayDeque()
                    val checkedCoords: MutableSet<Pair<Int, Int>> = HashSet()
                    var currentCoord: Pair<Int, Int>? = lowPoint

                    while (currentCoord != null) {
                        if (currentCoord !in checkedCoords) {
                            checkedCoords.add(currentCoord)

                            val value = smokeMap[currentCoord.second][currentCoord.first]
                            if (value != 9) {
                                add(currentCoord)
                                coordsToCheck.addAll(getAdjacentCoords(currentCoord, smokeMap))
                            }
                        }

                        currentCoord = coordsToCheck.poll()
                    }
                }
            }
            .toSet()
}

fun parseSmokeMap(lines: List<String>): Array<IntArray> =
        lines
                .map { line -> line.toCharArray().map { char -> char - '0' }.toIntArray() }
                .toTypedArray()

fun findSmokeLowPoints(smokeMap: Array<IntArray>): Set<Pair<Int, Int>> =
        smokeMap.indices.flatMap { y ->
            smokeMap[y].indices.flatMap { x ->
                val coord = Pair(x, y)
                val currentHeight = smokeMap[y][x]
                getAdjacentCoords(coord, smokeMap)
                        .map { smokeMap[it.second][it.first] }
                        .all { it > currentHeight }
                        .let {
                            when (it) {
                                true -> setOf(coord)
                                false -> emptySet()
                            }
                        }
            }
        }.toSet()

private fun getAdjacentCoords(coord: Pair<Int, Int>, smokeMap: Array<IntArray>): Set<Pair<Int, Int>> {
    return buildSet {
        if (coord.first > 0) {
            // Left
            add(Pair(coord.first - 1, coord.second))
        }
        if (coord.second > 0) {
            // Up
            add(Pair(coord.first, coord.second - 1))
        }
        if (coord.first < smokeMap[0].size - 1) {
            // Right
            add(Pair(coord.first + 1, coord.second))
        }
        if (coord.second < smokeMap.size - 1) {
            // Down
            add(Pair(coord.first, coord.second + 1))
        }
    }
}

fun getRiskLevel(coords: Pair<Int, Int>, smokeMap: Array<IntArray>, lowPoints: Set<Pair<Int, Int>>) =
        when (coords) {
            in lowPoints -> smokeMap[coords.second][coords.first] + 1
            else -> throw IllegalArgumentException("Unsupported coordinates: $coords")
        }
