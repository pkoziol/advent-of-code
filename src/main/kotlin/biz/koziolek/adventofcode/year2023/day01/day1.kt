package biz.koziolek.adventofcode.year2023.day01

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val lines = inputFile.bufferedReader().readLines()
    println("Sum of all calibration values is: ${sumCalibrationValues(parseCalibrationValues(lines))}")
    println("Sum of all calibration values including spelled numbers is: ${sumCalibrationValues(parseCalibrationValuesWithLetters(lines))}")
}

fun parseCalibrationValues(lines: Iterable<String>): List<Int> =
    lines
        .map { line -> line.filter { c -> c.isDigit() }}
        .map { it.first().digitToInt() * 10 + it.last().digitToInt() }

fun sumCalibrationValues(values: List<Int>) = values.sum()

private val spelledDigits = mapOf(
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9,
)

private val stringsToFind = spelledDigits.keys + (1..9).map { it.toString() }

fun parseCalibrationValuesWithLetters(lines: Iterable<String>): List<Int> =
    lines
        .asSequence()
        .map { line -> line.findAnyOf(stringsToFind) to line.findLastAnyOf(stringsToFind) }
        .map { toDigit(it.first?.second) to toDigit(it.second?.second) }
        .filter { it.first != null && it.second != null }
        .map { it.first!! * 10 + it.second!! }
        .toList()

private fun toDigit(str: String?): Int? =
    spelledDigits[str] ?: str?.toInt()
