package biz.koziolek.adventofcode.year2023.day12

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val springs = parseHotSpringsRecords(inputFile.bufferedReader().readLines())
    println("There are ${countPossibleArrangements(springs)} possible arrangements")
}

const val OPERATIONAL = '.'
const val DAMAGED = '#'
const val UNKNOWN = '?'

data class HotSpringsRow(val conditions: String, val damagedGroupSizes: List<Int>) {
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
        .trim(OPERATIONAL)
        .replace(Regex(Regex.escape(OPERATIONAL.toString()) + "+"), OPERATIONAL.toString())
        .split(OPERATIONAL)
        .map { it.length }
        .let { it == damagedGroupSizes }

fun generatePossibleArrangements(hotSpringsRow: HotSpringsRow): Sequence<HotSpringsRow> =
    sequence {
        if (!hotSpringsRow.isUnknown) {
            if (springsMatch(hotSpringsRow.conditions, hotSpringsRow.damagedGroupSizes)) {
                yield(hotSpringsRow)
            }
        } else {
            val (first, second) = hotSpringsRow.conditions.split(UNKNOWN, limit = 2)

            yieldAll(generatePossibleArrangements(
                HotSpringsRow(first + OPERATIONAL + second, hotSpringsRow.damagedGroupSizes)
            ))
            yieldAll(generatePossibleArrangements(
                HotSpringsRow(first + DAMAGED + second, hotSpringsRow.damagedGroupSizes)
            ))
        }
    }

fun countPossibleArrangements(hotSpringsRows: List<HotSpringsRow>): Int =
    hotSpringsRows.sumOf { generatePossibleArrangements(it).count() }
