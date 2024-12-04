package biz.koziolek.adventofcode.year2024.day04

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2024")
internal class Day4Test {

    private val sampleInput = """
            MMMSXXMASM
            MSAMXMSMSA
            AMXSXMAAMM
            MSAMASMSMX
            XMASAMXAMM
            XXAMMXXAMA
            SMSMSASXSS
            SAXAMASAAA
            MAMMMXMMMM
            MXMXAXMASX
        """.trimIndent().split("\n")

    @Test
    fun testSampleAnswer1() {
        val letterGrid = parseLetterGrid(sampleInput)
        val xmas = findXMAS(letterGrid)
        assertEquals(18, xmas.size)
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val letterGrid = parseLetterGrid(input)
        val xmas = findXMAS(letterGrid)
        assertEquals(2414, xmas.size)
    }
}
