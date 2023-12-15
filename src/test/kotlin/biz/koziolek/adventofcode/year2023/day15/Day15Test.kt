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
                AddOperation(lens = Lens(label = "rn", focalLength = 1)),
                RemoveOperation(lensLabel = "cm"),
                AddOperation(lens = Lens(label = "qp", focalLength = 3)),
                AddOperation(lens = Lens(label = "cm", focalLength = 2)),
                RemoveOperation(lensLabel = "qp"),
                AddOperation(lens = Lens(label = "pc", focalLength = 4)),
                AddOperation(lens = Lens(label = "ot", focalLength = 9)),
                AddOperation(lens = Lens(label = "ab", focalLength = 5)),
                RemoveOperation(lensLabel = "pc"),
                AddOperation(lens = Lens(label = "pc", focalLength = 6)),
                AddOperation(lens = Lens(label = "ot", focalLength = 7)),
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
        assertEquals("rn=1", AddOperation(lens = Lens(label = "rn", focalLength = 1)).toString())
        assertEquals("cm-", RemoveOperation(lensLabel = "cm").toString())
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
