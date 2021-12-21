package biz.koziolek.adventofcode.year2021.day21

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day21Test {

    private val sampleInput = """
        Player 1 starting position: 4
        Player 2 starting position: 8
    """.trimIndent().split("\n")

    @Test
    fun testParseInput() {
        val (player1, player2) = parseDiracDiceStartingPositions(sampleInput)
        assertEquals(4, player1.position)
        assertEquals(0, player1.score)
        assertEquals(8, player2.position)
        assertEquals(0, player2.score)
    }

    @Test
    fun testDeterministicDice() {
        val dice = DeterministicDice(sides = 7)
        assertEquals(1, dice.rollNext())
        assertEquals(2, dice.rollNext())
        assertEquals(3, dice.rollNext())
        assertEquals(4, dice.rollNext())
        assertEquals(5, dice.rollNext())
        assertEquals(6, dice.rollNext())
        assertEquals(7, dice.rollNext())
        assertEquals(1, dice.rollNext())
    }

    @Test
    fun testAnswer1Sample() {
        val (player1, player2) = parseDiracDiceStartingPositions(sampleInput)
        val dice = RollCountingDice(DeterministicDice(sides = 100))
        val wonGame = play(DiracDiceGame(player1, player2, dice))
        assertEquals(739785, answerPart1(wonGame))
    }

    @Test
    fun testAnswer1() {
        val fullInput = findInput(object {}).readLines()
        val (player1, player2) = parseDiracDiceStartingPositions(fullInput)
        val dice = RollCountingDice(DeterministicDice(sides = 100))
        val wonGame = play(DiracDiceGame(player1, player2, dice))
        assertEquals(893700, answerPart1(wonGame))
    }
}
