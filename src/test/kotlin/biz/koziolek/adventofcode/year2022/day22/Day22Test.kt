package biz.koziolek.adventofcode.year2022.day22

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2022")
internal class Day22Test {

    private val sampleInput = """
                    ...#
                    .#..
                    #...
                    ....
            ...#.......#
            ........#...
            ..#....#....
            ..........#.
                    ...#....
                    .....#..
                    .#......
                    ......#.
            
            10R5L5R10L4R5L5
        """.trimIndent().split("\n")

    @Test
    fun testParseForceFieldNotes() {
        val forceFieldNotes = parseForceFieldNotes(sampleInput)
        assertEquals(96, forceFieldNotes.board.size)
        assertEquals(
            """
                        ...#    
                        .#..    
                        #...    
                        ....    
                ...#.......#    
                ........#...    
                ..#....#....    
                ..........#.    
                        ...#....
                        .....#..
                        .#......
                        ......#.
            """.trimIndent(),
            visualizeForceShieldBoard(forceFieldNotes.board)
        )

        assertEquals(
            listOf(
                10 to TurnDirection.RIGHT,
                5 to TurnDirection.LEFT,
                5 to TurnDirection.RIGHT,
                10 to TurnDirection.LEFT,
                4 to TurnDirection.RIGHT,
                5 to TurnDirection.LEFT,
                5 to null,
            ),
            forceFieldNotes.path
        )
    }

    @Test
    fun testFollowPath() {
        val forceFieldNotes = parseForceFieldNotes(sampleInput)
        val (position, facing) = followPath(forceFieldNotes.board, forceFieldNotes.path)
        assertEquals(Coord(8, 6), position)
        assertEquals(Facing.RIGHT, facing)
    }

    @Test
    fun testGetPassword() {
        assertEquals(6032, getPassword(Coord(8, 6), Facing.RIGHT))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val forceFieldNotes = parseForceFieldNotes(input)
        val (position, facing) = followPath(forceFieldNotes.board, forceFieldNotes.path)
        val password = getPassword(position, facing)
        assertEquals(65368, password)
    }
}
