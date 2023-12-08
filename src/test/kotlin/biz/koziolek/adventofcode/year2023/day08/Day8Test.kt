package biz.koziolek.adventofcode.year2023.day08

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2023")
internal class Day8Test {

    private val sampleInput1 = """
            RL

            AAA = (BBB, CCC)
            BBB = (DDD, EEE)
            CCC = (ZZZ, GGG)
            DDD = (DDD, DDD)
            EEE = (EEE, EEE)
            GGG = (GGG, GGG)
            ZZZ = (ZZZ, ZZZ)
        """.trimIndent().split("\n")

    private val sampleInput2 = """
            LLR

            AAA = (BBB, BBB)
            BBB = (AAA, ZZZ)
            ZZZ = (ZZZ, ZZZ)
        """.trimIndent().split("\n")

    @Test
    fun testParseWastelandMap() {
        val map1 = parseWastelandMap(sampleInput1)
        assertEquals("RL", map1.instructions)
        assertEquals(7, map1.graph.nodes.size)
        assertEquals(14, map1.graph.edges.size)

        val map2 = parseWastelandMap(sampleInput2)
        assertEquals("LLR", map2.instructions)
        assertEquals(3, map2.graph.nodes.size)
        assertEquals(6, map2.graph.edges.size)
    }

    @Test
    fun testSampleAnswer1() {
        val map1 = parseWastelandMap(sampleInput1)
        assertEquals(
            listOf("AAA", "CCC", "ZZZ"),
            map1.followInstructions().map { it.id }
        )

        val map2 = parseWastelandMap(sampleInput2)
        assertEquals(
            listOf("AAA", "BBB", "AAA", "BBB", "AAA", "BBB", "ZZZ"),
            map2.followInstructions().map { it.id }
        )
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val map = parseWastelandMap(input)
        assertEquals(15871, map.followInstructions().size - 1)
    }
}
