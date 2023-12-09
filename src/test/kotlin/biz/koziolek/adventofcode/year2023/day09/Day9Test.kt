package biz.koziolek.adventofcode.year2023.day09

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2023")
internal class Day9Test {

    private val sampleInput = """
            0 3 6 9 12 15
            1 3 6 10 15 21
            10 13 16 21 30 45
        """.trimIndent().split("\n")

    @Test
    fun testParseOasisReport() {
        val report = parseOasisReport(sampleInput)
        assertEquals(
            OasisReport(
                readings = listOf(
                    listOf(0, 3, 6, 9, 12, 15),
                    listOf(1, 3, 6, 10, 15, 21),
                    listOf(10, 13, 16, 21, 30, 45),
                ),
            ),
            report
        )
    }
    
    @Test
    fun testReduceHistory() {
        val report = parseOasisReport(sampleInput)

        assertEquals(
            listOf(
                listOf(0, 3, 6, 9, 12, 15),
                listOf(3, 3, 3, 3, 3),
                listOf(0, 0, 0, 0),
            ),
            reduceHistory(report.readings[0])
        )
        assertEquals(
            listOf(
                listOf(1, 3, 6, 10, 15, 21),
                listOf(2, 3, 4, 5, 6),
                listOf(1, 1, 1, 1),
                listOf(0, 0, 0),
            ),
            reduceHistory(report.readings[1])
        )
        assertEquals(
            listOf(
                listOf(10, 13, 16, 21, 30, 45),
                listOf(3, 3, 5, 9, 15),
                listOf(0, 2, 4, 6),
                listOf(2, 2, 2),
                listOf(0, 0),
            ),
            reduceHistory(report.readings[2])
        )
    }

    @Test
    fun testExtrapolateHistory() {
        val report = parseOasisReport(sampleInput)

        assertEquals(
            listOf(
                listOf(0, 3, 6, 9, 12, 15, 18),
                listOf(3, 3, 3, 3, 3, 3),
                listOf(0, 0, 0, 0, 0),
            ),
            extrapolateHistory(report.readings[0])
        )
        assertEquals(
            listOf(
                listOf(1, 3, 6, 10, 15, 21, 28),
                listOf(2, 3, 4, 5, 6, 7),
                listOf(1, 1, 1, 1, 1),
                listOf(0, 0, 0, 0),
            ),
            extrapolateHistory(report.readings[1])
        )
        assertEquals(
            listOf(
                listOf(10, 13, 16, 21, 30, 45, 68),
                listOf(3, 3, 5, 9, 15, 23),
                listOf(0, 2, 4, 6, 8),
                listOf(2, 2, 2, 2),
                listOf(0, 0, 0),
            ),
            extrapolateHistory(report.readings[2])
        )
    }

    @Test
    fun testPredictNextValue() {
        val report = parseOasisReport(sampleInput)
        assertEquals(18, predictNextValue(report.readings[0]))
        assertEquals(28, predictNextValue(report.readings[1]))
        assertEquals(68, predictNextValue(report.readings[2]))
    }

    @Test
    fun testSampleAnswer1() {
        val report = parseOasisReport(sampleInput)
        assertEquals(114, report.readings.sumOf { predictNextValue(it) })
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val report = parseOasisReport(input)
        assertEquals(1479011877, report.readings.sumOf { predictNextValue(it) })
    }

    @Test
    fun testPredictPreviousValue() {
        val report = parseOasisReport(sampleInput)
        assertEquals(-3, predictPreviousValue(report.readings[0]))
        assertEquals(0, predictPreviousValue(report.readings[1]))
        assertEquals(5, predictPreviousValue(report.readings[2]))
    }

    @Test
    fun testSampleAnswer2() {
        val report = parseOasisReport(sampleInput)
        assertEquals(2, report.readings.sumOf { predictPreviousValue(it) })
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val report = parseOasisReport(input)
        assertEquals(973, report.readings.sumOf { predictPreviousValue(it) })
    }
}
