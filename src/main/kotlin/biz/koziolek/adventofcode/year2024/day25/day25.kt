package biz.koziolek.adventofcode.year2024.day25

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val (locks, keys) = parseLocksAndKeys(inputFile.bufferedReader().readLines())
    val fitting = countFitting(locks, keys)
    println("Fitting lock-key pairs: $fitting")
}

fun parseLocksAndKeys(lines: Iterable<String>): Pair<List<List<Int>>, List<List<Int>>> {
    val locks = mutableListOf<List<Int>>()
    val keys = mutableListOf<List<Int>>()
    var target = locks
    var element = mutableListOf<Int>()
    var first = true

    for (line in lines) {
        if (line.isBlank()) {
            target.add(element)
            first = true
        } else {
            if (first) {
                target = if (line.all { it == '#' }) locks else keys
                element = MutableList(line.length) { -1 } // First or last needs to be skipped
                first = false
            }
            line.forEachIndexed { index, c ->
                element[index] += if (c == '#') 1 else 0
            }
        }
    }
    if (element.isNotEmpty()) {
        target.add(element)
    }

    return locks to keys
}

fun countFitting(locks: List<List<Int>>, keys: List<List<Int>>): Int =
    locks.sumOf { lock ->
        keys.count { key -> key.zip(lock).all { (k, l) -> k + l <= 5 } }
    }
