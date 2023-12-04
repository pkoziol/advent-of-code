package biz.koziolek.adventofcode.year2023.day04

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2023")
internal class Day4Test {

    private val sampleInput = """
            Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
            Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
            Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
            Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
            Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
            Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
        """.trimIndent().split("\n")

    @Test
    fun testParseScratchCards() {
        val scratchCards = parseScratchCards(sampleInput)
        assertEquals(
            listOf(
                ScratchCard(id = 1, winningNumbers = listOf(41, 48, 83, 86, 17), myNumbers = listOf(83, 86,  6, 31, 17,  9, 48, 53)),
                ScratchCard(id = 2, winningNumbers = listOf(13, 32, 20, 16, 61), myNumbers = listOf(61, 30, 68, 82, 17, 32, 24, 19)),
                ScratchCard(id = 3, winningNumbers = listOf( 1, 21, 53, 59, 44), myNumbers = listOf(69, 82, 63, 72, 16, 21, 14,  1)),
                ScratchCard(id = 4, winningNumbers = listOf(41, 92, 73, 84, 69), myNumbers = listOf(59, 84, 76, 51, 58,  5, 54, 83)),
                ScratchCard(id = 5, winningNumbers = listOf(87, 83, 26, 28, 32), myNumbers = listOf(88, 30, 70, 12, 93, 22, 82, 36)),
                ScratchCard(id = 6, winningNumbers = listOf(31, 18, 13, 56, 72), myNumbers = listOf(74, 77, 10, 23, 35, 67, 36, 11)),
            ),
            scratchCards
        )
    }

    @Test
    fun testCalculatePoints() {
        val scratchCards = parseScratchCards(sampleInput)
        assertEquals(8, scratchCards[0].points)
        assertEquals(2, scratchCards[1].points)
        assertEquals(2, scratchCards[2].points)
        assertEquals(1, scratchCards[3].points)
        assertEquals(0, scratchCards[4].points)
        assertEquals(0, scratchCards[5].points)
    }

    @Test
    fun testSampleAnswer1() {
        val scratchCards = parseScratchCards(sampleInput)
        assertEquals(13, scratchCards.sumOf { it.points })
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val scratchCards = parseScratchCards(input)
        assertEquals(33950, scratchCards.sumOf { it.points })
    }

    @Test
    fun testSampleAnswer2() {
        val scratchCards = parseScratchCards(sampleInput)
        val finalScratchCards = winMoreScratchCards(scratchCards)
        assertEquals(1, finalScratchCards.find { it.id == 1 }?.count)
        assertEquals(2, finalScratchCards.find { it.id == 2 }?.count)
        assertEquals(4, finalScratchCards.find { it.id == 3 }?.count)
        assertEquals(8, finalScratchCards.find { it.id == 4 }?.count)
        assertEquals(14, finalScratchCards.find { it.id == 5 }?.count)
        assertEquals(1, finalScratchCards.find { it.id == 6 }?.count)
        assertEquals(30, finalScratchCards.sumOf { it.count })
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val scratchCards = parseScratchCards(input)
        val finalScratchCards = winMoreScratchCards(scratchCards)
        assertEquals(14814534, finalScratchCards.sumOf { it.count })
    }
}
