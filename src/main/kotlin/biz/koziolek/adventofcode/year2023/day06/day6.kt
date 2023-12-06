package biz.koziolek.adventofcode.year2023.day06

import biz.koziolek.adventofcode.findInput
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

fun main() {
    val inputFile = findInput(object {})
    val races = parseBoatRaces(inputFile.bufferedReader().readLines())
}

data class BoatRace(val time: Int, val distance: Int)

fun parseBoatRaces(lines: Iterable<String>): List<BoatRace> {
    val (firstLine, secondLine) = lines.toList()
    val times = readInts(firstLine)
    val distances = readInts(secondLine)
    return times.zip(distances).map { BoatRace(it.first, it.second) }
}

private fun readInts(line: String) =
    line
        .replace(Regex(" +"), " ")
        .split(" ")
        .drop(1)
        .map { it.toInt() }

fun findWaysToWinRace(race: BoatRace): IntRange {
    val delta = race.time.toDouble().pow(2) - 4 * (race.distance + 0.00000001)
    val maxTime = (-race.time - sqrt(delta)) / -2
    val minTime = (-race.time + sqrt(delta)) / -2

    return ceil(minTime).toInt() .. floor(maxTime).toInt()
}

fun findMarginOfError(races: List<BoatRace>): Int =
    races
        .map { findWaysToWinRace(it).count() }
        .reduce(Int::times)
