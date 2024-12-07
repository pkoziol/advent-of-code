package biz.koziolek.adventofcode.year2024.day07

import biz.koziolek.adventofcode.findInput
import kotlin.math.pow

fun main() {
    val inputFile = findInput(object {})
    val equations = parseCalibrationEquations(inputFile.bufferedReader().readLines())
    println("Sum of valid equations: ${findValidEquations(equations, operators = listOf('+', '*')).sumOf { it.value }}")
    println("Sum of valid equations 2: ${findValidEquations(equations, operators = listOf('+', '*', '|')).sumOf { it.value }}")
}

data class Equation(val value: Long, val factors: List<Long>) {
    fun evaluate(operators: List<Char>): Long {
        require(operators.size == factors.size - 1) { "Number of operators must be one less than number of factors" }
        return factors.drop(1).zip(operators).fold(factors.first()) { acc, (factor, operator) ->
            when (operator) {
                '+' -> acc + factor
                '*' -> acc * factor
                '|' -> acc * 10.0.pow(factor.toString().length).toLong() + factor
                else -> error("Unknown operator: $operator")
            }
        }
    }
}

fun parseCalibrationEquations(lines: Iterable<String>): List<Equation> =
    lines.map { line ->
        val (value, factors) = line.split(": ")
        Equation(
            value = value.toLong(),
            factors = factors.split(" ").map { it.toLong() }
        )
    }

fun findValidEquations(equations: List<Equation>, operators: List<Char>): List<Equation> =
    equations.filter { equation ->
        val allOperators = generateAllOperators(equation.factors.size - 1, operators)
        allOperators.any { operators -> equation.evaluate(operators) == equation.value }
    }

fun generateAllOperators(n: Int, availableOperators: List<Char>): List<List<Char>> =
    if (n == 1) {
        availableOperators.map { listOf(it) }
    } else {
        generateAllOperators(n - 1, availableOperators).flatMap { operators ->
            availableOperators.map { operators + it }
        }
    }
