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
    println("Most common - least common (10 steps): ${subtractLeastCommonFromMostCommon(afterStep10)}")

    val bigPolymer = convertPolymerToBigPolymer(polymerTemplate)
    val afterStep40 = generateSequence(bigPolymer) { expandBigPolymer(it, insertionRules) }
        .drop(40)
        .first()
    println("Most common - least common (40 steps): ${subtractLeastCommonFromMostCommon(polymerTemplate, afterStep40)}")
}

fun parsePolymerTemplate(lines: List<String>): String = lines[0]

fun parsePolymerInsertionRules(lines: List<String>): Map<Pair<Char, Char>, Char> =
    lines.drop(2)
        .map { it.split(' ', limit = 3) }
        .associate { (a, _, b) -> (a[0] to a[1]) to b[0] }

fun expandPolymer(polymer: String, insertionRules: Map<Pair<Char, Char>, Char>): String =
    polymer.drop(1).fold(polymer.first().toString()) { acc, b ->
        val a = acc[acc.length - 1]
        val ab = Pair(a, b)
        val new = insertionRules[ab] ?: ""

        acc + new + b
    }

fun convertPolymerToBigPolymer(polymer: String): Map<Pair<Char, Char>, Long> {
    return polymer.zipWithNext()
        .fold(emptyMap()) { map, pair ->
            map + (pair to ((map[pair] ?: 0L) + 1L))
        }
}

fun expandBigPolymer(bigPolymer: Map<Pair<Char, Char>, Long>, insertionRules: Map<Pair<Char, Char>, Char>): Map<Pair<Char, Char>, Long> =
    bigPolymer.flatMap { (pair, count) ->
        val newChar = insertionRules[pair]
        if (newChar != null) {
            setOf(
                (pair.first to newChar) to count,
                (newChar to pair.second) to count,
            )
        } else {
            setOf(pair to count)
        }
    }.fold(emptyMap()) { map, (pair, count) ->
        map + (pair to ((map[pair] ?: 0) + count))
    }

fun getLength(bigPolymer: Map<Pair<Char, Char>, Long>): Long =
    bigPolymer.values.sum() + 1

fun countOccurrences(template: String, bigPolymer: Map<Pair<Char, Char>, Long>, char: Char): Long =
    (bigPolymer.filter { (pair, _) -> pair.second == char }).values.sum() + if (template[0] == char) 1 else 0

fun subtractLeastCommonFromMostCommon(polymer: String): Int {
    val counts = polymer.toCharArray()
        .asSequence()
        .groupingBy { it }
        .eachCount()
    
    return counts.values.maxOf { it } - counts.values.minOf { it }
}

fun subtractLeastCommonFromMostCommon(template: String, bigPolymer: Map<Pair<Char, Char>, Long>): Long {
    val allChars = bigPolymer.keys.map { it.second }.toSet()
    val charCounts = allChars.associateWith { countOccurrences(template, bigPolymer, it) }
    return charCounts.values.maxOf { it } - charCounts.values.minOf { it }
}
