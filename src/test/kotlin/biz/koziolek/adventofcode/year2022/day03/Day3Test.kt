package biz.koziolek.adventofcode.year2022.day03

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2022")
internal class Day3Test {

    private val sampleInput = """
            vJrwpWtwJgWrhcsFMMfFFhFp
            jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
            PmmdzqPrVvPwwTWBwg
            wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
            ttgJtRGJQctTZtZT
            CrZsJsPPZsGzwwsLwLmpwMDw
        """.trimIndent().split("\n")

    @Test
    fun testParseRucksacks() {
        val rucksacks = parseRucksacks(sampleInput)
        assertEquals(6, rucksacks.size)

        assertEquals("vJrwpWtwJgWrhcsFMMfFFhFp", rucksacks[0].contents)
        assertEquals("vJrwpWtwJgWr", rucksacks[0].compartment1)
        assertEquals("hcsFMMfFFhFp", rucksacks[0].compartment2)

        assertEquals("jqHRNqRjqzjGDLGL", rucksacks[1].compartment1)
        assertEquals("rsFMfFZSrLrFZsSL", rucksacks[1].compartment2)

        assertEquals("PmmdzqPrV", rucksacks[2].compartment1)
        assertEquals("vPwwTWBwg", rucksacks[2].compartment2)
    }

    @Test
    fun testFindSharedItem() {
        val rucksacks = parseRucksacks(sampleInput)
        assertEquals(6, rucksacks.size)
        assertEquals('p', rucksacks[0].findSharedItem())
        assertEquals('L', rucksacks[1].findSharedItem())
        assertEquals('P', rucksacks[2].findSharedItem())
        assertEquals('v', rucksacks[3].findSharedItem())
        assertEquals('t', rucksacks[4].findSharedItem())
        assertEquals('s', rucksacks[5].findSharedItem())
    }

    @Test
    fun testGetItemPriority() {
        assertEquals(16, getItemPriority('p'))
        assertEquals(38, getItemPriority('L'))
        assertEquals(42, getItemPriority('P'))
        assertEquals(22, getItemPriority('v'))
        assertEquals(20, getItemPriority('t'))
        assertEquals(19, getItemPriority('s'))
    }

    @Test
    fun testSampleInput() {
        val rucksacks = parseRucksacks(sampleInput)
        assertEquals(157, getSumOfSharedItemPriorities(rucksacks))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val rucksacks = parseRucksacks(input)
        assertEquals(7742, getSumOfSharedItemPriorities(rucksacks))
    }

    @Test
    fun testGroupRucksacks() {
        val rucksacks = parseRucksacks(sampleInput)
        val groups = groupRucksacks(rucksacks)
        assertEquals(2, groups.size)
        assertEquals('r', groups[0].findBadge())
        assertEquals('Z', groups[1].findBadge())
        assertEquals(70, getSumOfGroupBadges(groups))
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val rucksacks = parseRucksacks(input)
        val groups = groupRucksacks(rucksacks)
        assertEquals(2276, getSumOfGroupBadges(groups))
    }
}
