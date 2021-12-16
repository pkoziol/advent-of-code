package biz.koziolek.adventofcode.year2021.day16

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test

internal class Day16Test {

    @Test
    fun testParseLiteralValuePacket() {
        val packet = parseBitsPacket("D2FE28")

        val literalValuePacket = assertInstanceOf(LiteralValuePacket::class.java, packet)
        assertEquals(6, literalValuePacket.version)
        assertEquals(4, literalValuePacket.type)
        assertEquals(2021, literalValuePacket.value)
    }

    @Test
    fun testParseOperatorPacketEncodedAsTotalLength() {
        val packet = parseBitsPacket("38006F45291200")

        val operatorPacket = assertInstanceOf(OperatorPacket::class.java, packet)
        assertEquals(1, operatorPacket.version)
        assertEquals(6, operatorPacket.type)
        assertEquals(2, operatorPacket.children.size)

        val nested0 = assertInstanceOf(LiteralValuePacket::class.java, operatorPacket.children[0])
        assertEquals(6, nested0.version)
        assertEquals(4, nested0.type)
        assertEquals(10, nested0.value)

        val nested1 = assertInstanceOf(LiteralValuePacket::class.java, operatorPacket.children[1])
        assertEquals(2, nested1.version)
        assertEquals(4, nested1.type)
        assertEquals(20, nested1.value)
    }

    @Test
    fun testParseOperatorPacketEncodedAsNumberOfChildren() {
        val packet = parseBitsPacket("EE00D40C823060")

        val operatorPacket = assertInstanceOf(OperatorPacket::class.java, packet)
        assertEquals(7, operatorPacket.version)
        assertEquals(3, operatorPacket.type)
        assertEquals(3, operatorPacket.children.size)

        val nested0 = assertInstanceOf(LiteralValuePacket::class.java, operatorPacket.children[0])
        assertEquals(1, nested0.value)

        val nested1 = assertInstanceOf(LiteralValuePacket::class.java, operatorPacket.children[1])
        assertEquals(2, nested1.value)

        val nested2 = assertInstanceOf(LiteralValuePacket::class.java, operatorPacket.children[2])
        assertEquals(3, nested2.value)
    }

    @Test
    fun testParseMoreExamples1() {
        val packet = parseBitsPacket("8A004A801A8002F478")

        val operatorPacket = assertInstanceOf(OperatorPacket::class.java, packet)
        assertEquals(4, operatorPacket.version)
        assertEquals(1, operatorPacket.children.size)

        val nested0 = assertInstanceOf(OperatorPacket::class.java, operatorPacket.children[0])
        assertEquals(1, nested0.version)
        assertEquals(1, nested0.children.size)

        val nested00 = assertInstanceOf(OperatorPacket::class.java, nested0.children[0])
        assertEquals(5, nested00.version)
        assertEquals(1, nested00.children.size)

        val nested000 = assertInstanceOf(LiteralValuePacket::class.java, nested00.children[0])
        assertEquals(6, nested000.version)

        assertEquals(16, sumAllVersions(packet))
    }

    @Test
    fun testParseMoreExamples2() {
        val packet = parseBitsPacket("620080001611562C8802118E34")

        val operatorPacket = assertInstanceOf(OperatorPacket::class.java, packet)
        assertEquals(3, operatorPacket.version)
        assertEquals(2, operatorPacket.children.size)

        val nested0 = assertInstanceOf(OperatorPacket::class.java, operatorPacket.children[0])
        assertEquals(2, nested0.children.size)

        assertInstanceOf(LiteralValuePacket::class.java, nested0.children[0])
        assertInstanceOf(LiteralValuePacket::class.java, nested0.children[1])

        val nested1 = assertInstanceOf(OperatorPacket::class.java, operatorPacket.children[1])
        assertEquals(2, nested1.children.size)

        assertInstanceOf(LiteralValuePacket::class.java, nested1.children[0])
        assertInstanceOf(LiteralValuePacket::class.java, nested1.children[1])

        assertEquals(12, sumAllVersions(packet))
    }

    @Test
    fun testParseMoreExamples3() {
        val packet = parseBitsPacket("C0015000016115A2E0802F182340")

        val operatorPacket = assertInstanceOf(OperatorPacket::class.java, packet)
        assertEquals(2, operatorPacket.children.size)

        val nested0 = assertInstanceOf(OperatorPacket::class.java, operatorPacket.children[0])
        assertEquals(2, nested0.children.size)

        assertInstanceOf(LiteralValuePacket::class.java, nested0.children[0])
        assertInstanceOf(LiteralValuePacket::class.java, nested0.children[1])

        val nested1 = assertInstanceOf(OperatorPacket::class.java, operatorPacket.children[1])
        assertEquals(2, nested1.children.size)

        assertInstanceOf(LiteralValuePacket::class.java, nested1.children[0])
        assertInstanceOf(LiteralValuePacket::class.java, nested1.children[1])

        assertEquals(23, sumAllVersions(packet))
    }

    @Test
    fun testParseMoreExamples4() {
        val packet = parseBitsPacket("A0016C880162017C3686B18A3D4780")

        val operatorPacket = assertInstanceOf(OperatorPacket::class.java, packet)
        assertEquals(1, operatorPacket.children.size)

        val nested0 = assertInstanceOf(OperatorPacket::class.java, operatorPacket.children[0])
        assertEquals(1, nested0.children.size)

        val nested00 = assertInstanceOf(OperatorPacket::class.java, nested0.children[0])
        assertEquals(5, nested00.children.size)

        assertInstanceOf(LiteralValuePacket::class.java, nested00.children[0])
        assertInstanceOf(LiteralValuePacket::class.java, nested00.children[1])
        assertInstanceOf(LiteralValuePacket::class.java, nested00.children[2])
        assertInstanceOf(LiteralValuePacket::class.java, nested00.children[3])
        assertInstanceOf(LiteralValuePacket::class.java, nested00.children[4])

        assertEquals(31, sumAllVersions(packet))
    }

    @Test
    fun testAnswer1() {
        val fullInput = findInput(object {}).readLines()
        val packet = parseBitsPacket(fullInput[0])
        assertEquals(955, sumAllVersions(packet))
    }
}
