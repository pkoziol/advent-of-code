package biz.koziolek.adventofcode.year2023.day13

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2023")
internal class Day13Test {

    private val sampleInput = """
            #.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.

            #...##..#
            #....#..#
            ..##..###
            #####.##.
            #####.##.
            ..##..###
            #....#..#
        """.trimIndent().split("\n")

    @Test
    fun testParseNotes202313() {
        val notes = parseNotes202313(sampleInput)
        assertEquals(
            listOf(
                RocksPattern(listOf(
                    "#.##..##.",
                    "..#.##.#.",
                    "##......#",
                    "##......#",
                    "..#.##.#.",
                    "..##..##.",
                    "#.#.##.#.",
                )),
                RocksPattern(listOf(
                    "#...##..#",
                    "#....#..#",
                    "..##..###",
                    "#####.##.",
                    "#####.##.",
                    "..##..###",
                    "#....#..#",
                ))
            ),
            notes.patterns
        )
    }

    @Test
    fun testFindReflection() {
        val notes = parseNotes202313(sampleInput)

//        println(notes.patterns[0].toStringWithReflection())
        assertEquals(HorizontalReflection(afterColumn = 4), notes.patterns[0].findReflection())

//        println(notes.patterns[1].toStringWithReflection())
        assertEquals(VerticalReflection(afterRow = 3), notes.patterns[1].findReflection())

        assertEquals(HorizontalReflection(afterColumn = 0), RocksPattern.fromString("""
            ....#..#..#..
            ####.######..
            ....######.#.
            ...##.##.#.#.
            ##......#.#..
            ..##.##..#..#
            ......###.#..
            ##...##.##.##
            ##...##.##.##
            ......###.#..
            ..##.##..#..#
            ##......#.#..
            ...##.##.#.#.
            .....#####.#.
            ####.######..
            ....#..#..#..
            ..##.#...#..#
        """.trimIndent()).findReflection())

        assertEquals(HorizontalReflection(afterColumn = 0), RocksPattern.fromString("""
            ...#...##...#..
            ..#....##....#.
            ..##.#....#.##.
            ##.....##.....#
            ##.##.#..#.##.#
            ##.##......##.#
            #####.####.####
            ..#..######....
            ..#.########.#.
        """.trimIndent()).findReflection())

        assertEquals(
            setOf(
                VerticalReflection(afterRow = 0),
                VerticalReflection(afterRow = 3),
            ),
            RocksPattern.fromString("""
                #....#..#
                #....#..#
                ..##..###
                #####.##.
                #####.##.
                ..##..###
                #....#..#
            """.trimIndent()).findReflections()
        )
    }

    @Test
    fun testSampleAnswer1() {
        val notes = parseNotes202313(sampleInput)
        assertEquals(405, notes.summarize())
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val notes = parseNotes202313(input)
        assertEquals(36015, notes.summarize())
    }

    @Test
    fun testFixSmudge() {
        val notes = parseNotes202313(sampleInput)

        assertEquals(
            RocksPattern.fromString("""
                ..##..##.
                ..#.##.#.
                ##......#
                ##......#
                ..#.##.#.
                ..##..##.
                #.#.##.#.
            """.trimIndent()),
            notes.patterns[0].fixSmudge()
        )
        assertEquals(
            setOf(
                HorizontalReflection(afterColumn = 4),
                VerticalReflection(afterRow = 2),
            ),
            notes.patterns[0].fixSmudge()?.findReflections()
        )

        assertEquals(
            RocksPattern.fromString("""
                #....#..#
                #....#..#
                ..##..###
                #####.##.
                #####.##.
                ..##..###
                #....#..#
            """.trimIndent()),
            notes.patterns[1].fixSmudge()
        )
        assertEquals(
            setOf(
                VerticalReflection(afterRow = 0),
                VerticalReflection(afterRow = 3),
            ),
            notes.patterns[1].fixSmudge()?.findReflections()
        )

        assertEquals(
            RocksPattern.fromString("""
                .#.#..#.#.#..
                ...###.#...##
                ###..####..##
                ..##.##..####
                #...#.....###
                .##..#...#.##
                #.##.##.#....
                ..#..#..#....
                ..#..#..#....
            """.trimIndent()),
            RocksPattern.fromString("""
                .#.#..#.#.#..
                ...###.#...##
                ###..####..##
                ..##.##..####
                #...#.....#.#
                .##..#...#.##
                #.##.##.#....
                ..#..#..#....
                ..#..#..#....
            """.trimIndent()).fixSmudge()
        )
        assertEquals(
            setOf(
                VerticalReflection(afterRow = 0),
                VerticalReflection(afterRow = 3),
            ),
            notes.patterns[1].fixSmudge()?.findReflections()
        )
    }

    @Test
    fun testSampleAnswer2() {
        val notes = parseNotes202313(sampleInput)
        assertEquals(400, notes.fixSmudges().summarize(previousNotes = notes))
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val notes = parseNotes202313(input)
        assertEquals(35335, notes.fixSmudges().summarize(previousNotes = notes))
    }
}
