package biz.koziolek.adventofcode.year2021.day7

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day7Test {

    private val sampleInput = "16,1,2,0,4,2,7,1,2,14"

    @Test
    fun testCalculateLinearCost() {
        assertEquals(14, calculateLinearCost(16, 2))
        assertEquals(1, calculateLinearCost(1, 2))
        assertEquals(0, calculateLinearCost(2, 2))
        assertEquals(2, calculateLinearCost(0, 2))
        assertEquals(2, calculateLinearCost(4, 2))
        assertEquals(0, calculateLinearCost(2, 2))
        assertEquals(5, calculateLinearCost(7, 2))
        assertEquals(1, calculateLinearCost(1, 2))
        assertEquals(0, calculateLinearCost(2, 2))
        assertEquals(12, calculateLinearCost(14, 2))
    }

    @Test
    fun testFindCheapestPositionSample() {
        val (position, cost) = findCheapestPosition(sampleInput, ::calculateLinearCost)
        assertEquals(2, position)
        assertEquals(37, cost)
    }

    @Test
    fun testAnswerPart1() {
        val fullInput = findInput(object {}).readLines().first()
        val (position, cost) = findCheapestPosition(fullInput, ::calculateLinearCost)
        assertEquals(324, position)
        assertEquals(344535, cost)
    }

    @Test
    fun testCalculateNonLinearCost() {
        assertEquals(66, calculateNonLinearCost(16, 5))
        assertEquals(10, calculateNonLinearCost(1, 5))
        assertEquals(6, calculateNonLinearCost(2, 5))
        assertEquals(15, calculateNonLinearCost(0, 5))
        assertEquals(1, calculateNonLinearCost(4, 5))
        assertEquals(6, calculateNonLinearCost(2, 5))
        assertEquals(3, calculateNonLinearCost(7, 5))
        assertEquals(10, calculateNonLinearCost(1, 5))
        assertEquals(6, calculateNonLinearCost(2, 5))
        assertEquals(45, calculateNonLinearCost(14, 5))
    }

    @Test
    fun testFindCheapestPositionSamplePart2() {
        val (position, cost) = findCheapestPosition(sampleInput, ::calculateNonLinearCost)
        assertEquals(5, position)
        assertEquals(168, cost)
    }

    @Test
    fun testAnswerPart2() {
        val fullInput = findInput(object {}).readLines().first()
        val (position, cost) = findCheapestPosition(fullInput, ::calculateNonLinearCost)
        assertEquals(472, position)
        assertEquals(95581659, cost)
    }
}
