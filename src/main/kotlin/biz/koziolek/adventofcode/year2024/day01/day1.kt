package biz.koziolek.adventofcode.year2024.day01

import biz.koziolek.adventofcode.findInput
import kotlin.math.absoluteValue

fun main() {
    val inputFile = findInput(object {})
    val data = parseLists(inputFile.bufferedReader().readLines())
    println("Total distance: ${findTotalDistance(data)}")
    println("Similarity score: ${findSimilarityScore(data)}")
}

fun parseLists(lines: Iterable<String>): Pair<List<Int>, List<Int>> =
    lines
        .asSequence()
        .filter { it.isNotBlank() }
        .map { line -> line.split(Regex(" +")) }
        .map { it[0].toInt() to it[1].toInt() }
        .unzip()

fun findTotalDistance(data: Pair<List<Int>, List<Int>>): Int =
    data.first.sorted().zip(data.second.sorted())
        .sumOf { (it.second - it.first).absoluteValue }

fun findSimilarityScore(data: Pair<List<Int>, List<Int>>): Int =
    data.first.sumOf { num -> num * data.second.count { it == num } }
