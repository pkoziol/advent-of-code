package biz.koziolek.adventofcode.year2021.day24

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.util.ArrayDeque

@Tag("2021")
internal class Day24Test {

    private val sampleInput1 = """
        inp x
        mul x -1
    """.trimIndent().split("\n")

    private val sampleInput2 = """
        inp z
        inp x
        mul z 3
        eql z x
    """.trimIndent().split("\n")

    private val sampleInput3 = """
        inp w
        add z w
        mod z 2
        div w 2
        add y w
        mod y 2
        div w 2
        add x w
        mod x 2
        div w 2
        mod w 2
    """.trimIndent().split("\n")

    @Test
    fun testParseSampleInput() {
        val aluInstructions1 = parseAluInstructions(sampleInput1)
        assertEquals(
            listOf(
                Inp(Var("x")),
                Mul(Var("x"), Num(-1)),
            ),
            aluInstructions1
        )

        val aluInstructions2 = parseAluInstructions(sampleInput2)
        assertEquals(
            listOf(
                Inp(Var("z")),
                Inp(Var("x")),
                Mul(Var("z"), Num(3)),
                Eql(Var("z"), Var("x")),
            ),
            aluInstructions2
        )

        val aluInstructions3 = parseAluInstructions(sampleInput3)
        assertEquals(11, aluInstructions3.size)
    }

    @Test
    fun testEvaluate() {
        val aluInstructions1 = parseAluInstructions(sampleInput1)
        val state1 = evaluate(aluInstructions1, input = ArrayDeque(listOf(123)))
        assertEquals(State(mapOf("x" to -123)), state1)

        val aluInstructions2 = parseAluInstructions(sampleInput2)
        val state2 = evaluate(aluInstructions2, input = ArrayDeque(listOf(100, 300)))
        assertEquals(State(mapOf("z" to 1, "x" to 300)), state2)
        val state2b = evaluate(aluInstructions2, input = ArrayDeque(listOf(200, 300)))
        assertEquals(State(mapOf("z" to 0, "x" to 300)), state2b)

        val aluInstructions3 = parseAluInstructions(sampleInput3)
        val state3 = evaluate(aluInstructions3, input = ArrayDeque(listOf(12)))
        assertEquals(State(mapOf("w" to 1, "x" to 1, "y" to 0, "z" to 0)), state3)
        val state3b = evaluate(aluInstructions3, input = ArrayDeque(listOf(3)))
        assertEquals(State(mapOf("w" to 0, "x" to 0, "y" to 1, "z" to 1)), state3b)
        val state3c = evaluate(aluInstructions3, input = ArrayDeque(listOf(15)))
        assertEquals(State(mapOf("w" to 1, "x" to 1, "y" to 1, "z" to 1)), state3c)
    }
    
    @Test
    fun testGenerateCode() {
        val aluInstructions1 = parseAluInstructions(sampleInput1)
        println(generateCode(aluInstructions1))
        println("-".repeat(80))

        val aluInstructions2 = parseAluInstructions(sampleInput2)
        println(generateCode(aluInstructions2))
        println("-".repeat(80))

        val aluInstructions3 = parseAluInstructions(sampleInput3)
        println(generateCode(aluInstructions3))
        println("-".repeat(80))

        val fullInput = findInput(object {}).readLines()
        val aluInstructions4 = parseAluInstructions(fullInput)
        println(generateCode(aluInstructions4))
    }

    @Test
    fun testGenerateValidSerialNumbers() {
        val serialNumbers = generateValidSerialNumbers()
        val (validSNs, invalidSNs) = serialNumbers.partition { verifySerialNumber(it) }
        assertFalse(validSNs.isEmpty())
        assertTrue(invalidSNs.isEmpty())

        val (validSNs2, invalidSNs2) = serialNumbers.partition { verifySerialNumberSimplified(it) }
        assertEquals(validSNs, validSNs2)
        assertEquals(invalidSNs, invalidSNs2)
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        assertEquals(99299513899971, generateValidSerialNumbers().maxOf { it })
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        assertEquals(93185111127911, generateValidSerialNumbers().minOf { it })
    }
}
