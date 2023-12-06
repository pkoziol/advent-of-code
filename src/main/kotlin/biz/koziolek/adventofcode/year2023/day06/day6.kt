package biz.koziolek.adventofcode.year2023.day06

import biz.koziolek.adventofcode.findInput
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

fun main() {
    val inputFile = findInput(object {})
    val lines = inputFile.bufferedReader().readLines()

    val races = parseBoatRaces(lines)
    println("Margin of error for ${races.size} races is: ${findMarginOfError(races)}")

    val theRace = parseTheBoatRace(lines)
    println("Ways to win the race is: ${findWaysToWinRace(theRace).count()}")
}

data class BoatRace(val time: Long, val distance: Long)

fun parseBoatRaces(lines: Iterable<String>): List<BoatRace> {
    val (firstLine, secondLine) = lines.toList()
    val times = readNumbers(firstLine)
    val distances = readNumbers(secondLine)
    return times.zip(distances).map { BoatRace(it.first, it.second) }
}

fun parseTheBoatRace(lines: Iterable<String>): BoatRace =
    lines
        .map { it.replace(Regex(" +"), "").replace(":", ": ") }
        .let { parseBoatRaces(it) }
        .single()

private fun readNumbers(line: String) =
    line
        .replace(Regex(" +"), " ")
        .split(" ")
        .drop(1)
        .map { it.toLong() }

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
