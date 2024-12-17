package biz.koziolek.adventofcode.year2024.day17

import biz.koziolek.adventofcode.findInput
import kotlin.math.pow

fun main() {
    val inputFile = findInput(object {})
    val computer = parseComputer(inputFile.bufferedReader().readLines())
    val haltedComputer = computer.run()
    println("Output: ${haltedComputer.output}")
}

fun parseComputer(lines: Iterable<String>): Computer {
    val iterator = lines.iterator()
    val a = Regex("Register A: ([0-9]+)").find(iterator.next())!!.groupValues.let { it[1].toInt() }
    val b = Regex("Register B: ([0-9]+)").find(iterator.next())!!.groupValues.let { it[1].toInt() }
    val c = Regex("Register C: ([0-9]+)").find(iterator.next())!!.groupValues.let { it[1].toInt() }
    iterator.next() // skip empty line
    val program = Regex("Program: ([0-9]+(,[0-9]+)+)").find(iterator.next())!!.groupValues.let { parseProgram(it[1]) }
    return Computer(a, b, c, program = program)
}

fun parseProgram(str: String): List<ProgramItem> =
    buildList {
        val opCodes = str.split(',').map { it.toInt() }
        val iterator = opCodes.iterator()

        while (iterator.hasNext()) {
            when (val opCode = iterator.next()) {
                0 -> {
                    add(Adv)
                    add(ComboOperand(iterator.next()))
                }
                1 -> {
                    add(Bxl)
                    add(LiteralOperand(iterator.next()))
                }
                2 -> {
                    add(Bst)
                    add(ComboOperand(iterator.next()))
                }
                3 -> {
                    add(Jnz)
                    add(LiteralOperand(iterator.next()))
                }
                4 -> {
                    add(Bxc)
                    iterator.next()
                    add(EmptyOperand)
                }
                5 -> {
                    add(Out)
                    add(ComboOperand(iterator.next()))
                }
                6 -> {
                    add(Bdv)
                    add(ComboOperand(iterator.next()))
                }
                7 -> {
                    add(Cdv)
                    add(ComboOperand(iterator.next()))
                }
                else -> throw IllegalArgumentException("Unknown opcode: $opCode")
            }
        }
    }

data class Computer(
    var a: Int = 0,
    var b: Int = 0,
    var c: Int = 0,
    var output: String = "",
    var program: List<ProgramItem> = emptyList(),
    var instructionPointer: Int = 0,
) {
    val isHalted: Boolean
        get() = instructionPointer >= program.size

    fun run(limit: Int = 1000, debug: Boolean = false): Computer {
        var currentComputer = this
        var step = 0
        try {
            while (!currentComputer.isHalted) {
                if (step >= limit) {
                    throw IllegalStateException("Step limit exceeded")
                }
                if (debug) {
                    println("Step $step: $currentComputer")
                }
                currentComputer = currentComputer.step()
                step++
            }
        } catch (e: Exception) {
            throw RuntimeException("Error at step $step", e)
        }
        return currentComputer
    }

    fun step(): Computer {
        try {
            val instruction = program[instructionPointer] as Instruction
            val newComputer = instruction.execute(this)
            return newComputer
        } catch (e: Exception) {
            throw RuntimeException("Error while trying to execute instruction at: $instructionPointer", e)
        }
    }

    fun getOperandValue(offset: Int = 0): Int {
        val operand = program[instructionPointer + 1 + offset] as Operand
        return operand.getValue(this)
    }

    fun programToString(): String =
        buildString {
            var first = true
            for (p in program) {
                when (p) {
                    is Instruction -> {
                        if (!first) {
                            append("\n")
                        }
                        first = false
                        append("$p")
                    }
                    is Operand -> append(" $p")
                }
            }
        }
}

sealed interface ProgramItem

sealed class Instruction : ProgramItem {
    abstract fun execute(computer: Computer): Computer

    override fun toString(): String = javaClass.simpleName.lowercase()
}

object Adv : Instruction() {
    override fun execute(computer: Computer): Computer {
        val numerator = computer.a
        val denominator = 2.0.pow(computer.getOperandValue()).toInt()
        return computer.copy(
            a = numerator / denominator,
            instructionPointer = computer.instructionPointer + 2,
        )
    }
}

object Bxl : Instruction() {
    override fun execute(computer: Computer): Computer {
        val value = computer.getOperandValue()
        return computer.copy(
            b = computer.b xor value,
            instructionPointer = computer.instructionPointer + 2,
        )
    }
}

object Bst : Instruction() {
    override fun execute(computer: Computer): Computer {
        val value = computer.getOperandValue()
        return computer.copy(
            b = value % 8,
            instructionPointer = computer.instructionPointer + 2,
        )
    }
}

object Jnz : Instruction() {
    override fun execute(computer: Computer): Computer {
        return computer.copy(
            instructionPointer =
                when (computer.a) {
                    0 -> computer.instructionPointer + 2
                    else -> computer.getOperandValue(0)
                }
        )
    }
}

object Bxc : Instruction() {
    override fun execute(computer: Computer): Computer {
        return computer.copy(
            b = computer.b xor computer.c,
            instructionPointer = computer.instructionPointer + 2,
        )
    }
}

object Out : Instruction() {
    override fun execute(computer: Computer): Computer {
        val value = computer.getOperandValue()
        val output = when {
            computer.output.isEmpty() -> "${value % 8}"
            else -> "${computer.output},${value % 8}"
        }

        return computer.copy(
            output = output,
            instructionPointer = computer.instructionPointer + 2,
        )
    }
}

object Bdv : Instruction() {
    override fun execute(computer: Computer): Computer {
        val numerator = computer.a
        val denominator = 2.0.pow(computer.getOperandValue()).toInt()
        return computer.copy(
            b = numerator / denominator,
            instructionPointer = computer.instructionPointer + 2,
        )
    }
}

object Cdv : Instruction() {
    override fun execute(computer: Computer): Computer {
        val numerator = computer.a
        val denominator = 2.0.pow(computer.getOperandValue()).toInt()
        return computer.copy(
            c = numerator / denominator,
            instructionPointer = computer.instructionPointer + 2,
        )
    }
}

sealed interface Operand : ProgramItem {
    fun getValue(computer: Computer): Int
}

data class LiteralOperand(val value: Int) : Operand {
    override fun getValue(computer: Computer): Int = value
    override fun toString(): String = value.toString()
}

data class ComboOperand(val value: Int) : Operand {
    override fun getValue(computer: Computer): Int =
        when (value) {
            in 0..3 -> value
            4 -> computer.a
            5 -> computer.b
            6 -> computer.c
            7 -> throw IllegalArgumentException("Reserved combo operand: $value")
            else -> throw IllegalArgumentException("Unknown combo operand: $value")
        }

    override fun toString(): String =
        when (value) {
            in 0..3 -> value.toString()
            4 -> "A"
            5 -> "B"
            6 -> "C"
            7 -> throw IllegalArgumentException("Reserved combo operand: $value")
            else -> throw IllegalArgumentException("Unknown combo operand: $value")
        }
}

object EmptyOperand : Operand {
    override fun getValue(computer: Computer): Int = throw IllegalArgumentException("Empty operand")
    override fun toString(): String = ""
}
