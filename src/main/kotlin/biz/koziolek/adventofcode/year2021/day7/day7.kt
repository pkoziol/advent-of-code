package biz.koziolek.adventofcode.year2021.day7

import biz.koziolek.adventofcode.findInput
import kotlin.math.abs

fun main() {
    val inputFile = findInput(object {})
    val line = inputFile.bufferedReader().readLines().first()

    val (position, cost) = findCheapestPosition(line, ::calculateLinearCost)
    println("Cheapest position is $position with linear cost: $cost")

    val (position2, cost2) = findCheapestPosition(line, ::calculateNonLinearCost)
    println("Cheapest position is $position2 with non-linear cost: $cost2")
}

fun findCheapestPosition(line: String, costFunction: (Int, Int) -> Int): Pair<Int, Int> {
    val initialPositions = line.split(',')
            .map { it.toInt() }

    return (0..initialPositions.maxOf { it })
            .map { dst -> Pair(dst, initialPositions.sumOf { src -> costFunction(src, dst) }) }
            .minByOrNull { it.second }
            ?: throw IllegalArgumentException("Could not find cheapest position")
}

fun calculateLinearCost(source: Int, destination: Int): Int =
        abs(source - destination)

fun calculateNonLinearCost(source: Int, destination: Int): Int {
    val n = abs(source - destination)
    return ((1 + n) / 2.0 * n).toInt()
}
