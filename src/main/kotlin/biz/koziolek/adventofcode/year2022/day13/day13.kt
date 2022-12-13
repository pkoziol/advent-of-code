package biz.koziolek.adventofcode.year2022.day13

import biz.koziolek.adventofcode.findInput
import java.util.ArrayDeque
import java.util.Deque

fun main() {
    val inputFile = findInput(object {})
    val packetPairs = parsePacketPairs(inputFile.bufferedReader().readLines())
    println("Sum of packet pairs in right order: ${getSumOfPacketPairsInRightOrder(packetPairs)}")
    println("Decoder key: ${getDecoderKey(packetPairs)}")
}

val DIVIDER_PACKETS = listOf(
    parseListPacket("[[2]]"),
    parseListPacket("[[6]]"),
)

sealed interface Packet : Comparable<Packet>

data class IntPacket(val value: Int) : Packet {
    override fun compareTo(other: Packet): Int =
        when (other) {
            is IntPacket -> this.value.compareTo(other.value)
            is ListPacket -> ListPacket(this).compareTo(other)
        }

    override fun toString() = value.toString()
}

data class ListPacket(val children: List<Packet>) : Packet {
    constructor(vararg ch: Packet) : this(ch.asList())

    fun addChild(child: Packet): ListPacket =
        copy(children = children + child)

    override fun compareTo(other: Packet): Int =
        when (other) {
            is IntPacket -> this.compareTo(ListPacket(other))
            is ListPacket -> {
                val childComparison = this.children.zip(other.children)
                    .fold(0) { acc, (first, second) ->
                        if (acc != 0) {
                            acc
                        } else {
                            first.compareTo(second)
                        } 
                    }

                if (childComparison != 0) {
                    childComparison
                } else {
                    this.children.size.compareTo(other.children.size)
                }
            }
        }

    override fun toString() = children.joinToString(",", "[", "]")
}

fun parsePacketPairs(lines: Iterable<String>): List<Pair<ListPacket, ListPacket>> =
    lines.filter { it.isNotBlank() }
        .chunked(2)
        .map { (firstLine, secondLine) ->
            parseListPacket(firstLine) to parseListPacket(secondLine)
        }

fun parseListPacket(line: String): ListPacket {
    val stack: Deque<ListPacket> = ArrayDeque()
    var isParsingNumber = false
    var number = 0

    fun pushChildUp() {
        if (stack.size > 1) {
            val inner = stack.pop()
            val outer = stack.pop()
            stack.push(outer.addChild(inner))
        }
    }

    fun pushNumber() {
        if (isParsingNumber) {
            val list = stack.pop()
            stack.push(list.addChild(IntPacket(number)))
            isParsingNumber = false
            number = 0
        }
    }

    for (c in line) {
        when (c) {
            '[' -> stack.push(ListPacket())
            ']' -> {
                pushNumber()
                pushChildUp()
            }
            ',' -> pushNumber()
            in '0'..'9' -> {
                isParsingNumber = true
                number = number * 10 + c.digitToInt()
            }
            else -> throw IllegalStateException("Unexpected character: '$c'")
        }
    }

    while (stack.size > 1) {
        pushChildUp()
    }

    return stack.single()
}

fun getSumOfPacketPairsInRightOrder(packetPairs: List<Pair<Packet, Packet>>): Int =
    packetPairs
        .withIndex()
        .filter { (_, pair) -> pair.first < pair.second }
        .sumOf { it.index + 1 }

fun getDecoderKey(packetPairs: List<Pair<Packet, Packet>>): Int =
    packetPairs.asSequence()
        .flatMap { listOf(it.first, it.second) }
        .let { it + DIVIDER_PACKETS }
        .sorted()
        .withIndex()
        .filter { (_, packet) -> packet in DIVIDER_PACKETS }
        .map { (index, _) -> index + 1 }
        .reduce(Int::times)
