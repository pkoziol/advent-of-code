package biz.koziolek.adventofcode.year2021

import java.io.File

fun main() {
    val inputFile = File("src/main/resources/year2021/day1/input")
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
                .fold(Pair<Int, List<Int>>(0, listOf())) { acc, currentHeight ->
                    val previousWindow = acc.second

                    if (previousWindow.size < windowSize) {
                        acc.copy(second = previousWindow + currentHeight)
                    } else {
                        val currentWindow = previousWindow.subList(1, previousWindow.size) + currentHeight

                        if (currentWindow.sum() > previousWindow.sum()) {
                            acc.copy(first = acc.first.inc(), second = currentWindow)
                        } else {
                            acc.copy(second = currentWindow)
                        }
                    }
                }.first
