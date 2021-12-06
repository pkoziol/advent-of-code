package biz.koziolek.adventofcode.year2021

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class Day6Test {

    private val sampleInput = "3,4,3,1,2"

    @Test
    fun testParsingSampleInput() {
        val fish = createFish(sampleInput)
        assertEquals(5, fish.size)

        assertEquals(3, fish[0].timer)
        assertEquals(4, fish[1].timer)
        assertEquals(3, fish[2].timer)
        assertEquals(1, fish[3].timer)
        assertEquals(2, fish[4].timer)
    }

    @Test
    fun testParseFullInput() {
        val fullInput = File("src/main/resources/year2021/day6/input").readLines().first()
        val fish = createFish(fullInput)

        assertEquals(300, fish.size)
    }

    @Test
    fun testSimulateSampleDay() {
        val initialFish = createFish(sampleInput)
        assertEquals(5, initialFish.size)

        val fishAfter18Days = (1..18).fold(initialFish) { acc, day -> simulateDay(acc) }
        assertEquals(26, fishAfter18Days.size)

        val fishAfter80Days = (1..80).fold(initialFish) { acc, day -> simulateDay(acc) }
        assertEquals(5934, fishAfter80Days.size)
    }

    @Test
    fun testAnswerPart1() {
        val fullInput = File("src/main/resources/year2021/day6/input").readLines().first()
        val initialFish = createFish(fullInput)
        val fishAfter80Days = (1..80).fold(initialFish) { acc, day -> simulateDay(acc) }
        assertEquals(354564, fishAfter80Days.size)
    }
}
