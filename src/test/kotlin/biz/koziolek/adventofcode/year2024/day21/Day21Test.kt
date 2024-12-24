package biz.koziolek.adventofcode.year2024.day21

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2024")
internal class Day21Test {

    private val sampleInput = """
            029A
            980A
            179A
            456A
            379A
        """.trimIndent().split("\n")

    @Test
    fun testParseCodes() {
        val codes = parseCodes(sampleInput)
        assertEquals(listOf("029A", "980A", "179A", "456A", "379A"), codes)
    }

    @Test
    fun testKeypads() {
        assertEquals("""
            789
            456
            123
             0A
        """.trimIndent(), NUMERIC_KEYPAD.toString())

        assertEquals("""
             ^A
            <v>
        """.trimIndent(), DIRECTIONAL_KEYPAD.toString())
    }

    @Test
    fun testFindButtonPresses() {
        val code = "029A"
        val seqLen1 = findSequenceLength(code, listOf(NUMERIC_KEYPAD))
        assertEquals("<A^A>^^AvvvA".length, seqLen1.toInt())

        val seqLen2 = findSequenceLength(code, listOf(NUMERIC_KEYPAD, DIRECTIONAL_KEYPAD))
        assertEquals("v<<A>>^A<A>AvA<^AA>A<vAAA>^A".length, seqLen2.toInt())

        val seqLen3 = findSequenceLength(code, listOf(NUMERIC_KEYPAD, DIRECTIONAL_KEYPAD, DIRECTIONAL_KEYPAD))
        assertEquals("<vA<AA>>^AvAA<^A>A<v<A>>^AvA^A<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A".length, seqLen3.toInt())
    }

    @Test
    fun testCalculateComplexityScore() {
        val codes = listOf("029A", "980A", "179A", "456A", "379A")
        val buttonsSeq3 = listOf(
            "<vA<AA>>^AvAA<^A>A<v<A>>^AvA^A<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A",
            "<v<A>>^AAAvA^A<vA<AA>>^AvAA<^A>A<v<A>A>^AAAvA<^A>A<vA>^A<A>A",
            "<v<A>>^A<vA<A>>^AAvAA<^A>A<v<A>>^AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A",
            "<v<A>>^AA<vA<A>>^AAvAA<^A>A<vA>^A<A>A<vA>^A<A>A<v<A>A>^AAvA<^A>A",
            "<v<A>>^AvA^A<vA<AA>>^AAvA<^A>AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A",
        )
        val seqLens = buttonsSeq3.map { it.length.toLong() }
        val complexityScore = calculateComplexityScore(codes, seqLens)
        assertEquals(126384, complexityScore)
    }

    @Test
    fun testSampleAnswer1() {
        val codes = parseCodes(sampleInput)
        val keypads = listOf(NUMERIC_KEYPAD, DIRECTIONAL_KEYPAD, DIRECTIONAL_KEYPAD)
        val seqLens = codes.map { findSequenceLength(it, keypads) }
        val complexityScore = calculateComplexityScore(codes, seqLens)
        assertEquals(126384, complexityScore)
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val codes = parseCodes(input)
        val keypads = listOf(NUMERIC_KEYPAD, DIRECTIONAL_KEYPAD, DIRECTIONAL_KEYPAD)
        val seqLens = codes.map { findSequenceLength(it, keypads) }

        val complexityScore = calculateComplexityScore(codes, seqLens)
        assertNotEquals(212830, complexityScore)
        assertNotEquals(200410, complexityScore)
        assert(complexityScore < 212830)
        assert(complexityScore > 200410)
        assertEquals(206798, complexityScore)
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val codes = parseCodes(input)
        val keypads = listOf(NUMERIC_KEYPAD) + List(25) { DIRECTIONAL_KEYPAD }
        val buttonsSeq = codes.map { findSequenceLength(it, keypads) }

        val complexityScore = calculateComplexityScore(codes, buttonsSeq)
        assertEquals(251_508_572_750_680, complexityScore)
    }
}
