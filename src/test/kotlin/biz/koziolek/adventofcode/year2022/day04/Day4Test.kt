package biz.koziolek.adventofcode.year2022.day04

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2022")
internal class Day4Test {

    private val sampleInput = """
            2-4,6-8
            2-3,4-5
            5-7,7-9
            2-8,3-7
            6-6,4-6
            2-6,4-8
        """.trimIndent().split("\n")

    @Test
    fun testParseAssignments() {
        val assignments = parseAssignments(sampleInput)
        assertEquals(6, assignments.size)

        assertEquals(2, assignments[0].first.beginning)
        assertEquals(4, assignments[0].first.end)
        assertEquals(6, assignments[0].second.beginning)
        assertEquals(8, assignments[0].second.end)
    }

    @Test
    fun testIsAnyFullyContainedInOther() {
        val assignments = parseAssignments(sampleInput)
        assertEquals(6, assignments.size)
        assertEquals(false, assignments[0].isAnyFullyContainedInOther())
        assertEquals(false, assignments[1].isAnyFullyContainedInOther())
        assertEquals(false, assignments[2].isAnyFullyContainedInOther())
        assertEquals(true, assignments[3].isAnyFullyContainedInOther())
        assertEquals(true, assignments[4].isAnyFullyContainedInOther())
        assertEquals(false, assignments[5].isAnyFullyContainedInOther())
    }

    @Test
    fun testSampleInput() {
        val assignments = parseAssignments(sampleInput)
        assertEquals(2, assignments.count { it.isAnyFullyContainedInOther() })
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val assignments = parseAssignments(input)
        assertEquals(571, assignments.count { it.isAnyFullyContainedInOther() })
    }
}
