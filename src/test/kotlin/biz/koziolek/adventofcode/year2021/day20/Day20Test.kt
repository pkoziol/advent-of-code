package biz.koziolek.adventofcode.year2021.day20

import biz.koziolek.adventofcode.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2021")
internal class Day20Test {

    private val sampleInput = """
        ..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..###..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#..#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#......#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.....####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.......##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#

        #..#.
        #....
        ##..#
        ..#..
        ..###
    """.trimIndent().split("\n")

    @Test
    fun testParseInput() {
        val lookupTable = parseLookupTable(sampleInput)
        assertEquals(512, lookupTable.size)
        assertEquals(false, lookupTable[0])
        assertEquals(false, lookupTable[1])
        assertEquals(true, lookupTable[2])
        assertEquals(false, lookupTable[3])
        assertEquals(true, lookupTable[4])
        assertEquals(false, lookupTable[5])
        assertEquals(false, lookupTable[6])
        assertEquals(true, lookupTable[7])
        assertEquals(true, lookupTable[8])
        assertEquals(true, lookupTable[9])

        val inputImage = parseInputImage(sampleInput)
        assertEquals(5, inputImage.finiteWidth)
        assertEquals(5, inputImage.finiteHeight)

        assertEquals(true, inputImage[Coord(0, 0)])
        assertEquals(false, inputImage[Coord(1, 0)])
        assertEquals(false, inputImage[Coord(2, 0)])
        assertEquals(true, inputImage[Coord(3, 0)])
        assertEquals(false, inputImage[Coord(4, 0)])

        assertEquals(true, inputImage[Coord(0, 1)])
        assertEquals(false, inputImage[Coord(1, 1)])
        assertEquals(false, inputImage[Coord(2, 1)])
        assertEquals(false, inputImage[Coord(3, 1)])
        assertEquals(false, inputImage[Coord(4, 1)])

        assertEquals(true, inputImage[Coord(0, 2)])
        assertEquals(true, inputImage[Coord(1, 2)])
        assertEquals(false, inputImage[Coord(2, 2)])
        assertEquals(false, inputImage[Coord(3, 2)])
        assertEquals(true, inputImage[Coord(4, 2)])

        assertEquals(false, inputImage[Coord(0, 3)])
        assertEquals(false, inputImage[Coord(1, 3)])
        assertEquals(true, inputImage[Coord(2, 3)])
        assertEquals(false, inputImage[Coord(3, 3)])
        assertEquals(false, inputImage[Coord(4, 3)])

        assertEquals(false, inputImage[Coord(0, 4)])
        assertEquals(false, inputImage[Coord(1, 4)])
        assertEquals(true, inputImage[Coord(2, 4)])
        assertEquals(true, inputImage[Coord(3, 4)])
        assertEquals(true, inputImage[Coord(4, 4)])
        
        assertEquals(
            """
                ...........
                ...........
                ...........
                ...#..#....
                ...#.......
                ...##..#...
                .....#.....
                .....###...
                ...........
                ...........
                ...........
            """.trimIndent(),
            inputImage.toString()
        )
    }

    @Test
    fun testEnhanceImage() {
        val lookupTable = parseLookupTable(sampleInput)
        val image0 = parseInputImage(sampleInput)

        val image1 = image0.enhance(lookupTable)
        assertEquals(
            """
                .............
                .............
                .............
                ....##.##....
                ...#..#.#....
                ...##.#..#...
                ...####..#...
                ....#..##....
                .....##..#...
                ......#.#....
                .............
                .............
                .............
            """.trimIndent(),
            image1.toString()
        )

        val image2 = image1.enhance(lookupTable)
        assertEquals(
            """
                ...............
                ...............
                ...............
                ..........#....
                ....#..#.#.....
                ...#.#...###...
                ...#...##.#....
                ...#.....#.#...
                ....#.#####....
                .....#.#####...
                ......##.##....
                .......###.....
                ...............
                ...............
                ...............
            """.trimIndent(),
            image2.toString()
        )

        assertEquals(35, image2.countLitPixels())
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val fullInput = findInput(object {}).readLines()
        val lookupTable = parseLookupTable(fullInput)
        val image0 = parseInputImage(fullInput)
        val image2 = enhanceNTimes(image0, lookupTable, n = 2)

        assertEquals(4917, image2.countLitPixels())
    }

    @Test
    fun testEnhance50Times() {
        val lookupTable = parseLookupTable(sampleInput)
        val image0 = parseInputImage(sampleInput)
        val image50 = enhanceNTimes(image0, lookupTable, n = 50)

        assertEquals(3351, image50.countLitPixels())
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val fullInput = findInput(object {}).readLines()
        val lookupTable = parseLookupTable(fullInput)
        val image0 = parseInputImage(fullInput)
        val image50 = enhanceNTimes(image0, lookupTable, n = 50)

        assertEquals(16389, image50.countLitPixels())
    }
}
