package biz.koziolek.adventofcode.year2021.day03

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day3Test {

    private val sampleInput = """
            00100
            11110
            10110
            10111
            10101
            01111
            00111
            11100
            10000
            11001
            00010
            01010
        """.trimIndent()

    @Test
    fun testSampleInput() {
        val lines = sampleInput.split("\n")
        val gammaRate = calculateGammaRate(lines)
        val epsilonRate = calculateEpsilonRate(lines)
        val powerConsumption = calculatePowerConsumption(lines)
        assertEquals(22, gammaRate)
        assertEquals(9, epsilonRate)
        assertEquals(198, powerConsumption)
    }

    @Test
    fun testMostCommonFirstBitBeingZero() {
        val lines = listOf("001", "011", "111")
        val gammaRate = calculateGammaRate(lines)
        val epsilonRate = calculateEpsilonRate(lines)
        val powerConsumption = calculatePowerConsumption(lines)
        assertEquals(3, gammaRate)
        assertEquals(4, epsilonRate)
        assertEquals(12, powerConsumption)
    }

    @Test
    fun testFullInput() {
        val lines = findInput(object {}).readLines()
        val gammaRate = calculateGammaRate(lines)
        val epsilonRate = calculateEpsilonRate(lines)
        val powerConsumption = calculatePowerConsumption(lines)
        assertEquals(1916, gammaRate)
        assertEquals(2179, epsilonRate)
        assertEquals(4174964, powerConsumption)
    }

    @Test
    fun testSampleInputPart2() {
        val lines = sampleInput.split("\n")
        val oxygenGeneratorRating = calculateOxygenGeneratorRating(lines)
        val co2ScrubberRating = calculateCO2ScrubberRating(lines)
        val lifeSupportRating = calculateLifeSupportRating(lines)
        assertEquals(23, oxygenGeneratorRating)
        assertEquals(10, co2ScrubberRating)
        assertEquals(230, lifeSupportRating)
    }

    @Test
    fun testFullInputPart2() {
        val lines = findInput(object {}).readLines()
        val oxygenGeneratorRating = calculateOxygenGeneratorRating(lines)
        val co2ScrubberRating = calculateCO2ScrubberRating(lines)
        val lifeSupportRating = calculateLifeSupportRating(lines)
        assertEquals(1371, oxygenGeneratorRating)
        assertEquals(3264, co2ScrubberRating)
        assertEquals(4474944, lifeSupportRating)
    }
}
