package biz.koziolek.adventofcode.year2023.day15

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val initSeq = parseInitializationSequence(inputFile.bufferedReader().readLines())
    println("Hash of initialization sequence is: ${initSeq.sumOf { it.hash() }}")
}

data class InitOperation(val name: String, val symbol: Char, val value: Int? = null) {

    override fun toString() = "$name$symbol${value ?: ""}"

    fun hash() = hash(this.toString())
}

fun parseInitializationSequence(lines: Iterable<String>): List<InitOperation> =
    lines.first()
        .split(',')
        .mapNotNull { Regex("^([a-zA-Z]+)(=([0-9]+)|-)$").find(it) }
        .map {
            InitOperation(
                name = it.groups[1]!!.value,
                symbol = it.groups[2]!!.value.first(),
                value = it.groups[3]?.value?.toInt()
            )
        }

fun hash(string: String): Int =
    string.fold(0) { acc, c ->
        ((acc + c.code) * 17) % 256
    }
