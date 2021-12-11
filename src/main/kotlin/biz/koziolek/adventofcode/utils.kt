package biz.koziolek.adventofcode

import java.util.*
import kotlin.collections.HashSet

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

enum class AsciiColor(val code: Int) {
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
