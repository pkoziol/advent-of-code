package biz.koziolek.adventofcode.year2024.day01

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2024")
internal class Day1Test {

    private val sampleInput = """
            3   4
            4   3
            2   5
            1   3
            3   9
            3   3
        """.trimIndent().split("\n")

    @Test
    fun testParse() {
        val data = parseLists(sampleInput)
        assertEquals(
            listOf(3, 4, 2, 1, 3, 3) to listOf(4, 3, 5, 3, 9, 3),
            data
        )
    }

    @Test
    fun testSampleAnswer1() {
        val data = parseLists(sampleInput)
        assertEquals(11, findTotalDistance(data))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val data = parseLists(input)
        assertEquals(2815556, findTotalDistance(data))
    }

    @Test
    fun testSampleAnswer2() {
        val data = parseLists(sampleInput)
        assertEquals(31, findSimilarityScore(data))
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val data = parseLists(input)
        assertEquals(23927637, findSimilarityScore(data))
    }
}
