package biz.koziolek.adventofcode.year2022.day01

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2022")
internal class Day1Test {

    private val sampleInput = """
            1000
            2000
            3000
            
            4000
            
            5000
            6000
            
            7000
            8000
            9000
            
            10000
        """.trimIndent().split("\n")

    @Test
    fun testSampleInput() {
        val elves = parseElvesCalories(sampleInput)
        assertEquals(5, elves.size)
        val elfWithMostCalories = findElfWithMostCalories(elves)
        assertEquals(24000, elfWithMostCalories?.totalCalories)
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val elves = parseElvesCalories(input)
        val elfWithMostCalories = findElfWithMostCalories(elves)
        assertEquals(68775, elfWithMostCalories?.totalCalories)
    }

    @Test
    fun testSampleInputTop3() {
        val elves = parseElvesCalories(sampleInput)
        val top3ElvesWithMostCalories = findTopElvesWithMostCalories(elves, count = 3)
        assertEquals(45000, top3ElvesWithMostCalories.sumOf { it.totalCalories })
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val elves = parseElvesCalories(input)
        val top3ElvesWithMostCalories = findTopElvesWithMostCalories(elves, count = 3)
        assertEquals(202585, top3ElvesWithMostCalories.sumOf { it.totalCalories })
    }
}
