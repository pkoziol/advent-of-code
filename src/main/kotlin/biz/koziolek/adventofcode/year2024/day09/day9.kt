package biz.koziolek.adventofcode.year2024.day09

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val blocks = diskMapToBlocks(inputFile.bufferedReader().readLines())
    val defragmentedBlocks = defragmentDisk(blocks)
    val checksum = calculateFilesystemChecksum(defragmentedBlocks)
    println("Checksum: $checksum")
}

data class Block(val id: Int?) {
    fun isFile() = id != null
    fun isFree() = id == null
}

fun diskMapToBlocks(lines: Iterable<String>): List<Block> =
    buildList {
        var id = 0
        var isFile = true
        for (c in lines.first()) {
            val count = c.digitToInt()
            repeat(count) {
                add(Block(id = if (isFile) id else null))
            }
            if (isFile) {
                id++
            }
            isFile = !isFile
        }
    }

fun defragmentDisk(blocks: List<Block>): List<Block> {
    val newBlocks = blocks.toMutableList()
    var freeIndex = blocks.indexOfFirst { it.isFree() }
    var movedFileIndex = blocks.indexOfLast { it.isFile() }

    while (movedFileIndex > freeIndex) {
        val tmp = newBlocks[freeIndex]
        newBlocks[freeIndex] = newBlocks[movedFileIndex]
        newBlocks[movedFileIndex] = tmp

        while (newBlocks[freeIndex].isFile()) {
            freeIndex++
        }

        while (newBlocks[movedFileIndex].isFree()) {
            movedFileIndex--
        }
    }

    return newBlocks
}

fun calculateFilesystemChecksum(blocks: List<Block>): Long =
    blocks.foldIndexed(0) { index, acc, block ->
        acc + if (block.isFile()) block.id!! * index else 0
    }
