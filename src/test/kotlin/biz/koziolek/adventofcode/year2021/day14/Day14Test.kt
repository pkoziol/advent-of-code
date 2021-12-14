package biz.koziolek.adventofcode.year2021.day14

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

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
    fun testExpandBigPolymer() {
        val polymerTemplate = parsePolymerTemplate(sampleInput)
        val insertionRules = parsePolymerInsertionRules(sampleInput)

        val bigPolymer = convertPolymerToBigPolymer(polymerTemplate)
        val expectedBigPolymer = mapOf(
            Pair('N', 'N') to 1L,
            Pair('N', 'C') to 1L,
            Pair('C', 'B') to 1L,
        )
        assertEquals(expectedBigPolymer, bigPolymer)

        val afterStep1 = expandBigPolymer(bigPolymer, insertionRules)
        assertEquals(convertPolymerToBigPolymer("NCNBCHB"), afterStep1)

        val afterStep2 = expandBigPolymer(afterStep1, insertionRules)
        assertEquals(convertPolymerToBigPolymer("NBCCNBBBCBHCB"), afterStep2)

        val afterStep3 = expandBigPolymer(afterStep2, insertionRules)
        assertEquals(convertPolymerToBigPolymer("NBBBCNCCNBBNBNBBCHBHHBCHB"), afterStep3)

        val afterStep4 = expandBigPolymer(afterStep3, insertionRules)
        assertEquals(convertPolymerToBigPolymer("NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB"), afterStep4)

        val afterStep5 = expandBigPolymer(afterStep4, insertionRules)
        assertEquals(97, getLength(afterStep5))

        val afterStep6 = expandBigPolymer(afterStep5, insertionRules)
        val afterStep7 = expandBigPolymer(afterStep6, insertionRules)
        val afterStep8 = expandBigPolymer(afterStep7, insertionRules)
        val afterStep9 = expandBigPolymer(afterStep8, insertionRules)
        val afterStep10 = expandBigPolymer(afterStep9, insertionRules)
        assertEquals(3073, getLength(afterStep10))
        assertEquals(1749, countOccurrences(polymerTemplate, afterStep10, 'B'))
        assertEquals(298, countOccurrences(polymerTemplate, afterStep10, 'C'))
        assertEquals(161, countOccurrences(polymerTemplate, afterStep10, 'H'))
        assertEquals(865, countOccurrences(polymerTemplate, afterStep10, 'N'))
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
    fun testSubtractLeastCommonFromMostCommonBigPolymer() {
        val polymerTemplate = parsePolymerTemplate(sampleInput)
        val insertionRules = parsePolymerInsertionRules(sampleInput)
        val bigPolymer = convertPolymerToBigPolymer(polymerTemplate)
        val afterStep10 = generateSequence(bigPolymer) { expandBigPolymer(it, insertionRules) }
            .drop(10)
            .first()
        assertEquals(3073, getLength(afterStep10))
        assertEquals(1588, subtractLeastCommonFromMostCommon(polymerTemplate, afterStep10))
    }

    @Test
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
    fun testAnswer2() {
        val fullInput = findInput(object {}).readLines()
        val polymerTemplate = parsePolymerTemplate(fullInput)
        val insertionRules = parsePolymerInsertionRules(fullInput)
        val bigPolymer = convertPolymerToBigPolymer(polymerTemplate)
        val afterStep40 = generateSequence(bigPolymer) { expandBigPolymer(it, insertionRules) }
            .drop(40)
            .first()
        assertEquals(3711743744429, subtractLeastCommonFromMostCommon(polymerTemplate, afterStep40))
    }
}
