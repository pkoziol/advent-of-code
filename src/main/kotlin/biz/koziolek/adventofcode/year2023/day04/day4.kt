package biz.koziolek.adventofcode.year2023.day04

import biz.koziolek.adventofcode.findInput
import kotlin.math.pow

fun main() {
    val inputFile = findInput(object {})
    val scratchCards = parseScratchCards(inputFile.bufferedReader().readLines())
    println("Total points: ${scratchCards.sumOf { it.points }}")
}

data class ScratchCard(val id: Int, val winningNumbers: List<Int>, val myNumbers: List<Int>) {
    val myWinningNumbers: Set<Int> = myNumbers.intersect(winningNumbers)

    val points: Int
        get() = if (myWinningNumbers.isNotEmpty()) {
            2.0.pow(myWinningNumbers.size - 1).toInt()
        } else {
            0
        }
}

fun parseScratchCards(lines: Iterable<String>): List<ScratchCard> =
    lines.map { line ->
        ScratchCard(
            id = line.split(":", "|")[0]
                .replace("Card", "")
                .trim()
                .toInt(),
            winningNumbers = line.split(":", "|")[1]
                .replace(Regex(" +"), " ")
                .trim()
                .split(" ")
                .map { it.trim().toInt() },
            myNumbers = line.split(":", "|")[2]
                .replace(Regex(" +"), " ")
                .trim()
                .split(" ")
                .map { it.trim().toInt() },
        )
    }
