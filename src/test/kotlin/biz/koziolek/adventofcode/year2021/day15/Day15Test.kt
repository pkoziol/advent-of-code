package biz.koziolek.adventofcode.year2021.day15

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput
import biz.koziolek.adventofcode.getHeight
import biz.koziolek.adventofcode.getWidth
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day15Test {

    private val sampleInput = """
        1163751742
        1381373672
        2136511328
        3694931569
        7463417111
        1319128137
        1359912421
        3125421639
        1293138521
        2311944581
    """.trimIndent().split("\n")

    @Test
    fun testParseRiskMap() {
        val riskMap = parseRiskMap(sampleInput)

        assertEquals(10, riskMap.getWidth())
        assertEquals(10, riskMap.getHeight())
        assertEquals(100, riskMap.size)
    }

    @Test
    fun testFindLowestRiskPath() {
        val riskMap = parseRiskMap(sampleInput)
        val lowestRiskPath = findLowestRiskPath(riskMap,
            start = Coord(0, 0),
            end = Coord(riskMap.getWidth() - 1, riskMap.getHeight() - 1)
        )
        println(toString(riskMap, lowestRiskPath))

        assertEquals(19, lowestRiskPath.size)
        assertEquals(40, getTotalRisk(riskMap, lowestRiskPath))
    }

    @Test
    fun testAnswer1() {
        val fullInput = findInput(object {}).readLines()
        val riskMap = parseRiskMap(fullInput)
        val lowestRiskPath = findLowestRiskPath(riskMap,
            start = Coord(0, 0),
            end = Coord(riskMap.getWidth() - 1, riskMap.getHeight() - 1)
        )
        assertEquals(410, getTotalRisk(riskMap, lowestRiskPath))
    }
}
