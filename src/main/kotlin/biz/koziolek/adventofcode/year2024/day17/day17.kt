package biz.koziolek.adventofcode.year2024.day17

import biz.koziolek.adventofcode.findInput
import kotlin.math.pow

fun main() {
    val inputFile = findInput(object {})
    val computer = parseComputer(inputFile.bufferedReader().readLines())
    val haltedComputer = computer.run()
    println("Output: ${haltedComputer.output}")

    val a = findCorrectA(computer)
    println("A that causes the program to output a copy of itself: $a")
}

fun parseComputer(lines: Iterable<String>): Computer {
    val iterator = lines.iterator()
    val a = Regex("Register A: ([0-9]+)").find(iterator.next())!!.groupValues.let { it[1].toLong() }
    val b = Regex("Register B: ([0-9]+)").find(iterator.next())!!.groupValues.let { it[1].toLong() }
    val c = Regex("Register C: ([0-9]+)").find(iterator.next())!!.groupValues.let { it[1].toLong() }
    iterator.next() // skip empty line
    val program = Regex("Program: ([0-9]+(,[0-9]+)+)").find(iterator.next())!!.groupValues.let { parseProgram(it[1]) }
    return Computer(a, b, c, program = program)
}

fun parseProgram(str: String): List<ProgramItem> =
    try {
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
                        add(EmptyOperand(iterator.next()))
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
    } catch (e: Exception) {
        throw IllegalArgumentException("Error while parsing program: $str", e)
    }

data class Computer(
    var a: Long = 0,
    var b: Long = 0,
    var c: Long = 0,
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

    fun getOperandValue(offset: Int = 0): Long {
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

    fun programToPseudocode(): String =
        buildString {
            var first = true
            val iterator = program.iterator()

            while (iterator.hasNext()) {
                val instruction = iterator.next() as Instruction
                val operand = iterator.next() as Operand
                if (!first) {
                    append("\n")
                }
                first = false
                append(instruction.toPseudocode(operand))
            }
        }

    fun programToCode(): List<Int> =
        program.map { it.toCode() }
}

sealed interface ProgramItem {
    fun toPseudocode(operand: Operand): String
    fun toCode(): Int
}

sealed class Instruction(val opcode: Int) : ProgramItem {
    abstract fun execute(computer: Computer): Computer

    override fun toString(): String = javaClass.simpleName.lowercase()
    override fun toCode(): Int = opcode
}

object Adv : Instruction(0) {
    override fun execute(computer: Computer): Computer {
        val numerator = computer.a
        val denominator = 2.0.pow(computer.getOperandValue().toInt()).toInt()
        return computer.copy(
            a = numerator / denominator,
            instructionPointer = computer.instructionPointer + 2,
        )
    }

    override fun toPseudocode(operand: Operand): String =
        "A = A / 2^${operand}"
}

object Bxl : Instruction(1) {
    override fun execute(computer: Computer): Computer {
        val value = computer.getOperandValue()
        return computer.copy(
            b = computer.b xor value,
            instructionPointer = computer.instructionPointer + 2,
        )
    }

    override fun toPseudocode(operand: Operand): String =
        "B = B xor $operand"
}

object Bst : Instruction(2) {
    override fun execute(computer: Computer): Computer {
        val value = computer.getOperandValue()
        return computer.copy(
            b = value % 8L,
            instructionPointer = computer.instructionPointer + 2,
        )
    }

    override fun toPseudocode(operand: Operand): String =
        "B = $operand mod 8"
}

object Jnz : Instruction(3) {
    override fun execute(computer: Computer): Computer {
        return computer.copy(
            instructionPointer =
                when (computer.a) {
                    0L -> computer.instructionPointer + 2
                    else -> computer.getOperandValue(0).toInt()
                }
        )
    }

    override fun toPseudocode(operand: Operand): String =
        "if (A != 0) jump(${operand})"
}

object Bxc : Instruction(4) {
    override fun execute(computer: Computer): Computer {
        return computer.copy(
            b = computer.b xor computer.c,
            instructionPointer = computer.instructionPointer + 2,
        )
    }

    override fun toPseudocode(operand: Operand): String =
        "B = B xor C"
}

object Out : Instruction(5) {
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

    override fun toPseudocode(operand: Operand): String =
        "print(${operand})"
}

object Bdv : Instruction(6) {
    override fun execute(computer: Computer): Computer {
        val numerator = computer.a
        val denominator = 2.0.pow(computer.getOperandValue().toInt()).toInt()
        return computer.copy(
            b = numerator / denominator,
            instructionPointer = computer.instructionPointer + 2,
        )
    }

    override fun toPseudocode(operand: Operand): String =
        "B = A / 2^${operand}"
}

object Cdv : Instruction(7) {
    override fun execute(computer: Computer): Computer {
        val numerator = computer.a
        val denominator = 2.0.pow(computer.getOperandValue().toInt()).toInt()
        try {
            return computer.copy(
                c = numerator / denominator,
                instructionPointer = computer.instructionPointer + 2,
            )
        } catch (e: Exception) {
            println("Error while dividing $numerator by 2^${computer.getOperandValue()}")
            println(computer)
            throw e
        }
    }

    override fun toPseudocode(operand: Operand): String =
        "C = A / 2^${operand}"
}

sealed class Operand(val value: Int) : ProgramItem {
    abstract fun getValue(computer: Computer): Long
    override fun toPseudocode(operand: Operand): String = toString()
    override fun toCode(): Int = value

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Operand

        return value == other.value
    }

    override fun hashCode(): Int {
        return value
    }
}

class LiteralOperand(value: Int) : Operand(value) {
    override fun getValue(computer: Computer): Long = value.toLong()
    override fun toString(): String = value.toString()
}

class ComboOperand(value: Int) : Operand(value) {
    override fun getValue(computer: Computer): Long =
        when (value) {
            in 0..3 -> value.toLong()
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

class EmptyOperand(value: Int) : Operand(value) {
    override fun getValue(computer: Computer): Long = throw IllegalArgumentException("Empty operand")
    override fun toString(): String = "-"
}

fun findCorrectA(computer: Computer): Long {
    var a = 0L
    var wantedOutput = ""
    for (opcode in computer.programToCode().reversed()) {
        wantedOutput = if (wantedOutput.isEmpty()) "$opcode" else "$opcode,$wantedOutput"

        for (aIncrease in 0..1_000_000_000) {
            val newA = a * 8 + aIncrease
            try {
                val fixedComputer = computer.copy(a = newA)
                val haltedComputer = fixedComputer.run()
                if (haltedComputer.output == wantedOutput) {
                    a = newA
                    break
                }
            } catch (e: Exception) {
                // Ignore
            }
        }
    }
    return a
}
