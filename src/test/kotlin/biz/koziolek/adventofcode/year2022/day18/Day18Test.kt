package biz.koziolek.adventofcode.year2022.day18

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2022")
internal class Day18Test {

    private val sampleInput = """
            2,2,2
            1,2,2
            3,2,2
            2,1,2
            2,3,2
            2,2,1
            2,2,3
            2,2,4
            2,2,6
            1,2,5
            3,2,5
            2,1,5
            2,3,5
        """.trimIndent().split("\n")

    @Test
    fun testParseDroplet() {
        val droplet = parseDroplet(sampleInput)
        assertEquals(13, droplet.size)
    }

    @Test
    fun testGetSurfaceArea() {
        val droplet = parseDroplet(sampleInput)
        assertEquals(64, getSurfaceArea(droplet))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val droplet = parseDroplet(input)
        assertEquals(4628, getSurfaceArea(droplet))
    }

    @Test
    fun testGetOutsideSurfaceArea() {
        val droplet = parseDroplet(sampleInput)
        val lavaAirMap = mapOutside(droplet)
        assertEquals(58, getOutsideSurfaceArea(lavaAirMap))
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val droplet = parseDroplet(input)
        val lavaAirMap = mapOutside(droplet)
        assertEquals(2582, getOutsideSurfaceArea(lavaAirMap))
    }
}
