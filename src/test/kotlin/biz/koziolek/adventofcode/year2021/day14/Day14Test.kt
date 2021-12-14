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
        assertEquals("B", insertionRules["CH"])
        assertEquals("N", insertionRules["HH"])
        assertEquals("H", insertionRules["CB"])
        assertEquals("C", insertionRules["NH"])
        assertEquals("C", insertionRules["HB"])
        assertEquals("B", insertionRules["HC"])
        assertEquals("C", insertionRules["HN"])
        assertEquals("C", insertionRules["NN"])
        assertEquals("H", insertionRules["BH"])
        assertEquals("B", insertionRules["NC"])
        assertEquals("B", insertionRules["NB"])
        assertEquals("B", insertionRules["BN"])
        assertEquals("N", insertionRules["BB"])
        assertEquals("B", insertionRules["BC"])
        assertEquals("N", insertionRules["CC"])
        assertEquals("C", insertionRules["CN"])
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
    fun testAnswer1() {
        val fullInput = findInput(object {}).readLines()
        val polymerTemplate = parsePolymerTemplate(fullInput)
        val insertionRules = parsePolymerInsertionRules(fullInput)
        val afterStep10 = generateSequence(polymerTemplate) { expandPolymer(it, insertionRules) }
            .drop(10)
            .first()
        assertEquals(3213, subtractLeastCommonFromMostCommon(afterStep10))
    }
}
