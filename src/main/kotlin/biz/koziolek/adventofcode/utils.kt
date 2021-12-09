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
