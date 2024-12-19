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

fun countAllPossibilities(patterns: List<String>, design: String,
                          memo: MutableMap<String, Long> = mutableMapOf()): Long {
    if (memo.containsKey(design)) {
        return memo[design]!!
    }
    val result = patterns.sumOf { pattern ->
        if (design.startsWith(pattern)) {
            val remainingDesign = design.removePrefix(pattern)
            if (remainingDesign.isEmpty()) {
                1
            } else {
                countAllPossibilities(patterns, remainingDesign, memo)
            }
        } else {
            0
        }
    }
    memo[design] = result
    return result
}
