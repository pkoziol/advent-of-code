package biz.koziolek.adventofcode.year2021.day13

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day13Test {

    private val sampleInput = """
        6,10
        0,14
        9,10
        0,3
        10,4
        4,11
        6,0
        6,12
        4,1
        0,13
        10,12
        3,4
        3,0
        8,4
        1,10
        2,14
        8,10
        9,0
        
        fold along y=7
        fold along x=5
    """.trimIndent().split("\n")

    @Test
    fun testParseTransparentDots() {
        val dotsMap = parseTransparentPaper(sampleInput)
        val expectedStr = """
            ...#..#..#.
            ....#......
            ...........
            #..........
            ...#....#.#
            ...........
            ...........
            ...........
            ...........
            ...........
            .#....#.##.
            ....#......
            ......#...#
            #..........
            #.#........
        """.trimIndent()

        assertEquals(expectedStr, toString(dotsMap))
    }

    @Test
    fun testParseFoldInstructions() {
        val foldInstructions = parseFoldInstructions(sampleInput)
        assertEquals(2, foldInstructions.size)
        assertEquals(Pair("up", 7), foldInstructions[0])
        assertEquals(Pair("left", 5), foldInstructions[1])
    }

    @Test
    fun testFolding() {
        val dotsMap = parseTransparentPaper(sampleInput)
        assertEquals(18, dotsMap.size)

        val foldedUp = foldUp(dotsMap, y = 7)

        val expectedStr1 = """
            #.##..#..#.
            #...#......
            ......#...#
            #...#......
            .#.#..#.###
        """.trimIndent()
        assertEquals(expectedStr1, toString(foldedUp))
        assertEquals(17, foldedUp.size)

        val foldedLeft = foldLeft(foldedUp, x = 5)

        val expectedStr2 = """
            #####
            #...#
            #...#
            #...#
            #####
        """.trimIndent()
        assertEquals(expectedStr2, toString(foldedLeft))
        assertEquals(17, foldedUp.size)
    }

    @Test
    fun testAnswer1() {
        val fullInput = findInput(object {}).readLines()
        val dotsMap = parseTransparentPaper(fullInput)
        val foldInstructions = parseFoldInstructions(fullInput)
        val foldedOnce = fold(dotsMap, foldInstructions.take(1))

        assertEquals(701, foldedOnce.size)
    }

    @Test
    fun testAnswer2() {
        val fullInput = findInput(object {}).readLines()
        val dotsMap = parseTransparentPaper(fullInput)
        val foldInstructions = parseFoldInstructions(fullInput)
        val fullyFolded = fold(dotsMap, foldInstructions)

        val expectedStr = """
            ####.###..####.#..#.###..####...##.#...
            #....#..#.#....#.#..#..#.#.......#.#...
            ###..#..#.###..##...###..###.....#.#...
            #....###..#....#.#..#..#.#.......#.#...
            #....#....#....#.#..#..#.#....#..#.#...
            #....#....####.#..#.###..####..##..####
        """.trimIndent()
        assertEquals(expectedStr, toString(fullyFolded))
    }
}
