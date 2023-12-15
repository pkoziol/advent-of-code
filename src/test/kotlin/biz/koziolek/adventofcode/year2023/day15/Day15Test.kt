package biz.koziolek.adventofcode.year2023.day15

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2023")
internal class Day15Test {

    private val sampleInput = """
            rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
        """.trimIndent().split("\n")

    @Test
    fun testParseInitializationSequence() {
        val initSeq = parseInitializationSequence(sampleInput)
        assertEquals(
            listOf(
                InitOperation(name = "rn", symbol = '=', value = 1),
                InitOperation(name = "cm", symbol = '-'),
                InitOperation(name = "qp", symbol = '=', value = 3),
                InitOperation(name = "cm", symbol = '=', value = 2),
                InitOperation(name = "qp", symbol = '-'),
                InitOperation(name = "pc", symbol = '=', value = 4),
                InitOperation(name = "ot", symbol = '=', value = 9),
                InitOperation(name = "ab", symbol = '=', value = 5),
                InitOperation(name = "pc", symbol = '-'),
                InitOperation(name = "pc", symbol = '=', value = 6),
                InitOperation(name = "ot", symbol = '=', value = 7),
            ),
            initSeq
        )
    }

    @Test
    fun testHash() {
        assertEquals(52, hash("HASH"))
    }

    @Test
    fun testInitOperationToString() {
        assertEquals("rn=1", InitOperation(name = "rn", symbol = '=', value = 1).toString())
        assertEquals("cm-", InitOperation(name = "cm", symbol = '-').toString())
    }

    @Test
    fun testSampleAnswer1() {
        val initSeq = parseInitializationSequence(sampleInput)
        assertEquals(1320, initSeq.sumOf { it.hash() })
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val initSeq = parseInitializationSequence(input)
        assertEquals(513172, initSeq.sumOf { it.hash() })
    }
}
