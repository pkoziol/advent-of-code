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

    val groups: List<String>
        get() = buildList {
            var tmpStr = ""
            for (c in conditions) {
                if (tmpStr == "" || c == tmpStr.last()) {
                    tmpStr += c
                } else {
                    add(tmpStr)
                    tmpStr = c.toString()
                }
            }

            add(tmpStr)
        }
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

fun countPossibleArrangements(hotSpringsRow: HotSpringsRow, debug: Boolean = false, level: Int = 0): Long {
    val count = cache[hotSpringsRow]
    if (count != null) {
        return count
    }

    val indent = " ".repeat(level)
    if (debug) print("$indent${hotSpringsRow.conditions} ${hotSpringsRow.damagedGroupSizes}")

    val newCount = if (!hotSpringsRow.isUnknown) {
        if (springsMatch(hotSpringsRow.conditions, hotSpringsRow.damagedGroupSizes)) {
            if (debug) println(" matches!")
            1
        } else {
            if (debug) println(" does not match")
            0
        }
    } else if (hotSpringsRow.damagedGroupSizes.isEmpty()) {
        if (hotSpringsRow.conditions.contains(DAMAGED)) {
            if (debug) println(" is not possible (v2)")
            0
        } else {
            if (debug) println(" matches! (shortcut)")
            1
        }
    } else {
        val groups = hotSpringsRow.groups
        if (debug) print(" $groups")

        var matchedGroups = 0
        var matchedSizes = 0
        for (group in groups) {
            if (group[0] == UNKNOWN) {
                break
            }

            if (group[0] == DAMAGED) {
                if (matchedSizes >= hotSpringsRow.damagedGroupSizes.size) {
                    cache[hotSpringsRow] = 0
                    return 0
                }
                if (group.length == hotSpringsRow.damagedGroupSizes[matchedSizes]) {
                    matchedGroups++
                    matchedSizes++
                } else {
                    break
                }
            } else {
                matchedGroups++
            }
        }
        if (debug) print(" groups=$matchedGroups sizes=$matchedSizes")

        val matchedConditions = groups.take(matchedGroups).joinToString("")
        val remainingConditions = groups.drop(matchedGroups).joinToString("")
        val remainingSizes = hotSpringsRow.damagedGroupSizes.drop(matchedSizes)

        if (matchedSizes > 0 && groups[matchedGroups - 1].last() == DAMAGED) {
            if (debug) println(" add .")
            countPossibleArrangements(
                HotSpringsRow(OPERATIONAL + remainingConditions.drop(1), remainingSizes),
                debug,
                level + matchedConditions.length
            )
        } else {
            if (groups[matchedGroups].last() == DAMAGED) {
                if (matchedGroups + 1 < groups.size && groups[matchedGroups + 1].first() == UNKNOWN) {
                    if (debug) println(" continue #")
                    countPossibleArrangements(
                        HotSpringsRow(
                            groups[matchedGroups]
                                    + DAMAGED
                                    + groups[matchedGroups + 1].drop(1)
                                    + groups.drop(matchedGroups + 2).joinToString(""), remainingSizes
                        ),
                        debug,
                        level + matchedConditions.length
                    )
                } else {
                    if (debug) println(" would like to continue # but it's not possible")
                    0
                }
            } else {
                if (debug) println(" try # and .")
                countPossibleArrangements(
                    HotSpringsRow(DAMAGED + remainingConditions.drop(1), remainingSizes),
                    debug,
                    level + matchedConditions.length
                ) + countPossibleArrangements(
                    HotSpringsRow(OPERATIONAL + remainingConditions.drop(1), remainingSizes),
                    debug,
                    level + matchedConditions.length
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
