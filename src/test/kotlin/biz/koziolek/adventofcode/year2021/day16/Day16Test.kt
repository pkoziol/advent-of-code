package biz.koziolek.adventofcode.year2021.day16

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test

internal class Day16Test {

    @Test
    fun testParseLiteralValuePacket() {
        val packet = parseBitsPacket("D2FE28")
        println(packet.toString(evaluate = false))

        val literalValuePacket = assertInstanceOf(LiteralValuePacket::class.java, packet)
        assertEquals(6, literalValuePacket.version)
        assertEquals(4, literalValuePacket.type)
        assertEquals(2021, literalValuePacket.value)
    }

    @Test
    fun testParseOperatorPacketEncodedAsTotalLength() {
        val packet = parseBitsPacket("38006F45291200")
        println(packet.toString(evaluate = false))

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
        println(packet.toString(evaluate = false))

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
        println(packet.toString(evaluate = false))

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
        println(packet.toString(evaluate = false))

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
        println(packet.toString(evaluate = false))

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
        println(packet.toString(evaluate = false))

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
        println(packet.toString(evaluate = false))

        assertEquals(955, sumAllVersions(packet))
    }

    @Test
    fun testEvaluateSum() {
        val packet = parseBitsPacket("C200B40A82")
        println(packet.toString(evaluate = true))

        val sumOp = assertInstanceOf(SumOperatorPacket::class.java, packet)
        assertEquals(2, sumOp.children.size)
        assertEquals(3, sumOp.evaluate())
    }

    @Test
    fun testEvaluateProduct() {
        val packet = parseBitsPacket("04005AC33890")
        println(packet.toString(evaluate = true))

        val productOp = assertInstanceOf(ProductOperatorPacket::class.java, packet)
        assertEquals(2, productOp.children.size)
        assertEquals(54, productOp.evaluate())
    }

    @Test
    fun testEvaluateMinimum() {
        val packet = parseBitsPacket("880086C3E88112")
        println(packet.toString(evaluate = true))

        val minOp = assertInstanceOf(MinimumOperatorPacket::class.java, packet)
        assertEquals(3, minOp.children.size)
        assertEquals(7, minOp.evaluate())
    }

    @Test
    fun testEvaluateMaximum() {
        val packet = parseBitsPacket("CE00C43D881120")
        println(packet.toString(evaluate = true))

        val maxOp = assertInstanceOf(MaximumOperatorPacket::class.java, packet)
        assertEquals(3, maxOp.children.size)
        assertEquals(9, maxOp.evaluate())
    }

    @Test
    fun testEvaluateLessThan() {
        val packet = parseBitsPacket("D8005AC2A8F0")
        println(packet.toString(evaluate = true))

        val lessThanOp = assertInstanceOf(LessThanOperatorPacket::class.java, packet)
        assertEquals(2, lessThanOp.children.size)
        assertEquals(1, lessThanOp.evaluate())
    }

    @Test
    fun testEvaluateGreaterThan() {
        val packet = parseBitsPacket("F600BC2D8F")
        println(packet.toString(evaluate = true))

        val greaterThanOp = assertInstanceOf(GreaterThanOperatorPacket::class.java, packet)
        assertEquals(2, greaterThanOp.children.size)
        assertEquals(0, greaterThanOp.evaluate())
    }

    @Test
    fun testEvaluateEqualTo() {
        val packet = parseBitsPacket("9C005AC2F8F0")
        println(packet.toString(evaluate = true))

        val equalToOp = assertInstanceOf(EqualToOperatorPacket::class.java, packet)
        assertEquals(2, equalToOp.children.size)
        assertEquals(0, equalToOp.evaluate())
    }

    @Test
    fun testEvaluateSumEqualToProduct() {
        val packet = parseBitsPacket("9C0141080250320F1802104A08")
        println(packet.toString(evaluate = true))

        val equalToOp = assertInstanceOf(EqualToOperatorPacket::class.java, packet)
        assertEquals(2, equalToOp.children.size)
        assertEquals(1, equalToOp.evaluate())
    }

    @Test
    fun testAnswer2() {
        val fullInput = findInput(object {}).readLines()
        val packet = parseBitsPacket(fullInput[0])
        println(packet.toString(evaluate = true))

        assertEquals(158135423448, packet.evaluate())
    }
}
