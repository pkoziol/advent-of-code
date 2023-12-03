package biz.koziolek.adventofcode.year2023.day03

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2023")
internal class Day3Test {

    private val sampleInput = """
            467..114..
            ...*......
            ..35..633.
            ......#...
            617*......
            .....+.58.
            ..592.....
            ......755.
            ...${'$'}.*....
            .664.598..
        """.trimIndent().split("\n")

    @Test
    fun testParseEngineSchematic() {
        val engineSchematic = parseEngineSchematic(sampleInput)
        assertEquals(
            EngineSchematic(
                numbers = listOf(
                    EngineNumber(number = 467, coord = Coord(0, 0), symbols = listOf(
                        EngineSymbol(symbol = '*', coord = Coord(3, 1)),
                    )),
                    EngineNumber(number = 114, coord = Coord(5, 0), symbols = listOf()),
                    EngineNumber(number = 35, coord = Coord(2, 2), symbols = listOf(
                        EngineSymbol(symbol = '*', coord = Coord(3, 1)),
                    )),
                    EngineNumber(number = 633, coord = Coord(6, 2), symbols = listOf(
                        EngineSymbol(symbol = '#', coord = Coord(6, 3)),
                    )),
                    EngineNumber(number = 617, coord = Coord(0, 4), symbols = listOf(
                        EngineSymbol(symbol = '*', coord = Coord(3, 4)),
                    )),
                    EngineNumber(number = 58, coord = Coord(7, 5), symbols = listOf()),
                    EngineNumber(number = 592, coord = Coord(2, 6), symbols = listOf(
                        EngineSymbol(symbol = '+', coord = Coord(5, 5)),
                    )),
                    EngineNumber(number = 755, coord = Coord(6, 7), symbols = listOf(
                        EngineSymbol(symbol = '*', coord = Coord(5, 8)),
                    )),
                    EngineNumber(number = 664, coord = Coord(1, 9), symbols = listOf(
                        EngineSymbol(symbol = '$', coord = Coord(3, 8)),
                    )),
                    EngineNumber(number = 598, coord = Coord(5, 9), symbols = listOf(
                        EngineSymbol(symbol = '*', coord = Coord(5, 8)),
                    )),
                )
            ),
            engineSchematic
        )
    }

    @Test
    fun testSampleAnswer1() {
        val engineSchematic = parseEngineSchematic(sampleInput)
        assertEquals(4361, engineSchematic.numbersAdjacentToSymbol.sumOf { it.number })
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val engineSchematic = parseEngineSchematic(input)
        assertEquals(551094, engineSchematic.numbersAdjacentToSymbol.sumOf { it.number })
    }
}
