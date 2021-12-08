package biz.koziolek.adventofcode.year2021

import java.io.File

fun main() {
    val inputFile = File("src/main/resources/year2021/day8/input")
    val lines = inputFile.bufferedReader().readLines()

    println("There are ${countEasyDigitsInOutput(lines)} digits 1,4,7,8 in output")
}

data class Note(val patterns: List<String>, val output: List<String>)

fun countEasyDigitsInOutput(lines: List<String>): Int {
    return parseNotes(lines)
            .sumOf { note -> note.output.count { out -> out.length in listOf(2, 3, 4, 7) } }
}

private fun parseNotes(lines: List<String>): List<Note> =
        lines
                .map { it.split('|') }
                .map { Note(
                        patterns = it[0].trim().split(' '),
                        output = it[1].trim().split(' ')
                ) }
