package biz.koziolek.adventofcode.year2024.day11

import biz.koziolek.adventofcode.findInput
import kotlin.math.pow

fun main() {
    val inputFile = findInput(object {})
    val stones0 = parseStones(inputFile.bufferedReader().readLines())
    val stones25 = updateStones(stones0, times = 25)
    println("The number of stones after 25 updates is ${stones25.size}")
    val stones75 = countStones2(stones0, times = 75)
    println("The number of stones after 75 updates is $stones75")
}

fun parseStones(lines: Iterable<String>): List<Long> =
    lines.first().split(" ").map { it.toLong() }

fun updateStones(stones: List<Long>, times: Int = 1): List<Long> {
    var current = stones
    repeat(times) {
        current = current.flatMap { updateStone(it) }
    }
    return current
}

fun updateStone(stone: Long): List<Long> =
    buildList {
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

fun countDigits(stone: Long) =
    "$stone".length

data class OptimizedStone(val value: Long, val count: Long) {
    override fun toString(): String {
        return "${count}x $value"
    }
}

fun countStones2(stones: List<Long>, times: Int = 1): Long =
    stones
        .map { OptimizedStone(value = it, count = 1) }
        .let { updateStones2(it, times) }
        .sumOf { it.count }

fun updateStones2(stones: List<OptimizedStone>, times: Int = 1): List<OptimizedStone> {
    var current = stones
    repeat(times) {
        current = current.flatMap { stone ->
            updateStone(stone.value)
                .map { OptimizedStone(value = it, count = stone.count) }
        }
        current = reduceStones(current)
    }
    return current
}

fun reduceStones(stones: List<OptimizedStone>): List<OptimizedStone> =
    stones
        .groupBy { it.value }
        .map { (value, stones) -> OptimizedStone(value, stones.sumOf { it.count }) }
