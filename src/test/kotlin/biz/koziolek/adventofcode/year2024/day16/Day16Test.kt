package biz.koziolek.adventofcode.year2024.day16

import biz.koziolek.adventofcode.AsciiColor
import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.Direction
import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2024")
internal class Day16Test {

    private val sampleInputA = """
            ###############
            #.......#....E#
            #.#.###.#.###.#
            #.....#.#...#.#
            #.###.#####.#.#
            #.#.#.......#.#
            #.#.#####.###.#
            #...........#.#
            ###.#.#####.#.#
            #...#.....#.#.#
            #.#.#.###.#.#.#
            #.....#...#.#.#
            #.###.#.#.#.#.#
            #S..#.....#...#
            ###############
        """.trimIndent().split("\n")

    private val sampleInputB = """
            #################
            #...#...#...#..E#
            #.#.#.#.#.#.#.#.#
            #.#.#.#...#...#.#
            #.#.#.#.###.#.#.#
            #...#.#.#.....#.#
            #.#.#.#.#.#####.#
            #.#...#.#.#.....#
            #.#.#####.#.###.#
            #.#.#.......#...#
            #.#.###.#####.###
            #.#.#...#.....#.#
            #.#.#.#####.###.#
            #.#.#.........#.#
            #.#.#.#########.#
            #S#.............#
            #################
        """.trimIndent().split("\n")

    @Test
    fun testParse() {
        val maze = parseMaze(sampleInputA)
        assertEquals(Coord(1, 13), maze.start)
        assertEquals(Coord(13, 1), maze.end)
        assertEquals(sampleInputA.joinToString("\n"), AsciiColor.cleanUp(maze.toString()))
    }

    @Test
    fun testPathToDirections() {
        val maze = Maze(
            map = mapOf(
                Coord(-1, -1) to START,
                Coord(4, 4) to END,
            ),
            start = Coord(-1, -1),
            end = Coord(4, 4),
        )
        val path = MazePath(maze, listOf(
            Coord(0, 0),
            Coord(1, 0),
            Coord(2, 0),
            Coord(2, 1),
            Coord(2, 2),
            Coord(3, 2),
            Coord(3, 3),
            Coord(2, 3),
            Coord(1, 3),
            Coord(1, 2),
        ))
        val directions = path.toDirections()

        assertEquals(listOf(
            Direction.EAST,
            Direction.EAST,
            Direction.SOUTH,
            Direction.SOUTH,
            Direction.EAST,
            Direction.SOUTH,
            Direction.WEST,
            Direction.WEST,
            Direction.NORTH,
            Direction.NORTH,
        ), directions)

        assertEquals(
            """
                S.....
                .>>v..
                ...v..
                ..^>v.
                ..^<<.
                .....E
            """.trimIndent(),
            AsciiColor.cleanUp(path.toString())
        )
    }

    @Test
    fun testSampleAnswer1A() {
        val maze = parseMaze(sampleInputA)
        val lowestScorePath = maze.findLowestScorePath()
        assertEquals(7036, lowestScorePath.score)
    }

    @Test
    fun testSampleAnswer1B() {
        val maze = parseMaze(sampleInputB)
        val lowestScorePath = maze.findLowestScorePath()
        assertEquals(11048, lowestScorePath.score)
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val maze = parseMaze(input)
        val lowestScorePath = maze.findLowestScorePath()
        assertNotEquals(114428, lowestScorePath.score)
        assertTrue(lowestScorePath.score < 114428)
        assertNotEquals(88472, lowestScorePath.score)
        assertTrue(lowestScorePath.score < 88472)
        assertEquals(88468, lowestScorePath.score)
    }

    @Test
    fun testSampleAnswer2B() {
        val maze = parseMaze(sampleInputB)
        val paths = maze.findPaths()
        val minScore = paths.minOf { it.score }

        paths.sortedBy { it.score }
            .filter { it.score == minScore }
            .flatMapIndexed { index, path ->
                println("#$index score: ${path.score} length: ${path.coords.size}")
                path.coords
            }
            .distinct()
            .let {
                println("Unique coords: ${it.size}")
            }
    }


    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val maze = parseMaze(input)
        val paths = maze.findPaths()
        val minScore = paths.minOf { it.score }
        val uniqueCoords = paths.sortedBy { it.score }
            .filter { it.score == minScore }
            .flatMap { path -> path.coords }
            .distinct()

        assertEquals(616, uniqueCoords.size)
    }
}
