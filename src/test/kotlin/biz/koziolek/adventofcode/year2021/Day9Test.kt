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
    fun testAnswerPart1Sample() {
        val smokeMap = parseSmokeMap(sampleInput)
        val lowPointsRiskSum = getLowPointsRiskSum(smokeMap)
        assertEquals(15, lowPointsRiskSum)
    }

    @Test
    fun testAnswerPart1() {
        val fullInput = File("src/main/resources/year2021/day9/input").readLines()
        val smokeMap = parseSmokeMap(fullInput)
        val lowPointsRiskSum = getLowPointsRiskSum(smokeMap)
        assertEquals(580, lowPointsRiskSum)
    }

    @Test
    fun testFindSmokeBasinsSample() {
        val smokeMap = parseSmokeMap(sampleInput)

        val basins = findSmokeBasins(smokeMap)
        assertEquals(4, basins.size)

        val topLeftBasin = basins.single { Pair(1, 0) in it }
        assertEquals(3, topLeftBasin.size)

        val topRightBasin = basins.single { Pair(9, 0) in it }
        assertEquals(9, topRightBasin.size)

        val middleBasin = basins.single { Pair(2, 2) in it }
        assertEquals(14, middleBasin.size)

        val middleRightBasin = basins.single { Pair(6, 4) in it }
        assertEquals(9, middleRightBasin.size)
    }

    @Test
    fun testAnswerPart2Sample() {
        val smokeMap = parseSmokeMap(sampleInput)
        val basins = findSmokeBasins(smokeMap)
        val largestBasinsSizeProduct = getLargestBasinsSizeProduct(basins, n = 3)
        assertEquals(1134, largestBasinsSizeProduct)
    }

    @Test
    fun testAnswerPart2() {
        val fullInput = File("src/main/resources/year2021/day9/input").readLines()
        val smokeMap = parseSmokeMap(fullInput)
        val basins = findSmokeBasins(smokeMap)
        val largestBasinsSizeProduct = getLargestBasinsSizeProduct(basins, n = 3)
        assertEquals(856716, largestBasinsSizeProduct)
    }
}
