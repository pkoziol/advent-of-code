package biz.koziolek.adventofcode.year2023.day12

import biz.koziolek.adventofcode.findInput
import java.time.LocalDateTime

fun main() {
    val inputFile = findInput(object {})
    val springs = parseHotSpringsRecords(inputFile.bufferedReader().readLines())
    println("There are ${countPossibleArrangements(springs)} possible arrangements")
}

const val OPERATIONAL = '.'
const val DAMAGED = '#'
const val UNKNOWN = '?'

data class HotSpringsRow(val conditions: String, val damagedGroupSizes: List<Int>) {
    fun unfold(): HotSpringsRow =
        HotSpringsRow(
            conditions = (1..5).joinToString(UNKNOWN.toString()) { this.conditions },
            damagedGroupSizes = (1..5).flatMap { this.damagedGroupSizes }
        )

    val isUnknown = UNKNOWN in conditions
}

fun parseHotSpringsRecords(lines: Iterable<String>): List<HotSpringsRow> =
    lines
        .map { line ->
            val (conditions, sizesStr) = line.split(" ")
            val sizes = sizesStr.split(",").map { it.toInt() }
            HotSpringsRow(conditions, sizes)
        }

fun springsMatch(conditions: String, damagedGroupSizes: List<Int>): Boolean =
    conditions
        .split(OPERATIONAL)
        .filter { it.isNotEmpty() }
        .map { it.length }
        .let { it == damagedGroupSizes }

fun springsHaveChanceToMatch(conditions: String, damagedGroupSizes: List<Int>): Boolean {
    val pattern = damagedGroupSizes.joinToString(separator = "[.?]+", prefix = "[.?]*", postfix = "[.?]*") { "[#?]{$it}" }
    return Regex(pattern).matches(conditions)
}

fun generatePossibleArrangements(hotSpringsRow: HotSpringsRow, debug: Boolean = false): Sequence<HotSpringsRow> =
    sequence {
        if (debug) println(hotSpringsRow.conditions)

        if (!hotSpringsRow.isUnknown) {
            if (springsMatch(hotSpringsRow.conditions, hotSpringsRow.damagedGroupSizes)) {
                yield(hotSpringsRow)
            }
        } else if (springsHaveChanceToMatch(hotSpringsRow.conditions, hotSpringsRow.damagedGroupSizes)) {
            val (first, second) = hotSpringsRow.conditions.split(UNKNOWN, limit = 2)

            yieldAll(generatePossibleArrangements(
                HotSpringsRow(first + OPERATIONAL + second, hotSpringsRow.damagedGroupSizes), debug
            ))
            yieldAll(generatePossibleArrangements(
                HotSpringsRow(first + DAMAGED + second, hotSpringsRow.damagedGroupSizes), debug
            ))
        }
    }

private val cache: MutableMap<HotSpringsRow, Long> = mutableMapOf()
internal var cacheHits = 0L
internal var cacheMisses = 0L

@Suppress("unused")
internal fun resetCache() {
    cache.clear()
    cacheHits = 0
    cacheMisses = 0
}

fun countPossibleArrangements(hotSpringsRow: HotSpringsRow, debug: Boolean = false, level: Int = 0): Long {
    val count = cache[hotSpringsRow]
    if (count != null) {
        cacheHits++
        return count
    }

    cacheMisses++

    val conditions = hotSpringsRow.conditions
    val damagedGroupSizes = hotSpringsRow.damagedGroupSizes

    if (debug) {
        val indent = " ".repeat(level)
        print("$indent$conditions $damagedGroupSizes")
    }

    val newCount = if (!hotSpringsRow.isUnknown) {
        if (springsMatch(conditions, damagedGroupSizes)) {
            if (debug) println(" matches!")
            1
        } else {
            if (debug) println(" does not match")
            0
        }
    } else if (damagedGroupSizes.isEmpty()) {
        if (conditions.contains(DAMAGED)) {
            if (debug) println(" is not possible (v2)")
            0
        } else {
            if (debug) println(" matches! (shortcut)")
            1
        }
    } else {
        var matchedSizes = 0
        var firstUnmatchedPos = 0
        var damagedCount = 0
        for (c in conditions) {
            if (damagedCount > 0 && c != DAMAGED) {
                if (damagedCount == damagedGroupSizes[matchedSizes]) {
                    firstUnmatchedPos += damagedCount
                    damagedCount = 0
                    matchedSizes++
                } else if (damagedCount > damagedGroupSizes[matchedSizes]) {
                    if (debug) println(" too many #")
                    cache[hotSpringsRow] = 0
                    return 0
                } else if (c != UNKNOWN){
                    if (debug) println(" too few #")
                    cache[hotSpringsRow] = 0
                    return 0
                }
            }

            when (c) {
                UNKNOWN -> break
                DAMAGED -> {
                    if (matchedSizes >= damagedGroupSizes.size) {
                        cache[hotSpringsRow] = 0
                        return 0
                    }

                    damagedCount++
                }
                OPERATIONAL -> firstUnmatchedPos++
                else -> throw IllegalStateException("Unexpected character: $c")
            }
        }
        if (debug) print(" firstUnmatchedPos=$firstUnmatchedPos sizes=$matchedSizes")

        val remainingSizes = damagedGroupSizes.drop(matchedSizes)

        if (firstUnmatchedPos > 0 && conditions[firstUnmatchedPos - 1] == DAMAGED) {
            if (debug) println(" add .")
            countPossibleArrangements(
                HotSpringsRow(OPERATIONAL + conditions.substring(firstUnmatchedPos + 1), remainingSizes),
                debug,
                level + firstUnmatchedPos
            )
        } else {
            val before = conditions.substring(firstUnmatchedPos, firstUnmatchedPos + damagedCount)
            val after = conditions.substring(firstUnmatchedPos + damagedCount + 1)

            if (damagedCount > 0) {
                if (firstUnmatchedPos + damagedCount < conditions.length && conditions[firstUnmatchedPos + damagedCount] == UNKNOWN) {
                    if (debug) println(" continue #")
                    countPossibleArrangements(
                        HotSpringsRow(before + DAMAGED + after, remainingSizes),
                        debug,
                        level + firstUnmatchedPos
                    )
                } else {
                    if (debug) println(" would like to continue # but it's not possible")
                    0
                }
            } else {
                if (debug) println(" try # and .")
                countPossibleArrangements(
                    HotSpringsRow(before + DAMAGED + after, remainingSizes),
                    debug,
                    level + firstUnmatchedPos
                ) + countPossibleArrangements(
                    HotSpringsRow(before + OPERATIONAL + after, remainingSizes),
                    debug,
                    level + firstUnmatchedPos
                )
            }
        }
    }

    cache[hotSpringsRow] = newCount
    return newCount
}

fun countPossibleArrangements(hotSpringsRows: List<HotSpringsRow>, debug: Boolean = false): Long =
    hotSpringsRows.mapIndexed { index, hotSpringsRow ->
        val count = countPossibleArrangements(hotSpringsRow, debug)
        if (debug) println("${LocalDateTime.now()} $index: $hotSpringsRow done = $count")
        count
    }.sum()
