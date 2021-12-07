package biz.koziolek.adventofcode.year2021

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class Day7Test {

    private val sampleInput = "16,1,2,0,4,2,7,1,2,14"

    @Test
    fun testFindCheapestPositionSample() {
        val (position, cost) = findCheapestPosition(sampleInput)
        assertEquals(2, position)
        assertEquals(37, cost)
    }

    @Test
    fun testAnswerPart1() {
        val fullInput = File("src/main/resources/year2021/day7/input").readLines().first()
        val (position, cost) = findCheapestPosition(fullInput)
        assertEquals(324, position)
        assertEquals(344535, cost)
    }
}
