package biz.koziolek.adventofcode.year2021

import java.io.File
import kotlin.math.abs

fun main() {
    val inputFile = File("src/main/resources/year2021/day7/input")
    val line = inputFile.bufferedReader().readLines().first()
    val (position, cost) = findCheapestPosition(line)

    println("Cheapest position is $position with cost: $cost")
}

fun findCheapestPosition(line: String): Pair<Int, Int> {
    val initialPositions = line.split(',')
            .map { it.toInt() }
    
    return (0..initialPositions.maxOf { it })
            .map { Pair(it, calculateTotalCost(initialPositions, it)) }
            .minByOrNull { it.second }
            ?: throw IllegalArgumentException("Could not find cheapest position")
}

fun calculateTotalCost(sourcePositions: List<Int>, destination: Int): Int =
        sourcePositions.map { abs(it - destination) }.sum()
