package biz.koziolek.adventofcode.year2024.day09

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val blocks = diskMapToBlocks(inputFile.bufferedReader().readLines())
    val defragmentedBlocks = defragmentDisk(blocks)
    val checksum = calculateFilesystemChecksum(defragmentedBlocks)
    println("Checksum: $checksum")
}

data class Block(val id: Int?, val size: Int = 1) {
    fun isFile() = id != null
    fun isFree() = id == null
}

fun diskMapToBlocks(lines: Iterable<String>, splitBlocks: Boolean = true): List<Block> =
    buildList {
        var id = 0
        var isFile = true
        for (c in lines.first()) {
            val count = c.digitToInt()

            if (splitBlocks) {
                repeat(count) {
                    add(Block(id = if (isFile) id else null))
                }
            } else {
                if (count > 0) {
                    add(
                        Block(
                            id = if (isFile) id else null,
                            size = count,
                        )
                    )
                }
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

fun defragmentDisk2(blocks: List<Block>): List<Block> {
    val newBlocks = blocks.toMutableList()
    var movedId = blocks.filter { it.isFile() }.mapNotNull { it.id }.max()
    var movedFile = newBlocks.single { it.id == movedId }

    while (true) {
//        println(newBlocks.toDebugString(verbose = true))
//        Thread.sleep(500)

        val freeBlock = newBlocks.firstOrNull { it.isFree() && it.size >= movedFile.size }
        if (freeBlock != null) {
            val freeBlockIndex = newBlocks.indexOf(freeBlock)
            var oldMovedFileIndex = newBlocks.lastIndexOf(movedFile)

            if (freeBlockIndex < oldMovedFileIndex) {
                oldMovedFileIndex = moveBlock(newBlocks, freeBlockIndex, freeBlock, oldMovedFileIndex, movedFile)
                addNewFreeBlock(newBlocks, oldMovedFileIndex, movedFile.size)
            }
        }

        if (movedId == 0) {
            break
        } else {
            movedId--
            movedFile = newBlocks.first { it.id == movedId }
        }
    }

    return newBlocks
}

private fun moveBlock(
    newBlocks: MutableList<Block>,
    freeBlockIndex: Int,
    freeBlock: Block,
    movedFileIndex: Int,
    movedFile: Block
): Int {
    var updatedMovedFileIndex = movedFileIndex

    newBlocks.remove(freeBlock)

    if (freeBlock.size > movedFile.size) {
        newBlocks.add(
            freeBlockIndex, Block(
                id = null,
                size = freeBlock.size - movedFile.size,
            )
        )
        updatedMovedFileIndex++
    }

    newBlocks.add(freeBlockIndex, movedFile)

    return updatedMovedFileIndex
}

private fun addNewFreeBlock(newBlocks: MutableList<Block>, index: Int, size: Int) {
    var newFreeSize = size
    var removeNextFree = false
    var removePrevFree = false
    newBlocks.getOrNull(index + 1)?.let {
        if (it.isFree()) {
            newFreeSize += it.size
            removeNextFree = true
        }
    }
    newBlocks.getOrNull(index - 1)?.let {
        if (it.isFree()) {
            newFreeSize += it.size
            removePrevFree = true
        }
    }

    newBlocks[index] = Block(id = null, size = newFreeSize)
    if (removeNextFree) {
        newBlocks.removeAt(index + 1)
    }
    if (removePrevFree) {
        newBlocks.removeAt(index - 1)
    }
}

private fun List<Block>.toDebugString(verbose: Boolean): String =
    buildString {
        require(this@toDebugString.maxOf { it.id ?: 0 } <= 9) { "Only 10 blocks are supported" }

        for (block in this@toDebugString) {
            if (verbose) {
                append('[')
            }
            repeat(block.size) {
                append(if (block.isFile()) block.id else '.')
            }
            if (verbose) {
                append(']')
            }
        }
    }

fun calculateFilesystemChecksum(blocks: List<Block>): Long {
    var pos = 0
    var checksum = 0L
    for (block in blocks) {
        for (i in 0 until block.size) {
            if (block.isFile()) {
                checksum += pos.toLong() * block.id!!
            }
            pos++
        }
    }
    return checksum
}
