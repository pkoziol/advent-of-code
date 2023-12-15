package biz.koziolek.adventofcode.year2023.day15

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val initSeq = parseInitializationSequence(inputFile.bufferedReader().readLines())
    println("Hash of initialization sequence is: ${initSeq.sumOf { it.hash() }}")
}

sealed interface InitOperation {
    val lensLabel: String
    override fun toString(): String
    fun hash() = hash(this.toString())
}

data class RemoveOperation(override val lensLabel: String) : InitOperation {
    override fun toString() = "$lensLabel-"
}

data class AddOperation(val lens: Lens) : InitOperation {
    override val lensLabel = lens.label
    override fun toString() = "${lens.label}=${lens.focalLength}"
}

data class Lens(val label: String, val focalLength: Int)

fun parseInitializationSequence(lines: Iterable<String>): List<InitOperation> =
    lines.first()
        .split(',')
        .mapNotNull { Regex("^([a-zA-Z]+)(=([0-9]+)|-)$").find(it) }
        .map {
            when (val symbol = it.groups[2]!!.value.first()) {
                '-' -> RemoveOperation(
                    lensLabel = it.groups[1]!!.value,
                )
                '=' -> AddOperation(
                    lens = Lens(
                        label = it.groups[1]!!.value,
                        focalLength = it.groups[3]!!.value.toInt(),
                    ),
                )
                else -> throw IllegalArgumentException("Unknown operation character: $symbol")
            }
        }

fun hash(string: String): Int =
    string.fold(0) { acc, c ->
        ((acc + c.code) * 17) % 256
    }
