package biz.koziolek.adventofcode.year2022.day03

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})

    val rucksacks = parseRucksacks(inputFile.bufferedReader().readLines())
    println("Sum of shared item priorities: ${getSumOfSharedItemPriorities(rucksacks)}")

    val groups = groupRucksacks(rucksacks)
    println("Sum of group badge priorities: ${getSumOfGroupBadges(groups)}")
}

data class Rucksack(val contents: String) {
    val compartment1: String
        get() = contents.substring(0, contents.length / 2)

    val compartment2: String
        get() = contents.substring(contents.length / 2)

    fun findSharedItem(): Char =
        compartment1.toSet().intersect(compartment2.toSet()).single()
}

data class ElfGroup(val rucksacks: List<Rucksack>) {
    fun findBadge(): Char =
        rucksacks
            .fold(rucksacks.first().contents.toSet()) { acc, rucksack ->
                acc.intersect(rucksack.contents.toSet())
            }
            .single()
}

fun parseRucksacks(lines: Iterable<String>): List<Rucksack> =
    lines.map { line -> Rucksack(line) }

fun groupRucksacks(rucksacks: List<Rucksack>, size: Int = 3): List<ElfGroup> =
    rucksacks.chunked(size).map { ElfGroup(it) }

fun getItemPriority(item: Char): Int =
    when (item) {
        in 'a'..'z' -> item - 'a' + 1
        in 'A'..'Z' -> item - 'A' + 27
        else -> throw IllegalArgumentException("Unsupported item: '$item'")
    }

fun getSumOfSharedItemPriorities(rucksacks: Iterable<Rucksack>): Int =
    rucksacks
        .map { it.findSharedItem() }
        .sumOf { getItemPriority(it) }

fun getSumOfGroupBadges(groups: List<ElfGroup>): Int =
    groups
        .map { it.findBadge() }
        .sumOf { getItemPriority(it) }
