package biz.koziolek.adventofcode.year2022.day10

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2022")
internal class Day10Test {

    private val sampleInput1 = """
            noop
            addx 3
            addx -5
        """.trimIndent().split("\n").asSequence()

    private val sampleInput2 = """
            addx 15
            addx -11
            addx 6
            addx -3
            addx 5
            addx -1
            addx -8
            addx 13
            addx 4
            noop
            addx -1
            addx 5
            addx -1
            addx 5
            addx -1
            addx 5
            addx -1
            addx 5
            addx -1
            addx -35
            addx 1
            addx 24
            addx -19
            addx 1
            addx 16
            addx -11
            noop
            noop
            addx 21
            addx -15
            noop
            noop
            addx -3
            addx 9
            addx 1
            addx -3
            addx 8
            addx 1
            addx 5
            noop
            noop
            noop
            noop
            noop
            addx -36
            noop
            addx 1
            addx 7
            noop
            noop
            noop
            addx 2
            addx 6
            noop
            noop
            noop
            noop
            noop
            addx 1
            noop
            noop
            addx 7
            addx 1
            noop
            addx -13
            addx 13
            addx 7
            noop
            addx 1
            addx -33
            noop
            noop
            noop
            addx 2
            noop
            noop
            noop
            addx 8
            noop
            addx -1
            addx 2
            addx 1
            noop
            addx 17
            addx -9
            addx 1
            addx 1
            addx -3
            addx 11
            noop
            noop
            addx 1
            noop
            addx 1
            noop
            noop
            addx -13
            addx -19
            addx 1
            addx 3
            addx 26
            addx -30
            addx 12
            addx -1
            addx 3
            addx 1
            noop
            noop
            noop
            addx -9
            addx 18
            addx 1
            addx 2
            noop
            noop
            addx 9
            noop
            noop
            noop
            addx -1
            addx 2
            addx -37
            addx 1
            addx 3
            noop
            addx 15
            addx -21
            addx 22
            addx -6
            addx 1
            noop
            addx 2
            addx 1
            noop
            addx -10
            noop
            noop
            addx 20
            addx 1
            addx 2
            addx 2
            addx -6
            addx -11
            noop
            noop
            noop
        """.trimIndent().split("\n").asSequence()

    @Test
    fun testParseCPUCommands() {
        val commands = parseCPUCommands(sampleInput1).toList()
        assertEquals(3, commands.size)
        assertEquals(Noop, commands[0])
        assertEquals(AddX(3), commands[1])
        assertEquals(AddX(-5), commands[2])
    }

    @Test
    fun testRunProgram() {
        val commands = parseCPUCommands(sampleInput1)
        val cpu = CPU(cycle = 1, registers = Registers(x = 1))
        val cpuStates = runProgram(cpu, commands).toList()

        assertEquals(listOf(
            CPU(cycle = 1, registers = Registers(x = 1)),
            CPU(cycle = 2, registers = Registers(x = 1)),
            CPU(cycle = 3, registers = Registers(x = 1)),
            CPU(cycle = 4, registers = Registers(x = 4)),
            CPU(cycle = 5, registers = Registers(x = 4)),
            CPU(cycle = 6, registers = Registers(x = -1)),
        ), cpuStates)
    }

    @Test
    fun testSignalStrength() {
        val commands = parseCPUCommands(sampleInput2)
        val cpu = CPU(cycle = 1, registers = Registers(x = 1))
        val cpuStates = runProgram(cpu, commands).toList()

        assertEquals(CPU(cycle = 20, registers = Registers(x = 21)), cpuStates[19])
        assertEquals(420, cpuStates[19].signalStrength)
        assertEquals(CPU(cycle = 60, registers = Registers(x = 19)), cpuStates[59])
        assertEquals(1140, cpuStates[59].signalStrength)
        assertEquals(CPU(cycle = 100, registers = Registers(x = 18)), cpuStates[99])
        assertEquals(1800, cpuStates[99].signalStrength)
        assertEquals(CPU(cycle = 140, registers = Registers(x = 21)), cpuStates[139])
        assertEquals(2940, cpuStates[139].signalStrength)
        assertEquals(CPU(cycle = 180, registers = Registers(x = 16)), cpuStates[179])
        assertEquals(2880, cpuStates[179].signalStrength)
        assertEquals(CPU(cycle = 220, registers = Registers(x = 18)), cpuStates[219])
        assertEquals(3960, cpuStates[219].signalStrength)
    }

    @Test
    fun testSumSignalStrengths() {
        val commands = parseCPUCommands(sampleInput2)
        val cpu = CPU(cycle = 1, registers = Registers(x = 1))
        val cpuStates = runProgram(cpu, commands)

        assertEquals(13140, sumSignalStrengths(cpuStates, cycles = listOf(20, 60, 100, 140, 180, 220)))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().lineSequence()
        val commands = parseCPUCommands(input)
        val cpu = CPU(cycle = 1, registers = Registers(x = 1))
        val cpuStates = runProgram(cpu, commands)

        assertEquals(14920, sumSignalStrengths(cpuStates, cycles = listOf(20, 60, 100, 140, 180, 220)))
    }
}
