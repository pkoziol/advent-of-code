package biz.koziolek.adventofcode.year2023.day02

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2023")
internal class Day2Test {

    private val sampleInput = """
            Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
            Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
            Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
            Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
            Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
        """.trimIndent().split("\n")

    @Test
    fun testParse() {
        val games = parseGames(sampleInput)
        assertEquals(
            listOf(
                Game(id = 1, sets = listOf(
                    CubeSet(blue = 3, red = 4),
                    CubeSet(red = 1, green = 2, blue = 6),
                    CubeSet(green = 2),
                )),
                Game(id = 2, sets = listOf(
                    CubeSet(blue = 1, green = 2),
                    CubeSet(green = 3, blue = 4, red = 1),
                    CubeSet(green = 1, blue = 1),
                )),
                Game(id = 3, sets = listOf(
                    CubeSet(green = 8, blue = 6, red = 20),
                    CubeSet(blue = 5, red = 4, green = 13),
                    CubeSet(green = 5, red = 1),
                )),
                Game(id = 4, sets = listOf(
                    CubeSet(green = 1, red = 3, blue = 6),
                    CubeSet(green = 3, red = 6),
                    CubeSet(green = 3, blue = 15, red = 14),
                )),
                Game(id = 5, sets = listOf(
                    CubeSet(red = 6, blue = 1, green = 3),
                    CubeSet(blue = 2, red = 1, green = 2),
                )),
            ),
            games
        )
    }

    @Test
    fun testFindPossibleGames() {
        val games = parseGames(sampleInput)
        val possible = findPossibleGames(games, FULL_SET)
        assertEquals(listOf(1, 2, 5), possible.map { it.id })
        assertEquals(8, sumGameIds(possible))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val games = parseGames(input)
        val possible = findPossibleGames(games, FULL_SET)
        assertEquals(2076, sumGameIds(possible))
    }
}
