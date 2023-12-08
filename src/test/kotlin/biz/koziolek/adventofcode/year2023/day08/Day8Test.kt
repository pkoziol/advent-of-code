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

    private val sampleInput3 = """
            LR

            11A = (11B, XXX)
            11B = (XXX, 11Z)
            11Z = (11B, XXX)
            22A = (22B, XXX)
            22B = (22C, 22C)
            22C = (22Z, 22Z)
            22Z = (22B, 22B)
            XXX = (XXX, XXX)
        """.trimIndent().split("\n")

    @Test
    fun testParseWastelandMap() {
        val map1 = parseWastelandMap(sampleInput1)
        assertEquals("RL", map1.instructions)
        assertEquals(7, map1.nodes.size)

        val map2 = parseWastelandMap(sampleInput2)
        assertEquals("LLR", map2.instructions)
        assertEquals(3, map2.nodes.size)
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

    @Test
    fun testGhostNodes() {
        val map = parseWastelandMap(sampleInput3)
        assertEquals(8, map.nodes.size)
        assertEquals(
            listOf("11A", "22A"),
            map.ghostStartNodes.map { it.id }
        )
        assertEquals(
            listOf("11Z", "22Z"),
            map.ghostEndNodes.map { it.id }
        )
    }

    @Test
    fun testSampleAnswer2() {
        val map = parseWastelandMap(sampleInput3)
        val expectedPaths = listOf(
            listOf("11A", "22A"),
            listOf("11B", "22B"),
            listOf("11Z", "22C"),
            listOf("11B", "22Z"),
            listOf("11Z", "22B"),
            listOf("11B", "22C"),
            listOf("11Z", "22Z"),
        )
        assertEquals(
            expectedPaths,
            map.followGhostInstructions1().map { it.map { node -> node.id } }.toList()
        )
        assertEquals(6, map.followGhostInstructions2())
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val map = parseWastelandMap(input)
        assertEquals(11283670395017, map.followGhostInstructions())
    }
}
