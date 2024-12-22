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

    @Test
    fun testParseInitialSecrets() {
        val initialSecrets = parseInitialSecrets(sampleInput)
        assertEquals(listOf(1L, 10L, 100L, 2024L), initialSecrets)
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
                15887950L,
                16495136L,
                527345L,
                704524L,
                1553684L,
                12683156L,
                11100544L,
                12249484L,
                7753432L,
                5908254L,
            ),
            (1..10).map { getSecretNumber(123, n = it) }
        )
    }

    @Test
    fun testSampleAnswer1() {
        val initialSecrets = parseInitialSecrets(sampleInput)
        val twoThousandths = getSecretNumbers(initialSecrets, n = 2000)
        assertEquals(listOf(8685429L, 4700978L, 15273692L, 8667524L), twoThousandths)
        assertEquals(37327623L, twoThousandths.sum())
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val initialSecrets = parseInitialSecrets(input)
        val twoThousandths = getSecretNumbers(initialSecrets, n = 2000)
        assertEquals(20411980517, twoThousandths.sum())
    }
}
