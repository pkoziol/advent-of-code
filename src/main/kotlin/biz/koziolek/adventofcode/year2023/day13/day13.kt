package biz.koziolek.adventofcode.year2023.day13

import biz.koziolek.adventofcode.AsciiColor
import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput
import kotlin.math.min

fun main() {
    val inputFile = findInput(object {})
    val notes = parseNotes202313(inputFile.bufferedReader().readLines())
    println("Summarized notes: ${notes.summarize()}")
    println("Summarized notes with fixed smudges: ${notes.fixSmudges().summarize(notes)}")
}

data class Notes202313(val patterns: List<RocksPattern>) {
    fun summarize(previousNotes: Notes202313? = null): Int =
        if (previousNotes == null) {
            patterns.mapNotNull { it.findReflection()?.summarize() }.sum()
        } else {
            patterns.zip(previousNotes.patterns)
                .map { (currentPattern, previousPattern) ->
                    val currentReflections = currentPattern.findReflections()
                    val previousReflections = previousPattern.findReflections()
                    currentReflections.subtract(previousReflections).single()
                }
                .sumOf { it.summarize() }
        }

    fun fixSmudges(): Notes202313 =
        Notes202313(patterns.map { it.fixSmudge()!! })
}

const val ROCK = '#'
const val ASH = '.'

private fun <T> Collection<T>.singleOrNullOnlyWhenZero(): T? =
    when (size) {
        0 -> null
        1 -> first()
        else -> throw IllegalArgumentException("Found $size elements in $this")
    }

data class RocksPattern(val contents: List<String>) {
    val width = contents[0].length
    val height = contents.size

    fun findReflection(): Reflection? =
        findReflections().singleOrNullOnlyWhenZero()

    fun findReflections(): Set<Reflection> =
        findReflections(contents)

    private fun findReflections(lines: List<String>): Set<Reflection> =
        buildSet {
            findHorizontalReflections(lines).forEach { add(it) }
            findVerticalReflections(lines).forEach { add(it) }
        }

    private fun findVerticalReflections(lines: List<String>): Set<VerticalReflection> =
        findReflectionIndexes(lines)
            .mapTo(mutableSetOf()) { VerticalReflection(it) }

    private fun findHorizontalReflections(lines: List<String>): Set<HorizontalReflection> =
        findReflectionIndexes(transpose(lines))
            .mapTo(mutableSetOf()) { HorizontalReflection(it) }

    private fun transpose(lines: List<String>): List<String> =
        lines.fold(List(lines.first().length) { "" }) { acc, line ->
            line.mapIndexed { index, c -> acc[index] + c }
        }

    private fun findReflectionIndexes(lines: List<String>): Set<Int> {
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

        return mirrorAfter
    }

    @Suppress("unused")
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

    fun fixSmudge(): RocksPattern? {
        val smudge = findSmudge(contents) ?: findSmudge(contents, horizontalReflection = true)

        return if (smudge != null) {
            RocksPattern(contents = replaceSmudge(contents, smudge))
        } else {
            null
        }
    }

    private fun findSmudge(lines: List<String>, horizontalReflection: Boolean = false): Coord? {
        val currentLines = if (horizontalReflection) transpose(lines) else lines
        val existingReflections = findReflections(currentLines)

        for (index in currentLines.indices) {
            if (index == currentLines.size - 1) {
                continue
            }

            for (distance in 0..min(index, currentLines.size - 2 - index)) {
                val indexA = index - distance
                val indexB = index + 1 + distance
                val lineA = currentLines[indexA]
                val lineB = currentLines[indexB]
                val differences = findDifferences(lineA, lineB)

                if (differences.size == 1) {
                    val smudgePos = Coord(
                        x = differences.single(),
                        y = indexA,
                    )
                    val newLines = replaceSmudge(currentLines, smudgePos)

                    val newReflections = findReflections(newLines)
                    if (newReflections.subtract(existingReflections).isNotEmpty()) {
                        return if (horizontalReflection) {
                            Coord(x = smudgePos.y, y = smudgePos.x)
                        } else {
                            smudgePos
                        }
                    }
                }
            }
        }

        return null
    }

    private fun findDifferences(a: String, b: String): List<Int> =
        a.zip(b).mapIndexedNotNull { index, pair ->
            if (pair.first != pair.second) index else null
        }

    private fun replaceSmudge(lines: List<String>, smudge: Coord) =
        lines.mapIndexed { y, line ->
            if (y == smudge.y) {
                line.withIndex().joinToString("") { (x, char) ->
                    if (x == smudge.x) {
                        opposite(char).toString()
                    } else {
                        char.toString()
                    }
                }
            } else {
                line
            }
        }

    private fun opposite(char: Char): Char =
        when (char) {
            ROCK -> ASH
            ASH -> ROCK
            else -> throw IllegalArgumentException("Unsupported char: $char")
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
