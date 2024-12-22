package biz.koziolek.adventofcode.year2024.day22

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val initialSecrets = parseInitialSecrets(inputFile.bufferedReader().readLines())
    val twoThousandths = getSecretNumbers(initialSecrets, n = 2000)
    println("Sum of 2000th secret numbers: ${sumSecrets(twoThousandths)}")
}

fun parseInitialSecrets(lines: Iterable<String>): List<Int> =
    lines.map { it.toInt() }

fun getSecretNumbers(currentSecrets: List<Int>, n: Int = 1): List<Int> =
    currentSecrets.map { getSecretNumber(it, n) }

fun getSecretNumber(currentSecret: Int, n: Int = 1): Int =
    generateSecretNumbers(currentSecret).take(n).last()

fun generateSecretNumbers(initial: Int): Sequence<Int> {
    var newSecret = initial.toLong()
    var tmp: Long

    return generateSequence {
        tmp = newSecret * 64
        newSecret = mix(newSecret, tmp)
        newSecret = prune(newSecret)

        tmp = newSecret / 32
        newSecret = mix(newSecret, tmp)
        newSecret = prune(newSecret)

        tmp = newSecret * 2048
        newSecret = mix(newSecret, tmp)
        newSecret = prune(newSecret)

        newSecret.toInt()
    }
}

fun mix(secret: Long, value: Long): Long =
    value xor secret

fun prune(secret: Long): Long =
    secret % 16777216

fun sumSecrets(secrets: List<Int>): Long =
    secrets.sumOf { it.toLong() }
