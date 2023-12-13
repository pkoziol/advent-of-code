package biz.koziolek.adventofcode.year2023.day13

import biz.koziolek.adventofcode.AsciiColor
import biz.koziolek.adventofcode.findInput
import kotlin.math.min

fun main() {
    val inputFile = findInput(object {})
    val notes = parseNotes202313(inputFile.bufferedReader().readLines())
    println("Summarized notes: ${notes.summarize()}")
}

data class Notes202313(val patterns: List<RocksPattern>) {
    fun summarize(): Int =
        patterns.mapNotNull { it.findReflection()?.summarize() }.sum()
}

data class RocksPattern(val contents: List<String>) {
    val width = contents[0].length
    val height = contents.size

    fun findReflection(): Reflection? =
        findHorizontalReflection() ?: findVerticalReflection()

    private fun findVerticalReflection(): VerticalReflection? =
        findReflectionIndex(contents)
            ?.let { VerticalReflection(it) }

    private fun findHorizontalReflection(): HorizontalReflection? =
        findReflectionIndex(transpose(contents))
            ?.let { HorizontalReflection(it) }

    private fun transpose(lines: List<String>): List<String> =
        lines.fold(List(lines.first().length) { "" }) { acc, line ->
            line.mapIndexed { index, c -> acc[index] + c }
        }

    private fun findReflectionIndex(lines: List<String>): Int? {
        val mirrorAfter = mutableSetOf<Int>()
        for (index in lines.indices) {
            if (index == lines.size - 1) {
                continue
            }

            var matches = true
            for (distance in 0..min(index, lines.size - 2 - index)) {
                if (lines[index - distance] != lines[index + 1 + distance]) {
                    matches = false
                    break
                }
            }

            if (matches) {
                mirrorAfter.add(index)
            }
        }

        return mirrorAfter.singleOrNull()
    }

    fun toStringWithReflection(): String =
        when (val reflection = findReflection()) {
            is HorizontalReflection -> buildString {
                for (line in contents) {
                    for ((index, char) in line.withIndex()) {
                        append(char)
                        if (index == reflection.afterColumn) {
                            append(AsciiColor.BRIGHT_WHITE.format("|"))
                        }
                    }
                    append("\n")
                }
            }
            is VerticalReflection -> buildString {
                for ((index, line) in contents.withIndex()) {
                    append("$line\n")
                    if (index == reflection.afterRow) {
                        append(AsciiColor.BRIGHT_WHITE.format("-".repeat(line.length)) + "\n")
                    }
                }
            }
            null -> contents.joinToString("\n", postfix = "\n")
        }

    companion object {
        fun fromString(string: String): RocksPattern =
            RocksPattern(string.split("\n"))
    }
}

sealed interface Reflection {
    fun summarize(): Int
}
data class HorizontalReflection(val afterColumn: Int) : Reflection {
    override fun summarize(): Int = afterColumn + 1
}
data class VerticalReflection(val afterRow: Int) : Reflection {
    override fun summarize(): Int = (afterRow + 1) * 100
}

fun parseNotes202313(lines: Iterable<String>): Notes202313 =
    buildList {
        var pattern = mutableListOf<String>()
        lines.forEach { line ->
            if (line.isBlank()) {
                add(RocksPattern(pattern))
                pattern = mutableListOf()
            } else {
                pattern.add(line)
            }
        }

        if (pattern.isNotEmpty()) {
            add(RocksPattern(pattern))
        }
    }.let { Notes202313(it) }
