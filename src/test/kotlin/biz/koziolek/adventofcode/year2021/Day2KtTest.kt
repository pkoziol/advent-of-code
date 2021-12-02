package biz.koziolek.adventofcode.year2021

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class Day2KtTest {

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
        assertEquals(15, position.first)
        assertEquals(10, position.second)
    }
    @Test
    fun testFullInput() {
        val position = File("src/main/resources/year2021/day2/input").useLines {
            calculatePosition(it)
        }
        assertEquals(1991, position.first)
        assertEquals(911, position.second)
    }
}
