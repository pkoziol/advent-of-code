package biz.koziolek.adventofcode.year2024.day19

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val (patterns, designs) = parseTowels(inputFile.bufferedReader().readLines())
    val possible = designs.count { isPossible(patterns, it) }
    println("Possible designs: $possible")
}

fun parseTowels(lines: Iterable<String>): Pair<List<String>, List<String>> {
    val patterns = lines.first().split(", ")
    val designs = lines.drop(2)
    return Pair(patterns, designs)
}

fun isPossible(patterns: List<String>, design: String): Boolean {
    val eitherPattern = patterns.joinToString("|")
    val regex = Regex("($eitherPattern)+")
    return regex.matches(design)
}
