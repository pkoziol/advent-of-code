package biz.koziolek.adventofcode.year2021.day14

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})
    val lines = inputFile.bufferedReader().readLines()

    val polymerTemplate = parsePolymerTemplate(lines)
    val insertionRules = parsePolymerInsertionRules(lines)
    val afterStep10 = generateSequence(polymerTemplate) { expandPolymer(it, insertionRules) }
        .drop(10)
        .first()
    println("Most common - least common: ${subtractLeastCommonFromMostCommon(afterStep10)}")
}

fun parsePolymerTemplate(lines: List<String>): String = lines[0]

fun parsePolymerInsertionRules(lines: List<String>): Map<String, String> =
    lines.drop(2)
        .map { it.split(' ', limit = 3) }
        .associate { (a, _, b) -> a to b }

fun expandPolymer(polymer: String, insertionRules: Map<String, String>): String =
    polymer.drop(1).fold(polymer.first().toString()) { acc, b ->
        val a = acc[acc.length - 1]
        val ab = "" + a + b
        val new = insertionRules[ab] ?: ""

        acc + new + b
    }

fun subtractLeastCommonFromMostCommon(polymer: String): Int {
    val counts = polymer.toCharArray()
        .asSequence()
        .groupingBy { it }
        .eachCount()
    
    return counts.values.maxOf { it } - counts.values.minOf { it }
}
