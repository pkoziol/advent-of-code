package biz.koziolek.adventofcode.year2023.day05

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2023")
internal class Day5Test {

    private val sampleInput = """
            seeds: 79 14 55 13

            seed-to-soil map:
            50 98 2
            52 50 48

            soil-to-fertilizer map:
            0 15 37
            37 52 2
            39 0 15

            fertilizer-to-water map:
            49 53 8
            0 11 42
            42 0 7
            57 7 4

            water-to-light map:
            88 18 7
            18 25 70

            light-to-temperature map:
            45 77 23
            81 45 19
            68 64 13

            temperature-to-humidity map:
            0 69 1
            1 0 69

            humidity-to-location map:
            60 56 37
            56 93 4
        """.trimIndent().split("\n")

    @Test
    fun testParseAlmanac() {
        val almanac = parseAlmanac(sampleInput)
        assertEquals(listOf(79L, 14L, 55L, 13L), almanac.seeds)
        assertEquals(7, almanac.maps.size)

        assertEquals(
            AlmanacMap(
                sourceCategory = "seed",
                destinationCategory = "soil",
                mapping = listOf(
                    AlmanacMapping(
                        destination = 50,
                        source = 98,
                        length = 2,
                    ),
                    AlmanacMapping(
                        destination = 52,
                        source = 50,
                        length = 48,
                    ),
                ),
            ),
            almanac.findMap(from = "seed", to = "soil")
        )

        assertEquals(
            AlmanacMap(
                sourceCategory = "soil",
                destinationCategory = "fertilizer",
                mapping = listOf(
                    AlmanacMapping(
                        destination = 0,
                        source = 15,
                        length = 37,
                    ),
                    AlmanacMapping(
                        destination = 37,
                        source = 52,
                        length = 2,
                    ),
                    AlmanacMapping(
                        destination = 39,
                        source = 0,
                        length = 15,
                    ),
                ),
            ),
            almanac.findMap(from = "soil")
        )
    }

    @Test
    fun testMapping() {
        val mapping = AlmanacMapping(
            destination = 50,
            source = 98,
            length = 2,
        )

        assertEquals(97, mapping.map(97))
        assertEquals(50, mapping.map(98))
        assertEquals(51, mapping.map(99))
        assertEquals(100, mapping.map(100))
    }

    @Test
    fun testMap() {
        val map = AlmanacMap(
            sourceCategory = "seed",
            destinationCategory = "soil",
            mapping = listOf(
                AlmanacMapping(
                    destination = 50,
                    source = 98,
                    length = 2,
                ),
                AlmanacMapping(
                    destination = 52,
                    source = 50,
                    length = 48,
                ),
            ),
        )

        assertEquals(81, map.map(79))
        assertEquals(14, map.map(14))
        assertEquals(57, map.map(55))
        assertEquals(13, map.map(13))
    }

    @Test
    fun testSampleAnswer1() {
        val almanac = parseAlmanac(sampleInput)
        assertEquals(
            listOf(82L, 43L, 86L, 35L),
            almanac.mapAll(from = "seed", to = "location", items = almanac.seeds)
        )
        assertEquals(35, almanac.mapAll(from = "seed", to = "location", items = almanac.seeds).min())
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val almanac = parseAlmanac(input)
        assertEquals(331445006, almanac.mapAll(from = "seed", to = "location", items = almanac.seeds).min())
    }
}
