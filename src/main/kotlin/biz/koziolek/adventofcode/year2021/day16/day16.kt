package biz.koziolek.adventofcode.year2021.day16

import biz.koziolek.adventofcode.*
import java.util.*

fun main() {
    val inputFile = findInput(object {})
    val line = inputFile.bufferedReader().readLine()

    val packet = parseBitsPacket(line)
    println("Sum: ${sumAllVersions(packet)}")
}

sealed interface Packet {
    val version: Int
    val type: Int
}

data class LiteralValuePacket(
    override val version: Int,
    override val type: Int,
    val value: Int
) : Packet

data class OperatorPacket(
    override val version: Int,
    override val type: Int,
    val children: List<Packet>
) : Packet

fun parseBitsPacket(hexString: String): Packet {
    val bitSet = hexString.hexStringToBitSet()
    val parser = BitsParser(bitSet)
    return parser.readNextPacket()
}

private class BitsParser(private val bitSet: BitSet) {

    private var index = 0

    fun readNextPacket(): Packet {
        val version = readVersion()
        val type = readType()

        return when (type) {
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

    private fun readLiteralValue(): Int {
        var isNotLastGroup = true
        var value = 0

        while (isNotLastGroup) {
            isNotLastGroup = bitSet.get(index)
            index += 1

            val groupValue = readRawInt(4)
            value = value * 16 + groupValue
        }

        return value
    }

    private fun readOperatorPacket(version: Int, type: Int): Packet {
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
        
        return OperatorPacket(version, type, children)
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
