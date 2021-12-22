package biz.koziolek.adventofcode.year2021.day22

import biz.koziolek.adventofcode.Coord3d
import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day22Test {

    private val sampleInput1 = """
        on x=10..12,y=10..12,z=10..12
        on x=11..13,y=11..13,z=11..13
        off x=9..11,y=9..11,z=9..11
        on x=10..10,y=10..10,z=10..10
    """.trimIndent().split("\n")

    private val sampleInput2 = """
        on x=-20..26,y=-36..17,z=-47..7
        on x=-20..33,y=-21..23,z=-26..28
        on x=-22..28,y=-29..23,z=-38..16
        on x=-46..7,y=-6..46,z=-50..-1
        on x=-49..1,y=-3..46,z=-24..28
        on x=2..47,y=-22..22,z=-23..27
        on x=-27..23,y=-28..26,z=-21..29
        on x=-39..5,y=-6..47,z=-3..44
        on x=-30..21,y=-8..43,z=-13..34
        on x=-22..26,y=-27..20,z=-29..19
        off x=-48..-32,y=26..41,z=-47..-37
        on x=-12..35,y=6..50,z=-50..-2
        off x=-48..-32,y=-32..-16,z=-15..-5
        on x=-18..26,y=-33..15,z=-7..46
        off x=-40..-22,y=-38..-28,z=23..41
        on x=-16..35,y=-41..10,z=-47..6
        off x=-32..-23,y=11..30,z=-14..3
        on x=-49..-5,y=-3..45,z=-29..18
        off x=18..30,y=-20..-8,z=-3..13
        on x=-41..9,y=-7..43,z=-33..15
        on x=-54112..-39298,y=-85059..-49293,z=-27449..7877
        on x=967..23432,y=45373..81175,z=27513..53682
    """.trimIndent().split("\n")

    @Test
    fun testParseInput() {
        val rebootSteps = parseReactorRebootSteps(sampleInput1)
        assertEquals(4, rebootSteps.size)

        assertEquals(true, rebootSteps[0].turnOn)
        assertEquals(Coord3d(10, 10, 10), rebootSteps[0].cuboid.from)
        assertEquals(Coord3d(12, 12, 12), rebootSteps[0].cuboid.to)

        assertEquals(true, rebootSteps[1].turnOn)
        assertEquals(Coord3d(11, 11, 11), rebootSteps[1].cuboid.from)
        assertEquals(Coord3d(13, 13, 13), rebootSteps[1].cuboid.to)

        assertEquals(false, rebootSteps[2].turnOn)
        assertEquals(Coord3d(9, 9, 9), rebootSteps[2].cuboid.from)
        assertEquals(Coord3d(11, 11, 11), rebootSteps[2].cuboid.to)

        assertEquals(true, rebootSteps[3].turnOn)
        assertEquals(Coord3d(10, 10, 10), rebootSteps[3].cuboid.from)
        assertEquals(Coord3d(10, 10, 10), rebootSteps[3].cuboid.to)
    }

    @Test
    fun testGetCubes() {
        val rebootSteps = parseReactorRebootSteps(sampleInput1)

        assertEquals(
            setOf(
                Coord3d(10, 10, 10),
                Coord3d(10, 10, 11),
                Coord3d(10, 10, 12),
                Coord3d(10, 11, 10),
                Coord3d(10, 11, 11),
                Coord3d(10, 11, 12),
                Coord3d(10, 12, 10),
                Coord3d(10, 12, 11),
                Coord3d(10, 12, 12),
                Coord3d(11, 10, 10),
                Coord3d(11, 10, 11),
                Coord3d(11, 10, 12),
                Coord3d(11, 11, 10),
                Coord3d(11, 11, 11),
                Coord3d(11, 11, 12),
                Coord3d(11, 12, 10),
                Coord3d(11, 12, 11),
                Coord3d(11, 12, 12),
                Coord3d(12, 10, 10),
                Coord3d(12, 10, 11),
                Coord3d(12, 10, 12),
                Coord3d(12, 11, 10),
                Coord3d(12, 11, 11),
                Coord3d(12, 11, 12),
                Coord3d(12, 12, 10),
                Coord3d(12, 12, 11),
                Coord3d(12, 12, 12),
            ),
            rebootSteps[0].cuboid.getCubes().toSet(),
        )

        assertEquals(
            setOf(
                Coord3d(11, 11, 11),
                Coord3d(11, 11, 12),
                Coord3d(11, 11, 13),
                Coord3d(11, 12, 11),
                Coord3d(11, 12, 12),
                Coord3d(11, 12, 13),
                Coord3d(11, 13, 11),
                Coord3d(11, 13, 12),
                Coord3d(11, 13, 13),
                Coord3d(12, 11, 11),
                Coord3d(12, 11, 12),
                Coord3d(12, 11, 13),
                Coord3d(12, 12, 11),
                Coord3d(12, 12, 12),
                Coord3d(12, 12, 13),
                Coord3d(12, 13, 11),
                Coord3d(12, 13, 12),
                Coord3d(12, 13, 13),
                Coord3d(13, 11, 11),
                Coord3d(13, 11, 12),
                Coord3d(13, 11, 13),
                Coord3d(13, 12, 11),
                Coord3d(13, 12, 12),
                Coord3d(13, 12, 13),
                Coord3d(13, 13, 11),
                Coord3d(13, 13, 12),
                Coord3d(13, 13, 13),
            ),
            rebootSteps[1].cuboid.getCubes().toSet(),
        )

        assertEquals(
            setOf(
                Coord3d(9, 9, 9),
                Coord3d(9, 9, 10),
                Coord3d(9, 9, 11),
                Coord3d(9, 10, 9),
                Coord3d(9, 10, 10),
                Coord3d(9, 10, 11),
                Coord3d(9, 11, 9),
                Coord3d(9, 11, 10),
                Coord3d(9, 11, 11),
                Coord3d(10, 9, 9),
                Coord3d(10, 9, 10),
                Coord3d(10, 9, 11),
                Coord3d(10, 10, 9),
                Coord3d(10, 10, 10),
                Coord3d(10, 10, 11),
                Coord3d(10, 11, 9),
                Coord3d(10, 11, 10),
                Coord3d(10, 11, 11),
                Coord3d(11, 9, 9),
                Coord3d(11, 9, 10),
                Coord3d(11, 9, 11),
                Coord3d(11, 10, 9),
                Coord3d(11, 10, 10),
                Coord3d(11, 10, 11),
                Coord3d(11, 11, 9),
                Coord3d(11, 11, 10),
                Coord3d(11, 11, 11),
            ),
            rebootSteps[2].cuboid.getCubes().toSet(),
        )

        assertEquals(
            setOf(
                Coord3d(10, 10, 10),
            ),
            rebootSteps[3].cuboid.getCubes().toSet(),
        )
    }

    @Test
    fun testExecuteRebootSteps() {
        val rebootSteps1 = parseReactorRebootSteps(sampleInput1)
        val onCubes1 = executeRebootSteps(rebootSteps1, min = -50, max = 50)
        assertEquals(39, onCubes1)

        val rebootSteps2 = parseReactorRebootSteps(sampleInput2)
        val onCubes2 = executeRebootSteps(rebootSteps2, min = -50, max = 50)
        assertEquals(590784, onCubes2)
    }

    @Test
    fun testAnswer1() {
        val fullInput = findInput(object {}).readLines()
        val rebootSteps = parseReactorRebootSteps(fullInput)
        val onCubes = executeRebootSteps(rebootSteps, min = -50, max = 50)
        assertEquals(647062, onCubes)
    }
}
