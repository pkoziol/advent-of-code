package biz.koziolek.adventofcode.year2023.day18

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})
    val digPlan = parseDigPlan(inputFile.bufferedReader().readLines())
    println("The trench can hold ${calculateTrenchVolume(buildTrenchEdge(digPlan))} cubic meters")
}

data class DigPlanEntry(val direction: Direction, val distance: Int, val color: String)

fun parseDigPlan(lines: Iterable<String>): List<DigPlanEntry> =
    lines
        .mapNotNull { line -> Regex("([UDLR]) ([0-9]+) \\((#[0-9a-fA-F]+)\\)").find(line) }
        .map { match ->
            DigPlanEntry(
                direction = when (val dirStr = match.groups[1]!!.value) {
                    "U" -> Direction.NORTH
                    "D" -> Direction.SOUTH
                    "L" -> Direction.WEST
                    "R" -> Direction.EAST
                    else -> throw IllegalArgumentException("Unexpected durection: $dirStr")
                },
                distance = match.groups[2]!!.value.toInt(),
                color = match.groups[3]!!.value,
            )
        }

const val TRENCH = '#'
const val LEVEL_TERRAIN = '.'

const val NORTH_EAST_CORNER = '7'
const val NORTH_WEST_CORNER = 'F'
const val SOUTH_WEST_CORNER = 'L'
const val SOUTH_EAST_CORNER = 'J'

fun buildTrenchEdge(digPlan: List<DigPlanEntry>): Map<Coord, Char> {
    var currentPos = Coord(0, 0)
    var previousDirection: Direction? = null
    val map = mutableMapOf(currentPos to TRENCH)

    for ((direction, distance, color) in digPlan) {
        var first = true
        for (coord in currentPos.walk(direction, distance, includeCurrent = false)) {
            val char = if (first) {
                first = false

                getCornerCharacter(previousDirection, direction)
            } else {
                direction.char
            }

            map[currentPos] = char

            currentPos = coord
            previousDirection = direction
        }
    }

    map[currentPos] = getCornerCharacter(previousDirection, digPlan.first().direction)

    return map
}

private fun getCornerCharacter(previousDirection: Direction?, nextDirection: Direction) =
    when (previousDirection) {
        Direction.NORTH -> when (nextDirection) {
            Direction.NORTH -> throw IllegalStateException("Cannot go north -> north")
            Direction.SOUTH -> throw IllegalStateException("Cannot go north -> south")
            Direction.WEST -> NORTH_EAST_CORNER
            Direction.EAST -> NORTH_WEST_CORNER
        }
        Direction.SOUTH -> when (nextDirection) {
            Direction.NORTH -> throw IllegalStateException("Cannot go south -> north")
            Direction.SOUTH -> throw IllegalStateException("Cannot go south -> south")
            Direction.WEST -> SOUTH_EAST_CORNER
            Direction.EAST -> SOUTH_WEST_CORNER
        }
        Direction.WEST -> when (nextDirection) {
            Direction.NORTH -> SOUTH_WEST_CORNER
            Direction.SOUTH -> NORTH_WEST_CORNER
            Direction.WEST -> throw IllegalStateException("Cannot go west -> west")
            Direction.EAST -> throw IllegalStateException("Cannot go west -> east")
        }
        Direction.EAST -> when (nextDirection) {
            Direction.NORTH -> SOUTH_EAST_CORNER
            Direction.SOUTH -> NORTH_EAST_CORNER
            Direction.WEST -> throw IllegalStateException("Cannot go east -> west")
            Direction.EAST -> throw IllegalStateException("Cannot go east -> east")
        }
        null -> nextDirection.char
    }

fun calculateTrenchVolume(trenchEdge: Map<Coord, Char>): Int {
    var size = 0
    val (minX, maxX) = trenchEdge.keys.minAndMaxOrNull { it.x }!!
    val (minY, maxY) = trenchEdge.keys.minAndMaxOrNull { it.y }!!

    for (y in minY..maxY) {
        var isInside = false
        var previousCorner: Char? = null

        for (x in minX..maxX) {
            val coord = Coord(x, y)
            val char = trenchEdge[coord] ?: LEVEL_TERRAIN

            val counts = when (char) {
                Direction.NORTH.char -> {
                    isInside = !isInside
                    true
                }
                Direction.SOUTH.char -> {
                    isInside = !isInside
                    true
                }
                Direction.WEST.char -> true
                Direction.EAST.char -> true
                NORTH_WEST_CORNER -> {
                    previousCorner = char
                    true
                }
                NORTH_EAST_CORNER -> {
                    if (previousCorner == SOUTH_WEST_CORNER) {
                        isInside = !isInside
                    }
                    previousCorner = char
                    true
                }
                SOUTH_WEST_CORNER -> {
                    previousCorner = char
                    true
                }
                SOUTH_EAST_CORNER -> {
                    if (previousCorner == NORTH_WEST_CORNER) {
                        isInside = !isInside
                    }
                    previousCorner = char
                    true
                }
                LEVEL_TERRAIN -> isInside
                else -> throw IllegalArgumentException("Unexpected character: $char")
            }

            if (counts) {
                size++
            }
        }
    }

    return size
}
