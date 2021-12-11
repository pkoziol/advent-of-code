package biz.koziolek.adventofcode.year2021.day8

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day8Test {

    private val sampleInput1 = "acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf"

    private val sampleInput2 = """
        be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
        edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
        fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
        fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
        aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
        fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
        dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
        bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
        egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
        gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce
    """.trimIndent().split("\n")

    @Test
    fun testCountEasyDigitsInOutput() {
        val count = countEasyDigitsInOutput(sampleInput2)
        assertEquals(26, count)
    }

    @Test
    fun testAnswer() {
        val fullInput = findInput(object {}).readLines()
        val count = countEasyDigitsInOutput(fullInput)
        assertEquals(352, count)
    }

    @Test
    fun testSolveWireConnections() {
        val note = parseNotes(listOf(sampleInput1)).first()
        val expectedWireMap = mapOf(
                'd' to 'a',
                'e' to 'b',
                'a' to 'c',
                'f' to 'd',
                'g' to 'e',
                'b' to 'f',
                'c' to 'g'
        )
        val wireMap = solveWireConnections(note)

        assertEquals(expectedWireMap, wireMap)
    }

    @Test
    fun testDecodeDigit() {
        val wireMap = mapOf(
                'd' to 'a',
                'e' to 'b',
                'a' to 'c',
                'f' to 'd',
                'g' to 'e',
                'b' to 'f',
                'c' to 'g'
        )

        assertEquals(8, decodeDigit("acedgfb", wireMap))
        assertEquals(5, decodeDigit("cdfbe", wireMap))
        assertEquals(2, decodeDigit("gcdfa", wireMap))
        assertEquals(3, decodeDigit("fbcad", wireMap))
        assertEquals(7, decodeDigit("dab", wireMap))
        assertEquals(9, decodeDigit("cefabd", wireMap))
        assertEquals(6, decodeDigit("cdfgeb", wireMap))
        assertEquals(4, decodeDigit("eafb", wireMap))
        assertEquals(0, decodeDigit("cagedb", wireMap))
        assertEquals(1, decodeDigit("ab", wireMap))
    }

    @Test
    fun testDecodeNumber() {
        val note = parseNotes(listOf(sampleInput1)).first()
        val wireMap = mapOf(
                'd' to 'a',
                'e' to 'b',
                'a' to 'c',
                'f' to 'd',
                'g' to 'e',
                'b' to 'f',
                'c' to 'g'
        )

        assertEquals(5353, decodeNumber(note.output, wireMap))
    }

    @Test
    fun testAnswer2Sample2() {
        val notes = parseNotes(sampleInput2)
        val answer = notes.sumOf { decodeNumber(it.output, solveWireConnections(it)) }

        assertEquals(61229, answer)
    }

    @Test
    fun testAnswer2() {
        val fullInput = findInput(object {}).readLines()
        val notes = parseNotes(fullInput)
        val answer = notes.sumOf { decodeNumber(it.output, solveWireConnections(it)) }

        assertEquals(936117, answer)
    }
}
