package biz.koziolek.adventofcode.year2021.day23

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import kotlin.test.assertContains

@Tag("2021")
internal class Day23Test {

    private val sampleInput = """
        #############
        #...........#
        ###B#C#B#D###
          #A#D#C#A#
          #########
    """.trimIndent()

    @Test
    fun testParseInput() {
        val burrow = AmphipodBurrow.fromString(sampleInput)
        assertEquals(AmphipodBurrow(".......BCBDADCA"), burrow)
        assertFalse(burrow.isOrganized)
        assertEquals(".......", burrow.hallway)
        assertEquals("BA", burrow.roomContents('A'))
        assertEquals("CD", burrow.roomContents('B'))
        assertEquals("BC", burrow.roomContents('C'))
        assertEquals("DA", burrow.roomContents('D'))
        assertEquals(sampleInput, burrow.toString())

        val otherString = """
            #############
            #12.3.4.5.67#
            ###9#A#B#C###
              #D#E#F#G#
              #########
        """.trimIndent()
        val otherBurrow = AmphipodBurrow.fromString(otherString)
        assertEquals("1234567", otherBurrow.hallway)
        assertEquals("9D", otherBurrow.roomContents('A'))
        assertEquals("AE", otherBurrow.roomContents('B'))
        assertEquals("BF", otherBurrow.roomContents('C'))
        assertEquals("CG", otherBurrow.roomContents('D'))
        assertEquals(otherString, otherBurrow.toString())
    }

    @Test
    fun testFindValidMoves() {
        val burrow = AmphipodBurrow.fromString(sampleInput)
        val validMoves = burrow.generateValidMoves()
        assertEquals(7 * 4, validMoves.size)

        assertContains(
            validMoves,
            Move(
                AmphipodBurrow.fromString(
                    """
                    #############
                    #.B.........#
                    ###.#C#B#D###
                      #A#D#C#A#
                      #########
                """.trimIndent()
                ),
                cost = 20,
            )
        )

        assertContains(
            validMoves,
            Move(
                AmphipodBurrow.fromString(
                    """
                    #############
                    #...C.......#
                    ###B#.#B#D###
                      #A#D#C#A#
                      #########
                """.trimIndent()
                ),
                cost = 200,
            )
        )

        assertContains(
            validMoves,
            Move(
                AmphipodBurrow.fromString(
                    """
                    #############
                    #..........D#
                    ###B#C#B#.###
                      #A#D#C#A#
                      #########
                """.trimIndent()
                ),
                cost = 3000,
            )
        )
    }

    @Test
    fun testSampleAnswerPart1() {
        val burrow = AmphipodBurrow.fromString(sampleInput)
        val moves = findCheapestMovesToOrganise(burrow)
        println(toString(moves))

        assertEquals(12521, moves.sumOf { it.cost })
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val fullInput = findInput(object {})
        val burrow = AmphipodBurrow.fromString(fullInput.readText())
        val moves = findCheapestMovesToOrganise(burrow)
        println(toString(moves))

        assertEquals(13336, moves.sumOf { it.cost })
    }

    @Test
    fun testParseSampleInputPart2() {
        val burrow = AmphipodBurrow.fromString(unfold(sampleInput))
        assertFalse(burrow.isOrganized)
        assertEquals(".......", burrow.hallway)
        assertEquals("BDDA", burrow.roomContents('A'))
        assertEquals("CCBD", burrow.roomContents('B'))
        assertEquals("BBAC", burrow.roomContents('C'))
        assertEquals("DACA", burrow.roomContents('D'))
        assertEquals(
            """
            #############
            #...........#
            ###B#C#B#D###
              #D#C#B#A#
              #D#B#A#C#
              #A#D#C#A#
              #########
        """.trimIndent(), burrow.toString()
        )
    }

    @Test
    fun testSampleAnswerPart2() {
        val burrow = AmphipodBurrow.fromString(unfold(sampleInput))
        val moves = findCheapestMovesToOrganise(burrow)

        assertEquals(44169, moves.sumOf { it.cost })
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val fullInput = findInput(object {})
        val burrow = AmphipodBurrow.fromString(unfold(fullInput.readText()))
        val moves = findCheapestMovesToOrganise(burrow)

        assertEquals(53308, moves.sumOf { it.cost })
    }
}
