package biz.koziolek.adventofcode.year2022.day11

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2022")
internal class Day11Test {

    private val sampleInput = """
            Monkey 0:
              Starting items: 79, 98
              Operation: new = old * 19
              Test: divisible by 23
                If true: throw to monkey 2
                If false: throw to monkey 3
            
            Monkey 1:
              Starting items: 54, 65, 75, 74
              Operation: new = old + 6
              Test: divisible by 19
                If true: throw to monkey 2
                If false: throw to monkey 0
            
            Monkey 2:
              Starting items: 79, 60, 97
              Operation: new = old * old
              Test: divisible by 13
                If true: throw to monkey 1
                If false: throw to monkey 3
            
            Monkey 3:
              Starting items: 74
              Operation: new = old + 3
              Test: divisible by 17
                If true: throw to monkey 0
                If false: throw to monkey 1
        """.trimIndent().split("\n")

    @Test
    fun testParse() {
        val monkeys = parseMonkeys(sampleInput)
        assertEquals(
            listOf(
                Monkey(
                    id = 0,
                    items = listOf(79, 98),
                    operation = Multiply(19),
                    test = Divisible(23),
                    targets = mapOf(
                        true to 2,
                        false to 3,
                    ),
                ),
                Monkey(
                    id = 1,
                    items = listOf(54, 65, 75, 74),
                    operation = Add(6),
                    test = Divisible(19),
                    targets = mapOf(
                        true to 2,
                        false to 0,
                    ),
                ),
                Monkey(
                    id = 2,
                    items = listOf(79, 60, 97),
                    operation = Square,
                    test = Divisible(13),
                    targets = mapOf(
                        true to 1,
                        false to 3,
                    ),
                ),
                Monkey(
                    id = 3,
                    items = listOf(74),
                    operation = Add(3),
                    test = Divisible(17),
                    targets = mapOf(
                        true to 0,
                        false to 1,
                    ),
                ),
            ),
            monkeys
        )
    }

    @Test
    fun testPlayKeepAway() {
        val monkeys = parseMonkeys(sampleInput)

        val monkeys1 = playKeepAway(monkeys)
        assertEquals(listOf(20, 23, 27, 26), monkeys1.find { it.id == 0 }?.items)
        assertEquals(listOf(2080, 25, 167, 207, 401, 1046), monkeys1.find { it.id == 1 }?.items)
        assertEquals(emptyList<Int>(), monkeys1.find { it.id == 2 }?.items)
        assertEquals(emptyList<Int>(), monkeys1.find { it.id == 3 }?.items)

        val monkeys2 = playKeepAway(monkeys1)
        assertEquals(listOf(695, 10, 71, 135, 350), monkeys2.find { it.id == 0 }?.items)
        assertEquals(listOf(43, 49, 58, 55, 362), monkeys2.find { it.id == 1 }?.items)
        assertEquals(emptyList<Int>(), monkeys2.find { it.id == 2 }?.items)
        assertEquals(emptyList<Int>(), monkeys2.find { it.id == 3 }?.items)

        val monkeys3 = playKeepAway(monkeys2)
        assertEquals(listOf(16, 18, 21, 20, 122), monkeys3.find { it.id == 0 }?.items)
        assertEquals(listOf(1468, 22, 150, 286, 739), monkeys3.find { it.id == 1 }?.items)
        assertEquals(emptyList<Int>(), monkeys3.find { it.id == 2 }?.items)
        assertEquals(emptyList<Int>(), monkeys3.find { it.id == 3 }?.items)
    }

    @Test
    fun testMonkeyBusiness() {
        val monkeys = parseMonkeys(sampleInput)
        val monkeys20 = playKeepAway(monkeys, rounds = 20)

        assertEquals(101, monkeys20.find { it.id == 0 }?.inspectedItems)
        assertEquals(95, monkeys20.find { it.id == 1 }?.inspectedItems)
        assertEquals(7, monkeys20.find { it.id == 2 }?.inspectedItems)
        assertEquals(105, monkeys20.find { it.id == 3 }?.inspectedItems)

        assertEquals(10605, getMonkeyBusiness(monkeys20))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val monkeys = parseMonkeys(input)
        val monkeys20 = playKeepAway(monkeys, rounds = 20)
        assertEquals(58322, getMonkeyBusiness(monkeys20))
    }
}
