package biz.koziolek.adventofcode.year2023.day09

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val report = parseOasisReport(inputFile.bufferedReader().readLines())
    println("Sum of extrapolated values: ${report.readings.sumOf { predictNextValue(it) }}")
}

data class OasisReport(val readings: List<List<Int>>)

fun parseOasisReport(lines: Iterable<String>): OasisReport =
    lines
        .map { line ->
            line.split(" ").map { it.toInt() }
        }
        .let { OasisReport(it) }

fun predictNextValue(history: List<Int>): Int =
    extrapolateHistory(history).first.last

fun extrapolateHistory(history: List<Int>): List<List<Int>> {
    val reduced = reduceHistory(history)
    val lastRowExtended = reduced.last + 0
    return reduced
        .dropLast(1)
        .foldRight(listOf(lastRowExtended)) { ints, acc ->
            val newRow = ints + (ints.last + acc.first.last)
            listOf(newRow) + acc
        }
}

fun reduceHistory(history: List<Int>): List<List<Int>> =
    buildList {
        var current = history
        add(current)

        while (current.any { it != 0 }) {
            current = current.zipWithNext { a, b -> b - a }
            add(current)
        }
    }
