package biz.koziolek.adventofcode.year2021.day01

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    println("Simple increases: ${countIncreases(inputFile.bufferedReader().lineSequence())}")
    println("Windowed increases: ${countIncreasesSlidingWindow(inputFile.bufferedReader().lineSequence(), windowSize = 3)}")
}

fun countIncreases(lines: Sequence<String>): Int =
        lines
                .map { it.toInt() }
                .fold(Pair<Int, Int?>(0, null)) { acc, currentHeight ->
                    val previousHeight = acc.second

                    if (previousHeight != null && currentHeight > previousHeight) {
                        acc.copy(first = acc.first.inc(), second = currentHeight)
                    } else {
                        acc.copy(second = currentHeight)
                    }
                }.first

fun countIncreasesSlidingWindow(lines: Sequence<String>, windowSize: Int = 3): Int =
        lines
                .map { it.toInt() }
                .windowed(windowSize)
                .map { it.sum() }
                .map { it.toString() }
                .let { countIncreases(it) }
