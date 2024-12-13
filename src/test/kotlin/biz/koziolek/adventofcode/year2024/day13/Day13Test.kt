package biz.koziolek.adventofcode.year2024.day13

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2024")
internal class Day13Test {

    private val sampleInput = """
            Button A: X+94, Y+34
            Button B: X+22, Y+67
            Prize: X=8400, Y=5400

            Button A: X+26, Y+66
            Button B: X+67, Y+21
            Prize: X=12748, Y=12176

            Button A: X+17, Y+86
            Button B: X+84, Y+37
            Prize: X=7870, Y=6450

            Button A: X+69, Y+23
            Button B: X+27, Y+71
            Prize: X=18641, Y=10279

        """.trimIndent().split("\n")

    @Test
    fun testParseClawMachines() {
        val machines = parseClawMachines(sampleInput)
        assertEquals(4, machines.size)
    }

    @Test
    fun testSampleAnswer1() {
        val machines = parseClawMachines(sampleInput)
        val winningMoves = winAllPossible(machines)
        assertEquals(listOf(
            WinningMove(a = 80, b = 40, machines[0]),
            WinningMove(a = 38, b = 86, machines[2]),
        ), winningMoves)
        assertEquals(480, winningMoves.sumOf { it.cost })
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val machines = parseClawMachines(input)
        val winningMoves = winAllPossible(machines)
        assertEquals(25751, winningMoves.sumOf { it.cost })
    }

    @Test
    fun testSampleAnswer2() {
        val machines = fixConversionError(parseClawMachines(sampleInput))
        val winningMoves = winAllPossible(machines)
        assertEquals(machines[1], winningMoves[0].machine)
        assertEquals(machines[3], winningMoves[1].machine)
        assertEquals(2, winningMoves.size)
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val machines = fixConversionError(parseClawMachines(input))
        val winningMoves = winAllPossible(machines)
        assertEquals(108528956728655, winningMoves.sumOf { it.cost })
    }
}
