package biz.koziolek.adventofcode.year2021.day24

import java.util.*

fun main() {
    val validSerialNumbers = generateValidSerialNumbers()
    println("Max valid serial number: ${validSerialNumbers.maxOf { it }}")
    println("Min valid serial number: ${validSerialNumbers.minOf { it }}")
}

sealed interface VarOrNum
data class Var(val name: String) : VarOrNum
data class Num(val number: Int) : VarOrNum

sealed interface AluInstruction
data class Inp(val a: Var) : AluInstruction
data class Add(val a: Var, val b: VarOrNum) : AluInstruction
data class Mul(val a: Var, val b: VarOrNum) : AluInstruction
data class Div(val a: Var, val b: VarOrNum) : AluInstruction
data class Mod(val a: Var, val b: VarOrNum) : AluInstruction
data class Eql(val a: Var, val b: VarOrNum) : AluInstruction

data class State(val state: Map<String, Int> = emptyMap()) {
    operator fun get(variable: Var): Int =
        state.getOrDefault(variable.name, 0)

    operator fun get(varOrNum: VarOrNum): Int =
        when (varOrNum) {
            is Num -> varOrNum.number
            is Var -> get(varOrNum)
        }

    fun set(variable: Var, value: Int): State =
        copy(state = state + (variable.name to value))

    fun update(variable: Var, updater: (Int) -> Int): State =
        set(variable, updater(get(variable)))
}

fun parseAluInstructions(lines: List<String>): List<AluInstruction> =
    lines.map { it.split(' ') }
        .map {
            when (it[0]) {
                "inp" -> Inp(Var(it[1]))
                "add" -> Add(Var(it[1]), readVarOrNum(it[2]))
                "mul" -> Mul(Var(it[1]), readVarOrNum(it[2]))
                "div" -> Div(Var(it[1]), readVarOrNum(it[2]))
                "mod" -> Mod(Var(it[1]), readVarOrNum(it[2]))
                "eql" -> Eql(Var(it[1]), readVarOrNum(it[2]))
                else -> throw IllegalArgumentException("Unknown instruction: $it")
            }
        }

private fun readVarOrNum(string: String): VarOrNum =
    when {
        string.all { it.isLetter() } -> Var(string)
        else -> Num(string.toInt())
    }

fun evaluate(instructions: List<AluInstruction>, input: Queue<Int>, state: State = State()): State =
    instructions.fold(state) { currentState, instruction -> evaluate(instruction, input, currentState) }

fun evaluate(instruction: AluInstruction, input: Queue<Int>, state: State): State =
    when (instruction) {
        is Inp -> state.set(instruction.a, input.poll())
        is Add -> state.update(instruction.a) { it + state[instruction.b] }
        is Mul -> state.update(instruction.a) { it * state[instruction.b] }
        is Div -> state.update(instruction.a) { it / state[instruction.b] }
        is Mod -> state.update(instruction.a) { it % state[instruction.b] }
        is Eql -> state.update(instruction.a) { if (it == state[instruction.b]) 1 else 0 }
    }

fun generateCode(instructions: List<AluInstruction>): String =
    instructions.joinToString(separator = "\n") {
        generateCode(it)
    }

fun generateCode(instruction: AluInstruction): String =
    when (instruction) {
        is Inp -> "${instruction.a.name} = input.poll()"
        is Add -> "${instruction.a.name} += ${asCode(instruction.b)}"
        is Mul -> "${instruction.a.name} *= ${asCode(instruction.b)}"
        is Div -> "${instruction.a.name} /= ${asCode(instruction.b)}"
        is Mod -> "${instruction.a.name} %= ${asCode(instruction.b)}"
        is Eql -> "${instruction.a.name} = if (${instruction.a.name} == ${asCode(instruction.b)}) 1 else 0"
    }

fun asCode(varOrNum: VarOrNum): String =
    when (varOrNum) {
        is Num -> varOrNum.number.toString()
        is Var -> varOrNum.name
    }

fun generateValidSerialNumbers() =
    sequence {
        val w1 = 9
        for (w2 in 1..9) {
            for (w3 in 1..9) {
                val w4 = w3 + 7
                for (w5 in 1..9) {
                    val w6 = w5 - 4
                    val w7 = 1
                    for (w8 in 1..9) {
                        for (w9 in 1..9) {
                            val w10 = w9 + 1
                            val w11 = w8 + 6
                            val w12 = w7 + 8
                            val w13 = w2 - 2
                            val w14 = w1 - 8

                            val digits = listOf(w1, w2, w3, w4, w5, w6, w7, w8, w9, w10, w11, w12, w13, w14)
                            if (digits.all { it in 1..9 }) {
                                yield(digits.joinToString(separator = "").toLong())
                            }
                        }
                    }
                }
            }
        }
    }
