package biz.koziolek.adventofcode.year2024.day08

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})
    val map = parseAntennasMap(inputFile.bufferedReader().readLines())
    println("Number of unique antinodes: ${map.addAntinodes().countUnique('#')}")
    println("Number of unique antinodes including resonant harmonics: ${map.addAntinodes(resonant = true).countUnique('#')}")
}

const val EMPTY = '.'
const val ANTINODE = '#'

data class AntennasMap(
    val antennas: Map<Coord, String>,
    val width: Int = antennas.getWidth(),
    val height: Int = antennas.getHeight(),
) {
    fun countUnique(c: Char): Int =
        antennas
            .filter { c in it.value }
            .map { it.key }
            .distinct()
            .count()

    fun addAntinodes(resonant: Boolean = false): AntennasMap {
        val antinodesCoords = productWithItself(antennas.entries.toList(), diagonal = false)
            .filter { (a, b) -> a.value != EMPTY.toString() && b.value != EMPTY.toString() }
            .filter { (a, b) ->
                a.key != b.key
                        && findFrequency(a.value) == findFrequency(b.value)
            }
            .map { (a, b) -> a.key to b.key }
            .flatMap { (a, b) ->
                val diff = b - a
                sequence {
                    var c = b
                    if (resonant) {
                        yield(c)
                    }

                    c += diff
                    yield(c)

                    if (resonant) {
                        c += diff
                        while (c in this@AntennasMap) {
                            yield(c)
                            c += diff
                        }
                    }
                }
            }
            .filter { it in this }
            .toSet()
        val antinodes = antinodesCoords.associate { coord ->
            val existingStr: String = antennas[coord] ?: EMPTY.toString()
            if (existingStr != EMPTY.toString()) {
                coord to existingStr + ANTINODE
            } else {
                coord to ANTINODE.toString()
            }
        }
        return copy(antennas = antennas + antinodes)
    }

    operator fun contains(coord: Coord): Boolean =
        coord.x in 0 ..< width && coord.y in 0 ..< height

    override fun toString(): String {
        val tmpMap = mapOf(
            Coord(0, 0) to EMPTY.toString(),
            Coord(width - 1, height - 1) to EMPTY.toString(),
        ) + antennas
        return tmpMap.to2DStringOfStrings { _, str ->
            val frequency = findFrequency(str)
            val char = frequency ?: str?.last() ?: EMPTY
            val colors = listOf(
                AsciiColor.RED,
                AsciiColor.GREEN,
                AsciiColor.YELLOW,
                AsciiColor.BLUE,
                AsciiColor.MAGENTA,
                AsciiColor.CYAN,
                AsciiColor.WHITE,
            )

            when (char) {
                EMPTY -> AsciiColor.BRIGHT_BLACK.format(char)
                ANTINODE -> AsciiColor.BRIGHT_WHITE.format(char)
                else -> {
                    colors[char.code % colors.size].format(char)
                }
            }
        }
    }
}

fun findFrequency(str: String?): Char? =
    str?.singleOrNull { it != ANTINODE }

fun parseAntennasMap(lines: Iterable<String>): AntennasMap =
    lines
        .parse2DMap { it.toString() }
        .toMap()
        .let { AntennasMap(it) }
