package biz.koziolek.adventofcode.year2022.day01

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val elves = parseElvesCalories(inputFile.bufferedReader().readLines())
    println("Elf with most calories has: ${findElfWithMostCalories(elves)?.calories?.sum()}")
}

data class Elf(val calories: List<Int>)

fun parseElvesCalories(lines: Iterable<String>): List<Elf> {
    val elves = mutableListOf<Elf>()
    var elf: Elf? = null

    for (line in lines) {
        if (line.isEmpty()) {
            if (elf != null) {
                elves.add(elf)
            }
            elf = null
        } else {
            if (elf == null) {
                elf = Elf(emptyList())
            }

            elf = elf.copy(calories = elf.calories + line.toInt())
        }
    }

    if (elf != null) {
        elves.add(elf)
    }

    return elves
}

fun findElfWithMostCalories(elves: List<Elf>): Elf? =
    elves.maxByOrNull { it.calories.sum() }
