package biz.koziolek.adventofcode.year2023.day03

import biz.koziolek.adventofcode.*
import kotlin.math.log10

fun main() {
    val inputFile = findInput(object {})
    val engineSchematic = parseEngineSchematic(inputFile.bufferedReader().readLines())
    println("Sum of numbers adjacent to a symbol: ${engineSchematic.numbersAdjacentToSymbol.sumOf { it.number }}")
    println("Sum of all of the gear ratios: ${engineSchematic.gears.sumOf { it.ratio }}")
}

data class EngineSchematic(val numbers: List<EngineNumber>) {
    val numbersAdjacentToSymbol: List<EngineNumber>
        get() = numbers.filter { it.symbols.isNotEmpty() }

    val gears: List<EngineGear>
        get() = numbers
            .flatMap { number ->
                number.symbols
                    .filter { it.symbol.isGear() }
                    .map { it to number }
            }
            .groupBy(
                keySelector = { it.first },
                valueTransform = { it.second }
            )
            .filter { it.value.size == 2 }
            .map { EngineGear(it.key.coord, it.value[0], it.value[1]) }
}

data class EngineNumber(val number: Int, val coord: Coord, val symbols: List<EngineSymbol>)

data class EngineSymbol(val symbol: Char, val coord: Coord)

data class EngineGear(val coord: Coord, val num1: EngineNumber, val num2: EngineNumber) {
    val ratio = num1.number * num2.number
}

fun parseEngineSchematic(lines: Iterable<String>): EngineSchematic {
    val map = lines.parse2DMap().toMap()
    val width = map.getWidth()
    val height = map.getHeight()

    val numbers = buildList {
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
                    val numDigits = log10(number.toDouble()).toInt() + 1
                    val symbols = (startCoord.x until (startCoord.x + numDigits))
                        .flatMap { xx -> map.getAdjacentCoords(Coord(x = xx, y = startCoord.y), includeDiagonal = true) }
                        .toSet()
                        .mapNotNull { adjCoord ->
                            map[adjCoord]
                                ?.takeIf { it.isSymbol() }
                                ?.let { symbol -> EngineSymbol(symbol, adjCoord) }
                        }

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

private const val EMPTY = '.'
private const val GEAR = '*'
private fun Char.isSymbol() = !this.isDigit() && this != EMPTY // What about letters?
private fun Char.isGear() = this == GEAR
