package biz.koziolek.adventofcode.year2024.day22

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val initialSecrets = parseInitialSecrets(inputFile.bufferedReader().readLines())
    val twoThousandths = getSecretNumbers(initialSecrets, n = 2000)
    println("Sum of 2000th secret numbers: ${twoThousandths.sum()}")
}

fun parseInitialSecrets(lines: Iterable<String>): List<Long> =
    lines.map { it.toLong() }

fun getSecretNumbers(currentSecrets: List<Long>, n: Int = 1): List<Long> =
    currentSecrets.map { getSecretNumber(it, n) }

fun getSecretNumber(secret: Long, n: Int = 1): Long {
    var newSecret = secret
    var tmp: Long

    repeat(n) {
        tmp = newSecret * 64
        newSecret = mix(newSecret, tmp)
        newSecret = prune(newSecret)

        tmp = newSecret / 32
        newSecret = mix(newSecret, tmp)
        newSecret = prune(newSecret)

        tmp = newSecret * 2048
        newSecret = mix(newSecret, tmp)
        newSecret = prune(newSecret)
    }

    return newSecret
}

fun mix(secret: Long, value: Long): Long =
    value xor secret

fun prune(secret: Long): Long =
    secret % 16777216
