package biz.koziolek.adventofcode.year{YEAR}.day{DAY_LEADING_ZERO}

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("{YEAR}")
internal class Day{DAY}Test {

    private val sampleInput = """
            SAMPLE_INPUT_HERE
        """.trimIndent().split("\n")

    @Test
    fun testParse() {
        val data = parse(sampleInput)
        assertEquals()
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val data = parse(sampleInput)
        assertEquals(TODO, data)
    }
}
