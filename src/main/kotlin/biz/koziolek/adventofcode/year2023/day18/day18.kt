package biz.koziolek.adventofcode.year2023.day18

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})
    val digPlan = parseDigPlan(inputFile.bufferedReader().readLines())
    println("The trench can hold ${calculateTrenchVolume(buildTrenchEdge(digPlan))} cubic meters")
}

data class DigPlanEntry(val direction: Direction, val distance: Long)

fun parseDigPlan(lines: Iterable<String>): List<DigPlanEntry> =
    lines
        .mapNotNull { line -> Regex("([UDLR]) ([0-9]+) \\(#([0-9a-fA-F]{6})\\)").find(line) }
        .map { match ->
            DigPlanEntry(
                direction = when (val dirStr = match.groups[1]!!.value) {
                    "U" -> Direction.NORTH
                    "D" -> Direction.SOUTH
                    "L" -> Direction.WEST
                    "R" -> Direction.EAST
                    else -> throw IllegalArgumentException("Unexpected direction: $dirStr")
                },
                distance = match.groups[2]!!.value.toLong(),
            )
        }

fun parseDigPlanV2(lines: Iterable<String>): List<DigPlanEntry> =
    lines
        .mapNotNull { line -> Regex("([UDLR]) ([0-9]+) \\(#([0-9a-fA-F]{6})\\)").find(line) }
        .map { match ->
            DigPlanEntry(
                direction = when (val lastHexDigit = match.groups[3]!!.value[5]) {
                    '0' -> Direction.EAST
                    '1' -> Direction.SOUTH
                    '2' -> Direction.WEST
                    '3' -> Direction.NORTH
                    else -> throw IllegalArgumentException("Unexpected hex digit: $lastHexDigit")
                },
                distance = match.groups[3]!!.value.dropLast(1).toLong(radix = 16),
            )
        }

const val TRENCH = '#'
const val LEVEL_TERRAIN = '.'

const val NORTH_EAST_CORNER = '7'
const val NORTH_WEST_CORNER = 'F'
const val SOUTH_WEST_CORNER = 'L'
const val SOUTH_EAST_CORNER = 'J'
const val VERTICAL_EDGE = '|'

fun buildTrenchEdge(digPlan: List<DigPlanEntry>): Map<LongCoord, Char> {
    var currentPos = LongCoord(0, 0)
    var previousDirection: Direction? = null
    val map = mutableMapOf(currentPos to TRENCH)

    for ((direction, distance) in digPlan) {
        map[currentPos] = getCornerCharacter(previousDirection, direction)

        currentPos = currentPos.move(direction, distance)
        previousDirection = direction
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

fun calculateTrenchVolume(trenchEdge: Map<LongCoord, Char>, debug: Boolean = false): Long {
    var size = 0L
    val coords = trenchEdge.keys.sortByYX()
    val groupedCoords = coords.groupBy { it.y }
    var previousY = coords.first().y
    val verticals = mutableSetOf<Long>()

    for ((y, cornersInLine) in groupedCoords.entries.sortedBy { it.key }) {
        if (debug) print("y = $y size = $size\n")

        val yDiff = y - previousY

        if (yDiff > 1) {
            val skippedLines = yDiff - 1
            val volumeInLine = sumEvenPairDifferences(verticals)
            val count = skippedLines * volumeInLine
            if (debug) println("  skipped $skippedLines of volume $volumeInLine = $count")
            size += count
        }

        val line: List<Pair<Long, Char>> = cornersInLine
            .map { it.x to (trenchEdge[it] ?: throw IllegalStateException("No corner found for $it")) }
            .plus(
                verticals
                    .filter { x -> cornersInLine.none { it.x == x } }
                    .map { x -> x to VERTICAL_EDGE }
            )
            .sortedBy { it.first }

        var isInside = false
        var previousCorner: Char? = null
        var previousX = line.first().first - 1

        if (debug) println(
            (coords.minOf { it.x }..coords.maxOf { it.x })
                .map { x ->
                    line.firstOrNull { it.first == x }?.second ?: LEVEL_TERRAIN
                }
                .joinToString("") { it.toString() }
        )

        for ((x, char) in line) {
            val xDiff = x - previousX
            val count = when {
                isVerticalEdge(char) -> {
                    val wasInside = isInside
                    isInside = !isInside

                    if (wasInside)
                        xDiff
                    else
                        1
                }
                isHorizontalEdge(char) -> throw IllegalStateException("Should never encounter horizontal edge but did at ($x,$y)")
                isEastCornerSameAsWest(char, previousCorner) -> {
                    previousCorner = char
                    xDiff
                }
                isEastCornerDifferentThanWest(char, previousCorner) -> {
                    isInside = !isInside
                    previousCorner = char
                    xDiff
                }
                isCorner(char) -> {
                    previousCorner = char

                    if (isInside)
                        xDiff
                    else
                        1
                }
                isLevelTerrain(char) -> throw IllegalStateException("Should never encounter level terrain but did at ($x,$y)")
                else -> throw IllegalArgumentException("Unexpected character: $char")
            }

            size += count
            if (debug) print("  $char @ x = $previousX -> $x = $count inside = $isInside\n")

            when (char) {
                NORTH_WEST_CORNER, NORTH_EAST_CORNER -> verticals.add(x)
                SOUTH_EAST_CORNER, SOUTH_WEST_CORNER -> verticals.remove(x)
            }

            previousX = x
        }

        if (debug) println()

        previousY = y
    }

    return size
}

internal fun sumEvenPairDifferences(numbers: Set<Long>): Long =
    numbers.asSequence()
        .sorted()
        .zipWithNext()
        .withIndex()
        .filter { it.index % 2 == 0 }
        .sumOf { it.value.second - it.value.first + 1 }

private fun isLevelTerrain(char: Char) =
    char == LEVEL_TERRAIN

private fun isVerticalEdge(char: Char) =
    char == Direction.NORTH.char
            || char == Direction.SOUTH.char
            || char == VERTICAL_EDGE

private fun isHorizontalEdge(char: Char) =
    char == Direction.WEST.char
            || char == Direction.EAST.char

private fun isCorner(char: Char) =
    char == NORTH_WEST_CORNER
            || char == NORTH_EAST_CORNER
            || char == SOUTH_EAST_CORNER
            || char == SOUTH_WEST_CORNER

private fun isEastCornerSameAsWest(char: Char, previousCorner: Char?) =
        (char == NORTH_EAST_CORNER && previousCorner == NORTH_WEST_CORNER)
                || (char == SOUTH_EAST_CORNER && previousCorner == SOUTH_WEST_CORNER)

private fun isEastCornerDifferentThanWest(char: Char, previousCorner: Char?) =
        (char == NORTH_EAST_CORNER && previousCorner == SOUTH_WEST_CORNER)
                || (char == SOUTH_EAST_CORNER && previousCorner == NORTH_WEST_CORNER)
