package biz.koziolek.adventofcode.year2021

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class Day10Test {

    private val sampleInput = """
        [({(<(())[]>[[{[]{<()<>>
        [(()[<>])]({[<{<<[]>>(
        {([(<{}[<>[]}>{[]{[(<()>
        (((({<>}<{<{<>}{[]{[]{}
        [[<[([]))<([[{}[[()]]]
        [{[{({}]{}}([{[{{{}}([]
        {<[[]]>}<{[{[{[]{()[[[]
        [<(<(<(<{}))><([]([]()
        <{([([[(<>()){}]>(<<{{
        <{([{{}}[<[[[<>{}]]]>[]]
    """.trimIndent().split("\n")

    @Test
    fun testParseSampleCompleteChunks() {
        var rootChunk = parseChunks("()")
        printChunk(rootChunk)
        assertEquals(1, rootChunk.children.size)
        assertCompleteChunk('(', ')', 0, rootChunk.children[0])

        rootChunk = parseChunks("{()()()}")
        printChunk(rootChunk)
        assertEquals(1, rootChunk.children.size)
        assertCompleteChunk('{', '}', 3, rootChunk.children[0])
        assertCompleteChunk('(', ')', 0, rootChunk.children[0].children[0])
        assertCompleteChunk('(', ')', 0, rootChunk.children[0].children[1])
        assertCompleteChunk('(', ')', 0, rootChunk.children[0].children[2])

        rootChunk = parseChunks("<([{}])>")
        printChunk(rootChunk)
        assertEquals(1, rootChunk.children.size)
        assertCompleteChunk('<', '>', 1, rootChunk.children[0])
        assertCompleteChunk('(', ')', 1, rootChunk.children[0].children[0])
        assertCompleteChunk('[', ']', 1, rootChunk.children[0].children[0].children[0])
        assertCompleteChunk('{', '}', 0, rootChunk.children[0].children[0].children[0].children[0])

        rootChunk = parseChunks("()[]{}<>")
        printChunk(rootChunk)
        assertEquals(4, rootChunk.children.size)
        assertCompleteChunk('(', ')', 0, rootChunk.children[0])
        assertCompleteChunk('[', ']', 0, rootChunk.children[1])
        assertCompleteChunk('{', '}', 0, rootChunk.children[2])
        assertCompleteChunk('<', '>', 0, rootChunk.children[3])
    }

    @Test
    fun testParseSampleIncompleteChunks() {
        var rootChunk = parseChunks("(")
        printChunk(rootChunk)
        assertEquals(1, rootChunk.children.size)
        assertIncompleteChunk('(', 0, rootChunk.children[0])

        rootChunk = parseChunks("()[<>({")
        printChunk(rootChunk)
        assertEquals(2, rootChunk.children.size)
        assertCompleteChunk('(', ')', 0, rootChunk.children[0])
        assertIncompleteChunk('[', 2, rootChunk.children[1])
        assertCompleteChunk('<', '>', 0, rootChunk.children[1].children[0])
        assertIncompleteChunk('(', 1, rootChunk.children[1].children[1])
        assertIncompleteChunk('{', 0, rootChunk.children[1].children[1].children[0])
    }

    @Test
    fun testParseSampleCorruptedChunks() {
        var rootChunk = parseChunks("(]")
        printChunk(rootChunk)
        assertEquals(1, rootChunk.children.size)
        assertCorruptedChunk('(', ']', 0, rootChunk.children[0])

        rootChunk = parseChunks("{()()()>")
        printChunk(rootChunk)
        assertEquals(1, rootChunk.children.size)
        assertCorruptedChunk('{', '>', 3, rootChunk.children[0])
        assertCompleteChunk('(', ')', 0, rootChunk.children[0].children[0])
        assertCompleteChunk('(', ')', 0, rootChunk.children[0].children[1])
        assertCompleteChunk('(', ')', 0, rootChunk.children[0].children[2])
    }

    private fun assertCompleteChunk(open: Char, close: Char, childrenSize: Int, chunk: Chunk) {
        val c = assertInstanceOf(CompleteChunk::class.java, chunk)
        assertEquals(open, c.open)
        assertEquals(close, c.close)
        assertEquals(childrenSize, c.children.size)
    }

    private fun assertIncompleteChunk(open: Char, childrenSize: Int, chunk: Chunk) {
        val c = assertInstanceOf(IncompleteChunk::class.java, chunk)
        assertEquals(open, c.open)
        assertEquals(childrenSize, c.children.size)
    }

    private fun assertCorruptedChunk(open: Char, close: Char, childrenSize: Int, chunk: Chunk) {
        val c = assertInstanceOf(CorruptedChunk::class.java, chunk)
        assertEquals(open, c.open)
        assertEquals(close, c.close)
        assertEquals(childrenSize, c.children.size)
    }

    @Test
    fun testGetErrorMessagesFromSample() {
        val rootChunks = sampleInput.map { parseChunks(it) }
        val containingCorruptedChunks = rootChunks.filter { it.containsCorrupted() }
        assertEquals(5, containingCorruptedChunks.size)

        assertEquals("Expected ], but found } instead.", containingCorruptedChunks[0].getCorrupted()?.getErrorMessage())
        assertEquals("Expected ], but found ) instead.", containingCorruptedChunks[1].getCorrupted()?.getErrorMessage())
        assertEquals("Expected ), but found ] instead.", containingCorruptedChunks[2].getCorrupted()?.getErrorMessage())
        assertEquals("Expected >, but found ) instead.", containingCorruptedChunks[3].getCorrupted()?.getErrorMessage())
        assertEquals("Expected ], but found > instead.", containingCorruptedChunks[4].getCorrupted()?.getErrorMessage())
    }

    @Test
    fun testCorruptedChunksScoreForSample() {
        val rootChunks = sampleInput.map { parseChunks(it) }
        assertEquals(26397, getCorruptedChunksScore(rootChunks))
    }

    @Test
    fun testAnswerPart1() {
        val fullInput = File("src/main/resources/year2021/day10/input").readLines()
        val rootChunks = fullInput.map { parseChunks(it) }
        assertEquals(387363, getCorruptedChunksScore(rootChunks))
    }
}
