package biz.koziolek.adventofcode.year2023.day17

import biz.koziolek.adventofcode.*
import java.util.*
import kotlin.Comparator

fun main() {
    val inputFile = findInput(object {})
    val map = parseHeatLossMap(inputFile.bufferedReader().readLines())
}

fun parseHeatLossMap(lines: Iterable<String>): Map<Coord, Int> =
    lines.parse2DMap { it.digitToInt() }.toMap()

data class PathElement(val coord: Coord,
                       val currentDirection: Direction?,
                       val nextDirection: Direction?,
                       val totalHeatLoss: Int,
                       val next: List<Pair<Coord, Direction>> = emptyList()) : Comparable<PathElement> {
    fun toPair() = Pair(coord, currentDirection!!)

    override fun compareTo(other: PathElement): Int =
        Comparator.comparing<PathElement, Int> { it.totalHeatLoss }
            .compare(this, other)

    fun isNotTooLongStraight(limit: Int = 3): Boolean =
        next.takeWhile { it.second == currentDirection }.size < limit

    fun createPreviousPathElement(coordAndDir: Pair<Coord, Direction>, map: Map<Coord, Int>): PathElement? =
        map[coordAndDir.first]?.let { currentHeatLoss ->
            PathElement(
                coord = coordAndDir.first,
                currentDirection = coordAndDir.second,
                nextDirection = currentDirection,
                totalHeatLoss = totalHeatLoss + currentHeatLoss,
                next = listOf(toPair()) + next
            )
        }
}

fun findMinimalHeatLossPath(map: Map<Coord, Int>,
                            start: Coord = Coord(0, 0 ),
                            end: Coord = Coord(map.getWidth() - 1, map.getHeight() - 1)): List<Pair<Coord, Direction>> {
//    val horizontalRange = map.getHorizontalRange()
//    val verticalRange = map.getVerticalRange()

    val toVisit: Queue<PathElement> = PriorityQueue()
    toVisit.addAll(Direction.entries.map {
        PathElement(coord = end, currentDirection = it, nextDirection = null, totalHeatLoss = map[end]!!)
    })

    val visited: MutableMap<Coord, PathElement> = mutableMapOf()
//    val totalHeatLossMap: MutableMap<Coord, Int> = mutableMapOf()
    var current: PathElement? = toVisit.poll()

    while (current != null) {
        if (/*current.coord !in visited &&*/ current.isNotTooLongStraight()) {
//            totalHeatLossMap[current.coord] = current.totalHeatLoss

            val existingElement = visited[current.coord]
            if (existingElement == null || current.totalHeatLoss <= existingElement.totalHeatLoss) {
                val prevCoords = getPreviousCoords(current.coord, current.currentDirection)
                val nextToVisit = prevCoords
//                .filter { it.first.x in horizontalRange && it.first.y in verticalRange }
                    .mapNotNull { current?.createPreviousPathElement(it, map) }

                visited[current.coord] = current
                toVisit.addAll(nextToVisit)
            }
        }

        current = toVisit.poll()
    }

    val path = sequence {
        var currentEl = visited[start]

        while (currentEl != null && currentEl.coord != end) {
            yield(currentEl.toPair())

            val newCoord = currentEl.coord.move(currentEl.currentDirection!!)
            currentEl = visited[newCoord]

//            currentEl = map.getAdjacentCoords(currentEl, includeDiagonal = false)
//                .minBy { totalHeatLossMap[it] ?: Int.MAX_VALUE }
        }

        if (currentEl != null) {
            yield(currentEl.toPair())
        }
    }.toList()

    return path
}

private fun getPreviousCoords(coord: Coord, direction: Direction?): List<Pair<Coord, Direction>> =
    when (direction) {
        Direction.NORTH -> listOf(
            Direction.WEST,
            Direction.NORTH,
            Direction.EAST,
        )
        Direction.EAST -> listOf(
            Direction.NORTH,
            Direction.EAST,
            Direction.SOUTH,
        )
        Direction.SOUTH -> listOf(
            Direction.EAST,
            Direction.SOUTH,
            Direction.WEST,
        )
        Direction.WEST -> listOf(
            Direction.SOUTH,
            Direction.WEST,
            Direction.NORTH,
        )
        else -> emptyList()
    }.map { coord.moveBackwards(it) to it }

fun showPath(map: Map<Coord, Int>, path: List<Pair<Coord, Direction>>): String {
    return map
        .to2DStringOfStrings { coord, heatLoss ->
            path.find { it.first == coord }
                ?.let { AsciiColor.BRIGHT_WHITE.format(it.second.char) }
                ?: heatLoss.toString()
        }
}
