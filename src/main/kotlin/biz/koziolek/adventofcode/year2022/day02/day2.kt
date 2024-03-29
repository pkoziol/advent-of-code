package biz.koziolek.adventofcode.year2022.day02

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val encryptedGuide = parseRockPaperScissorsGuide(inputFile.bufferedReader().readLines())

    val guessedMoves = decodeGuessedMoves(encryptedGuide)
    println("Total score: ${totalScore(guessedMoves)}")

    val actualMoves = decodeActualMoves(encryptedGuide)
    println("Total score: ${totalScore(actualMoves)}")
}

enum class RoundOutcome(val points: Int) {
    VICTORY(points = 6),
    DEFEAT(points = 0),
    DRAW(points = 3),
}

enum class Shape(val points: Int) {
    ROCK(points = 1),
    PAPER(points = 2),
    SCISSORS(points = 3);

    val winsOver: Shape
        get() = when (this) {
            ROCK -> SCISSORS
            PAPER -> ROCK
            SCISSORS -> PAPER
        }

    val losesTo: Shape
        get() = values().first { it.winsOver == this }
}

data class Round(val opponents: Shape, val my: Shape) {

    val outcome = when {
        my.winsOver == opponents -> RoundOutcome.VICTORY
        my.losesTo == opponents -> RoundOutcome.DEFEAT
        else -> RoundOutcome.DRAW
    }

    val score = my.points + outcome.points
}

fun parseRockPaperScissorsGuide(lines: Iterable<String>): List<Pair<Char, Char>> =
    lines.map { line -> line[0] to line[2] }

fun decodeGuessedMoves(guide: List<Pair<Char, Char>>): List<Round> =
    guide.map { (opponents, my) ->
        Round(
            opponents = decodeOpponentsShape(opponents),
            my = decodeMyShape(my)
        )
    }

fun decodeActualMoves(guide: List<Pair<Char, Char>>): List<Round> =
    guide
        .map { (opponents, outcome) -> decodeOpponentsShape(opponents) to decodeExpectedOutcome(outcome) }
        .map { (opponents, outcome) ->
            Round(
                opponents = opponents,
                my = when (outcome) {
                    RoundOutcome.VICTORY -> opponents.losesTo
                    RoundOutcome.DEFEAT -> opponents.winsOver
                    RoundOutcome.DRAW -> opponents
                }
            )
        }

fun decodeOpponentsShape(shape: Char): Shape =
    when (shape) {
        'A' -> Shape.ROCK
        'B' -> Shape.PAPER
        'C' -> Shape.SCISSORS
        else -> throw IllegalArgumentException("Illegal opponent's shape: $shape")
    }

fun decodeMyShape(shape: Char): Shape =
    when (shape) {
        'X' -> Shape.ROCK
        'Y' -> Shape.PAPER
        'Z' -> Shape.SCISSORS
        else -> throw IllegalArgumentException("Illegal my shape: $shape")
    }

fun decodeExpectedOutcome(outcome: Char): RoundOutcome =
    when (outcome) {
        'X' -> RoundOutcome.DEFEAT
        'Y' -> RoundOutcome.DRAW
        'Z' -> RoundOutcome.VICTORY
        else -> throw IllegalArgumentException("Illegal expected outcome: $outcome")
    }

fun totalScore(moves: List<Round>): Int = moves.sumOf { it.score }
