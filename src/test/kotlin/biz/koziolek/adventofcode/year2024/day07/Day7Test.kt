package biz.koziolek.adventofcode.year2024.day07

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2024")
internal class Day7Test {

    private val sampleInput = """
            190: 10 19
            3267: 81 40 27
            83: 17 5
            156: 15 6
            7290: 6 8 6 15
            161011: 16 10 13
            192: 17 8 14
            21037: 9 7 18 13
            292: 11 6 16 20
        """.trimIndent().split("\n")

    @Test
    fun testParseCalibrationEquations() {
        val equations = parseCalibrationEquations(sampleInput)
        assertEquals(9, equations.size)
    }

    @Test
    fun testEvaluate() {
        assertEquals(123, Equation(123, listOf(100, 23)).evaluate(listOf('+')))
        assertEquals(24, Equation(24, listOf(2, 3, 4)).evaluate(listOf('*', '*')))
        assertEquals(20, Equation(20, listOf(2, 3, 4)).evaluate(listOf('+', '*')))
        assertEquals(156, Equation(156, listOf(15, 6)).evaluate(listOf('|')))
        assertEquals(7290, Equation(7290, listOf(6, 8, 6, 15)).evaluate(listOf('*', '|', '*')))
        assertEquals(192, Equation(192, listOf(17, 8, 14)).evaluate(listOf('|', '+')))
    }

    @Test
    fun testSampleAnswer1() {
        val equations = parseCalibrationEquations(sampleInput)
        val validEquations = findValidEquations(equations, operators = listOf('+', '*'))
        assertEquals(3749, validEquations.sumOf { it.value })
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val equations = parseCalibrationEquations(input)
        val validEquations = findValidEquations(equations, operators = listOf('+', '*'))
        assertEquals(20281182715321, validEquations.sumOf { it.value })
    }

    @Test
    fun testSampleAnswer2() {
        val equations = parseCalibrationEquations(sampleInput)
        val validEquations = findValidEquations(equations, operators = listOf('+', '*', '|'))
        assertEquals(11387, validEquations.sumOf { it.value })
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val equations = parseCalibrationEquations(input)
        val validEquations = findValidEquations(equations, operators = listOf('+', '*', '|'))
        assertEquals(159490400628354, validEquations.sumOf { it.value })
    }
}
