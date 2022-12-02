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
        val encryptedMoves = parseRockPaperScissorsMoves(sampleInput)
        assertEquals(3, encryptedMoves.size)

        val decodedMoves = decodeMoves(encryptedMoves)
        assertEquals(3, decodedMoves.size)

        assertEquals(Shape.ROCK, decodedMoves[0].opponents)
        assertEquals(Shape.PAPER, decodedMoves[0].my)
        assertEquals(RoundOutcome.VICTORY, decodedMoves[0].outcome)
        assertEquals(8, decodedMoves[0].score)

        assertEquals(Shape.PAPER, decodedMoves[1].opponents)
        assertEquals(Shape.ROCK, decodedMoves[1].my)
        assertEquals(RoundOutcome.DEFEAT, decodedMoves[1].outcome)
        assertEquals(1, decodedMoves[1].score)

        assertEquals(Shape.SCISSORS, decodedMoves[2].opponents)
        assertEquals(Shape.SCISSORS, decodedMoves[2].my)
        assertEquals(RoundOutcome.DRAW, decodedMoves[2].outcome)
        assertEquals(6, decodedMoves[2].score)

        assertEquals(15, totalScore(decodedMoves))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val encryptedMoves = parseRockPaperScissorsMoves(input)
        val decodedMoves = decodeMoves(encryptedMoves)
        assertEquals(11386, totalScore(decodedMoves))
    }
}
