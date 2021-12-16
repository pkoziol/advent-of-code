package biz.koziolek.adventofcode

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

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
}
