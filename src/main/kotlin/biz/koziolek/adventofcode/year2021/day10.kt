package biz.koziolek.adventofcode.year2021

import java.io.File
import java.util.*

fun main() {
    val inputFile = File("src/main/resources/year2021/day10/input")
    val lines = inputFile.bufferedReader().readLines()
    val rootChunks = parseChunks(lines)

    println("Corrupted chunks score: ${getCorruptedChunksScore(rootChunks)}")
}

sealed interface Chunk {
    val children: List<Chunk>
    fun addChild(chunk: Chunk): Chunk
    fun asCode(): String
    fun containsCorrupted(): Boolean = children.any { it.containsCorrupted() }
    fun getCorrupted(): CorruptedChunk? = children.mapNotNull { it.getCorrupted() }.firstOrNull()
}

data class RootChunk(override val children: List<Chunk>) : Chunk {
    override fun addChild(chunk: Chunk) = RootChunk(children + chunk)

    override fun asCode() = children.joinToString("") { it.asCode() }
}

data class IncompleteChunk(
        val open: Char,
        override val children: List<Chunk> = emptyList()
) : Chunk {
    fun close(close: Char): Chunk =
            if (bracketsMatch(open, close)) {
                CompleteChunk(open, close, children)
            } else {
                CorruptedChunk(open, close, children)
            }

    override fun addChild(chunk: Chunk): Chunk =
            IncompleteChunk(open, children + chunk)

    override fun asCode(): String = open + children.joinToString("") { it.asCode() }
}

data class CompleteChunk(
        val open: Char,
        val close: Char,
        override val children: List<Chunk>
) : Chunk {
    override fun addChild(chunk: Chunk): Chunk =
            CompleteChunk(open, close, children + chunk)

    override fun asCode(): String = open + children.joinToString("") { it.asCode() } + close
}

data class CorruptedChunk(
        val open: Char,
        val close: Char,
        override val children: List<Chunk>
) : Chunk {
    override fun addChild(chunk: Chunk): Chunk =
            CorruptedChunk(open, close, children + chunk)

    override fun asCode(): String = open + children.joinToString("") { it.asCode() } + close
    
    fun getErrorMessage() = "Expected ${openToCloseBracketsMap[open]}, but found $close instead."

    override fun containsCorrupted() = true

    override fun getCorrupted() = this
}

private val openToCloseBracketsMap = mapOf(
        '(' to ')',
        '[' to ']',
        '{' to '}',
        '<' to '>'
)

private fun bracketsMatch(open: Char, close: Char): Boolean = openToCloseBracketsMap[open] == close

fun parseChunks(lines: List<String>): List<RootChunk> = lines.map { parseChunks(it) }

fun parseChunks(line: String): RootChunk {
    val stack: Deque<Chunk> = ArrayDeque()
    val chunks = mutableListOf<Chunk>()
    
    fun updateChildOnStack(child: Chunk) {
        if (stack.isNotEmpty()) {
            stack.pop()
                    .addChild(child)
                    .let { newParent -> stack.push(newParent) }
        } else {
            chunks.add(child)
        }
    }

    for (c in line.toCharArray()) {
        if (c in "([{<") {
            val newChunk = IncompleteChunk(c)
            stack.push(newChunk)
        } else {
            when (val currentChunk = stack.pop()) {
                is IncompleteChunk -> updateChildOnStack(currentChunk.close(c))
                else -> throw IllegalStateException("Tried to close " + currentChunk::class.simpleName)
            }
        }
    }

    while (stack.isNotEmpty()) {
        updateChildOnStack(stack.pop())
    }

    return RootChunk(chunks)
}

fun printChunk(chunk: RootChunk) {
    println("Chunk string: '${chunk.asCode()}'")
    println("Has tree:")
    printChunks(chunk.children)
}

fun printChunks(chunks: List<Chunk>, indent: Int = 0) {
    for (chunk in chunks) {
        print(" ".repeat(indent))
        when (chunk) {
            is RootChunk -> {}
            is IncompleteChunk -> print(chunk.open)
            is CompleteChunk -> print(chunk.open)
            is CorruptedChunk -> print(chunk.open)
        }

        if (chunk.children.isNotEmpty()) {
            println()
            printChunks(chunk.children, indent = indent + 2)
            print(" ".repeat(indent))
        }

        when (chunk) {
            is RootChunk -> {}
            is IncompleteChunk -> println()
            is CompleteChunk -> println(chunk.close)
            is CorruptedChunk -> println(chunk.close)
        }
    }
}

fun getCorruptedChunksScore(rootChunks: List<RootChunk>): Int =
        rootChunks.filter { it.containsCorrupted() }
                .map { it.getCorrupted() }
                .sumOf { getPoints(it!!) }

fun getPoints(corruptedChunk: CorruptedChunk): Int =
        when (corruptedChunk.close) {
            ')' -> 3
            ']' -> 57
            '}' -> 1197
            '>' -> 25137
            else -> throw IllegalArgumentException("Unexpected close character: ${corruptedChunk.close}")
        }
