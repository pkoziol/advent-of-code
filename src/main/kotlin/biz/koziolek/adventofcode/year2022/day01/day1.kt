package biz.koziolek.adventofcode.year2022.day01

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})

    val elves = parseElvesCalories(inputFile.bufferedReader().readLines())
    println("Elf with most calories has: ${findElfWithMostCalories(elves)?.totalCalories}")

    val top3ElvesWithMostCalories = findTopElvesWithMostCalories(elves, count = 3)
    println("Top 3 elves with most calories have: ${top3ElvesWithMostCalories.sumOf { it.totalCalories }}")
}

data class Elf(val calories: List<Int> = listOf()) {

    val totalCalories = calories.sum()

    fun addCalories(calorie: Int) = copy(calories = calories + calorie)
}

fun parseElvesCalories(lines: Iterable<String>): List<Elf> =
    lines
        .fold(listOf(Elf())) { elves, line ->
            when {
                line.isEmpty() -> elves + Elf()
                else -> elves.dropLast(1) + elves.last().addCalories(line.toInt())
            }
        }
        .filter { it.calories.isNotEmpty() }

fun findElfWithMostCalories(elves: List<Elf>): Elf? =
    elves.maxByOrNull { it.totalCalories }

fun findTopElvesWithMostCalories(elves: List<Elf>, count: Int): List<Elf> =
    elves
        .sortedByDescending { it.totalCalories }
        .take(count)
