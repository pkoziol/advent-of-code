package biz.koziolek.adventofcode.year2022.day07

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val rootDir = parseTerminalOutput(inputFile.bufferedReader().readLines())
    println(printTree(rootDir))

    println("Total size of directories with a total size of at most 100000: ${sumDirectoriesSize(rootDir, maxSize = 100_000)}")

    val dirToDelete = chooseDirectoryToDelete(
        rootDir,
        fileSystemSize = 70000000,
        minRequiredSpace = 30000000,
    )
    println("Need to delete ${dirToDelete.name} to free up ${dirToDelete.size}")
}

sealed class FileOrDir {
    abstract val name: String
    abstract val size: Int
}

data class File(
    override val name: String,
    override val size: Int,
) : FileOrDir()

data class Dir(
    override val name: String,
    val children: List<FileOrDir> = emptyList(),
) : FileOrDir() {

    override val size: Int
        get() = children.sumOf { it.size }

    fun find(path: String): FileOrDir? {
        val parts = path.split('/', limit = 2)
        val (head, tail) = if (parts.size == 2) {
            parts[0] to parts[1]
        } else {
            parts[0] to ""
        }
        val child = children.find { it.name == head }

        return when {
            tail.isBlank() -> child
            child is Dir -> child.find(tail)
            else -> null
        }
    }

    fun walk(): Sequence<FileOrDir> =
        sequence {
            yield(this@Dir)
            children.sortedBy { it.name }
                .forEach { child ->
                    when (child) {
                        is File -> yield(child)
                        is Dir -> yieldAll(child.walk())
                    }
                }
        }

    fun add(child: FileOrDir): Dir =
        copy(children = children + child)

    fun remove(childName: String): Dir =
        copy(children = children.filter { it.name != childName })
}

fun printTree(fileOrDir: FileOrDir, level: Int = 0): String =
    buildString {
        append("  ".repeat(level))
        append("- ${fileOrDir.name}")
        
        when (fileOrDir) {
            is File -> append(" (file, size=${fileOrDir.size})")
            is Dir -> {
                append(" (dir)")
                fileOrDir.children.sortedBy { it.name }.forEachIndexed { index, child ->
                    append("\n")
                    append(printTree(child, level = level + 1))
                }
            }
        }
    }

fun parseTerminalOutput(lines: Iterable<String>): Dir {
    fun updateCurrentDir(dirs: List<Dir>, newFile: FileOrDir): List<Dir> {
        val currentDir = dirs.last()
        val updatedDir = currentDir.add(newFile)
        return dirs.dropLast(1) + updatedDir
    }

    fun goUp(dirs: List<Dir>): List<Dir> {
        return if (dirs.size >= 2) {
            val (parentDir, currentDir) = dirs.takeLast(2)
            val updatedParent = parentDir.remove(currentDir.name).add(currentDir)
            dirs.dropLast(2) + updatedParent
        } else {
            dirs
        }
    }

    fun goRoot(dirs: List<Dir>): List<Dir> {
        var tmpDirs = dirs
        while (tmpDirs.size >= 2) {
            tmpDirs = goUp(tmpDirs)
        }
        return tmpDirs
    }

    return lines
        .fold(listOf<Dir>(Dir("/"))) { dirs, line ->
            when {
                line == "$ cd /" -> goRoot(dirs)
                line == "$ cd .." -> goUp(dirs)
                line.startsWith("$ cd ") -> {
                    val (_, _, name) = line.split(' ', limit = 3)
                    val currentDir = dirs.last()
                    when (val childDir = currentDir.find(name)) {
                        null -> throw IllegalStateException("'$name' not found")
                        is File -> throw IllegalStateException("'$name' is a file")
                        is Dir -> dirs + childDir
                    }
                }
                line == "$ ls" -> dirs
                line.startsWith("dir ") -> {
                    val (_, name) = line.split(' ', limit = 2)
                    val newDir = Dir(name)
                    updateCurrentDir(dirs, newDir)
                }
                else -> {
                    val (size, name) = line.split(' ', limit = 2)
                    val newFile = File(name, size.toInt())
                    updateCurrentDir(dirs, newFile)
                }
            }
        }
        .let { dirs -> goRoot(dirs).single() }
}

fun sumDirectoriesSize(fileOrDir: FileOrDir, maxSize: Int) =
    when (fileOrDir) {
        is File -> 0
        is Dir -> fileOrDir
            .walk()
            .filter { it is Dir && it.size <= maxSize }
            .sumOf { it.size }
    }

fun chooseDirectoryToDelete(dir: Dir, fileSystemSize: Int, minRequiredSpace: Int): Dir {
    val minSize = minRequiredSpace - (fileSystemSize - dir.size)

    return dir.walk()
        .filter { it is Dir }
        .map { it as Dir }
        .filter { it.size >= minSize }
        .sortedBy { it.size }
        .first()
}
