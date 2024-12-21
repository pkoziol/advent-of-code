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
    fun testPressButtons() {
        val buttonsSeq1 = NUMERIC_KEYPAD.pressButtons("<A^A>^^AvvvA")
        assertEquals("029A", buttonsSeq1)

        val tmpButtonsSeq1 = DIRECTIONAL_KEYPAD.pressButtons("<v<A>>^A<vA<A>>^AAvAA<^A>A<v<A>>^AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A")
        val tmpButtonsSeq2 = DIRECTIONAL_KEYPAD.pressButtons(tmpButtonsSeq1)
        val tmpButtonsSeq3 = NUMERIC_KEYPAD.pressButtons(tmpButtonsSeq2)
        assertEquals("179A", tmpButtonsSeq3)

        assertThrows(IllegalArgumentException::class.java) {
            NUMERIC_KEYPAD.pressButtons("<<^A")
        }
    }

    @Test
    fun testFindButtonPresses() {
        val code = "029A"
        val buttonsSeq1 = findButtonPresses(code, listOf(NUMERIC_KEYPAD))
        println("buttonsSeq1: $buttonsSeq1\n")
        assertEquals(code, NUMERIC_KEYPAD.pressButtons(buttonsSeq1))
        assertEquals("<A^A>^^AvvvA".length, buttonsSeq1.length)
        //            <A^A>^^AvvvA

        val buttonsSeq2 = findButtonPresses(buttonsSeq1, listOf(DIRECTIONAL_KEYPAD))
        println("buttonsSeq2: $buttonsSeq2\n")
        assertEquals(buttonsSeq1, DIRECTIONAL_KEYPAD.pressButtons(buttonsSeq2))
        assertEquals("v<<A>>^A<A>AvA<^AA>A<vAAA>^A".length, buttonsSeq2.length)
        //            <v<A>>^A<A>AvA<^AA>A<vAAA>^A

        val buttonsSeq3 = findButtonPresses(buttonsSeq2, listOf(DIRECTIONAL_KEYPAD))
        println("buttonsSeq3: $buttonsSeq3\n")
        assertEquals(buttonsSeq2, DIRECTIONAL_KEYPAD.pressButtons(buttonsSeq3))
        assertEquals("<vA<AA>>^AvAA<^A>A<v<A>>^AvA^A<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A".length, buttonsSeq3.length)
        //            <vA<AA>>^AvAA<^A>Av<<A>>^AvA^A<vA>^Av<<A>^A>AAvA^Av<<A>A>^AAAvA<^A>A
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
        val complexityScore = calculateComplexityScore(codes, buttonsSeq3)
        assertEquals(126384, complexityScore)
    }

    @Test
    fun testSampleAnswer1() {
        val codes = parseCodes(sampleInput)
        val keypads = listOf(
            NUMERIC_KEYPAD,
            DIRECTIONAL_KEYPAD,
            DIRECTIONAL_KEYPAD,
        )
        val buttonsSeq = findButtonPresses(codes, keypads)
        val complexityScore = calculateComplexityScore(codes, buttonsSeq)
        assertEquals(126384, complexityScore)
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val codes = parseCodes(input)
        val keypads = listOf(
            NUMERIC_KEYPAD,
            DIRECTIONAL_KEYPAD,
            DIRECTIONAL_KEYPAD,
        )

        val buttonsSeq = findButtonPresses(codes, keypads)

        val complexityScore = calculateComplexityScore(codes, buttonsSeq)
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

        val buttonsSeq = findButtonPresses(codes, keypads)

        val complexityScore = calculateComplexityScore(codes, buttonsSeq)
        assertNotEquals(212830, complexityScore)
        assertNotEquals(200410, complexityScore)
        assert(complexityScore < 212830)
        assert(complexityScore > 200410)
        assertEquals(206798, complexityScore)
    }
}
