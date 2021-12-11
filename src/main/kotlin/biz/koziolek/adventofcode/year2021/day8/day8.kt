package biz.koziolek.adventofcode.year2021.day8

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val lines = inputFile.bufferedReader().readLines()

    println("There are ${countEasyDigitsInOutput(lines)} digits 1,4,7,8 in output")

    val notes = parseNotes(lines)
    val sum = notes.sumOf { decodeNumber(it.output, solveWireConnections(it)) }
    println("Sum of all output si: $sum")
}

data class Note(val patterns: List<String>, val output: List<String>)

fun countEasyDigitsInOutput(lines: List<String>): Int {
    return parseNotes(lines)
            .sumOf { note -> note.output.count { out -> out.length in listOf(2, 3, 4, 7) } }
}

fun parseNotes(lines: List<String>): List<Note> =
        lines
                .map { it.split('|') }
                .map { Note(
                        patterns = it[0].trim().split(' '),
                        output = it[1].trim().split(' ')
                ) }

fun solveWireConnections(note: Note): Map<Char, Char> {
    val wireMap = HashMap<Char, Char>()
    
    val signalsCF = note.patterns.single { it.length == 2 }.toSet()
    val signalsACF = note.patterns.single { it.length == 3 }.toSet()
    val signalsBCDF = note.patterns.single { it.length == 4 }.toSet()
    val signalsABCDEFG = note.patterns.single { it.length == 7 }.toSet()

    val signals235 = note.patterns.filter { it.length == 5 }.map { it.toSet() }
    val signals069 = note.patterns.filter { it.length == 6 }.map { it.toSet() }

    val signalA = (signalsACF - signalsCF).single()
    wireMap[signalA] = 'a'

    val signalsADG = signals235.flatten()
            .groupBy { it }
            .filter { it.value.size == 3 }
            .keys
    val signalG = (signalsADG - signalsBCDF - signalsACF).single()
    wireMap[signalG] = 'g'

    val signalD = (signalsADG - signalA - signalG).single()
    wireMap[signalD] = 'd'

    val signalB = (signalsBCDF - signalsCF - signalD).single()
    wireMap[signalB] = 'b'

    val signalsABFG = signals069.flatten()
            .groupBy { it }
            .filter { it.value.size == 3 }
            .keys
    val signalF = (signalsABFG - signalA - signalB - signalG).single()
    wireMap[signalF] = 'f'

    val signalE = (signalsABCDEFG - signalA - signalB - signalsCF - signalD - signalG).single()
    wireMap[signalE] = 'e'

    val signalC = (signalsABCDEFG - signalA - signalB - signalD - signalE - signalF - signalG).single()
    wireMap[signalC] = 'c'

    return wireMap
}

fun decodeDigit(signals: String, wireMap: Map<Char, Char>): Int {
    val segments = signals.toSet()
            .map { wireMap[it] }
            .joinToString(separator = "")
            .let { sortString(it) }

    return when (segments) {
        "abcefg" -> 0
        "cf" -> 1
        "acdeg" -> 2
        "acdfg" -> 3
        "bcdf" -> 4
        "abdfg" -> 5
        "abdefg" -> 6
        "acf" -> 7
        "abcdefg" -> 8
        "abcdfg" -> 9
        else -> throw IllegalArgumentException("Unknown segments: $segments")
    }
}

private fun sortString(string: String): String =
        string.toSet().sortedBy { it }.joinToString(separator = "")

fun decodeNumber(strings: List<String>, wireMap: Map<Char, Char>): Int =
        strings
                .map { decodeDigit(it, wireMap) }
                .fold(0) { sum, digit -> 10 * sum + digit }
