package biz.koziolek.adventofcode.year2022.day15

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2022")
internal class Day15Test {

    private val sampleInput = """
            Sensor at x=2, y=18: closest beacon is at x=-2, y=15
            Sensor at x=9, y=16: closest beacon is at x=10, y=16
            Sensor at x=13, y=2: closest beacon is at x=15, y=3
            Sensor at x=12, y=14: closest beacon is at x=10, y=16
            Sensor at x=10, y=20: closest beacon is at x=10, y=16
            Sensor at x=14, y=17: closest beacon is at x=10, y=16
            Sensor at x=8, y=7: closest beacon is at x=2, y=10
            Sensor at x=2, y=0: closest beacon is at x=2, y=10
            Sensor at x=0, y=11: closest beacon is at x=2, y=10
            Sensor at x=20, y=14: closest beacon is at x=25, y=17
            Sensor at x=17, y=20: closest beacon is at x=21, y=22
            Sensor at x=16, y=7: closest beacon is at x=15, y=3
            Sensor at x=14, y=3: closest beacon is at x=15, y=3
            Sensor at x=20, y=1: closest beacon is at x=15, y=3
        """.trimIndent().split("\n")

    @Test
    fun testParseSensors() {
        val sensors = parseSensors(sampleInput)
        assertEquals(14, sensors.size)
        assertEquals(
            Sensor(
                location = Coord(2, 18),
                beacon = Beacon(
                    location = Coord(-2, 15),
                ),
            ),
            sensors[0]
        )
    }

    @Test
    fun testCountPositionsWithoutBeacons() {
        val sensors = parseSensors(sampleInput)
        assertEquals(26, countPositionsWithoutBeacons(sensors, y = 10))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val sensors = parseSensors(input)
        val countPositionsWithoutBeacons = countPositionsWithoutBeacons(sensors, y = 2000000)
        assertEquals(5832528, countPositionsWithoutBeacons)
    }
}