package biz.koziolek.adventofcode.year2021.day25

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput
import biz.koziolek.adventofcode.getHeight
import biz.koziolek.adventofcode.getWidth
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2021")
internal class Day25Test {

    private val sampleInput1 = """
        ...>>>>>...
    """.trimIndent()

    private val sampleInput2 = """
        ..........
        .>v....v..
        .......>..
        ..........
    """.trimIndent()

    private val sampleInput3 = """
        ...>...
        .......
        ......>
        v.....>
        ......>
        .......
        ..vvv..
    """.trimIndent()

    private val sampleInput4 = """
        v...>>.vv>
        .vv>>.vv..
        >>.>v>...v
        >>v>>.>.v.
        v>v.vv.v..
        >.>>..v...
        .vv..>.>v.
        v.v..>>v.v
        ....v..v.>
    """.trimIndent()

    @Test
    fun testParseSampleInputs() {
        val map1 = parseSeaCucumberMap(sampleInput1.split("\n"))
        assertEquals(11, map1.getWidth())
        assertEquals(1, map1.getHeight())
        assertEquals(sampleInput1, seaCucumberMapToString(map1))

        val map2 = parseSeaCucumberMap(sampleInput2.split("\n"))
        assertEquals(10, map2.getWidth())
        assertEquals(4, map2.getHeight())
        assertEquals(sampleInput2, seaCucumberMapToString(map2))

        val map3 = parseSeaCucumberMap(sampleInput3.split("\n"))
        assertEquals(7, map3.getWidth())
        assertEquals(7, map3.getHeight())
        assertEquals(sampleInput3, seaCucumberMapToString(map3))

        val map4 = parseSeaCucumberMap(sampleInput4.split("\n"))
        assertEquals(10, map4.getWidth())
        assertEquals(9, map4.getHeight())
        assertEquals(sampleInput4, seaCucumberMapToString(map4))
    }
    
    @Test
    fun testMove() {
        val map1 = parseSeaCucumberMap(sampleInput1.split("\n"))
        val expectedMap1 = mapOf(
            0 to """
                ...>>>>>...
            """,
            1 to """
                ...>>>>.>..
            """,
            2 to """
                ...>>>.>.>.
            """,
        )
        assertMapSteps(expectedMap1, map1)

        val map2 = parseSeaCucumberMap(sampleInput2.split("\n"))
        val expectedMap2 = mapOf(
            0 to """
                ..........
                .>v....v..
                .......>..
                ..........
            """,
            1 to """
                ..........
                .>........
                ..v....v>.
                ..........
            """,
        )
        assertMapSteps(expectedMap2, map2)

        val map3 = parseSeaCucumberMap(sampleInput3.split("\n"))
        val expectedMap3 = mapOf(
            0 to """
                ...>...
                .......
                ......>
                v.....>
                ......>
                .......
                ..vvv..
            """,
            1 to """
                ..vv>..
                .......
                >......
                v.....>
                >......
                .......
                ....v..
            """,
            2 to """
                ....v>.
                ..vv...
                .>.....
                ......>
                v>.....
                .......
                .......
            """,
            3 to """
                ......>
                ..v.v..
                ..>v...
                >......
                ..>....
                v......
                .......
            """,
            4 to """
                >......
                ..v....
                ..>.v..
                .>.v...
                ...>...
                .......
                v......
            """,
        )
        assertMapSteps(expectedMap3, map3)

        val map4 = parseSeaCucumberMap(sampleInput4.split("\n"))
        val expectedMap4 = mapOf(
            0 to """
                v...>>.vv>
                .vv>>.vv..
                >>.>v>...v
                >>v>>.>.v.
                v>v.vv.v..
                >.>>..v...
                .vv..>.>v.
                v.v..>>v.v
                ....v..v.>
            """,
            1 to """
                ....>.>v.>
                v.v>.>v.v.
                >v>>..>v..
                >>v>v>.>.v
                .>v.v...v.
                v>>.>vvv..
                ..v...>>..
                vv...>>vv.
                >.v.v..v.v
            """,
            2 to """
                >.v.v>>..v
                v.v.>>vv..
                >v>.>.>.v.
                >>v>v.>v>.
                .>..v....v
                .>v>>.v.v.
                v....v>v>.
                .vv..>>v..
                v>.....vv.
            """,
            3 to """
                v>v.v>.>v.
                v...>>.v.v
                >vv>.>v>..
                >>v>v.>.v>
                ..>....v..
                .>.>v>v..v
                ..v..v>vv>
                v.v..>>v..
                .v>....v..
            """,
            4 to """
                v>..v.>>..
                v.v.>.>.v.
                >vv.>>.v>v
                >>.>..v>.>
                ..v>v...v.
                ..>>.>vv..
                >.v.vv>v.v
                .....>>vv.
                vvv>...v..
            """,
            5 to """
                vv>...>v>.
                v.v.v>.>v.
                >.v.>.>.>v
                >v>.>..v>>
                ..v>v.v...
                ..>.>>vvv.
                .>...v>v..
                ..v.v>>v.v
                v.v.>...v.
            """,
            10 to """
                ..>..>>vv.
                v.....>>.v
                ..v.v>>>v>
                v>.>v.>>>.
                ..v>v.vv.v
                .v.>>>.v..
                v.v..>v>..
                ..v...>v.>
                .vv..v>vv.
            """,
            20 to """
                v>.....>>.
                >vv>.....v
                .>v>v.vv>>
                v>>>v.>v.>
                ....vv>v..
                .v.>>>vvv.
                ..v..>>vv.
                v.v...>>.v
                ..v.....v>
            """,
            30 to """
                .vv.v..>>>
                v>...v...>
                >.v>.>vv.>
                >v>.>.>v.>
                .>..v.vv..
                ..v>..>>v.
                ....v>..>v
                v.v...>vv>
                v.v...>vvv
            """,
            40 to """
                >>v>v..v..
                ..>>v..vv.
                ..>>>v.>.v
                ..>>>>vvv>
                v.....>...
                v.v...>v>>
                >vv.....v>
                .>v...v.>v
                vvv.v..v.>
            """,
            50 to """
                ..>>v>vv.v
                ..v.>>vv..
                v.>>v>>v..
                ..>>>>>vv.
                vvv....>vv
                ..v....>>>
                v>.......>
                .vv>....v>
                .>v.vv.v..
            """,
            55 to """
                ..>>v>vv..
                ..v.>>vv..
                ..>>v>>vv.
                ..>>>>>vv.
                v......>vv
                v>v....>>v
                vvv...>..>
                >vv.....>.
                .>v.vv.v..
            """,
            56 to """
                ..>>v>vv..
                ..v.>>vv..
                ..>>v>>vv.
                ..>>>>>vv.
                v......>vv
                v>v....>>v
                vvv....>.>
                >vv......>
                .>v.vv.v..
            """,
            57 to """
                ..>>v>vv..
                ..v.>>vv..
                ..>>v>>vv.
                ..>>>>>vv.
                v......>vv
                v>v....>>v
                vvv.....>>
                >vv......>
                .>v.vv.v..
            """,
            58 to """
                ..>>v>vv..
                ..v.>>vv..
                ..>>v>>vv.
                ..>>>>>vv.
                v......>vv
                v>v....>>v
                vvv.....>>
                >vv......>
                .>v.vv.v..
            """,
            59 to """
                ..>>v>vv..
                ..v.>>vv..
                ..>>v>>vv.
                ..>>>>>vv.
                v......>vv
                v>v....>>v
                vvv.....>>
                >vv......>
                .>v.vv.v..
            """,
            60 to """
                ..>>v>vv..
                ..v.>>vv..
                ..>>v>>vv.
                ..>>>>>vv.
                v......>vv
                v>v....>>v
                vvv.....>>
                >vv......>
                .>v.vv.v..
            """,
        )
        assertMapSteps(expectedMap4, map4)
    }

    private fun assertMapSteps(expectedSteps: Map<Int, String>, startingMap: Map<Coord, Char>) {
        var map = startingMap
        val maxStep = expectedSteps.maxOf { it.key }

        for (step in 0..maxStep) {
            val expectedMap = expectedSteps[step]
            if (expectedMap != null) {
                val title = "After step $step:"
                println("$title\n${seaCucumberMapToString(map)}\n")

                assertMap(expectedMap, map, title)
            }

            map = moveSeaCucumbers(map)
        }
    }

    private fun assertMap(expectedMap: String, actualMap: Map<Coord, Char>, message: String = "") {
        assertEquals(expectedMap.trimIndent(), seaCucumberMapToString(actualMap), message)
    }

    @Test
    fun testMoveUntilStop() {
        val map = parseSeaCucumberMap(sampleInput4.split("\n"))
        val (stepNumber, finalMap) = moveUntilStop(map)

        assertEquals(58, stepNumber)
        assertMap("""
            ..>>v>vv..
            ..v.>>vv..
            ..>>v>>vv.
            ..>>>>>vv.
            v......>vv
            v>v....>>v
            vvv.....>>
            >vv......>
            .>v.vv.v..
        """, finalMap)
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val fullInput = findInput(object {}).readLines()
        val map = parseSeaCucumberMap(fullInput)
        val (stepNumber, _) = moveUntilStop(map)
        assertEquals(432, stepNumber)
    }
}
