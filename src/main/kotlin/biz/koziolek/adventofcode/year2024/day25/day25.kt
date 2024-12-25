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

    val maps2d = sequence {
        var tmpMap = mutableListOf<String>()
        for (line in lines) {
            if (line.isBlank()) {
                yield(tmpMap)
                tmpMap = mutableListOf()
            } else {
                tmpMap.add(line)
            }
        }
        if (tmpMap.isNotEmpty()) {
            yield(tmpMap)
        }
    }

    for (map in maps2d) {
        val numbers = (0 until map.first().length)
            .map { column ->
                map.count { row -> row[column] == '#' } - 1
            }
        if (map.first().all { it == '#' }) {
            locks.add(numbers)
        } else {
            keys.add(numbers)
        }
    }

    return locks.toList() to keys.toList()
}

fun countFitting(locks: List<List<Int>>, keys: List<List<Int>>): Int =
    locks.sumOf { lock ->
        keys.count { key -> key.zip(lock).all { (k, l) -> k + l <= 5 } }
    }
