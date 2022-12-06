package biz.koziolek.adventofcode.year2022.day06

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val startOfPacket = findStartOfPacket(inputFile.bufferedReader().readLine())
    println("Characters to be read before finding start-of-packet: $startOfPacket")

    val startOfMessage = findStartOfMessage(inputFile.bufferedReader().readLine())
    println("Characters to be read before finding start-of-message: $startOfMessage")
}

fun findStartOfPacket(buffer: String): Int =
    findFirstNUniqueChars(buffer, uniqueCount = 4)

fun findStartOfMessage(buffer: String): Int =
    findFirstNUniqueChars(buffer, uniqueCount = 14)

private fun findFirstNUniqueChars(buffer: String, uniqueCount: Int): Int =
    buffer.windowedSequence(size = uniqueCount, step = 1)
        .withIndex()
        .find { (_, value) -> value.toSet().size == uniqueCount }
        ?.let { it.index + uniqueCount }
        ?: throw IllegalArgumentException("No $uniqueCount unique characters found")
