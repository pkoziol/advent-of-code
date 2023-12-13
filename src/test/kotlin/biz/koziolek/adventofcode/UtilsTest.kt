package biz.koziolek.adventofcode

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.util.*

internal class UtilsTest {

    @Test
    fun testConvertHexStringToBitSet() {
        assertEquals("110100101111111000101", "D2FE28".hexStringToBitSet().toBinaryString())
        assertEquals("110100101111111000101000", "D2FE28".hexStringToBitSet().toBinaryString(24))

        assertEquals("00111000000000000110111101000101001010010001001", "38006F45291200".hexStringToBitSet().toBinaryString())
        assertEquals("00111000000000000110111101000101001010010001001000000000", "38006F45291200".hexStringToBitSet().toBinaryString(56))

        assertEquals("111011100000000011010100000011001000001000110000011", "EE00D40C823060".hexStringToBitSet().toBinaryString())
        assertEquals("11101110000000001101010000001100100000100011000001100000", "EE00D40C823060".hexStringToBitSet().toBinaryString(56))
    }

    @Test
    fun testBitSetToIntOrLong() {
        val bitSet = BitSet()
        bitSet.set(1)
        bitSet.set(2)
        bitSet.set(5)
        bitSet.set(8)
        bitSet.set(13)
        bitSet.set(21)
        bitSet.set(35)

        assertEquals("011001001000010000000100000000000001", bitSet.toBinaryString())
        assertEquals("01100100", bitSet.toBinaryString(8))
        assertEquals(4 + 32 + 64, bitSet.toInt(8))
        assertEquals(4 + 32 + 64L, bitSet.toLong(8))
        assertEquals(1686373376, bitSet.toInt(32))
        assertEquals(26981974017, bitSet.toLong())

        val cutBitSet = bitSet.get(2, 9)
        assertEquals("1001001", cutBitSet.toBinaryString(7))
        assertEquals(1 + 8 + 64, cutBitSet.toInt(7))
        assertEquals(1 + 8 + 64, cutBitSet.toLong(7))
        assertEquals(1 + 8 + 64, bitSet.toInt(2, 9))
        assertEquals(1 + 8 + 64, bitSet.toLong(2, 9))
        assertEquals(26981974017, bitSet.toLong(0, 36))
        assertEquals(26981974017, bitSet.toLong(1, 36))
        assertEquals(53963948034, bitSet.toLong(1, 37))
    }

    @Test
    fun transposeStringList() {
        assertEquals(
            listOf(
                "adgj",
                "behk",
                "cfil",
            ),
            listOf(
                "abc",
                "def",
                "ghi",
                "jkl",
            ).transpose()
        )
    }

    @Test
    fun testCharSwap() {
        assertEquals('b', 'a'.swap('a', 'b'))
        assertEquals('a', 'b'.swap('a', 'b'))
    }

    @Test
    fun testSingleOrNullOnlyWhenZero() {
        assertEquals(null, emptyList<Int>().singleOrNullOnlyWhenZero())
        assertEquals(1, listOf(1).singleOrNullOnlyWhenZero())
        assertThrows(IllegalArgumentException::class.java) {
            listOf(3, 4).singleOrNullOnlyWhenZero()
        }

        assertEquals(null, emptySet<Int>().singleOrNullOnlyWhenZero())
        assertEquals(2, setOf(2).singleOrNullOnlyWhenZero())
        assertThrows(IllegalArgumentException::class.java) {
            setOf(3, 4).singleOrNullOnlyWhenZero()
        }
    }
}
