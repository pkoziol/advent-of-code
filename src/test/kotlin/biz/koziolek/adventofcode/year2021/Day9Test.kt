package biz.koziolek.adventofcode.year2021

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class Day9Test {

    private val sampleInput = """
        2199943210
        3987894921
        9856789892
        8767896789
        9899965678
    """.trimIndent().split("\n")

    @Test
    fun testFindSmokeLowPoints() {
        val smokeMap = parseSmokeMap(sampleInput)
        val lowPoints = findSmokeLowPoints(smokeMap)

        assertEquals(setOf(
                Pair(1, 0),
                Pair(9, 0),
                Pair(2, 2),
                Pair(6, 4),
        ), lowPoints)
    }

    @Test
    fun testGetRiskLevel() {
        val smokeMap = parseSmokeMap(sampleInput)
        val lowPoints = findSmokeLowPoints(smokeMap)

        assertEquals(2, getRiskLevel(Pair(1, 0), smokeMap, lowPoints))
        assertEquals(1, getRiskLevel(Pair(9, 0), smokeMap, lowPoints))
        assertEquals(6, getRiskLevel(Pair(2, 2), smokeMap, lowPoints))
        assertEquals(6, getRiskLevel(Pair(6, 4), smokeMap, lowPoints))
    }

    @Test
    fun testAnswerSample() {
        val smokeMap = parseSmokeMap(sampleInput)
        val lowPointsRiskSum = getLowPointsRiskSum(smokeMap)
        assertEquals(15, lowPointsRiskSum)
    }

    @Test
    fun testAnswer() {
        val fullInput = File("src/main/resources/year2021/day9/input").readLines()
        val smokeMap = parseSmokeMap(fullInput)
        val lowPointsRiskSum = getLowPointsRiskSum(smokeMap)
        assertEquals(580, lowPointsRiskSum)
    }
}
