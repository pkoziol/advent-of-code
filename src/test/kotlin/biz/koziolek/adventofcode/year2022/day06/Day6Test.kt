package biz.koziolek.adventofcode.year2022.day06

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2022")
internal class Day6Test {

    @Test
    fun testFindStartOfPacket() {
        Assertions.assertEquals(7, findStartOfPacket("mjqjpqmgbljsphdztnvjfqwrcgsmlb"))
        Assertions.assertEquals(5, findStartOfPacket("bvwbjplbgvbhsrlpgdmjqwftvncz"))
        Assertions.assertEquals(6, findStartOfPacket("nppdvjthqldpwncqszvftbrmjlhg"))
        Assertions.assertEquals(10, findStartOfPacket("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"))
        Assertions.assertEquals(11, findStartOfPacket("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"))
    }

    @Test
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLine()
        val startOfPacket = findStartOfPacket(input)
        Assertions.assertEquals(1987, startOfPacket)
    }
}