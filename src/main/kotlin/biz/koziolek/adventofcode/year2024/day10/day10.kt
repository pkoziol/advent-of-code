package biz.koziolek.adventofcode.year2024.day10

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput
import biz.koziolek.adventofcode.getAdjacentCoords
import biz.koziolek.adventofcode.parse2DMap

fun main() {
    val inputFile = findInput(object {})
    val map = parseMap(inputFile.bufferedReader().readLines())
    val trails = findTrails(map)
    val scores = findTrailHeadScores(trails)
    println("The sum of the trail head scores is $scores")
}

fun parseMap(lines: Iterable<String>): Map<Coord, Int> =
    lines.parse2DMap { c -> c.digitToInt() }.toMap()

fun findTrails(map: Map<Coord, Int>): List<List<Coord>> =
    map
        .filterValues { it == 0 }
        .keys
        .flatMap { findNextTrailSegment(map, it) }

fun findNextTrailSegment(map: Map<Coord, Int>, start: Coord): List<List<Coord>> =
    buildList {
        val startValue = map[start]!!

        if (startValue == 9) {
            add(listOf(start))
        } else {
            for (adjCoord in map.getAdjacentCoords(start, includeDiagonal = false)) {
                if (map[adjCoord] == startValue + 1) {
                    findNextTrailSegment(map, adjCoord)
                        .forEach { add(listOf(start) + it) }
                }
            }
        }
    }

fun findTrailHeadScores(trails: List<List<Coord>>): Int =
    trails
        .groupingBy { it.first() }
        .fold(setOf<Coord>()) { acc, trail -> acc + trail.last() }
        .values
        .sumOf { it.size }
