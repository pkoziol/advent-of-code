package biz.koziolek.adventofcode.year2024.day25

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2024")
internal class Day25Test {

    private val sampleInput = """
            #####
            .####
            .####
            .####
            .#.#.
            .#...
            .....

            #####
            ##.##
            .#.##
            ...##
            ...#.
            ...#.
            .....

            .....
            #....
            #....
            #...#
            #.#.#
            #.###
            #####

            .....
            .....
            #.#..
            ###..
            ###.#
            ###.#
            #####

            .....
            .....
            .....
            #....
            #.#..
            #.#.#
            #####
        """.trimIndent().split("\n")

    @Test
    fun testParse() {
        val (locks, keys) = parseLocksAndKeys(sampleInput)
        assertEquals(listOf(
            listOf(0, 5, 3, 4, 3),
            listOf(1, 2, 0, 5, 3),
        ), locks
        )
        assertEquals(listOf(
            listOf(5, 0, 2, 1, 3),
            listOf(4, 3, 4, 0, 2),
            listOf(3, 0, 2, 0, 1),
        ), keys)
    }

    @Test
    fun testSampleAnswer1() {
        val (locks, keys) = parseLocksAndKeys(sampleInput)
        val fitting = countFitting(locks, keys)
        assertEquals(3, fitting)
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val (locks, keys) = parseLocksAndKeys(input)
        val fitting = countFitting(locks, keys)
        assertEquals(2900, fitting)
    }
}
