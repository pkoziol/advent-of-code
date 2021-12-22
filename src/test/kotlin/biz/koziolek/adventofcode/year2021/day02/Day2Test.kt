package biz.koziolek.adventofcode.year2021.day02

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2021")
internal class Day2Test {

    private val sampleInput = """
            forward 5
            down 5
            forward 8
            up 3
            down 8
            forward 2
        """.trimIndent()

    @Test
    fun testSampleInput() {
        val position = calculatePosition(sampleInput.splitToSequence("\n"))
        assertEquals(15, position.horizontal)
        assertEquals(10, position.depth)
    }

    @Test
    fun testSampleInputWithAim() {
        val position = calculatePositionWithAim(sampleInput.splitToSequence("\n"))
        assertEquals(15, position.horizontal)
        assertEquals(60, position.depth)
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val position = findInput(object {}).useLines {
            calculatePosition(it)
        }
        assertEquals(1991, position.horizontal)
        assertEquals(911, position.depth)
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val position = findInput(object {}).useLines {
            calculatePositionWithAim(it)
        }
        assertEquals(1991, position.horizontal)
        assertEquals(984716, position.depth)
    }
}
