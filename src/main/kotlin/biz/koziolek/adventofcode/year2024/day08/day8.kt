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
        val antinodesCoords = antennas
            .filterValues { it != "$EMPTY" }
            .entries
            .toList()
            .let { productWithItself(it, ordered = true, withSelf = false) }
            .filter { (a, b) ->
                a.key != b.key
                        && findFrequency(a.value) == findFrequency(b.value)
            }
            .map { (a, b) -> a.key to b.key }
            .flatMap { (a, b) ->
                val diff = b - a
                sequence {
                    if (resonant) {
                        yield(b)
                    }

                    var c = b + diff
                    yield(c)

                    while (resonant) {
                        c += diff
                        yield(c)
                    }
                }.takeWhile { it in this }
            }
            .filter { it in this }
            .toSet()

        val antinodes = antinodesCoords.associateWith { coord ->
            val existingStr: String = antennas[coord] ?: "$EMPTY"
            if (existingStr != "$EMPTY") {
                existingStr + ANTINODE
            } else {
                "$ANTINODE"
            }
        }

        return copy(antennas = antennas + antinodes)
    }

    operator fun contains(coord: Coord): Boolean =
        coord.x in 0 ..< width && coord.y in 0 ..< height

    override fun toString(): String {
        return antennas.to2DStringOfStrings(
            from = Coord(0, 0),
            to = Coord(width - 1, height - 1)
        ) { _, str ->
            val frequency = findFrequency(str)
            val char = frequency ?: str?.last() ?: EMPTY

            val color = when (char) {
                EMPTY -> AsciiColor.BRIGHT_BLACK
                ANTINODE -> AsciiColor.BRIGHT_WHITE
                else -> frequencyColors[char.code % frequencyColors.size]
            }

            color.format(char)
        }
    }
}

private val frequencyColors = listOf(
    AsciiColor.RED,
    AsciiColor.GREEN,
    AsciiColor.YELLOW,
    AsciiColor.BLUE,
    AsciiColor.MAGENTA,
    AsciiColor.CYAN,
    AsciiColor.WHITE,
)

fun findFrequency(str: String?): Char? =
    str?.singleOrNull { it != ANTINODE }

fun parseAntennasMap(lines: Iterable<String>): AntennasMap =
    lines
        .parse2DMap { it.toString() }
        .toMap()
        .let { AntennasMap(it) }
