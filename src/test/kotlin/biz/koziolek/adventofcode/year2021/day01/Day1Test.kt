package biz.koziolek.adventofcode.year2021.day01

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2021")
internal class Day1Test {

    private val sampleInput = """
            199
            200
            208
            210
            200
            207
            240
            269
            260
            263
        """.trimIndent()

    @Test
    fun testSampleInput() {
        val increases = countIncreases(sampleInput.splitToSequence("\n"))
        assertEquals(7, increases)
    }

    @Test
    fun testSampleInputWindow3() {
        val increases = countIncreasesSlidingWindow(sampleInput.splitToSequence("\n"), windowSize = 3)
        assertEquals(5, increases)
    }

    @Test
    fun testSampleInputWindow1() {
        val increases = countIncreasesSlidingWindow(sampleInput.splitToSequence("\n"), windowSize = 1)
        assertEquals(7, increases)
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val increases = findInput(object {}).useLines {
            countIncreases(it)
        }
        assertEquals(1665, increases)
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val increases = findInput(object {}).useLines {
            countIncreasesSlidingWindow(it, windowSize = 3)
        }
        assertEquals(1702, increases)
    }

    @Test
    fun testFullInputWindow1() {
        val increases = findInput(object {}).useLines {
            countIncreasesSlidingWindow(it, windowSize = 1)
        }
        assertEquals(1665, increases)
    }
}
