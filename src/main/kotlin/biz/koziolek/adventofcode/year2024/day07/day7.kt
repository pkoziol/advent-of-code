package biz.koziolek.adventofcode.year2024.day07

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val equations = parseCalibrationEquations(inputFile.bufferedReader().readLines())
    val validEquations = findValidEquations(equations)
    println("Sum of valid equations: ${validEquations.sumOf { it.value }}")
}

data class Equation(val value: Long, val factors: List<Long>) {
    fun evaluate(operators: List<Char>): Long {
        require(operators.size == factors.size - 1) { "Number of operators must be one less than number of factors" }
        return factors.drop(1).zip(operators).fold(factors.first()) { acc, (factor, operator) ->
            when (operator) {
                '+' -> acc + factor
                '*' -> acc * factor
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

fun findValidEquations(equations: List<Equation>): List<Equation> =
    equations.filter { equation ->
        val allOperators = generateAllOperators(equation.factors.size - 1)
        allOperators.any { operators -> equation.evaluate(operators) == equation.value }
    }

fun generateAllOperators(n: Int): List<List<Char>> =
    if (n == 1) {
        listOf(listOf('+'), listOf('*'))
    } else {
        generateAllOperators(n - 1).flatMap { operators ->
            listOf('+', '*').map { operators + it }
        }
    }
