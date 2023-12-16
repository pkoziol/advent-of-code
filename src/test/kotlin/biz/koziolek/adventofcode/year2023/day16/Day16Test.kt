package biz.koziolek.adventofcode.year2023.day16

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2023")
internal class Day16Test {

    private val sampleInput = """
            .|...\....
            |.-.\.....
            .....|-...
            ........|.
            ..........
            .........\
            ..../.\\..
            .-.-/..|..
            .|....-|.\
            ..//.|....
        """.trimIndent().split("\n")

    @Test
    fun testParseMirrorContraption() {
        val mirrors = parseMirrorContraption(sampleInput)
        assertEquals(23, mirrors.size)
        assertEquals(SPLITTER_VERTICAL, mirrors[Coord(1, 0)])
        assertEquals(MIRROR_LEFT, mirrors[Coord(5, 0)])
        assertEquals(SPLITTER_VERTICAL, mirrors[Coord(0, 1)])
        assertEquals(SPLITTER_HORIZONTAL, mirrors[Coord(2, 1)])
        assertEquals(MIRROR_LEFT, mirrors[Coord(4, 1)])
        assertEquals(SPLITTER_VERTICAL, mirrors[Coord(5, 2)])
        assertEquals(SPLITTER_HORIZONTAL, mirrors[Coord(6, 2)])
        assertEquals(MIRROR_RIGHT, mirrors[Coord(2, 9)])
        assertEquals(MIRROR_RIGHT, mirrors[Coord(3, 9)])
    }

    @Test
    fun testShowPath() {
        val mirrors = parseMirrorContraption(sampleInput)
        val beamPath = simulateBeam(mirrors)
        assertEquals(
            """
                >|<<<\....
                |v-.\^....
                .v...|->>>
                .v...v^.|.
                .v...v^...
                .v...v^..\
                .v../2\\..
                <->-/vv|..
                .|<<<2-|.\
                .v//.|.v..
            """.trimIndent(),
            showPath(mirrors, beamPath)
        )
    }

    @Test
    fun testShowEnergizedTiles() {
        val mirrors = parseMirrorContraption(sampleInput)
        val beamPath = simulateBeam(mirrors)
        assertEquals(
            """
                ######....
                .#...#....
                .#...#####
                .#...##...
                .#...##...
                .#...##...
                .#..####..
                ########..
                .#######..
                .#...#.#..
            """.trimIndent(),
            showEnergizedTiles(beamPath)
        )
    }

    @Test
    fun testSampleAnswer1() {
        val mirrors = parseMirrorContraption(sampleInput)
        val beamPath = simulateBeam(mirrors)
        assertEquals(46, countEnergizedTiles(beamPath))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val mirrors = parseMirrorContraption(input)
        val beamPath = simulateBeam(mirrors)
        assertEquals(7242, countEnergizedTiles(beamPath))
    }
}
