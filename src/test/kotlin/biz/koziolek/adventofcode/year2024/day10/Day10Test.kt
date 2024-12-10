package biz.koziolek.adventofcode.year2024.day10

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2024")
internal class Day10Test {

    private val sampleInput = """
            89010123
            78121874
            87430965
            96549874
            45678903
            32019012
            01329801
            10456732
        """.trimIndent().split("\n")

    @Test
    fun testSampleAnswer1() {
        val map = parseMap(sampleInput)
        val trails = findTrails(map)
        val scores = findTrailHeadScores(trails)
        assertEquals(36, scores)
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val map = parseMap(input)
        val trails = findTrails(map)
        val scores = findTrailHeadScores(trails)
        assertEquals(566, scores)
    }

    @Test
    fun testSampleAnswer2() {
        val map = parseMap(sampleInput)
        val trails = findTrails(map)
        assertEquals(81, trails.size)
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val map = parseMap(input)
        val trails = findTrails(map)
        assertEquals(1324, trails.size)
    }
}
