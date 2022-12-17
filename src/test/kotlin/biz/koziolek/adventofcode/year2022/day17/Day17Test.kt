package biz.koziolek.adventofcode.year2022.day17

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2022")
internal class Day17Test {

    private val sampleInput = """
            >>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>
        """.trimIndent()

    @Test
    fun testDropRock() {
        val jetPattern = sampleInput
        val chamber = Chamber(width = 7, jetPattern = jetPattern)
        assertEquals("""
            +-------+
        """.trimIndent(), chamber.toString())

        val chamber1 = chamber.dropRock(ROCKS[0])
        assertEquals("""
            |..####.|
            +-------+
        """.trimIndent(), chamber1.toString())

        val chamber2 = chamber1.dropRock(ROCKS[1])
        assertEquals("""
            |...#...|
            |..###..|
            |...#...|
            |..####.|
            +-------+
        """.trimIndent(), chamber2.toString())

        val chamber3 = chamber2.dropRock(ROCKS[2])
        assertEquals("""
            |..#....|
            |..#....|
            |####...|
            |..###..|
            |...#...|
            |..####.|
            +-------+
        """.trimIndent(), chamber3.toString())

        val chamber4 = chamber3.dropRock(ROCKS[3])
        assertEquals("""
            |....#..|
            |..#.#..|
            |..#.#..|
            |#####..|
            |..###..|
            |...#...|
            |..####.|
            +-------+
        """.trimIndent(), chamber4.toString())

        val chamber5 = chamber4.dropRock(ROCKS[4])
        assertEquals("""
            |....##.|
            |....##.|
            |....#..|
            |..#.#..|
            |..#.#..|
            |#####..|
            |..###..|
            |...#...|
            |..####.|
            +-------+
        """.trimIndent(), chamber5.toString())

        val chamber6 = chamber5.dropRock(ROCKS[0])
        assertEquals("""
            |.####..|
            |....##.|
            |....##.|
            |....#..|
            |..#.#..|
            |..#.#..|
            |#####..|
            |..###..|
            |...#...|
            |..####.|
            +-------+
        """.trimIndent(), chamber6.toString())

        val chamber7 = chamber6.dropRock(ROCKS[1])
        assertEquals("""
            |..#....|
            |.###...|
            |..#....|
            |.####..|
            |....##.|
            |....##.|
            |....#..|
            |..#.#..|
            |..#.#..|
            |#####..|
            |..###..|
            |...#...|
            |..####.|
            +-------+
        """.trimIndent(), chamber7.toString())

        val chamber8 = chamber7.dropRock(ROCKS[2])
        assertEquals("""
            |.....#.|
            |.....#.|
            |..####.|
            |.###...|
            |..#....|
            |.####..|
            |....##.|
            |....##.|
            |....#..|
            |..#.#..|
            |..#.#..|
            |#####..|
            |..###..|
            |...#...|
            |..####.|
            +-------+
        """.trimIndent(), chamber8.toString())

        val chamber9 = chamber8.dropRock(ROCKS[3])
        assertEquals("""
            |....#..|
            |....#..|
            |....##.|
            |....##.|
            |..####.|
            |.###...|
            |..#....|
            |.####..|
            |....##.|
            |....##.|
            |....#..|
            |..#.#..|
            |..#.#..|
            |#####..|
            |..###..|
            |...#...|
            |..####.|
            +-------+
        """.trimIndent(), chamber9.toString())

        val chamber10 = chamber9.dropRock(ROCKS[4])
        assertEquals("""
            |....#..|
            |....#..|
            |....##.|
            |##..##.|
            |######.|
            |.###...|
            |..#....|
            |.####..|
            |....##.|
            |....##.|
            |....#..|
            |..#.#..|
            |..#.#..|
            |#####..|
            |..###..|
            |...#...|
            |..####.|
            +-------+
        """.trimIndent(), chamber10.toString())
        assertEquals(chamber10, chamber.dropManyRocks(count = 10))
    }

    @Test
    fun testSample1() {
        val jetPattern = sampleInput
        val chamber = Chamber(width = 7, jetPattern = jetPattern)
        val chamber2022 = chamber.dropManyRocks(count = 2022)
        assertEquals(3068, chamber2022.height)
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val jetPattern = findInput(object {}).bufferedReader().readLine()
        val chamber = Chamber(width = 7, jetPattern = jetPattern)
        val chamber2022 = chamber.dropManyRocks(count = 2022)
        assertEquals(3166, chamber2022.height)
    }
}
