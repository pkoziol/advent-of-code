package biz.koziolek.adventofcode.year2024.day09

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2024")
internal class Day9Test {

    private val sampleInput = """
            2333133121414131402
        """.trimIndent().split("\n")

    @Test
    fun testParseDiskMap() {
        val blocks = diskMapToBlocks(sampleInput)
        assertEquals(listOf(
            Block(id = 0),
            Block(id = 0),
            Block(id = null),
            Block(id = null),
            Block(id = null),
            Block(id = 1),
            Block(id = 1),
            Block(id = 1),
            Block(id = null),
            Block(id = null),
            Block(id = null),
            Block(id = 2),
            Block(id = null),
            Block(id = null),
            Block(id = null),
            Block(id = 3),
            Block(id = 3),
            Block(id = 3),
            Block(id = null),
            Block(id = 4),
            Block(id = 4),
            Block(id = null),
            Block(id = 5),
            Block(id = 5),
            Block(id = 5),
            Block(id = 5),
            Block(id = null),
            Block(id = 6),
            Block(id = 6),
            Block(id = 6),
            Block(id = 6),
            Block(id = null),
            Block(id = 7),
            Block(id = 7),
            Block(id = 7),
            Block(id = null),
            Block(id = 8),
            Block(id = 8),
            Block(id = 8),
            Block(id = 8),
            Block(id = 9),
            Block(id = 9),
        ), blocks)
    }

    @Test
    fun testDefragmentDisk() {
        val blocks = diskMapToBlocks(sampleInput)
        val defragmentedBlocks = defragmentDisk(blocks)
        assertEquals(listOf(
            Block(id = 0),
            Block(id = 0),
            Block(id = 9),
            Block(id = 9),
            Block(id = 8),
            Block(id = 1),
            Block(id = 1),
            Block(id = 1),
            Block(id = 8),
            Block(id = 8),
            Block(id = 8),
            Block(id = 2),
            Block(id = 7),
            Block(id = 7),
            Block(id = 7),
            Block(id = 3),
            Block(id = 3),
            Block(id = 3),
            Block(id = 6),
            Block(id = 4),
            Block(id = 4),
            Block(id = 6),
            Block(id = 5),
            Block(id = 5),
            Block(id = 5),
            Block(id = 5),
            Block(id = 6),
            Block(id = 6),
            Block(id = null),
            Block(id = null),
            Block(id = null),
            Block(id = null),
            Block(id = null),
            Block(id = null),
            Block(id = null),
            Block(id = null),
            Block(id = null),
            Block(id = null),
            Block(id = null),
            Block(id = null),
            Block(id = null),
            Block(id = null),
        ), defragmentedBlocks)
    }

    @Test
    fun testSampleAnswer1() {
        val blocks = diskMapToBlocks(sampleInput)
        val defragmentedBlocks = defragmentDisk(blocks)
        val checksum = calculateFilesystemChecksum(defragmentedBlocks)
        assertEquals(1928, checksum)
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val blocks = diskMapToBlocks(input)
        val defragmentedBlocks = defragmentDisk(blocks)
        val checksum = calculateFilesystemChecksum(defragmentedBlocks)
        assertEquals(6225730762521, checksum)
    }
}
