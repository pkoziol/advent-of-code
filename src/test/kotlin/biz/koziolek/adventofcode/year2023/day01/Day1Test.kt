package biz.koziolek.adventofcode.year2023.day01

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2023")
internal class Day1Test {

    private val sampleInput = """
            1abc2
            pqr3stu8vwx
            a1b2c3d4e5f
            treb7uchet
        """.trimIndent().split("\n")

    private val sampleInput2 = """
            two1nine
            eightwothree
            abcone2threexyz
            xtwone3four
            4nineeightseven2
            zoneight234
            7pqrstsixteen
        """.trimIndent().split("\n")

    @Test
    fun testSampleInput() {
        val data = parseCalibrationValues(sampleInput)
        assertEquals(listOf(12, 38, 15, 77), data)
        assertEquals(142, sumCalibrationValues(data))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val data = parseCalibrationValues(input)
        assertEquals(55123, sumCalibrationValues(data))
    }

    @Test
    fun testSampleInput2() {
        val data = parseCalibrationValuesWithLetters(sampleInput2)
        assertEquals(listOf(29, 83, 13, 24, 42, 14, 76), data)
        assertEquals(281, sumCalibrationValues(data))
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val data = parseCalibrationValuesWithLetters(input)
        assertEquals(55260, sumCalibrationValues(data))
    }
}
