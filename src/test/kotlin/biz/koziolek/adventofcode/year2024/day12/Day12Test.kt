package biz.koziolek.adventofcode.year2024.day12

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2024")
internal class Day12Test {

    private val sampleInput1 = """
            AAAA
            BBCD
            BBCC
            EEEC
        """.trimIndent().split("\n")

    private val sampleInput2 = """
            OOOOO
            OXOXO
            OOOOO
            OXOXO
            OOOOO
        """.trimIndent().split("\n")

    private val sampleInput3 = """
            RRRRIICCFF
            RRRRIICCCF
            VVRRRCCFFF
            VVRCCCJFFF
            VVVVCJJCFE
            VVIVCCJJEE
            VVIIICJJEE
            MIIIIIJJEE
            MIIISIJEEE
            MMMISSJEEE
        """.trimIndent().split("\n")

    @Test
    fun testParse() {
        val regions1 = parseRegions(sampleInput1)
        assertEquals(5, regions1.size)

        val regions2 = parseRegions(sampleInput2)
        assertEquals(5, regions2.size)

        val regions3 = parseRegions(sampleInput3)
        assertEquals(11, regions3.size)
    }

    @Test
    fun testSampleAnswer1() {
        val regions1 = parseRegions(sampleInput1)
        val prices1 = regions1.map { it.price }
        assertEquals(listOf(4, 24, 32, 40, 40), prices1.sorted())
        assertEquals(140, prices1.sum())

        val regions2 = parseRegions(sampleInput2)
        val prices2 = regions2.map { it.price }
        assertEquals(listOf(4, 4, 4, 4, 756), prices2.sorted())
        assertEquals(772, prices2.sum())

        val regions3 = parseRegions(sampleInput3)
        val prices3 = regions3.map { it.price }
        assertEquals(listOf(4, 24, 32, 60, 180, 216, 220, 234, 260, 308, 392), prices3.sorted())
        assertEquals(1930, prices3.sum())
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val regions = parseRegions(input)
        val prices = regions.map { it.price }
        assertEquals(1465112, prices.sum())
    }
}
