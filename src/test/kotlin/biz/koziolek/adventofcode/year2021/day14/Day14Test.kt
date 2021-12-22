package biz.koziolek.adventofcode.year2021.day14

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2021")
internal class Day14Test {

    private val sampleInput = """
        NNCB

        CH -> B
        HH -> N
        CB -> H
        NH -> C
        HB -> C
        HC -> B
        HN -> C
        NN -> C
        BH -> H
        NC -> B
        NB -> B
        BN -> B
        BB -> N
        BC -> B
        CC -> N
        CN -> C
    """.trimIndent().split("\n")

    @Test
    fun testParseTransparentDots() {
        val polymerTemplate = parsePolymerTemplate(sampleInput)
        val insertionRules = parsePolymerInsertionRules(sampleInput)

        assertEquals("NNCB", polymerTemplate)
        assertEquals(16, insertionRules.size)
        assertEquals('B', insertionRules[Pair('C', 'H')])
        assertEquals('N', insertionRules[Pair('H', 'H')])
        assertEquals('H', insertionRules[Pair('C', 'B')])
        assertEquals('C', insertionRules[Pair('N', 'H')])
        assertEquals('C', insertionRules[Pair('H', 'B')])
        assertEquals('B', insertionRules[Pair('H', 'C')])
        assertEquals('C', insertionRules[Pair('H', 'N')])
        assertEquals('C', insertionRules[Pair('N', 'N')])
        assertEquals('H', insertionRules[Pair('B', 'H')])
        assertEquals('B', insertionRules[Pair('N', 'C')])
        assertEquals('B', insertionRules[Pair('N', 'B')])
        assertEquals('B', insertionRules[Pair('B', 'N')])
        assertEquals('N', insertionRules[Pair('B', 'B')])
        assertEquals('B', insertionRules[Pair('B', 'C')])
        assertEquals('N', insertionRules[Pair('C', 'C')])
        assertEquals('C', insertionRules[Pair('C', 'N')])
    }

    @Test
    fun testExpandPolymer() {
        val polymerTemplate = parsePolymerTemplate(sampleInput)
        val insertionRules = parsePolymerInsertionRules(sampleInput)

        val afterStep1 = expandPolymer(polymerTemplate, insertionRules)
        assertEquals("NCNBCHB", afterStep1)

        val afterStep2 = expandPolymer(afterStep1, insertionRules)
        assertEquals("NBCCNBBBCBHCB", afterStep2)

        val afterStep3 = expandPolymer(afterStep2, insertionRules)
        assertEquals("NBBBCNCCNBBNBNBBCHBHHBCHB", afterStep3)

        val afterStep4 = expandPolymer(afterStep3, insertionRules)
        assertEquals("NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB", afterStep4)

        val afterStep5 = expandPolymer(afterStep4, insertionRules)
        assertEquals(97, afterStep5.length)

        val afterStep6 = expandPolymer(afterStep5, insertionRules)
        val afterStep7 = expandPolymer(afterStep6, insertionRules)
        val afterStep8 = expandPolymer(afterStep7, insertionRules)
        val afterStep9 = expandPolymer(afterStep8, insertionRules)
        val afterStep10 = expandPolymer(afterStep9, insertionRules)
        assertEquals(3073, afterStep10.length)
        assertEquals(1749, afterStep10.count { it == 'B' })
        assertEquals(298, afterStep10.count { it == 'C' })
        assertEquals(161, afterStep10.count { it == 'H' })
        assertEquals(865, afterStep10.count { it == 'N' })
    }

    @Test
    fun testExpandLongPolymer() {
        val polymerTemplate = parsePolymerTemplate(sampleInput)
        val insertionRules = parsePolymerInsertionRules(sampleInput)

        val longPolymer = LongPolymer.fromString(polymerTemplate)
        val expectedLongPolymer = LongPolymer(
            firstChar = 'N',
            pairStats = mapOf(
                Pair('N', 'N') to 1L,
                Pair('N', 'C') to 1L,
                Pair('C', 'B') to 1L,
            )
        )
        assertEquals(expectedLongPolymer, longPolymer)

        val afterStep1 = longPolymer.expand(insertionRules)
        assertEquals(LongPolymer.fromString("NCNBCHB"), afterStep1)

        val afterStep2 = afterStep1.expand(insertionRules)
        assertEquals(LongPolymer.fromString("NBCCNBBBCBHCB"), afterStep2)

        val afterStep3 = afterStep2.expand(insertionRules)
        assertEquals(LongPolymer.fromString("NBBBCNCCNBBNBNBBCHBHHBCHB"), afterStep3)

        val afterStep4 = afterStep3.expand(insertionRules)
        assertEquals(LongPolymer.fromString("NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB"), afterStep4)

        val afterStep5 = afterStep4.expand(insertionRules)
        assertEquals(97, afterStep5.length)

        val afterStep6 = afterStep5.expand(insertionRules)
        val afterStep7 = afterStep6.expand(insertionRules)
        val afterStep8 = afterStep7.expand(insertionRules)
        val afterStep9 = afterStep8.expand(insertionRules)
        val afterStep10 = afterStep9.expand(insertionRules)
        assertEquals(3073, afterStep10.length)
        assertEquals(1749, afterStep10.countOccurrences('B'))
        assertEquals(298, afterStep10.countOccurrences('C'))
        assertEquals(161, afterStep10.countOccurrences('H'))
        assertEquals(865, afterStep10.countOccurrences('N'))
    }

    @Test
    fun testSubtractLeastCommonFromMostCommon() {
        val polymerTemplate = parsePolymerTemplate(sampleInput)
        val insertionRules = parsePolymerInsertionRules(sampleInput)
        val afterStep10 = generateSequence(polymerTemplate) { expandPolymer(it, insertionRules) }
            .drop(10)
            .first()
        assertEquals(3073, afterStep10.length)
        assertEquals(1588, subtractLeastCommonFromMostCommon(afterStep10))
    }

    @Test
    fun testSubtractLeastCommonFromMostCommonLongPolymer() {
        val polymerTemplate = parsePolymerTemplate(sampleInput)
        val insertionRules = parsePolymerInsertionRules(sampleInput)
        val longPolymer = LongPolymer.fromString(polymerTemplate)
        val afterStep10 = generateSequence(longPolymer) { it.expand(insertionRules) }
            .drop(10)
            .first()
        assertEquals(3073, afterStep10.length)
        assertEquals(1588, afterStep10.subtractLeastCommonFromMostCommon())
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val fullInput = findInput(object {}).readLines()
        val polymerTemplate = parsePolymerTemplate(fullInput)
        val insertionRules = parsePolymerInsertionRules(fullInput)
        val afterStep10 = generateSequence(polymerTemplate) { expandPolymer(it, insertionRules) }
            .drop(10)
            .first()
        assertEquals(3213, subtractLeastCommonFromMostCommon(afterStep10))
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val fullInput = findInput(object {}).readLines()
        val polymerTemplate = parsePolymerTemplate(fullInput)
        val insertionRules = parsePolymerInsertionRules(fullInput)
        val longPolymer = LongPolymer.fromString(polymerTemplate)
        val afterStep40 = generateSequence(longPolymer) { it.expand(insertionRules) }
            .drop(40)
            .first()
        assertEquals(3711743744429, afterStep40.subtractLeastCommonFromMostCommon())
    }
}
