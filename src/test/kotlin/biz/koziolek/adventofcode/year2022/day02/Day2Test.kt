package biz.koziolek.adventofcode.year2022.day02

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2022")
internal class Day2Test {

    private val sampleInput = """
            A Y
            B X
            C Z
        """.trimIndent().split("\n")

    @Test
    fun testSampleInput() {
        val encryptedGuide = parseRockPaperScissorsGuide(sampleInput)
        assertEquals(3, encryptedGuide.size)

        val moves = decodeGuessedMoves(encryptedGuide)
        assertEquals(3, moves.size)

        assertEquals(Shape.ROCK, moves[0].opponents)
        assertEquals(Shape.PAPER, moves[0].my)
        assertEquals(RoundOutcome.VICTORY, moves[0].outcome)
        assertEquals(8, moves[0].score)

        assertEquals(Shape.PAPER, moves[1].opponents)
        assertEquals(Shape.ROCK, moves[1].my)
        assertEquals(RoundOutcome.DEFEAT, moves[1].outcome)
        assertEquals(1, moves[1].score)

        assertEquals(Shape.SCISSORS, moves[2].opponents)
        assertEquals(Shape.SCISSORS, moves[2].my)
        assertEquals(RoundOutcome.DRAW, moves[2].outcome)
        assertEquals(6, moves[2].score)

        assertEquals(15, totalScore(moves))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val encryptedGuide = parseRockPaperScissorsGuide(input)
        val guessedMoves = decodeGuessedMoves(encryptedGuide)
        assertEquals(11386, totalScore(guessedMoves))
    }

    @Test
    fun testSampleInputPart2() {
        val encryptedGuide = parseRockPaperScissorsGuide(sampleInput)
        assertEquals(3, encryptedGuide.size)

        val moves = decodeActualMoves(encryptedGuide)
        assertEquals(3, moves.size)

        assertEquals(Shape.ROCK, moves[0].opponents)
        assertEquals(Shape.ROCK, moves[0].my)
        assertEquals(RoundOutcome.DRAW, moves[0].outcome)
        assertEquals(4, moves[0].score)

        assertEquals(Shape.PAPER, moves[1].opponents)
        assertEquals(Shape.ROCK, moves[1].my)
        assertEquals(RoundOutcome.DEFEAT, moves[1].outcome)
        assertEquals(1, moves[1].score)

        assertEquals(Shape.SCISSORS, moves[2].opponents)
        assertEquals(Shape.ROCK, moves[2].my)
        assertEquals(RoundOutcome.VICTORY, moves[2].outcome)
        assertEquals(7, moves[2].score)

        assertEquals(12, totalScore(moves))
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val encryptedGuide = parseRockPaperScissorsGuide(input)
        val actualMoves = decodeActualMoves(encryptedGuide)
        assertEquals(13600, totalScore(actualMoves))
    }
}
