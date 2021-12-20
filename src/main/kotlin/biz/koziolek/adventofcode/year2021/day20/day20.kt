package biz.koziolek.adventofcode.year2021.day20

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})
    val lines = inputFile.bufferedReader().readLines()

    val lookupTable = parseLookupTable(lines)
    val image0 = parseInputImage(lines)
    val image1 = image0.enhance(lookupTable)
    val image2 = image1.enhance(lookupTable)

    println("Lit pixels on 2nd image: ${image2.countLitPixels()}")
}

data class InfiniteImage(
    val finitePixels: Map<Coord, Boolean>,
    val infinitePixels: Boolean,
) {
    val finiteWidth: Int = finitePixels.getWidth()
    val finiteHeight: Int = finitePixels.getHeight()

    operator fun get(coord: Coord): Boolean =
        finitePixels.getOrDefault(coord, infinitePixels)

    fun countLitPixels(): Int =
        finitePixels.count { it.value }

    fun enhance(lookupTable: BooleanArray): InfiniteImage =
        InfiniteImage(
            finitePixels = enhanceFinitePixels(lookupTable),
            infinitePixels = enhanceInfinitePixels(lookupTable),
        )

    private fun enhanceFinitePixels(lookupTable: BooleanArray) =
        buildMap {
            for (newY in 0 until finiteHeight + 2) {
                for (newX in 0 until finiteWidth + 2) {
                    val newCoord = Coord(newX, newY)
                    val lookupIndex = getLookupIndex(newCoord)
                    val newValue = lookupTable[lookupIndex]
                    put(newCoord, newValue)
                }
            }
        }

    private fun enhanceInfinitePixels(lookupTable: BooleanArray): Boolean =
        when (infinitePixels) {
            // Off infinite pixels map to 000000000(2) == 0(10)
            false -> lookupTable[0]
            // On infinite pixels map to 111111111(2) == 511(10)
            true -> lookupTable[511]
        }

    private fun getLookupIndex(newCoord: Coord): Int =
        listOf(
            Coord(-1, -1),
            Coord(0, -1),
            Coord(1, -1),

            Coord(-1, 0),
            Coord(0, 0),
            Coord(1, 0),

            Coord(-1, 1),
            Coord(0, 1),
            Coord(1, 1),
        )
            .map { newCoord + it + Coord(-1, -1) }
            .fold(0) { lookupIndex, oldCoord ->
                lookupIndex * 2 + if (get(oldCoord)) 1 else 0
            }

    override fun toString(): String =
        buildString {
            val infiniteBorder = 3
            for (y in -infiniteBorder until finiteHeight + infiniteBorder) {
                for (x in -infiniteBorder until finiteWidth + infiniteBorder) {
                    val coord = Coord(x, y)
                    if (get(coord)) {
                        append('#')
                    } else {
                        append('.')
                    }
                }

                if (y < finiteHeight + infiniteBorder - 1) {
                    append("\n")
                }
            }
        }
}

fun parseLookupTable(lines: Iterable<String>): BooleanArray =
    lines.first()
        .map { it == '#' }
        .toBooleanArray()

fun parseInputImage(lines: Iterable<String>): InfiniteImage =
    InfiniteImage(
        finitePixels = lines
            .drop(2)
            .flatMapIndexed { y, line ->
                line.mapIndexed { x, char ->
                    Coord(x, y) to (char == '#')
                }
            }.toMap(),
        infinitePixels = false,
    )
