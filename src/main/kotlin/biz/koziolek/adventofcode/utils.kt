package biz.koziolek.adventofcode

import java.awt.Color
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

    companion object {
        fun rainbow(value: Float, obj: Any?): String {
            val color = Color.getHSBColor(value, 1f, 1f)
            return rgb(color.red, color.green, color.blue, obj)
        }

        fun rgb(r: Int, g: Int, b: Int, obj: Any?) = "$ESC[38;2;${r};${g};${b}m$obj$ESC[0m"

        fun cleanUp(str: String) =
            str.replace(Regex("$ESC\\[.*?m"), "")
    }
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

fun gcd(a: Long, b: Long): Long {
    var aa = a
    var bb = b
    while (bb > 0) {
        val temp = bb
        bb = aa % bb
        aa = temp
    }
    return aa
}

/**
 * Greatest common divisor
 */
fun gcd(numbers: Iterable<Long>): Long =
    numbers.reduce { acc, l -> gcd(acc, l) }

/**
 * Least common multiple
 */
fun lcm(a: Long, b: Long): Long =
    a * (b / gcd(a, b))

/**
 * Least common multiple
 */
fun lcm(numbers: Iterable<Long>): Long =
    numbers.reduce { acc, l -> lcm(acc, l) }

/**
 * Transposes 2D strings.
 *
 *     abc    adgj
 *     def -> behk
 *     ghi    cfil
 *     jkl
 */
fun List<String>.transpose(): List<String> =
    fold(List(first().length) { "" }) { acc, line ->
        line.mapIndexed { index, c -> acc[index] + c }
    }

/**
 * Swaps this character to the other one from set of 2 characters.
 */
fun Char.swap(c1: Char, c2: Char): Char =
    when (this) {
        c1 -> c2
        c2 -> c1
        else -> throw IllegalArgumentException("No character to swap: $this")
    }

/**
 * Returns single element, `null` if the iterable is empty
 * or throws an exception if the iterable has more than one element.
 *
 * Mix of [Iterable.single] and [Iterable.singleOrNull]
 */
fun <T> Iterable<T>.singleOrNullOnlyWhenZero(): T? {
    when (this) {
        is List -> {
            return when (size) {
                0 -> null
                1 -> this[0]
                else -> throw IllegalArgumentException("Found more than one element")
            }
        }
        else -> {
            val iterator = iterator()
            if (!iterator.hasNext())
                return null
            val single = iterator.next()
            if (iterator.hasNext())
                throw IllegalArgumentException("Found more than one element")
            return single
        }
    }
}

fun <T, R : Comparable<R>> Iterable<T>.minAndMaxOrNull(selector: (T) -> R): Pair<R, R>? {
    val iterator = iterator()
    if (!iterator.hasNext()) return null
    var minValue = selector(iterator.next())
    var maxValue = minValue
    while (iterator.hasNext()) {
        val v = selector(iterator.next())
        if (v < minValue) {
            minValue = v
        }
        if (v > maxValue) {
            maxValue = v
        }
    }
    return minValue to maxValue
}

fun <T> productWithItself(list: List<T>, diagonal: Boolean): Sequence<Pair<T, T>> =
    sequence {
        for ((index1, mas1) in list.withIndex()) {
            for ((index2, mas2) in list.withIndex()) {
                if (!diagonal || index2 > index1) {
                    yield(mas1 to mas2)
                }
            }
        }
    }
