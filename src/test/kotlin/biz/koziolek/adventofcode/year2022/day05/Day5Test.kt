package biz.koziolek.adventofcode.year2022.day05

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2022")
internal class Day5Test {

    private val sampleInput = """
                [D]    
            [N] [C]    
            [Z] [M] [P]
             1   2   3 
            
            move 1 from 2 to 1
            move 3 from 1 to 3
            move 2 from 2 to 1
            move 1 from 1 to 2
        """.trimIndent().split("\n")

    @Test
    fun testParseCargo() {
        val (stacks, instructions) = parseCargo(sampleInput)

        assertEquals(
            """
                    [D]    
                [N] [C]    
                [Z] [M] [P]
                 1   2   3 
            """.trimIndent(),
            stacks.toString()
        )

        assertEquals(4, instructions.size)
        assertEquals(1, instructions[0].count)
        assertEquals(2, instructions[0].from)
        assertEquals(1, instructions[0].to)
        assertEquals(3, instructions[1].count)
        assertEquals(1, instructions[1].from)
        assertEquals(3, instructions[1].to)
        assertEquals(2, instructions[2].count)
        assertEquals(2, instructions[2].from)
        assertEquals(1, instructions[2].to)
        assertEquals(1, instructions[3].count)
        assertEquals(1, instructions[3].from)
        assertEquals(2, instructions[3].to)
    }

    @Test
    fun testMoveCargo() {
        val (stacks, instructions) = parseCargo(sampleInput)
        val movedStacks = moveCargo(stacks, instructions)
        assertEquals(
            """
                        [Z]
                        [N]
                        [D]
                [C] [M] [P]
                 1   2   3 
            """.trimIndent(),
            movedStacks.toString()
        )
    }

    @Test
    fun testReadTopOfEachStack() {
        val (stacks, instructions) = parseCargo(sampleInput)
        val movedStacks = moveCargo(stacks, instructions)
        assertEquals("CMZ", readTopOfEachStack(movedStacks))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val (stacks, instructions) = parseCargo(input)
        val movedStacks = moveCargo(stacks, instructions)
        assertEquals("TLNGFGMFN", readTopOfEachStack(movedStacks))
    }

    @Test
    fun testMoveCargoAtOnce() {
        val (stacks, instructions) = parseCargo(sampleInput)
        val movedStacks = moveCargo(stacks, instructions, atOnce = true)
        assertEquals(
            """
                    [D]
                    [N]
                    [Z]
            [M] [C] [P]
             1   2   3 
            """.trimIndent(),
            movedStacks.toString()
        )
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val (stacks, instructions) = parseCargo(input)
        val movedStacks = moveCargo(stacks, instructions, atOnce = true)
        assertEquals("FGLQJCMBD", readTopOfEachStack(movedStacks))
    }
}
