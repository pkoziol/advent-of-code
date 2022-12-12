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
                    items = listOf(79, 98).map { SimpleItem(it) },
                    operation = Multiply(19),
                    test = Divisible(23),
                    targets = mapOf(
                        true to 2,
                        false to 3,
                    ),
                ),
                Monkey(
                    id = 1,
                    items = listOf(54, 65, 75, 74).map { SimpleItem(it) },
                    operation = Add(6),
                    test = Divisible(19),
                    targets = mapOf(
                        true to 2,
                        false to 0,
                    ),
                ),
                Monkey(
                    id = 2,
                    items = listOf(79, 60, 97).map { SimpleItem(it) },
                    operation = Square,
                    test = Divisible(13),
                    targets = mapOf(
                        true to 1,
                        false to 3,
                    ),
                ),
                Monkey(
                    id = 3,
                    items = listOf(74).map { SimpleItem(it) },
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

        fun Monkey.itemsAsIntegers() = items.map { (it as SimpleItem).value }

        val monkeys1 = playKeepAway(monkeys)
        assertEquals(listOf(20, 23, 27, 26), monkeys1.find { it.id == 0 }?.itemsAsIntegers())
        assertEquals(listOf(2080, 25, 167, 207, 401, 1046), monkeys1.find { it.id == 1 }?.itemsAsIntegers())
        assertEquals(emptyList<Int>(), monkeys1.find { it.id == 2 }?.itemsAsIntegers())
        assertEquals(emptyList<Int>(), monkeys1.find { it.id == 3 }?.itemsAsIntegers())

        val monkeys2 = playKeepAway(monkeys1)
        assertEquals(listOf(695, 10, 71, 135, 350), monkeys2.find { it.id == 0 }?.itemsAsIntegers())
        assertEquals(listOf(43, 49, 58, 55, 362), monkeys2.find { it.id == 1 }?.itemsAsIntegers())
        assertEquals(emptyList<Int>(), monkeys2.find { it.id == 2 }?.itemsAsIntegers())
        assertEquals(emptyList<Int>(), monkeys2.find { it.id == 3 }?.itemsAsIntegers())

        val monkeys3 = playKeepAway(monkeys2)
        assertEquals(listOf(16, 18, 21, 20, 122), monkeys3.find { it.id == 0 }?.itemsAsIntegers())
        assertEquals(listOf(1468, 22, 150, 286, 739), monkeys3.find { it.id == 1 }?.itemsAsIntegers())
        assertEquals(emptyList<Int>(), monkeys3.find { it.id == 2 }?.itemsAsIntegers())
        assertEquals(emptyList<Int>(), monkeys3.find { it.id == 3 }?.itemsAsIntegers())
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

//    @ParameterizedTest(name = "rounds={0}")
//    @MethodSource
//    fun testPlayWithoutDividingWorryLevel(rounds: Int, expectedInspectedItems: List<Int>) {
//        val monkeys = parseMonkeys(sampleInput)
//        val monkeysAfterNRounds = playKeepAway(monkeys, rounds = rounds, worryDivider = 1)
//
//        assertEquals(
//            expectedInspectedItems,
//            monkeysAfterNRounds.sortedBy { it.id }.map { it.inspectedItems }
//        )
//    }
//
//    companion object {
//        @JvmStatic
//        fun testPlayWithoutDividingWorryLevel(): Stream<Arguments> =
//            Stream.of(
//                Arguments.of(1, listOf(2, 4, 3, 6)),
//                Arguments.of(20, listOf(99, 97, 8, 103)),
//                Arguments.of(1000, listOf(5204, 4792, 199, 5192)),
//                Arguments.of(2000, listOf(10419, 9577, 392, 10391)),
//                Arguments.of(3000, listOf(15638, 14358, 587, 15593)),
//                Arguments.of(4000, listOf(20858, 19138, 780, 20797)),
//                Arguments.of(5000, listOf(26075, 23921, 974, 26000)),
//                Arguments.of(6000, listOf(31294, 28702, 1165, 31204)),
//                Arguments.of(7000, listOf(36508, 33488, 1360, 36400)),
//                Arguments.of(8000, listOf(41728, 38268, 1553, 41606)),
//                Arguments.of(9000, listOf(46945, 43051, 1746, 46807)),
//                Arguments.of(10000, listOf(52166, 47830, 1938, 52013)),
//            )
//    }
//
//    @Test
//    fun testPlay10000RoundsWithoutDividingWorryLevel() {
//        val monkeys = parseMonkeys(sampleInput)
//
//        val monkeys1 = playKeepAway(monkeys, rounds = 1, worryDivider = 1)
//        assertEquals(2, monkeys1.find { it.id == 0 }?.inspectedItems)
//        assertEquals(4, monkeys1.find { it.id == 1 }?.inspectedItems)
//        assertEquals(3, monkeys1.find { it.id == 2 }?.inspectedItems)
//        assertEquals(6, monkeys1.find { it.id == 3 }?.inspectedItems)
//
//        val monkeys10000 = playKeepAway(monkeys, rounds = 10000)
//        assertEquals(2713310158, getMonkeyBusiness(monkeys10000))
//    }
}
