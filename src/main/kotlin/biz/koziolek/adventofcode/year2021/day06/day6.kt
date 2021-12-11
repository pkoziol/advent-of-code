package biz.koziolek.adventofcode.year2021.day06

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val line = inputFile.bufferedReader().readLines().first()
    val initialFish = createFish(line)
    
    val fishAfter80Days = (1..80).fold(initialFish) { acc, _ -> simulateDay(acc) }
    println("Fish count after 80 days: ${fishAfter80Days.size}")

    val fishAfter256Days = (1..256).fold(convertV1StateToV2(initialFish)) { acc, _ -> simulateDayV2(acc) }
    println("Fish count after 256 days: ${countFish(fishAfter256Days)}")
}

data class Fish(val timer: Int)

fun createFish(line: String): List<Fish> {
    return line.split(',')
            .map { Fish(timer = it.toInt()) }
}

fun simulateDay(fish: List<Fish>): List<Fish> {
    return fish.flatMap {
        when (it.timer) {
            0 -> listOf(Fish(timer = 6), Fish(timer = 8))
            else -> listOf(Fish(timer = it.timer.dec()))
        }
    }
}

fun convertV1StateToV2(fish: List<Fish>): Map<Int, Long> {
    return fish.groupingBy { it.timer }.fold(0L) { acc, _ -> acc.inc() }
}

fun simulateDayV2(fishGroups: Map<Int, Long>): Map<Int, Long> {
    return buildMap {
        fishGroups.forEach {
            when (it.key) {
                0 -> {
                    merge(6, it.value, Long::plus)
                    merge(8, it.value, Long::plus)
                }
                else -> merge(it.key.dec(), it.value, Long::plus)
            }
        }
    }
}

fun countFish(fishGroups: Map<Int, Long>): Long =
        fishGroups.values.reduce(Long::plus)
