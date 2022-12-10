package biz.koziolek.adventofcode.year2022.day10

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val commands = parseCPUCommands(inputFile.bufferedReader().lineSequence()).toList()
    val cpu = CPU(cycle = 1, registers = Registers(x = 1))
    val cpuStates = runProgram(cpu, commands.asSequence())

    println("Sum of signal strength during the 20th, 60th, 100th, 140th, 180th, and 220th cycles: ${sumSignalStrengths(cpuStates, cycles = listOf(20, 60, 100, 140, 180, 220))}")

    val crt = CRT(width = 40, height = 6, contents = "")
    val lastCRT = syncCRTWithCPU(crt, cpu, commands.asSequence())
    println("CRT:\n${lastCRT.render()}")
}

data class CPU(
    val cycle: Int = 1,
    val registers: Registers = Registers(),
) {
    val signalStrength: Int = cycle * registers.x
}

data class Registers(
    val x: Int = 1,
)

sealed interface Command {
    val cycles: Int
    fun execute(registers: Registers): Registers
}

object Noop : Command {
    override val cycles = 1
    override fun execute(registers: Registers) = registers
}

data class AddX(val value: Int) : Command {
    override val cycles = 2

    override fun execute(registers: Registers) =
        registers.copy(x = registers.x + value)
}

data class CRT(
    val width: Int,
    val height: Int,
    val contents: String,
) {
    fun addPixel(isLit: Boolean): CRT =
        copy(contents = contents + if (isLit) '#' else '.')

    fun render() =
        contents.chunked(width).joinToString("\n")
}

fun parseCPUCommands(lines: Sequence<String>): Sequence<Command> =
    lines.map { line ->
        val parts = line.split(' ')
        when (parts[0]) {
            "noop" -> Noop
            "addx" -> AddX(parts[1].toInt())
            else -> throw IllegalArgumentException("Unknown command: '${parts[0]}'")
        }
    }

fun runProgram(cpu: CPU, commands: Sequence<Command>): Sequence<CPU> =
    sequence {
        var currentCPU = cpu
        for (command in commands) {
            repeat(command.cycles) {
                yield(currentCPU)
                currentCPU = currentCPU.copy(
                    cycle = currentCPU.cycle + 1,
                )
            }
            currentCPU = currentCPU.copy(
                registers = command.execute(currentCPU.registers),
            )
        }
        yield(currentCPU)
    }

fun sumSignalStrengths(cpuStates: Sequence<CPU>, cycles: Collection<Int>): Int =
    cpuStates
        .filter { cycles.contains(it.cycle) }
        .sumOf { it.signalStrength }

fun syncCRTWithCPU(crt: CRT, cpu: CPU, commands: Sequence<Command>): CRT =
    runProgram(cpu, commands)
        .take(crt.width * crt.height)
        .fold(crt, ::updateCRT)

fun updateCRT(crt: CRT, cpu: CPU, spriteLength: Int = 3): CRT {
    val spritePosition = cpu.registers.x
    val spriteRange = (spritePosition - spriteLength/2)..(spritePosition + spriteLength/2)
    val drawnPixelPosition = crt.contents.length % crt.width
    val isLit = drawnPixelPosition in spriteRange
    return crt.addPixel(isLit)
}
