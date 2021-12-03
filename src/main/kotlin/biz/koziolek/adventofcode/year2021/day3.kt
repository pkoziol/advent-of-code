package biz.koziolek.adventofcode.year2021

import java.io.File

fun main() {
    val inputFile = File("src/main/resources/year2021/day3/input")
    val lines = inputFile.bufferedReader().readLines()
    val gammaRate = calculateGammaRate(lines)
    val epsilonRate = calculateEpsilonRate(lines)
    println("Gamma rate: $gammaRate (${gammaRate.toString(2)})")
    println("Epsilon rate: $epsilonRate (${epsilonRate.toString(2)})")
    println("Power consumption: ${calculatePowerConsumption(lines)}")
}

fun calculateGammaRate(lines: List<String>): Int {
    val mostCommonBits = getMostCommonBits(lines)
    return mostCommonBits.toInt(2)
}

fun calculateEpsilonRate(lines: List<String>): Int {
    val leastCommonBits = getLeastCommonBits(lines)
    return leastCommonBits.toInt(2)
}

fun calculatePowerConsumption(lines: List<String>): Int {
    val epsilonRate = calculateEpsilonRate(lines)
    val gammaRate = calculateGammaRate(lines)
    return epsilonRate * gammaRate
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
    val bitCount = getBitCount(lines)
    val mask = "1".repeat(bitCount).toInt(2)
    return mostCommonBits.toInt(2).xor(mask).toString(2)
}

private fun getBitCount(lines: List<String>) = lines[0].length
