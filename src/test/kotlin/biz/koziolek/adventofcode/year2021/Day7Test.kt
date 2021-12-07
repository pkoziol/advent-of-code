package biz.koziolek.adventofcode.year2021

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

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
        val fullInput = File("src/main/resources/year2021/day7/input").readLines().first()
        val (position, cost) = findCheapestPosition(fullInput, ::calculateLinearCost)
        assertEquals(324, position)
        assertEquals(344535, cost)
    }
}
