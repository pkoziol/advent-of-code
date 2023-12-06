package biz.koziolek.adventofcode.year2023.day06

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2023")
internal class Day6Test {

    private val sampleInput = """
            Time:      7  15   30
            Distance:  9  40  200
        """.trimIndent().split("\n")

    @Test
    fun testParse() {
        val races = parseBoatRaces(sampleInput)
        assertEquals(
            listOf(
                BoatRace(time = 7, distance = 9),
                BoatRace(time = 15, distance = 40),
                BoatRace(time = 30, distance = 200),
            ),
            races
        )
    }

    @Test
    fun testFindWaysToWinRace() {
        val races = parseBoatRaces(sampleInput)
        assertEquals(2..5, findWaysToWinRace(races[0]))
        assertEquals(4..11, findWaysToWinRace(races[1]))
        assertEquals(11..19, findWaysToWinRace(races[2]))
    }

    @Test
    fun testSampleAnswer1() {
        val races = parseBoatRaces(sampleInput)
        assertEquals(288, findMarginOfError(races))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val races = parseBoatRaces(input)
        assertEquals(227850, findMarginOfError(races))
    }
}
