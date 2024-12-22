package biz.koziolek.adventofcode.year2024.day22

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val initialSecrets = parseInitialSecrets(inputFile.bufferedReader().readLines())

    val twoThousandths = getSecretNumbers(initialSecrets, n = 2000)
    println("Sum of 2000th secret numbers: ${sumSecrets(twoThousandths)}")

    val prices = initialSecrets.map { getPrices(it).take(2001).toList() }
    val bestSeq = findBestSequence(prices)
    val total = sellAll(prices, bestSeq)
    println("Most bananas we can get is $total for sequence $bestSeq")
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

fun getPrices(initialSecret: Int): Sequence<Int> =
    sequence {
        yield(initialSecret % 10)
        generateSecretNumbers(initialSecret).forEach {
            yield(it % 10)
        }
    }

fun sellAll(pricesList: List<List<Int>>, sellSequence: SellSequence): Int =
    pricesList.sumOf { prices ->
        sell(prices, sellSequence) ?: 0
    }

fun sell(prices: List<Int>, sellSequence: SellSequence): Int? {
    var matched = 0

    val changes = prices
        .zipWithNext()
        .map { (prev, current) ->
            (current - prev) to current
        }

    for ((change, price) in changes) {
        if (change == sellSequence[matched]) {
            matched++
            if (matched == 4) {
                return price
            }
        } else if (change == sellSequence[0]) {
            matched = 1
        } else {
            matched = 0
        }
    }

    return null
}

fun findBestSequence(pricesList: List<List<Int>>): SellSequence {
    val totalPerSequence = mutableMapOf<SellSequence, Int>()

    for (prices in pricesList) {
        val changes = prices
            .zipWithNext()
            .map { (prev, current) ->
                (current - prev) to current
            }

        val first3Changes = SellSequence(
            0,
            changes[0].first,
            changes[1].first,
            changes[2].first,
        )

        val heardSequences = mutableSetOf<SellSequence>()
        changes.drop(3).fold(first3Changes) { sellSequence, (change, price) ->
            val nextSellSequence = sellSequence.next(change)
            if (nextSellSequence !in heardSequences) {
                totalPerSequence.compute(nextSellSequence) { _, v ->
                    (v ?: 0) + price
                }
            }
            heardSequences.add(nextSellSequence)
            nextSellSequence
        }
    }

    return totalPerSequence.maxBy { it.value }.key
}

data class SellSequence(val a: Int, val b: Int, val c: Int, val d: Int) {
    fun next(newD: Int) =
        SellSequence(b, c, d, newD)

    operator fun get(index: Int): Int? =
        when (index) {
            0 -> a
            1 -> b
            2 -> c
            3 -> d
            else -> null
        }

    override fun toString(): String =
        "($a, $b, $c, $d)"
}
