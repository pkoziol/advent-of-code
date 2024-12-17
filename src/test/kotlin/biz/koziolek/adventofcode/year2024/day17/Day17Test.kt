package biz.koziolek.adventofcode.year2024.day17

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2024")
internal class Day17Test {

    private val sampleInput = """
            Register A: 729
            Register B: 0
            Register C: 0

            Program: 0,1,5,4,3,0
        """.trimIndent().split("\n")

    @Test
    fun testParseComputer() {
        val computer = parseComputer(sampleInput)
        assertEquals(729, computer.a)
        assertEquals(0, computer.b)
        assertEquals(0, computer.c)
        assertEquals("", computer.output)
        assertEquals(6, computer.program.size)
        assertEquals(0, computer.instructionPointer)
        assertFalse(computer.isHalted)
    }

    @Test
    fun testExampleProgram1() {
        val computer = Computer(c = 9, program = parseProgram("2,6"))
        println(computer.programToString())
        val haltedComputer = computer.run()
        assertTrue(haltedComputer.isHalted)
        assertEquals(1, haltedComputer.b)
    }

    @Test
    fun testExampleProgram2() {
        val computer = Computer(a = 10, program = parseProgram("5,0,5,1,5,4"))
        println(computer.programToString())
        val haltedComputer = computer.run()
        assertTrue(haltedComputer.isHalted)
        assertEquals("0,1,2", haltedComputer.output)
    }

    @Test
    fun testExampleProgram3() {
        val computer = Computer(a = 2024, program = parseProgram("0,1,5,4,3,0"))
        println(computer.programToString())
        val haltedComputer = computer.run(limit = 100)
        assertTrue(haltedComputer.isHalted)
        assertEquals("4,2,5,6,7,7,7,7,3,1,0", haltedComputer.output)
        assertEquals(0, haltedComputer.a)
    }

    @Test
    fun testExampleProgram4() {
        val computer = Computer(b = 29, program = parseProgram("1,7"))
        println(computer.programToString())
        val haltedComputer = computer.run()
        assertTrue(haltedComputer.isHalted)
        assertEquals(26, haltedComputer.b)
    }

    @Test
    fun testExampleProgram5() {
        val computer = Computer(b = 2024, c = 43690, program = parseProgram("4,0"))
        println(computer.programToString())
        val haltedComputer = computer.run()
        assertTrue(haltedComputer.isHalted)
        assertEquals(44354, haltedComputer.b)
    }

    @Test
    fun testSampleAnswer1() {
        val computer = parseComputer(sampleInput)
        println(computer.programToString())
        val haltedComputer = computer.run()
        assertEquals("4,6,3,5,6,3,5,2,1,0", haltedComputer.output)
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val computer = parseComputer(input)
//        println(computer.programToString())
        val haltedComputer = computer.run()
        assertEquals("7,3,0,5,7,1,4,0,5", haltedComputer.output)
    }
}
