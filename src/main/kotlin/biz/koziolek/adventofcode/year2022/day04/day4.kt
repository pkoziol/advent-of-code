package biz.koziolek.adventofcode.year2022.day04

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val assignments = parseAssignments(inputFile.bufferedReader().readLines())
    println("Fully contained assignment pairs: ${assignments.count { it.isAnyFullyContainedInOther() }}")
    println("Overlapping assignment pairs: ${assignments.count { it.areOverlapping() }}")
}

data class Assignment(val beginning: Int, val end: Int)

data class AssignmentPair(val first: Assignment, val second: Assignment) {

    fun isAnyFullyContainedInOther(): Boolean =
        (first.beginning >= second.beginning && first.end <= second.end)
                || (second.beginning >= first.beginning && second.end <= first.end)

    fun areOverlapping(): Boolean =
        (first.beginning <= second.end && first.end >= second.beginning)
}

fun parseAssignments(lines: Iterable<String>): List<AssignmentPair> =
    lines.map { line ->
        val parts = line.split(',', '-')
        AssignmentPair(
            first = Assignment(
                beginning = parts[0].toInt(),
                end = parts[1].toInt(),
            ),
            second = Assignment(
                beginning = parts[2].toInt(),
                end = parts[3].toInt(),
            )
        )
    }
