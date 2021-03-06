package biz.koziolek.adventofcode.year2021.day21

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2021")
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
    @Tag("answer")
    fun testAnswer1() {
        val fullInput = findInput(object {}).readLines()
        val (player1, player2) = parseDiracDiceStartingPositions(fullInput)
        val dice = RollCountingDice(DeterministicDice(sides = 100))
        val wonGame = play(DiracDiceGame(player1, player2, dice))
        assertEquals(893700, answerPart1(wonGame))
    }

    @Test
    fun testPlayQuantumSample() {
        val (player1, player2) = parseDiracDiceStartingPositions(sampleInput)
        val dice = QuantumDice(sides = 3)
        val (player1Wins, player2Wins) = playQuantum(DiracDiceGame(player1, player2, dice))
        assertEquals(444_356_092_776_315, player1Wins)
        assertEquals(341_960_390_180_808, player2Wins)
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val fullInput = findInput(object {}).readLines()
        val (player1, player2) = parseDiracDiceStartingPositions(fullInput)
        val dice = QuantumDice(sides = 3)
        val (player1Wins, player2Wins) = playQuantum(DiracDiceGame(player1, player2, dice))
        assertEquals(568_867_175_661_958, player1Wins)
        assertEquals(408_746_284_676_519, player2Wins)
    }
}
