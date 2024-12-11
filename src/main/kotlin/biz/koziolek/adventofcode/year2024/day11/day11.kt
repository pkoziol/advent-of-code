package biz.koziolek.adventofcode.year2024.day11

import biz.koziolek.adventofcode.findInput
import kotlin.math.pow

fun main() {
    val inputFile = findInput(object {})
    val stones0 = parseStones(inputFile.bufferedReader().readLines())
    val stones25 = updateStones(stones0, times = 25)
    print("The number of stones after 25 updates is ${stones25.size}")
}

fun parseStones(lines: Iterable<String>): List<Long> =
    lines.first().split(" ").map { it.toLong() }

fun updateStones(stones: List<Long>, times: Int = 1): List<Long> {
    var current = stones
    repeat(times) {
        current = updateStonesOnce(current)
    }
    return current
}

fun updateStonesOnce(stones: List<Long>): List<Long> =
    buildList {
        for (stone in stones) {
            if (stone == 0L) {
                add(1)
            } else {
                val digits = countDigits(stone)
                if (digits % 2 == 0) {
                    val factor = 10.0.pow(digits / 2)
                    add((stone / factor).toLong())
                    add((stone % factor).toLong())
                } else {
                    add(stone * 2024)
                }
            }
        }
    }

fun countDigits(stone: Long) =
    "$stone".length
