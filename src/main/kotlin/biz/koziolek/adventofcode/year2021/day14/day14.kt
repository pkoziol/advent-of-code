package biz.koziolek.adventofcode.year2021.day14

import biz.koziolek.adventofcode.*
import java.util.*

fun main() {
    val inputFile = findInput(object {})
    val lines = inputFile.bufferedReader().readLines()

    val polymerTemplate = parsePolymerTemplate(lines)
    val insertionRules = parsePolymerInsertionRules(lines)
    val afterStep10 = generateSequence(polymerTemplate) { expandPolymer(it, insertionRules) }
        .drop(10)
        .first()
    println("Most common - least common (10 steps): ${subtractLeastCommonFromMostCommon(afterStep10)}")

    val longPolymer = LongPolymer.fromString(polymerTemplate)
    val afterStep40 = generateSequence(longPolymer) { it.expand(insertionRules) }
        .drop(40)
        .first()
    println("Most common - least common (40 steps): ${afterStep40.subtractLeastCommonFromMostCommon()}")
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

fun subtractLeastCommonFromMostCommon(polymer: String): Int {
    val counts = polymer.toCharArray()
        .asSequence()
        .groupingBy { it }
        .eachCount()
    
    return counts.values.maxOf { it } - counts.values.minOf { it }
}

class LongPolymer(
    private val firstChar: Char,
    private val pairStats: Map<Pair<Char, Char>, Long>
) {
    companion object {
        fun fromString(template: String) =
            LongPolymer(
                firstChar = template[0],
                pairStats = template.zipWithNext()
                    .fold(emptyMap()) { map, pair ->
                        map + (pair to ((map[pair] ?: 0L) + 1L))
                    }
            )
    }

    val length: Long by lazy { pairStats.values.sum() + 1 }

    private val allChars by lazy { pairStats.keys.map { it.second }.toSet() }

    fun expand(insertionRules: Map<Pair<Char, Char>, Char>): LongPolymer =
        LongPolymer(
            firstChar = firstChar,
            pairStats = pairStats.flatMap { (pair, count) ->
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
        )

    fun countOccurrences( char: Char): Long =
        (pairStats.filter { (pair, _) -> pair.second == char }).values.sum() + if (firstChar == char) 1 else 0

    fun subtractLeastCommonFromMostCommon(): Long {
        val charCounts = allChars.associateWith { countOccurrences(it) }
        return charCounts.values.maxOf { it } - charCounts.values.minOf { it }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LongPolymer

        return firstChar == other.firstChar
                && pairStats == other.pairStats
    }

    override fun hashCode() = Objects.hash(firstChar, pairStats)
}
