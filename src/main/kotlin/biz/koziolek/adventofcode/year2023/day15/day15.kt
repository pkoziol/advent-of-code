package biz.koziolek.adventofcode.year2023.day15

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val initSeq = parseInitializationSequence(inputFile.bufferedReader().readLines())
    println("Hash of initialization sequence is: ${initSeq.sumOf { it.hash() }}")
    println("Boxes focusing power: ${BoxesLine().initialize(initSeq).contents.sumOf { it.focusingPower }}")
}

sealed interface InitOperation {
    val lensLabel: String
    val boxId: Int
        get() = hash(lensLabel)

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

data class BoxesLine(val contents: List<Box> = List(size = 256, init = { Box(it, emptyList()) })) {
    fun initialize(initSeq: List<InitOperation>): BoxesLine =
        initSeq.fold(this) { boxes, operation ->
            BoxesLine(
                contents = boxes.contents.map { box ->
                    if (box.id == operation.boxId) {
                        when (operation) {
                            is RemoveOperation -> box.remove(operation.lensLabel)
                            is AddOperation -> box.add(operation.lens)
                        }
                    } else {
                        box
                    }
                }
            )
        }
}

data class Box(val id: Int, val contents: List<Lens>) {
    val focusingPower: Int = contents
        .mapIndexed { index, lens ->
            (id + 1) * (index + 1) * lens.focalLength
        }
        .sum()

    fun remove(lensLabel: String): Box =
        copy(contents = contents.filter { it.label != lensLabel })

    fun add(newLens: Lens): Box =
        copy(contents = if (contents.any { it.label == newLens.label }) {
            contents.map { if (it.label == newLens.label) newLens else it }
        } else {
            contents + newLens
        })
}

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
