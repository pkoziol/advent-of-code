package biz.koziolek.adventofcode.year2021

import java.io.File

fun main() {
    val inputFile = File("src/main/resources/year2021/day2/input")
    val position = calculatePosition(inputFile.bufferedReader().lineSequence())
    println("Position: $position")
    println("Answer: ${position.first * position.second}")
}

fun calculatePosition(lines: Sequence<String>): Pair<Int, Int> =
        lines
                .map { it.split(" ", limit = 2) }
                .filter { it.size == 2 }
                .fold(Pair(0, 0)) { acc, splitCommand ->
                    val direction = splitCommand[0]
                    val value = splitCommand[1].toInt()

                    when (direction) {
                        "forward" -> acc.copy(first = acc.first + value)
                        "down" -> acc.copy(second = acc.second + value)
                        "up" -> acc.copy(second = acc.second - value)
                        else -> throw IllegalArgumentException("Unknown direction: $direction")
                    }
                }
