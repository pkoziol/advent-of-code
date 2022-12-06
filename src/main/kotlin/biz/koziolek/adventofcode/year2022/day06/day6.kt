package biz.koziolek.adventofcode.year2022.day06

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val startOfPacket = findStartOfPacket(inputFile.bufferedReader().readLine())
    println("Characters to be read before finding start-of-packet: $startOfPacket")
}

fun findStartOfPacket(buffer: String): Int =
    buffer.windowedSequence(size = 4, step = 1)
        .withIndex()
        .find { (_, value) -> value.toSet().size == 4 }
        ?.let { it.index + 4 }
        ?: throw IllegalArgumentException("No start-of-packet found")
