package biz.koziolek.adventofcode.year2024.day06

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})
    val map = parseGuardMap(inputFile.bufferedReader().readLines())
    val finalMap = walkUntilGuardLeaves(map)
    val visitedPosCount = countVisitedPositions(finalMap)
    println("Visited positions: $visitedPosCount")
}

const val EMPTY = '.'
const val OBSTACLE = '#'
const val GUARD_NORTH = '^'
const val GUARD_EAST = '>'
const val GUARD_SOUTH = 'v'
const val GUARD_WEST = '<'
const val VISITED = 'X'

fun parseGuardMap(lines: Iterable<String>): Map<Coord, Char> =
    lines.parse2DMap().toMap()

fun walkUntilGuardLeaves(map: Map<Coord, Char>): Map<Coord, Char> {
    var (guardPos, _) = findGuard(map)
    var currentMap = map
    while (guardPos.x in 0 until map.getWidth() && guardPos.y in 0 until map.getHeight()) {
        currentMap = moveOnce(currentMap)
        guardPos = findGuard(currentMap).first
    }
    return currentMap
}

private fun moveOnce(map: Map<Coord, Char>): Map<Coord, Char> {
    val (guardPos, guardDir) = findGuard(map)
    val direction = when (guardDir) {
        GUARD_NORTH -> Direction.NORTH
        GUARD_EAST -> Direction.EAST
        GUARD_SOUTH -> Direction.SOUTH
        GUARD_WEST -> Direction.WEST
        else -> throw IllegalArgumentException("Unknown guard direction: $guardDir")
    }
    var newDir = guardDir
    var newPos = guardPos.move(direction)

    if (map[newPos] == OBSTACLE) {
        newDir = when (guardDir) {
            GUARD_NORTH -> GUARD_EAST
            GUARD_EAST -> GUARD_SOUTH
            GUARD_SOUTH -> GUARD_WEST
            GUARD_WEST -> GUARD_NORTH
            else -> throw IllegalArgumentException("Unknown guard direction: $guardDir")
        }
        newPos = guardPos
    }

    return map + (guardPos to VISITED) + (newPos to newDir)
}

private fun findGuard(map: Map<Coord, Char>): Pair<Coord, Char> =
    map.entries.first {
        it.value == GUARD_NORTH
                || it.value == GUARD_EAST
                || it.value == GUARD_SOUTH
                || it.value == GUARD_WEST
    }.toPair()

fun countVisitedPositions(map: Map<Coord, Char>): Int =
    map.values.count { it == VISITED }
