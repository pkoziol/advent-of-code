package biz.koziolek.adventofcode.year2021.day10

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

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
        val corruptedChunks = rootChunks
                .filter { it.contains(CorruptedChunk::class) }
                .map { it.find(CorruptedChunk::class) }
        assertEquals(5, corruptedChunks.size)

        assertEquals("Expected ], but found } instead.", corruptedChunks[0]?.getErrorMessage())
        assertEquals("Expected ], but found ) instead.", corruptedChunks[1]?.getErrorMessage())
        assertEquals("Expected ), but found ] instead.", corruptedChunks[2]?.getErrorMessage())
        assertEquals("Expected >, but found ) instead.", corruptedChunks[3]?.getErrorMessage())
        assertEquals("Expected ], but found > instead.", corruptedChunks[4]?.getErrorMessage())
    }

    @Test
    fun testCorruptedChunksScoreForSample() {
        val rootChunks = sampleInput.map { parseChunks(it) }
        assertEquals(26397, getCorruptedChunksScore(rootChunks))
    }

    @Test
    fun testAnswerPart1() {
        val fullInput = findInput(object {}).readLines()
        val rootChunks = fullInput.map { parseChunks(it) }
        assertEquals(387363, getCorruptedChunksScore(rootChunks))
    }

    @Test
    fun testGetIncompleteChunksFromSample() {
        val rootChunks = sampleInput.map { parseChunks(it) }
        val containingIncompleteChunks = rootChunks.filter { !it.contains(CorruptedChunk::class) && it.contains(IncompleteChunk::class) }
        assertEquals(5, containingIncompleteChunks.size)

        assertEquals("[({(<(())[]>[[{[]{<()<>>", containingIncompleteChunks[0].asCode())
        assertEquals("[(()[<>])]({[<{<<[]>>(", containingIncompleteChunks[1].asCode())
        assertEquals("(((({<>}<{<{<>}{[]{[]{}", containingIncompleteChunks[2].asCode())
        assertEquals("{<[[]]>}<{[{[{[]{()[[[]", containingIncompleteChunks[3].asCode())
        assertEquals("<{([{{}}[<[[[<>{}]]]>[]]", containingIncompleteChunks[4].asCode())
    }

    @Test
    fun testAutocompleteChunksFromSample() {
        val rootChunks = sampleInput.map { parseChunks(it) }
        val autocompletedChunks = rootChunks.filter { !it.contains(CorruptedChunk::class) && it.contains(IncompleteChunk::class) }
                .map { it.complete() }
        assertEquals(5, autocompletedChunks.size)

        assertEquals("[({(<(())[]>[[{[]{<()<>>}}]])})]", autocompletedChunks[0].asCode())
        assertEquals("[(()[<>])]({[<{<<[]>>()}>]})", autocompletedChunks[1].asCode())
        assertEquals("(((({<>}<{<{<>}{[]{[]{}}}>}>))))", autocompletedChunks[2].asCode())
        assertEquals("{<[[]]>}<{[{[{[]{()[[[]]]}}]}]}>", autocompletedChunks[3].asCode())
        assertEquals("<{([{{}}[<[[[<>{}]]]>[]]])}>", autocompletedChunks[4].asCode())
    }

    @Test
    fun testAutocompleteScoreForSample() {
        val rootChunks = sampleInput.map { parseChunks(it) }
        val containingIncompleteChunks = rootChunks.filter { !it.contains(CorruptedChunk::class) && it.contains(IncompleteChunk::class) }
        assertEquals(288957, getAutocompleteScore(containingIncompleteChunks))
    }

    @Test
    fun testAnswer2() {
        val fullInput = findInput(object {}).readLines()
        val rootChunks = fullInput.map { parseChunks(it) }
        val containingIncompleteChunks = rootChunks.filter { !it.contains(CorruptedChunk::class) && it.contains(IncompleteChunk::class) }
        assertEquals(4330777059, getAutocompleteScore(containingIncompleteChunks))
    }
}
