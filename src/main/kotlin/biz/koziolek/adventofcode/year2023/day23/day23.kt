package biz.koziolek.adventofcode.year2023.day23

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})
    val lines = inputFile.bufferedReader().readLines()

    val hikingTrails = parseHikingTrails(lines)
    println("Longest path has ${findLongestPathLen(hikingTrails)} steps")

    val graph = parseHikingTrailsAsGraph(lines)
    println("Longest path ignoring slopes has ${findLongestPathLen(graph)} steps")
}

const val PATH = '.'
const val FOREST = '#'
const val SLOPE_N = '^'
const val SLOPE_E = '>'
const val SLOPE_S = 'v'
const val SLOPE_W = '<'

private val slopeToDirection = mapOf(
    SLOPE_N to Direction.NORTH,
    SLOPE_E to Direction.EAST,
    SLOPE_S to Direction.SOUTH,
    SLOPE_W to Direction.WEST,
)

fun parseHikingTrails(lines: Iterable<String>): Map<Coord, Char> =
    lines.parse2DMap().filter { (_, char) -> char != FOREST }.toMap()

fun parseHikingTrailsAsGraph(lines: Iterable<String>): Graph<CoordNode, BiDirectionalGraphEdge<CoordNode>> =
    lines.parse2DMap()
        .filter { (_, char) -> char != FOREST }
        .toMap()
        .mapValues { 1 }
        .toBiDirectionalGraph(includeDiagonal = false)

fun findLongestPathLen(hikingTrails: Map<Coord, Char>): Int {
    val start = hikingTrails.entries.single { it.key.y == 0 && it.value == PATH }.key
    val distance = hikingTrails.keys.associateWith { Int.MIN_VALUE }.toMutableMap()
    distance[start] = 0

    val sorted = topologicalSort(hikingTrails, start)
    val visited = mutableSetOf<Coord>()

    while (sorted.isNotEmpty()) {
        val coord = sorted.removeFirst()
        visited.add(coord)

        val newDist = distance[coord]!! + 1
        for (adjCoord in getAdjacentTiles(hikingTrails, coord).filter { it !in visited }) {
            if (distance[adjCoord]!! < newDist) {
                distance[adjCoord] = newDist
            }
        }
    }

    val end = hikingTrails.entries.single { it.key.y == hikingTrails.getHeight() - 1 }.key
    return distance[end]!!
}

fun findLongestPathLen(hikingTrails: Graph<CoordNode, BiDirectionalGraphEdge<CoordNode>>): Int {
    val simplified = hikingTrails.simplify()
    val path = simplified.findLongestPath(
        start = simplified.nodes.minBy { it.coord.y },
        end = simplified.nodes.maxBy { it.coord.y },
    )
    return path.sumOf { it.weight }
}

fun topologicalSort(
    hikingTrails: Map<Coord, Char>,
    coord: Coord,
    visited: MutableSet<Coord> = mutableSetOf(),
    sorted: MutableList<Coord> = mutableListOf(coord)
): List<Coord> {
    if (coord in visited) {
        return emptyList()
    }

    visited.add(coord)

    for (adjCoord in getAdjacentTiles(hikingTrails, coord)) {
        if (adjCoord !in visited) {
            topologicalSort(hikingTrails, adjCoord, visited, sorted)
        }
    }

    sorted.addFirst(coord)

    return sorted
}

private fun getAdjacentTiles(hikingTrails: Map<Coord, Char>, coord: Coord): List<Coord> =
    Direction.entries
        .mapNotNull { direction ->
            val newCoord = coord.move(direction)
            val char = hikingTrails[newCoord]

            if (char == PATH || slopeToDirection[char] == direction) {
                newCoord
            } else {
                null
            }
        }
