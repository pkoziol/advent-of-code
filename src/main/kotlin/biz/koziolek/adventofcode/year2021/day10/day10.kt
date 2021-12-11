package biz.koziolek.adventofcode.year2021.day10

import biz.koziolek.adventofcode.findInput
import java.util.*
import kotlin.reflect.KClass

fun main() {
    val inputFile = findInput(object {})
    val lines = inputFile.bufferedReader().readLines()
    val rootChunks = parseChunks(lines)

    println("Corrupted chunks score: ${getCorruptedChunksScore(rootChunks)}")

    val containingIncompleteChunks = rootChunks.filter { !it.contains(CorruptedChunk::class) && it.contains(IncompleteChunk::class) }
    println("Autocomplete score: ${getAutocompleteScore(containingIncompleteChunks)}")
}

sealed interface Chunk {
    val children: List<Chunk>
    fun addChild(chunk: Chunk): Chunk
    fun asCode(): String

    fun contains(clazz: KClass<out Chunk>): Boolean = clazz.isInstance(this) || children.any { it.contains(clazz) }

    fun <T : Chunk> find(clazz: KClass<T>): T? =
            if (clazz.isInstance(this)) {
                this as T
            } else {
                children.mapNotNull { it.find(clazz) }.firstOrNull()
            }

    fun complete(): Chunk
}

data class RootChunk(override val children: List<Chunk>) : Chunk {
    override fun addChild(chunk: Chunk) = RootChunk(children + chunk)

    override fun asCode() = children.joinToString("") { it.asCode() }

    override fun complete() = RootChunk(children.map { it.complete() })
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

    override fun complete() = CompleteChunk(
            open = open,
            close = openToCloseBracketsMap[open]!!,
            children = children.map { it.complete() }
    )
}

data class CompleteChunk(
        val open: Char,
        val close: Char,
        override val children: List<Chunk>
) : Chunk {
    override fun addChild(chunk: Chunk): Chunk =
            CompleteChunk(open, close, children + chunk)

    override fun asCode(): String = open + children.joinToString("") { it.asCode() } + close

    override fun complete() = CompleteChunk(
            open = open,
            close = close,
            children = children.map { it.complete() }
    )
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

    override fun complete() = CompleteChunk(
            open = open,
            close = openToCloseBracketsMap[open]!!,
            children = children.map { it.complete() }
    )
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
        rootChunks.filter { it.contains(CorruptedChunk::class) }
                .map { it.find(CorruptedChunk::class) }
                .sumOf { getCorruptedPoints(it!!.close) }

fun getCorruptedPoints(char: Char): Int =
        when (char) {
            ')' -> 3
            ']' -> 57
            '}' -> 1197
            '>' -> 25137
            else -> throw IllegalArgumentException("Unexpected close character: $char")
        }

fun getAutocompleteScore(rootChunks: List<RootChunk>): Long {
    val scores = rootChunks.map { getAutocompleteScore(it) }.sorted()

    if (scores.size % 2 != 1) {
        throw IllegalArgumentException("Even number of incomplete chunks passed - this is not supported")
    }

    return scores[scores.size / 2]
}

fun getAutocompleteScore(rootChunk: RootChunk): Long {
    val oldCode = rootChunk.asCode()
    val newCode = rootChunk.complete().asCode()
    val diff = newCode.substring(oldCode.length)

    return diff.fold(0) { acc, c -> acc * 5 + getAutocompletePoints(c) }
}

fun getAutocompletePoints(char: Char): Int =
        when (char) {
            ')' -> 1
            ']' -> 2
            '}' -> 3
            '>' -> 4
            else -> throw IllegalArgumentException("Unexpected close character: $char")
        }
