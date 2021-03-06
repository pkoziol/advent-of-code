package biz.koziolek.adventofcode

import java.io.File
import java.util.*
import kotlin.collections.HashSet

fun findInput(obj: Any): File =
    obj.javaClass.getResource("input")
        ?.toURI()
        ?.let { File(it) }
        ?: throw IllegalArgumentException("Could not find input file for: ${obj.javaClass.packageName}")

fun <T> visitAll(start: T, visitor: (T) -> Iterable<T>) {
    val toVisit: Queue<T> = ArrayDeque()
    val visited: MutableSet<T> = HashSet()
    var current: T? = start

    while (current != null) {
        if (current !in visited) {
            val nextToVisit = visitor(current)
            visited.add(current)
            toVisit.addAll(nextToVisit)
        }

        current = toVisit.poll()
    }
}

private val ESC = Char(27)

@Suppress("unused")
enum class AsciiColor(private val code: Int) {
    BLACK(30),
    RED(31),
    GREEN(32),
    YELLOW(33),
    BLUE(34),
    MAGENTA(35),
    CYAN(36),
    WHITE(37),
    BRIGHT_BLACK(90),
    BRIGHT_RED(91),
    BRIGHT_GREEN(92),
    BRIGHT_YELLOW(93),
    BRIGHT_BLUE(94),
    BRIGHT_MAGENTA(95),
    BRIGHT_CYAN(96),
    BRIGHT_WHITE(97);

    fun format(obj: Any?) = "$ESC[${code}m$obj$ESC[0m"
}

fun String.hexStringToBitSet(): BitSet {
    val bitSet = BitSet(length)

    map { char -> char.digitToInt(16) }
        .forEachIndexed { index, int ->
            if (int and 0b1000 == 0b1000) bitSet.set(index * 4 + 0)
            if (int and 0b0100 == 0b0100) bitSet.set(index * 4 + 1)
            if (int and 0b0010 == 0b0010) bitSet.set(index * 4 + 2)
            if (int and 0b0001 == 0b0001) bitSet.set(index * 4 + 3)
        }

    return bitSet
}

fun BitSet.toBinaryString(n: Int = length()) =
    (0 until n)
        .map { if (this[it]) '1' else '0' }
        .joinToString(separator = "")

fun BitSet.toInt(n: Int = length()): Int = toInt(0, n)

fun BitSet.toInt(fromIndex: Int, toIndex: Int): Int =
    (fromIndex until toIndex).fold(0) { int, index -> int * 2 + if (this[index]) 1 else 0 }

fun BitSet.toLong(n: Int = length()): Long = toLong(0, n)

fun BitSet.toLong(fromIndex: Int, toIndex: Int): Long =
    (fromIndex until toIndex).fold(0L) { long, index -> long * 2 + if (this[index]) 1 else 0 }
