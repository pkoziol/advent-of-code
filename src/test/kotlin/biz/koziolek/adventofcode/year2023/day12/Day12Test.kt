package biz.koziolek.adventofcode.year2023.day12

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@Tag("2023")
internal class Day12Test {

    private val sampleInput = """
            ???.### 1,1,3
            .??..??...?##. 1,1,3
            ?#?#?#?#?#?#?#? 1,3,1,6
            ????.#...#... 4,1,1
            ????.######..#####. 1,6,5
            ?###???????? 3,2,1
        """.trimIndent().split("\n")

    private val sampleInputNotDamaged = """
            #.#.### 1,1,3
            .#...#....###. 1,1,3
            .#.###.#.###### 1,3,1,6
            ####.#...#... 4,1,1
            #....######..#####. 1,6,5
            .###.##....# 3,2,1
        """.trimIndent().split("\n")

    @Test
    fun testParseHotSpringsRecords() {
        val springs = parseHotSpringsRecords(sampleInput)
        assertEquals(
            listOf(
                HotSpringsRow(conditions = "???.###", damagedGroupSizes = listOf(1, 1, 3)),
                HotSpringsRow(conditions = ".??..??...?##.", damagedGroupSizes = listOf(1, 1, 3)),
                HotSpringsRow(conditions = "?#?#?#?#?#?#?#?", damagedGroupSizes = listOf(1, 3, 1, 6)),
                HotSpringsRow(conditions = "????.#...#...", damagedGroupSizes = listOf(4, 1, 1)),
                HotSpringsRow(conditions = "????.######..#####.", damagedGroupSizes = listOf(1, 6, 5)),
                HotSpringsRow(conditions = "?###????????", damagedGroupSizes = listOf(3, 2, 1)),
            ),
            springs
        )
        assertEquals(List(6) { _ -> true }, springs.map { it.isUnknown })

        val springs2 = parseHotSpringsRecords(sampleInputNotDamaged)
        assertEquals(
            listOf(
                HotSpringsRow(conditions = "#.#.###", damagedGroupSizes = listOf(1, 1, 3)),
                HotSpringsRow(conditions = ".#...#....###.", damagedGroupSizes = listOf(1, 1, 3)),
                HotSpringsRow(conditions = ".#.###.#.######", damagedGroupSizes = listOf(1, 3, 1, 6)),
                HotSpringsRow(conditions = "####.#...#...", damagedGroupSizes = listOf(4, 1, 1)),
                HotSpringsRow(conditions = "#....######..#####.", damagedGroupSizes = listOf(1, 6, 5)),
                HotSpringsRow(conditions = ".###.##....#", damagedGroupSizes = listOf(3, 2, 1)),
            ),
            springs2
        )
        assertEquals(List(6) { _ -> false }, springs2.map { it.isUnknown })
    }

    @Test
    fun testHotSpringsRowMatchesPattern() {
        val springs = parseHotSpringsRecords(sampleInputNotDamaged)
        assertEquals(List(6) { _ -> true }, springs.map { springsMatch(it.conditions, it.damagedGroupSizes) })
    }

    companion object {
        @JvmStatic
        fun testGeneratePossibleArrangements(): Stream<Arguments> =
            Stream.of(
                Arguments.of("???.###", listOf(1, 1, 3), setOf(
                    "#.#.###",
                )),
                Arguments.of(".??..??...?##.", listOf(1, 1, 3), setOf(
                    ".#...#....###.",
                    ".#....#...###.",
                    "..#..#....###.",
                    "..#...#...###.",
                )),
                Arguments.of("?#?#?#?#?#?#?#?", listOf(1, 3, 1, 6), setOf(
                    ".#.###.#.######",
                )),
                Arguments.of("????.#...#...", listOf(4, 1, 1), setOf(
                    "####.#...#...",
                )),
                Arguments.of("????.######..#####.", listOf(1, 6, 5), setOf(
                    "#....######..#####.",
                    ".#...######..#####.",
                    "..#..######..#####.",
                    "...#.######..#####.",
                )),
                Arguments.of("?###????????", listOf(3, 2, 1), setOf(
                    ".###.##.#...",
                    ".###.##..#..",
                    ".###.##...#.",
                    ".###.##....#",
                    ".###..##.#..",
                    ".###..##..#.",
                    ".###..##...#",
                    ".###...##.#.",
                    ".###...##..#",
                    ".###....##.#",
                )),
            )
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    fun testGeneratePossibleArrangements(damagedPattern: String,
                                         damagedGroupSizes: List<Int>,
                                         expectedPatterns: Set<String>) {
        assertEquals(
            expectedPatterns.map { HotSpringsRow(conditions = it, damagedGroupSizes = damagedGroupSizes) }.toSet(),
            generatePossibleArrangements(HotSpringsRow(conditions = damagedPattern, damagedGroupSizes = damagedGroupSizes)).toSet()
        )
    }

    @Test
    fun testSampleAnswer1() {
        val springs = parseHotSpringsRecords(sampleInput)
        assertEquals(21, countPossibleArrangements(springs))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val springs = parseHotSpringsRecords(input)
        assertEquals(7350, countPossibleArrangements(springs))
    }
}
