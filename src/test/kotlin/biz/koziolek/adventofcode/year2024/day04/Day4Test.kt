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
        val xmas = countXMAS(letterGrid)
        assertEquals(18, xmas)
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val letterGrid = parseLetterGrid(input)
        val xmas = countXMAS(letterGrid)
        assertEquals(2414, xmas)
    }

    @Test
    fun testSampleAnswer2() {
        val letterGrid = parseLetterGrid(sampleInput)
        val x_mas = countX_MAS(letterGrid)
        assertEquals(9, x_mas)
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val letterGrid = parseLetterGrid(input)
        val x_mas = countX_MAS(letterGrid)
        assertEquals(1871, x_mas)
    }
}
