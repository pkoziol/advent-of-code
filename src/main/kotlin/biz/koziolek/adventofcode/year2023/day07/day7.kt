package biz.koziolek.adventofcode.year2023.day07

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val bids = parseCamelBids(inputFile.bufferedReader().readLines())
    println("Total winnings are: ${getTotalWinnings(bids)}")
}

data class CamelBid(val hand: CamelHand, val bid: Int)

data class CamelHand(var cards: List<CamelCard>) : Comparable<CamelHand> {
    val type = CamelHandType.fromCards(cards)

    companion object {
        fun fromString(str: String): CamelHand =
            CamelHand(cards = str.map { CamelCard.fromChar(it) })
    }

    override fun compareTo(other: CamelHand): Int {
        val typeComparison = this.type.compareTo(other.type)
        if (typeComparison != 0) {
            return -typeComparison
        }

        for ((thisCard, otherCard) in cards.zip(other.cards)) {
            val cardComparison = thisCard.compareTo(otherCard)
            if (cardComparison != 0) {
                return cardComparison
            }
        }

        return 0
    }
}

enum class CamelCard(val symbol: Char) {
    TWO('2'),
    THREE('3'),
    FOUR('4'),
    FIVE('5'),
    SIX('6'),
    SEVEN('7'),
    EIGHT('8'),
    NINE('9'),
    TEN('T'),
    JACK('J'),
    QUEEN('Q'),
    KING('K'),
    ACE('A');

    companion object {
        fun fromChar(symbol: Char): CamelCard =
            entries.single { it.symbol == symbol }
    }
}

enum class CamelHandType(private val expectedCounts: List<Int>) {
    FIVE_OF_A_KIND(listOf(5)),
    FOUR_OF_A_KIND(listOf(4)),
    FULL_HOUSE(listOf(3, 2)),
    THREE_OF_A_KIND(listOf(3)),
    TWO_PAIR(listOf(2, 2)),
    ONE_PAIR(listOf(2)),
    HIGH_CARD(listOf(1, 1, 1, 1, 1));

    companion object {
        fun fromCards(cards: List<CamelCard>): CamelHandType =
            entries.filter { it.matches(cards) }.minOf { it }
    }

    fun matches(cards: List<CamelCard>): Boolean {
        if (cards.size != 5) {
            throw IllegalArgumentException("Got ${cards.size} instead of 5")
        }

        val countCounts = cards
            .groupingBy { it }
            .eachCount()
            .values
            .groupingBy { it }
            .eachCount()

        val expected = expectedCounts
            .groupingBy { it }
            .eachCount()

        return expected.all { countCounts[it.key] == it.value }
    }
}

fun parseCamelBids(lines: Iterable<String>): List<CamelBid> =
    lines.map { line ->
        val (cardsStr, bidValue) = line.split(" ")
        CamelBid(
            hand = CamelHand(
                cards = cardsStr.map { CamelCard.fromChar(it) },
            ),
            bid = bidValue.toInt(),
        )
    }

fun getTotalWinnings(bids: List<CamelBid>): Int =
    bids
        .sortedBy { it.hand }
        .foldIndexed(0) { index, acc, bid ->
            acc + bid.bid * (index + 1)
        }
