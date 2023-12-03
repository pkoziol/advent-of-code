package biz.koziolek.adventofcode.year2023.day03

import biz.koziolek.adventofcode.*
import kotlin.math.log10

fun main() {
    val inputFile = findInput(object {})
    val engineSchematic = parseEngineSchematic(inputFile.bufferedReader().readLines())
    println("Sum of numbers adjacent to a symbol: ${engineSchematic.numbersAdjacentToSymbol.sumOf { it.number }}")
}

data class EngineSchematic(val numbers: List<EngineNumber>) {
    val numbersAdjacentToSymbol: List<EngineNumber>
        get() = numbers.filter { it.symbols.isNotEmpty() }
}

data class EngineNumber(val number: Int, val coord: Coord, val symbols: List<EngineSymbol>)

data class EngineSymbol(val symbol: Char, val coord: Coord)

fun parseEngineSchematic(lines: Iterable<String>): EngineSchematic {
    val map = lines.flatMapIndexed { y, line ->
        line.mapIndexed { x, char ->
            Coord(x, y) to char
        }
    }.toMap()

    val width = map.getWidth()
    val height = map.getHeight()

    val numbers = buildList<EngineNumber> {
        var y = 0
        while (y < height) {
            var x = 0
            while (x < width) {
                var number = 0
                val startCoord = Coord(x, y)
                var coord = startCoord

                while (coord.x < width
                    && map[coord]?.isDigit() == true) {
                    number = number * 10 + map[coord]!!.digitToInt()
                    coord += Coord(1, 0)
                }

                if (number > 0) {
                    val numDigits: Int = log10(number.toDouble()).toInt() + 1
                    val symbols = (startCoord.x until (startCoord.x + numDigits))
                        .flatMap { xx -> map.getAdjacentCoords(Coord(x = xx, y = startCoord.y), includeDiagonal = true) }
                        .toSet()
                        .map { adjCoord ->
                            map[adjCoord]
                                ?.takeIf { !it.isDigit() && it != '.' }
                                ?.let { symbol ->
                                    EngineSymbol(symbol, adjCoord)
                                }
                        }
                        .filterNotNull()

                    add(EngineNumber(number, startCoord, symbols))
                    x += numDigits
                } else {
                    x++
                }
            }

            y++
        }
    }

    return EngineSchematic(numbers)
}
