package biz.koziolek.adventofcode.year2022.day07

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2022")
internal class Day7Test {

    private val sampleInput = """
            ${'$'} cd /
            ${'$'} ls
            dir a
            14848514 b.txt
            8504156 c.dat
            dir d
            ${'$'} cd a
            ${'$'} ls
            dir e
            29116 f
            2557 g
            62596 h.lst
            ${'$'} cd e
            ${'$'} ls
            584 i
            ${'$'} cd ..
            ${'$'} cd ..
            ${'$'} cd d
            ${'$'} ls
            4060174 j
            8033020 d.log
            5626152 d.ext
            7214296 k
        """.trimIndent().split("\n")

    @Test
    fun testStaticData() {
        val rootDir = Dir("/", listOf(
            File("aaa", 10),
            Dir("bbb", listOf(
                File("ccc", 20),
                File("ddd", 30)
            )),
            Dir("eee", emptyList()),
        ))

        assertEquals(60, rootDir.size)
        assertEquals(10, rootDir.find("aaa")?.size)
        assertEquals(50, rootDir.find("bbb")?.size)
        assertEquals(20, rootDir.find("bbb/ccc")?.size)
        assertEquals(30, rootDir.find("bbb/ddd")?.size)
        assertEquals(0, rootDir.find("eee")?.size)
        assertNull(rootDir.find("fff"))
        assertNull(rootDir.find("aaa/xxx"))
        assertNull(rootDir.find("bbb/yyy"))
        
        assertEquals("""
            - / (dir)
              - aaa (file, size=10)
              - bbb (dir)
                - ccc (file, size=20)
                - ddd (file, size=30)
              - eee (dir)
        """.trimIndent(), printTree(rootDir))

        assertEquals(
            listOf("/", "aaa", "bbb", "ccc", "ddd", "eee"),
            rootDir.walk().map { it.name }.toList()
        )
    }

    @Test
    fun testParseTerminalOutput() {
        val rootDir = parseTerminalOutput(sampleInput)

        assertEquals("""
            - / (dir)
              - a (dir)
                - e (dir)
                  - i (file, size=584)
                - f (file, size=29116)
                - g (file, size=2557)
                - h.lst (file, size=62596)
              - b.txt (file, size=14848514)
              - c.dat (file, size=8504156)
              - d (dir)
                - d.ext (file, size=5626152)
                - d.log (file, size=8033020)
                - j (file, size=4060174)
                - k (file, size=7214296)
        """.trimIndent(), printTree(rootDir))
    }

    @Test
    fun testSizes() {
        val rootDir = parseTerminalOutput(sampleInput)
        assertEquals(48381165, rootDir.size)
        assertEquals(584, rootDir.find("a/e/")?.size)
        assertEquals(584, rootDir.find("a/e")?.size)
        assertEquals(94853, rootDir.find("a")?.size)
        assertEquals(14848514, rootDir.find("b.txt")?.size)
        assertEquals(8504156, rootDir.find("c.dat")?.size)
        assertEquals(24933642, rootDir.find("d")?.size)
        assertEquals(48381165, rootDir.size)
        assertEquals(48381165, rootDir.size)
    }

    @Test
    fun testSampleAnswer1() {
        val rootDir = parseTerminalOutput(sampleInput)
        assertEquals(95437, sumDirectoriesSize(rootDir, maxSize = 100_000))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val rootDir = parseTerminalOutput(input)
        assertEquals(1182909, sumDirectoriesSize(rootDir, maxSize = 100_000))
    }

    @Test
    fun testSampleAnswer2() {
        val rootDir = parseTerminalOutput(sampleInput)
        val dirToDelete = chooseDirectoryToDelete(
            rootDir,
            fileSystemSize = 70000000,
            minRequiredSpace = 30000000,
        )
        assertEquals("d", dirToDelete.name)
        assertEquals(24933642, dirToDelete.size)
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val rootDir = parseTerminalOutput(input)
        val dirToDelete = chooseDirectoryToDelete(
            rootDir,
            fileSystemSize = 70000000,
            minRequiredSpace = 30000000,
        )
        assertEquals(2832508, dirToDelete.size)
    }
}
