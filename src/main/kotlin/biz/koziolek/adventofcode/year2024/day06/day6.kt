package biz.koziolek.adventofcode.year2024.day06

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})
    val map = parseGuardMap(inputFile.bufferedReader().readLines())
    val finalMap = walkUntilGuardLeaves(map).last()

    val visitedPosCount = countVisitedPositions(finalMap)
    println("Visited positions: $visitedPosCount")

    val newObstacles = putAllObstacles(map)
    println("Obstacles to place: ${newObstacles.size}")
}

const val EMPTY = '.'
const val OBSTACLE = '#'
const val NEW_OBSTACLE = 'O'
const val GUARD_NORTH = '^'
const val GUARD_EAST = '>'
const val GUARD_SOUTH = 'v'
const val GUARD_WEST = '<'
const val VISITED_NORTH = 'N'
const val VISITED_EAST = 'E'
const val VISITED_SOUTH = 'S'
const val VISITED_WEST = 'W'

data class MapWithGuard(
    val map: Map<Coord, String>,
    val guardPos: Coord,
    val guardDir: Char,
    val width: Int = map.getWidth(),
    val height: Int = map.getHeight(),
    val guardStartPos: Coord = guardPos,
) {

    fun isInside(coord: Coord) =
        coord.x in 0 until width && coord.y in 0 until height

    fun isFree(coord: Coord): Boolean =
        (map[coord]?.last() ?: EMPTY) == EMPTY

    override fun toString() =
        toStringWithHighlights(getStandardHighlights())

    fun toStringWithObstacles(obstacles: Set<Coord>): String {
        val tmpRawMap = map + obstacles.associateWith { NEW_OBSTACLE.toString() }
        val tmpMap = copy(map = tmpRawMap)
        return tmpMap.toStringWithHighlights(
            listOf(obstacles to AsciiColor.RED)
                    + tmpMap.getStandardHighlights()
        )
    }

    fun toStringWithHighlights(highlights: List<Pair<Set<Coord>, AsciiColor>>): String {
        return ensure00Included(map).to2DStringOfStrings { coord, str ->
            val char = str?.last() ?: EMPTY
            val color = (highlights.firstOrNull { it.first.contains(coord) })?.second ?: AsciiColor.BRIGHT_BLACK
            color.format("$char ")
        } + "\n"
    }

    fun getStandardHighlights(): List<Pair<Set<Coord>, AsciiColor>> =
        listOf(
            setOf(guardStartPos) to AsciiColor.BRIGHT_MAGENTA,
            findCoordsFor(OBSTACLE) to AsciiColor.YELLOW,
            findCoordsFor(VISITED_NORTH) to AsciiColor.GREEN,
            findCoordsFor(VISITED_EAST) to AsciiColor.BLUE,
            findCoordsFor(VISITED_SOUTH) to AsciiColor.MAGENTA,
            findCoordsFor(VISITED_WEST) to AsciiColor.CYAN,
            ensure00Included(map).walkSouth()
                .filter { it.x < 0 || it.x >= width || it.y < 0 || it.y >= height }
                .toSet()
                    to AsciiColor.BLACK,
        )

    private fun findCoordsFor(char: Char) =
        map.filterValues { it.last() == char }.keys
}

private fun ensure00Included(map: Map<Coord, String>): Map<Coord, String> =
    if (Coord(0, 0) in map) {
        map
    } else {
        map + (Coord(0, 0) to EMPTY.toString())
    }

fun parseGuardMap(lines: Iterable<String>): MapWithGuard =
    lines.parse2DMap { it.toString() }
        .filter { it.second != EMPTY.toString() }
        .toMap()
        .let { map ->
            val (guardPos, guardDir) = findGuard(map)
            MapWithGuard(ensure00Included(map), guardPos, guardDir)
        }

fun walkUntilGuardLeaves(map: MapWithGuard): Sequence<MapWithGuard> = sequence {
    var currentMap = map
    while (map.isInside(currentMap.guardPos)) {
        val (newMap, newGuardPos, newGuardDir) = moveOnce(
            map = currentMap.map.toMutableMap(),
            guardPos = currentMap.guardPos,
            guardDir = currentMap.guardDir,
        )
        currentMap = currentMap.copy(
            map = newMap.toMap(),
            guardPos = newGuardPos,
            guardDir = newGuardDir,
        )
        yield(currentMap)
    }
}

private fun moveOnce(map: MutableMap<Coord, String>,
                     guardPos: Coord,
                     guardDir: Char): Triple<MutableMap<Coord, String>, Coord, Char> {
    val direction = getDirection(guardDir)
    var newDir = guardDir
    var newPos = guardPos.move(direction)

    if (map[newPos]?.last() == OBSTACLE) {
        newDir = rotateRight(guardDir)
        newPos = guardPos
    }
    val prevPosMarker = getVisitedMarker(newDir)

    return Triple(
        updateMap(map, guardPos, prevPosMarker, newPos, newDir),
        newPos,
        newDir
    )
}

private fun updateMap(map: MutableMap<Coord, String>,
                      src: Coord,
                      srcMarker: Char,
                      dst: Coord,
                      dstMarker: Char): MutableMap<Coord, String> {
    map[src] = (map[src] ?: "") + srcMarker
    map[dst] = (map[dst] ?: "") + dstMarker
    return map
}

private fun findGuard(map: Map<Coord, String>): Pair<Coord, Char> =
    map.entries
        .first { isGuard(it.value.last()) }
        .let { it.key to it.value.last() }

fun countVisitedPositions(map: MapWithGuard): Int =
    findAllVisited(map.map).size

fun putAllObstacles(map: MapWithGuard): Set<Coord> =
    walkUntilGuardLeaves(map)
        .filter { it.isInside(it.guardPos) }
        .mapNotNull { maybePutObstacle(it) }
        .toSet()

private fun maybePutObstacle(map: MapWithGuard): Coord? {
//    println("Maybe put obstacle for guard at ${map.guardPos}, dir: ${map.guardDir}")

    val direction = getDirection(map.guardDir)
    val newObstaclePos = map.guardPos.move(direction)

    if (!map.isFree(newObstaclePos)) {
        return null
    }

    if (newObstaclePos == map.guardStartPos) {
        return null
    }

    val newDir = rotateRight(map.guardDir)
    var debug = false
    var debugPrintCount = 0

    if (map.guardPos.x == Int.MAX_VALUE && map.guardPos.y == Int.MAX_VALUE) {
        debug = true
        println("DEBUG: Guard at ${map.guardPos}, dir: ${map.guardDir}")
        println(map)
        debugPrintCount += 1
    }

    var currentGuardPos = map.guardPos
    var currentGuardDir = newDir
    val currentMap = map.map.toMutableMap()
    currentMap[newObstaclePos] = OBSTACLE.toString()
    currentMap[currentGuardPos] = newDir.toString()
    var foundOldPath = map.map[currentGuardPos]?.any { it == getVisitedMarker(newDir) } ?: false

    fun debugPrintMap() {
        val existingPath = findAllVisited(map.map).keys
        val newPath = currentMap.filter { map.map[it.key] != it.value }.keys

        println(
            map.copy(
                map = currentMap + (newObstaclePos to NEW_OBSTACLE.toString()),
                guardPos = currentGuardPos,
                guardDir = currentGuardDir,
            )
                .toStringWithHighlights(
                    listOf(
                        setOf(map.guardStartPos) to AsciiColor.BRIGHT_MAGENTA,
                        setOf(newObstaclePos) to AsciiColor.RED,
                        newPath to AsciiColor.BRIGHT_WHITE,
                        existingPath to AsciiColor.WHITE,
                    ) + map.getStandardHighlights()
                )
        )
    }

    while (!foundOldPath && map.isInside(currentGuardPos)) {
        val moveResult = moveOnce(currentMap, currentGuardPos, currentGuardDir)

        if (!map.isInside(moveResult.second)) {
            break
        }

        if (map.map[moveResult.second]?.any { it == getVisitedMarker(moveResult.third) } == true ||
            (currentMap[moveResult.second]?.dropLast(2)?.any { it == getVisitedMarker(moveResult.third) } == true)) {
            if (debug) {
                println("Found loop! Place obstacle at $newObstaclePos")
                println("Map at ${moveResult.second}: ${currentMap[moveResult.second]}")
                debugPrintMap()
            }

            foundOldPath = true
        }

        currentGuardPos = moveResult.second
        currentGuardDir = moveResult.third
    }

    return if (foundOldPath) {
        newObstaclePos
    } else {
        null
    }
}

private fun findAllVisited(map: Map<Coord, String>): Map<Coord, String> =
    map.filterValues {
        it.any { pastCellValue ->
            isVisited(pastCellValue)
        }
    }

private fun isGuard(c: Char) =
    c == GUARD_NORTH
            || c == GUARD_EAST
            || c == GUARD_SOUTH
            || c == GUARD_WEST

private fun isVisited(c: Char) =
    c == VISITED_NORTH
            || c == VISITED_EAST
            || c == VISITED_SOUTH
            || c == VISITED_WEST

private fun getDirection(guardDir: Char) = when (guardDir) {
    GUARD_NORTH -> Direction.NORTH
    GUARD_EAST -> Direction.EAST
    GUARD_SOUTH -> Direction.SOUTH
    GUARD_WEST -> Direction.WEST
    else -> throw IllegalArgumentException("Unknown guard direction: $guardDir")
}

private fun rotateRight(guardDir: Char) =
    when (guardDir) {
        GUARD_NORTH -> GUARD_EAST
        GUARD_EAST -> GUARD_SOUTH
        GUARD_SOUTH -> GUARD_WEST
        GUARD_WEST -> GUARD_NORTH
        else -> throw IllegalArgumentException("Unknown guard direction: $guardDir")
    }

private fun getVisitedMarker(newDir: Char) =
    when (newDir) {
        GUARD_NORTH -> VISITED_NORTH
        GUARD_EAST -> VISITED_EAST
        GUARD_SOUTH -> VISITED_SOUTH
        GUARD_WEST -> VISITED_WEST
        else -> throw IllegalArgumentException("Unknown guard direction: $newDir")
    }
