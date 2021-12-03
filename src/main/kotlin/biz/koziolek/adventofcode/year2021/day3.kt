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
    val bitCount = lines[0].length
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
    
    var mostCommonBits = ""

    for (oneCount in oneCounts) {
        val zeroCount = totalCount - oneCount
        mostCommonBits += when {
            oneCount > zeroCount -> "1"
            oneCount < zeroCount -> "0" 
            else -> throw RuntimeException("One count is the same as zero count")
        }
    }
    
    return mostCommonBits.toInt(2)
}


fun calculateEpsilonRate(lines: List<String>): Int {
    val gammaRate = calculateGammaRate(lines)
    val bitCount = lines[0].length
    val mask = "1".repeat(bitCount).toInt(2)
    return gammaRate xor mask
}

fun calculatePowerConsumption(lines: List<String>): Int {
    val epsilonRate = calculateEpsilonRate(lines)
    val gammaRate = calculateGammaRate(lines)
    return epsilonRate * gammaRate
}
