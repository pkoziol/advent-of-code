package biz.koziolek.adventofcode.year2021.day06

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2021")
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
        val fullInput = findInput(object {}).readLines().first()
        val fish = createFish(fullInput)

        assertEquals(300, fish.size)
    }

    @Test
    fun testSimulateSampleDay() {
        val initialFish = createFish(sampleInput)
        assertEquals(5, initialFish.size)

        val fishAfter18Days = (1..18).fold(initialFish) { acc, _ -> simulateDay(acc) }
        assertEquals(26, fishAfter18Days.size)

        val fishAfter80Days = (1..80).fold(initialFish) { acc, _ -> simulateDay(acc) }
        assertEquals(5934, fishAfter80Days.size)
    }

    @Test
    fun testSimulateSampleDayV2() {
        val initialFish = convertV1StateToV2(createFish(sampleInput))
        assertEquals(5, countFish(initialFish))

        val fishAfter18Days = (1..18).fold(initialFish) { acc, _ -> simulateDayV2(acc) }
        assertEquals(26, countFish(fishAfter18Days))

        val fishAfter80Days = (1..80).fold(initialFish) { acc, _ -> simulateDayV2(acc) }
        assertEquals(5934, countFish(fishAfter80Days))

        val fishAfter256Days = (1..256).fold(initialFish) { acc, _ -> simulateDayV2(acc) }
        assertEquals(26984457539, countFish(fishAfter256Days))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val fullInput = findInput(object {}).readLines().first()
        val initialFish = createFish(fullInput)

        val fishAfter80Days = (1..80).fold(initialFish) { acc, _ -> simulateDay(acc) }
        assertEquals(354564, fishAfter80Days.size)

        val fishAfter80DaysV2 = (1..80).fold(convertV1StateToV2(initialFish)) { acc, _ -> simulateDayV2(acc) }
        assertEquals(354564, countFish(fishAfter80DaysV2))
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val fullInput = findInput(object {}).readLines().first()
        val initialFish = createFish(fullInput)

        val fishAfter80DaysV2 = (1..256).fold(convertV1StateToV2(initialFish)) { acc, _ -> simulateDayV2(acc) }
        assertEquals(1609058859115, countFish(fishAfter80DaysV2))
    }
}
