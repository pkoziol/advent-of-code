package biz.koziolek.adventofcode.year2024.day05

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2024")
internal class Day5Test {

    private val sampleInput = """
            47|53
            97|13
            97|61
            97|47
            75|29
            61|13
            75|53
            29|13
            97|29
            53|29
            61|53
            97|53
            61|29
            47|13
            75|47
            97|75
            47|61
            75|61
            47|29
            75|13
            53|13

            75,47,61,53,29
            97,61,53,29,13
            75,29,13
            75,97,47,61,53
            61,13,29
            97,13,75,29,47
        """.trimIndent().split("\n")

    @Test
    fun testParseSafetyManual() {
        val (rules, updates) = parseSafetyManual(sampleInput)
        assertEquals(21, rules.size)
        assertEquals(6, updates.size)
    }

    @Test
    fun testSampleAnswer1() {
        val (rules, updates) = parseSafetyManual(sampleInput)
        val (correctlyOrdered, _) = findCorrectOrder(rules, updates)
        assertEquals(3, correctlyOrdered.size)
        val middlePageSum = correctlyOrdered.sumOf { it.middlePage }
        assertEquals(143, middlePageSum)
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val (rules, updates) = parseSafetyManual(input)
        val (correctlyOrdered, _) = findCorrectOrder(rules, updates)
        val middlePageSum = correctlyOrdered.sumOf { it.middlePage }
        assertEquals(6612, middlePageSum)
    }

    @Test
    fun testSampleAnswer2() {
        val (rules, updates) = parseSafetyManual(sampleInput)
        val (_, incorrectlyOrdered) = findCorrectOrder(rules, updates)
        assertEquals(3, incorrectlyOrdered.size)
        val fixedOrder = fixOrdering(rules, incorrectlyOrdered)
        val middlePageSum = fixedOrder.sumOf { it.middlePage }
        assertEquals(123, middlePageSum)
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val (rules, updates) = parseSafetyManual(input)
        val (_, incorrectlyOrdered) = findCorrectOrder(rules, updates)
        val fixedOrder = fixOrdering(rules, incorrectlyOrdered)
        val middlePageSum = fixedOrder.sumOf { it.middlePage }
        assertEquals(4944, middlePageSum)
    }
}
