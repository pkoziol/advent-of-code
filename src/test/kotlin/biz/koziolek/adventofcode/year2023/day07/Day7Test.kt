package biz.koziolek.adventofcode.year2023.day07

import biz.koziolek.adventofcode.findInput
import biz.koziolek.adventofcode.year2023.day07.CamelCard.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2023")
internal class Day7Test {

    private val sampleInput = """
            32T3K 765
            T55J5 684
            KK677 28
            KTJJT 220
            QQQJA 483
        """.trimIndent().split("\n")

    @Test
    fun testParseCamelBids() {
        val bids = parseCamelBids(sampleInput)
        assertEquals(
            listOf(
                CamelBid(
                    hand = CamelHand(cards = listOf(THREE, TWO, TEN, THREE, KING)),
                    bid = 765,
                ),
                CamelBid(
                    hand = CamelHand(cards = listOf(TEN, FIVE, FIVE, JACK, FIVE)),
                    bid = 684,
                ),
                CamelBid(
                    hand = CamelHand(cards = listOf(KING, KING, SIX, SEVEN, SEVEN)),
                    bid = 28,
                ),
                CamelBid(
                    hand = CamelHand(cards = listOf(KING, TEN, JACK, JACK, TEN)),
                    bid = 220,
                ),
                CamelBid(
                    hand = CamelHand(cards = listOf(QUEEN, QUEEN, QUEEN, JACK, ACE)),
                    bid = 483,
                ),
            ),
            bids
        )
    }

    @Test
    fun testType() {
        assertEquals(CamelHandType.FIVE_OF_A_KIND, CamelHand.fromString("AAAAA").type)
        assertEquals(CamelHandType.FOUR_OF_A_KIND, CamelHand.fromString("AA8AA").type)
        assertEquals(CamelHandType.FULL_HOUSE, CamelHand.fromString("23332").type)
        assertEquals(CamelHandType.THREE_OF_A_KIND, CamelHand.fromString("TTT98").type)
        assertEquals(CamelHandType.TWO_PAIR, CamelHand.fromString("23432").type)
        assertEquals(CamelHandType.ONE_PAIR, CamelHand.fromString("A23A4").type)
        assertEquals(CamelHandType.HIGH_CARD, CamelHand.fromString("23456").type)
    }

    @Test
    fun testOrdering() {
        val hands = listOf(
            CamelHand.fromString("23456"),
            CamelHand.fromString("A23A4"),
            CamelHand.fromString("23432"),
            CamelHand.fromString("TTT98"),
            CamelHand.fromString("23332"),
            CamelHand.fromString("AA8AA"),
            CamelHand.fromString("AAAAA"),
        )
        assertEquals(hands, hands.shuffled().sorted())
        assertTrue(CamelHand.fromString("33332") > CamelHand.fromString("2AAAA"))
        assertTrue(CamelHand.fromString("77888") > CamelHand.fromString("77788"))
        assertTrue(CamelHand.fromString("KK677") > CamelHand.fromString("KTJJT"))
    }

    @Test
    fun testSampleAnswer1() {
        val bids = parseCamelBids(sampleInput)
        println(bids.sortedBy { it.hand })
        assertEquals(6440, getTotalWinnings(bids))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val bids = parseCamelBids(input)
        assertEquals(251058093, getTotalWinnings(bids))
    }
}
