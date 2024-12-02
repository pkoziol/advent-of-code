package biz.koziolek.adventofcode.year2024.day02

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2024")
internal class Day2Test {

    private val sampleInput = """
            7 6 4 2 1
            1 2 7 8 9
            9 7 6 2 1
            1 3 2 4 5
            8 6 4 4 1
            1 3 6 7 9
        """.trimIndent().split("\n")

    @Test
    fun testSampleAnswer1() {
        val reports = parseReports(sampleInput)
        assertTrue(isSafeReport(reports[0]))
        assertFalse(isSafeReport(reports[1]))
        assertFalse(isSafeReport(reports[2]))
        assertFalse(isSafeReport(reports[3]))
        assertFalse(isSafeReport(reports[4]))
        assertTrue(isSafeReport(reports[5]))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val reports = parseReports(input)
        assertEquals(332, reports.count { isSafeReport(it) })
    }

    @Test
    fun testSampleAnswer2() {
        val reports = parseReports(sampleInput)
        assertTrue(isSafeReport2(reports[0]))
        assertFalse(isSafeReport2(reports[1]))
        assertFalse(isSafeReport2(reports[2]))
        assertTrue(isSafeReport2(reports[3]))
        assertTrue(isSafeReport2(reports[4]))
        assertTrue(isSafeReport2(reports[5]))
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val reports = parseReports(input)
        assertEquals(398, reports.count { isSafeReport2(it) })
    }
}
