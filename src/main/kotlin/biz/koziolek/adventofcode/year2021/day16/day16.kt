package biz.koziolek.adventofcode.year2021.day16

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})
    val line = inputFile.bufferedReader().readLine()

    val packet = parseBitsPacket(line)
    println("Version numbers sum: ${sumAllVersions(packet)}")
    println("Packet value: ${packet.evaluate()}")
}

sealed interface Packet {
    val version: Int
    val type: Int
    fun evaluate(): Long
}

data class LiteralValuePacket(
    override val version: Int,
    override val type: Int,
    val value: Long
) : Packet {
    override fun evaluate() = value
}

sealed interface OperatorPacket : Packet {
    val children: List<Packet>
}

data class SumOperatorPacket(
    override val version: Int,
    override val type: Int,
    override val children: List<Packet>
) : OperatorPacket {
    override fun evaluate() = children.sumOf { it.evaluate() }
}

data class ProductOperatorPacket(
    override val version: Int,
    override val type: Int,
    override val children: List<Packet>
) : OperatorPacket {
    override fun evaluate() = children.fold(1L) { product, packet -> product * packet.evaluate() }
}

data class MinimumOperatorPacket(
    override val version: Int,
    override val type: Int,
    override val children: List<Packet>
) : OperatorPacket {
    override fun evaluate() = children.minOf { it.evaluate() }
}

data class MaximumOperatorPacket(
    override val version: Int,
    override val type: Int,
    override val children: List<Packet>
) : OperatorPacket {
    override fun evaluate() = children.maxOf { it.evaluate() }
}

data class GreaterThanOperatorPacket(
    override val version: Int,
    override val type: Int,
    override val children: List<Packet>
) : OperatorPacket {
    override fun evaluate() = if (children[0].evaluate() > children[1].evaluate()) 1L else 0L
}

data class LessThanOperatorPacket(
    override val version: Int,
    override val type: Int,
    override val children: List<Packet>
) : OperatorPacket {
    override fun evaluate() = if (children[0].evaluate() < children[1].evaluate()) 1L else 0L
}

data class EqualToOperatorPacket(
    override val version: Int,
    override val type: Int,
    override val children: List<Packet>
) : OperatorPacket {
    override fun evaluate() = if (children[0].evaluate() == children[1].evaluate()) 1L else 0L
}

fun parseBitsPacket(hexString: String): Packet {
    return BitsParser(hexString).readNextPacket()
}

private class BitsParser(hexString: String) {

    private val bitSet = hexString.hexStringToBitSet()
    private var index = 0

    fun readNextPacket(): Packet {
        val version = readVersion()

        return when (val type = readType()) {
            4 -> readLiteralValuePacket(version, type)
            else -> readOperatorPacket(version, type)
        }
    }

    private fun readVersion(): Int {
        return readRawInt(3)
    }

    private fun readType(): Int {
        return readRawInt(3)
    }

    private fun readLiteralValuePacket(version: Int, type: Int): LiteralValuePacket {
        val value = readLiteralValue()
        return LiteralValuePacket(version, type, value)
    }

    private fun readLiteralValue(): Long {
        var isNotLastGroup = true
        var value = 0L

        while (isNotLastGroup) {
            isNotLastGroup = (readRawInt(1) == 1)
            val groupValue = readRawInt(4)
            value = value * 16 + groupValue
        }

        return value
    }

    private fun readOperatorPacket(version: Int, type: Int): Packet {
        val children = readChildren()

        return when (type) {
            0 -> SumOperatorPacket(version, type, children)
            1 -> ProductOperatorPacket(version, type, children)
            2 -> MinimumOperatorPacket(version, type, children)
            3 -> MaximumOperatorPacket(version, type, children)
            5 -> GreaterThanOperatorPacket(version, type, children)
            6 -> LessThanOperatorPacket(version, type, children)
            7 -> EqualToOperatorPacket(version, type, children)
            else -> throw IllegalArgumentException("Unknown type: $type")
        }
    }

    private fun readChildren(): List<Packet> {
        val lengthTypeId = readRawInt(1)
        val children = mutableListOf<Packet>()

        when (lengthTypeId) {
            0 -> {
                val totalLength = readRawInt(15)
                val childrenEndIndex = index + totalLength

                while (index < childrenEndIndex) {
                    children.add(readNextPacket())
                }
            }
            1 -> {
                val numberOfSubPackets = readRawInt(11)

                for (i in 1..numberOfSubPackets) {
                    children.add(readNextPacket())
                }
            }
            else -> throw IllegalArgumentException("Unknown length type ID: $lengthTypeId")
        }

        return children
    }

    private fun readRawInt(bits: Int): Int {
        val int = bitSet.get(index, index + bits).toBinaryString(bits).toInt(2)
        index += bits
        return int
    }
}

fun sumAllVersions(packet: Packet): Int =
    packet.version + when (packet) {
        is LiteralValuePacket -> 0
        is OperatorPacket -> packet.children.sumOf { sumAllVersions(it) }
    }
