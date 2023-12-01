package biz.koziolek.adventofcode.year2023.day01

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val data = parseCalibrationValues(inputFile.bufferedReader().readLines())
    println("Sum of all calibration values is: ${sumCalibrationValues(data)}")
}

fun parseCalibrationValues(lines: Iterable<String>): List<Int> =
    lines
        .map { line -> line.filter { c -> c.isDigit() }}
        .map { it.first().digitToInt() * 10 + it.last().digitToInt() }

fun sumCalibrationValues(values: List<Int>) = values.sum()
