package biz.koziolek.adventofcode.year2022.day08

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2022")
internal class Day8Test {

    private val sampleInput = """
            30373
            25512
            65332
            33549
            35390
        """.trimIndent().split("\n")

    @Test
    fun testParseTrees() {
        val trees = parseTrees(sampleInput)
        assertEquals(7, trees[Coord(3, 0)])
        assertEquals(9, trees[Coord(4, 3)])
    }

    @Test
    fun testFindTreesIn4Directions() {
        val trees = parseTrees(sampleInput)
        val start = Coord(x = 0, y = 1)
        assertEquals("3", getNorthTrees(trees, start).joinToString("") { it.value.toString() })
        assertEquals("5512", getEastTrees(trees, start).joinToString("") { it.value.toString() })
        assertEquals("633", getSouthTrees(trees, start).joinToString("") { it.value.toString() })
        assertEquals("", getWestTrees(trees, start).joinToString("") { it.value.toString() })
    }

    @Test
    fun testCountVisibleTrees() {
        val trees = parseTrees(sampleInput)
        assertEquals(21, countVisibleTreesFromEdges(trees))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val trees = parseTrees(input)
        assertEquals(1688, countVisibleTreesFromEdges(trees))
    }

    @Test
    fun testGetScenicScore() {
        val trees = parseTrees(sampleInput)
        assertEquals(4, getScenicScore(trees, Coord(x = 2, y = 1)))
        assertEquals(8, getScenicScore(trees, Coord(x = 2, y = 3)))
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val trees = parseTrees(input)
        assertEquals(410400, findHighestScenicScore(trees))
    }
}
