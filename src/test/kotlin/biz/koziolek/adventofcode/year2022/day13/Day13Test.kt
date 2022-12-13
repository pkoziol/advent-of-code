package biz.koziolek.adventofcode.year2022.day13

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2022")
internal class Day13Test {

    private val sampleInput = """
            [1,1,3,1,1]
            [1,1,5,1,1]

            [[1],[2,3,4]]
            [[1],4]

            [9]
            [[8,7,6]]

            [[4,4],4,4]
            [[4,4],4,4,4]

            [7,7,7,7]
            [7,7,7]

            []
            [3]

            [[[]]]
            [[]]

            [1,[2,[3,[4,[5,6,7]]]],8,9]
            [1,[2,[3,[4,[5,6,0]]]],8,9]
        """.trimIndent().split("\n")

    @Test
    fun testParsePackets() {
        val packetPairs = parsePacketPairs(sampleInput)
        assertEquals(8, packetPairs.size)

        assertEquals(
            ListPacket(
                IntPacket(1),
                IntPacket(1),
                IntPacket(3),
                IntPacket(1),
                IntPacket(1),
            ),
            packetPairs[0].first
        )
        assertEquals(
            ListPacket(
                IntPacket(1),
                IntPacket(1),
                IntPacket(5),
                IntPacket(1),
                IntPacket(1),
            ),
            packetPairs[0].second
        )

        assertEquals(
            ListPacket(
                ListPacket(
                    IntPacket(1),
                ),
                ListPacket(
                    IntPacket(2),
                    IntPacket(3),
                    IntPacket(4),
                ),
            ),
            packetPairs[1].first
        )
        assertEquals(
            ListPacket(
                ListPacket(
                    IntPacket(1),
                ),
                IntPacket(4),
            ),
            packetPairs[1].second
        )

        assertEquals(
            ListPacket(
                ListPacket(
                    ListPacket(),
                ),
            ),
            packetPairs[6].first
        )
        assertEquals(
            ListPacket(
                ListPacket(),
            ),
            packetPairs[6].second
        )
    }

    @Test
    fun testPacketComparison() {
        val packetPairs = parsePacketPairs(sampleInput)
        assertTrue(packetPairs[0].first < packetPairs[0].second)
        assertTrue(packetPairs[1].first < packetPairs[1].second)
        assertTrue(packetPairs[2].first > packetPairs[2].second)
        assertTrue(packetPairs[3].first < packetPairs[3].second)
        assertTrue(packetPairs[4].first > packetPairs[4].second)
        assertTrue(packetPairs[5].first < packetPairs[5].second)
        assertTrue(packetPairs[6].first > packetPairs[6].second)
        assertTrue(packetPairs[7].first > packetPairs[7].second)
    }

    @Test
    fun testGetSumOfPacketPairsInRightOrder() {
        val packetPairs = parsePacketPairs(sampleInput)
        assertEquals(13, getSumOfPacketPairsInRightOrder(packetPairs))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val packetPairs = parsePacketPairs(input)
        assertEquals(6369, getSumOfPacketPairsInRightOrder(packetPairs))
    }

    @Test
    fun testGetDecoderKey() {
        val packetPairs = parsePacketPairs(sampleInput)
        assertEquals(140, getDecoderKey(packetPairs))
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val packetPairs = parsePacketPairs(input)
        assertEquals(25800, getDecoderKey(packetPairs))
    }
}
