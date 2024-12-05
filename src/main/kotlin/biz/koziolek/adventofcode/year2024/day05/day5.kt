package biz.koziolek.adventofcode.year2024.day05

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val (rules, updates) = parseSafetyManual(inputFile.bufferedReader().readLines())
    val correctlyOrdered = findCorrectOrder(rules, updates)
    val middlePageSum = correctlyOrdered.sumOf { it.middlePage }
    println("Middle page sum: $middlePageSum")
}

data class OrderingRule(val page1: Int, val page2: Int)

data class ManualUpdate(val pages: List<Int>) {
    val middlePage: Int =
        if (pages.size % 2 == 1) {
            pages[pages.size / 2]
        } else {
            throw IllegalArgumentException("Even number of pages")
        }

    fun matches(rule: OrderingRule): Boolean {
        val index1 = pages.indexOf(rule.page1)
        val index2 = pages.indexOf(rule.page2)
        return index1 == -1 || index2 == -1 || index1 < index2
    }
}

fun parseSafetyManual(lines: Iterable<String>): Pair<List<OrderingRule>, List<ManualUpdate>> {
    val rules = lines
        .takeWhile { line -> line.isNotBlank() }
        .map { line -> line.split("|") }
        .map { OrderingRule(it[0].toInt(), it[1].toInt()) }

    val updates = lines
        .dropWhile { line -> line.isNotBlank() }
        .drop(1)
        .map { line -> line.split(",").map { it.toInt() } }
        .map { ManualUpdate(it) }

    return rules to updates
}

fun findCorrectOrder(rules: List<OrderingRule>, updates: List<ManualUpdate>): List<ManualUpdate> {
    updates.forEach {
        if (it.pages.size != it.pages.toSet().size) {
            println(it)
        }
    }
    return updates.filter { update -> rules.all { rule -> update.matches(rule) } }
}
