package biz.koziolek.adventofcode.year2021.day3

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val lines = inputFile.bufferedReader().readLines()

    val gammaRate = calculateGammaRate(lines)
    val epsilonRate = calculateEpsilonRate(lines)
    println("Gamma rate: $gammaRate (${gammaRate.toString(2)})")
    println("Epsilon rate: $epsilonRate (${epsilonRate.toString(2)})")
    println("Power consumption: ${calculatePowerConsumption(lines)}")

    println()

    val oxygenGeneratorRating = calculateOxygenGeneratorRating(lines)
    val co2ScrubberRating = calculateCO2ScrubberRating(lines)
    println("Oxygen generator rating: $oxygenGeneratorRating (${oxygenGeneratorRating.toString(2)})")
    println("CO2 scrubber rating: $co2ScrubberRating (${co2ScrubberRating.toString(2)})")
    println("Life support rating: ${calculateLifeSupportRating(lines)}")
}

fun calculatePowerConsumption(lines: List<String>): Int {
    val epsilonRate = calculateEpsilonRate(lines)
    val gammaRate = calculateGammaRate(lines)
    return epsilonRate * gammaRate
}

fun calculateGammaRate(lines: List<String>): Int {
    val mostCommonBits = getMostCommonBits(lines)
    return mostCommonBits.toInt(2)
}

fun calculateEpsilonRate(lines: List<String>): Int {
    val leastCommonBits = getLeastCommonBits(lines)
    return leastCommonBits.toInt(2)
}

fun calculateLifeSupportRating(lines: List<String>): Int {
    val oxygenGeneratorRating = calculateOxygenGeneratorRating(lines)
    val co2ScrubberRating = calculateCO2ScrubberRating(lines)
    return oxygenGeneratorRating * co2ScrubberRating
}

fun calculateOxygenGeneratorRating(lines: List<String>): Int {
    val theLine = findLine(lines, ::getMostCommonBits)
    return theLine.toInt(2)
}

fun calculateCO2ScrubberRating(lines: List<String>): Int {
    val theLine = findLine(lines, ::getLeastCommonBits)
    return theLine.toInt(2)
}

private fun findLine(lines: List<String>, bitCriteria: (List<String>) -> String): String {
    var currentIndex = 0
    var currentLines = lines
    val bitCount = getBitCount(lines)

    while (currentLines.size > 1) {
        check(currentIndex < bitCount) { "No more bits to check" }

        val mostCommonBits = bitCriteria(currentLines)
        currentLines = currentLines.filter { it[currentIndex] == mostCommonBits[currentIndex] }
        currentIndex++
    }

    check(currentLines.isNotEmpty()) { "No lines left" }

    return currentLines[0]
}

private fun getMostCommonBits(lines: List<String>): String {
    val bitCount = getBitCount(lines)
    val initialAcc = MutableList(bitCount) { 0 }
    val oneCounts = lines
            .fold(initialAcc) { acc, line ->
                for ((i, c) in line.withIndex()) {
                    if (c == '1') {
                        acc[i] = acc[i].inc()
                    }
                }
                acc
            }
    val totalCount = lines.size

    return oneCounts.fold("") { acc, oneCount ->
        val zeroCount = totalCount - oneCount
        acc + if (oneCount >= zeroCount) "1" else "0"
    }
}

private fun getLeastCommonBits(lines: List<String>): String {
    val mostCommonBits = getMostCommonBits(lines)
    return mostCommonBits.fold("") { acc, c -> acc + if (c == '1') "0" else "1" }
}

private fun getBitCount(lines: List<String>) = lines[0].length
