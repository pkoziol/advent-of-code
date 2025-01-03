package biz.koziolek.adventofcode.year2024.day22

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2024")
internal class Day22Test {

    private val sampleInput = """
            1
            10
            100
            2024
        """.trimIndent().split("\n")

    private val sampleInput2 = """
            1
            2
            3
            2024
        """.trimIndent().split("\n")

    @Test
    fun testParseInitialSecrets() {
        val initialSecrets = parseInitialSecrets(sampleInput)
        assertEquals(listOf(1, 10, 100, 2024), initialSecrets)
    }

    @Test
    fun testMix() {
        assertEquals(37, mix(secret = 42, value = 15))
    }

    @Test
    fun testPrune() {
        assertEquals(16113920, prune(secret = 100000000))
    }

    @Test
    fun testGetSecretNumber() {
        assertEquals(
            listOf(
                15887950,
                16495136,
                527345,
                704524,
                1553684,
                12683156,
                11100544,
                12249484,
                7753432,
                5908254,
            ),
            generateSecretNumbers(123).take(10).toList()
        )
    }

    @Test
    fun testSampleAnswer1() {
        val initialSecrets = parseInitialSecrets(sampleInput)
        val twoThousandths = getSecretNumbers(initialSecrets, n = 2000)
        assertEquals(listOf(8685429, 4700978, 15273692, 8667524), twoThousandths)
        assertEquals(37327623, sumSecrets(twoThousandths))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val initialSecrets = parseInitialSecrets(input)
        val twoThousandths = getSecretNumbers(initialSecrets, n = 2000)
        assertEquals(20411980517, sumSecrets(twoThousandths))
    }

    @Test
    fun testGetPrices() {
        assertEquals(
            listOf(3, 0, 6, 5, 4, 4, 6, 4, 4, 2),
            getPrices(123).take(10).toList()
        )
    }

    @Test
    fun testSell() {
        val prices = listOf(3, 0, 6, 5, 4, 4, 6, 4, 4, 2)
        assertEquals(6, sell(prices, SellSequence(-1, -1, 0, 2)))
        assertEquals(4, sell(prices, SellSequence(-1, 0, 2, -2)))
        assertEquals(null, sell(prices, SellSequence(6, 0, 2, 0)))
        assertEquals(4, sell(prices, SellSequence(-3, 6, -1, -1)))
        assertEquals(2, sell(prices, SellSequence(2, -2, 0, -2)))
    }

    @Test
    fun testSampleAnswer2Small() {
        val initialSecrets = listOf(123)
        val prices = initialSecrets.map { getPrices(it).take(10).toList() }
        val bestSeq = findBestSequence(prices)
        assertEquals(SellSequence(-1, -1, 0, 2), bestSeq)
        val total = sellAll(prices, bestSeq)
        assertEquals(6, total)
    }

    @Test
    fun testSampleAnswer2Big() {
        val initialSecrets = parseInitialSecrets(sampleInput2)
        val prices = initialSecrets.map { getPrices(it).take(2001).toList() }
        val bestSeq = findBestSequence(prices)
        val total = sellAll(prices, bestSeq)
        assertEquals(
            SellSequence(-2, 1, -1, 3) to 23,
            bestSeq to total
        )
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val initialSecrets = parseInitialSecrets(input)
        val prices = initialSecrets.map { getPrices(it).take(2001).toList() }
        val bestSeq = findBestSequence(prices)
        val total = sellAll(prices, bestSeq)
        assertEquals(2362, total)
    }
}
