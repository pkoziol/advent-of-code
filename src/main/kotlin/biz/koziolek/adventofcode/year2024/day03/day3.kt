package biz.koziolek.adventofcode.year2024.day03

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})

    val instructions = parseMulInstructions(inputFile.bufferedReader().readLines())
    println("Sum of mul instructions: ${sumMul(instructions)}")

    val instructions2 = parseMulInstructions2(inputFile.bufferedReader().readLines())
    println("Sum of mul instructions 2: ${sumMul(instructions2)}")
}

sealed interface Instruction
data class Mul(val a: Int, val b: Int) : Instruction {
    fun value() = a * b
}
data object Do : Instruction
data object Dont : Instruction

fun parseMulInstructions(lines: Iterable<String>): List<Instruction> =
    lines.flatMap { line ->
        Regex("mul\\(([0-9]+),([0-9]+)\\)")
            .findAll(line)
            .map { Mul(it.groupValues[1].toInt(), it.groupValues[2].toInt()) }
            .toList()
    }

fun parseMulInstructions2(lines: Iterable<String>): List<Instruction> =
    lines.flatMap { line ->
        Regex("mul\\(([0-9]+),([0-9]+)\\)|do\\(\\)|don't\\(\\)")
            .findAll(line)
            .map { match ->
                when (match.groupValues[0]) {
                    "do()" -> Do
                    "don't()" -> Dont
                    else -> Mul(match.groupValues[1].toInt(), match.groupValues[2].toInt())
                }
            }
    }

fun sumMul(mulInstructions: List<Instruction>): Int =
    mulInstructions.fold(true to 0) { (enabled, sum), instruction ->
        when (instruction) {
            is Mul -> enabled to (if (enabled) { sum + instruction.value() } else { sum })
            Do -> true to sum
            Dont -> false to sum
        }
    }.second
