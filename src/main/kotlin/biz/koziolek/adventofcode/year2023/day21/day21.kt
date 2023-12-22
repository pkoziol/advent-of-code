package biz.koziolek.adventofcode.year2023.day21

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})
    val map = parseGardenMap(inputFile.bufferedReader().readLines())
    val steps = 64
    println("${walk(map, steps).size} plots can be reached in $steps steps")
}

const val START = 'S'
const val GARDEN_PLOT = '.'
const val ROCK = '#'
const val REACHED_POS = 'O'

fun parseGardenMap(lines: Iterable<String>): Map<Coord, Char> =
    lines.parse2DMap().toMap()

fun gardenMapToString(map: Map<Coord, Char>, markCoords: Collection<Coord> = emptySet()): String =
    map.to2DString { coord, char ->
        if (coord in markCoords) {
            REACHED_POS
        } else {
            char ?: GARDEN_PLOT
        }
    }

fun moveStartTo00(map: Map<Coord, Char>): Map<Coord, Char> =
    when (val start = findStart(map)) {
        Coord(0, 0) -> map
        else -> map.mapKeys { (coord, _) -> coord - start }
    }

fun findStart(map: Map<Coord, Char>) =
    map.entries.single { it.value == START }.key

fun walk(map: Map<Coord, Char>, steps: Int): Set<Coord> {
    val flooded = flood(map, steps)
    val remainder = steps % 2

    return flooded.entries
        .filter { it.value % 2 == remainder }
        .map { it.key }
        .toSet()
}

fun flood(map: Map<Coord, Char>, distance: Int): Map<Coord, Int> {
    val flooded = mutableMapOf<Coord, Int>()
    val start = findStart(map)
    val toCheck = mutableListOf(start to 0)
    val queued = mutableSetOf(start)
    var lastSeenDistance = -1

    while (toCheck.isNotEmpty()) {
        val (coord, walkedDistance) = toCheck.removeFirst()

        if (walkedDistance > distance) {
            break
        }

        if (walkedDistance != lastSeenDistance && walkedDistance % 100 == 0) {
            println("Distance $walkedDistance - flooded ${flooded.size}, queued ${queued.size}, to check ${toCheck.size}")
            lastSeenDistance = walkedDistance
        }

        flooded[coord] = walkedDistance

        coord.getAdjacentCoords()
            .filter { adjCoord -> adjCoord !in queued && map[adjCoord] != ROCK }
            .forEach {
                toCheck.add(it to (walkedDistance + 1))
                queued.add(it)
            }
    }

    return flooded
}

fun infiniteWalk(map: Map<Coord, Char>, steps: Int): Long {
    val startingChunk = Coord(0, 0)
    val floodedChunks = mutableMapOf<Coord, FloodResult>()
    val chunksToCheck = LinkedHashMap<Coord, FloodInput>()

    chunksToCheck[startingChunk] = FloodInput(
        chunk = startingChunk,
        map = map,
        start = findStart(map),
        absoluteStartingDistance = 0,
    )

    while (chunksToCheck.isNotEmpty()) {
        val floodInput = chunksToCheck.pollFirstEntry().value
//        println("Checking chunk ${floodInput.chunk}")

        if (floodedChunks.size % 1000 == 1) {
            println("Flooded ${floodedChunks.size} chunks - max distance = ${floodedChunks.values.maxOf { it.absoluteMaxDistance }}")
        }

        val floodResult: FloodResult =
            floodedChunks.values
                .firstOrNull { existingResult -> existingResult.input.start == floodInput.start }
                ?.let { existingResult ->
                    println("Same as ${existingResult.input.chunk} @ (${existingResult.input.start}) - reusing")
                    existingResult.reuse(floodInput)
                }
                ?: infiniteFlood(floodInput)

        floodedChunks[floodResult.input.chunk] = floodResult

        floodResult.exits
            .filter { it.toChunk !in floodedChunks && it.toChunk !in chunksToCheck }
            .map {
                FloodInput(
                    chunk = it.toChunk,
                    map = map,
                    start = wrapAround(it.mapCoord, map),
                    absoluteStartingDistance = floodResult.input.absoluteStartingDistance + it.distance,
                )
            }
            .filter {
//                println("abs distance to ${it.chunk} (starting at ${it.start}): ${it.absoluteStartingDistance}")
                it.absoluteStartingDistance <= steps
            }
            .forEach { chunksToCheck[it.chunk] = it }
    }

    floodedChunks.to2DStringOfStrings { coord, floodResult ->
        floodResult?.let { "%9s".format(it.input.start) } ?: "         "
    }.let { println(it) }

//    var sum = 0L
//    val chunksToSum = mutableListOf(startingChunk)
//    val alreadySummed = mutableSetOf<Coord>()
//    val remainder = steps % 2L
//
//    while (chunksToSum.isNotEmpty()) {
//        val chunk = chunksToSum.removeFirst()
//        val floodResult = floodedChunks[chunk]!!
//        val partialSum = countStandingAt(floodResult, steps)
//
////        println("Summing chunk $chunk += $partialSum")
//        sum += partialSum
//        alreadySummed.add(chunk)
//
//        if (floodResult.absoluteMaxDistance < steps) {
//            floodResult.exits
//                .map { it.toChunk }
//                .filter { it !in chunksToSum && it !in alreadySummed }
//                .forEach { chunksToSum.add(it) }
//        }
//    }
//
//    return sum

    val center = floodedChunks[startingChunk]!!

    val axes = listOf(
        Coord(+0, -1),
        Coord(+1, +0),
        Coord(+0, +1),
        Coord(-1, +0),
    ).map { floodedChunks[it]!! }

    val quadrants = listOf(
        Coord(+1, -1),
        Coord(+1, +1),
        Coord(-1, +1),
        Coord(-1, -1),
    ).map { floodedChunks[it]!! }

    val repetitions = 202300L
    return (countStandingAt(center, steps).toLong()
            + repetitions * axes.sumOf { countStandingAt(it, steps).toLong() }
            + (repetitions * (repetitions - 1) / 2L) * quadrants.sumOf { countStandingAt(it, steps).toLong() })
}

private fun countStandingAt(floodResult: FloodResult, steps: Int): Int {
    val remainder = steps % 2L

    return if (floodResult.absoluteMaxDistance <= steps) {
        if (remainder == 0L) {
            floodResult.absoluteEvenDistances
        } else {
            floodResult.absoluteOddDistances
        }
    } else {
        floodResult.relativeDistances
            .map { it.value + floodResult.input.absoluteStartingDistance }
            .count { it % 2 == remainder }
    }
}

data class FloodInput(
    val chunk: Coord, // Coordinate of a chunk - map copy moved from original position
    val map: Map<Coord, Char>,
    val start: Coord,
    val absoluteStartingDistance: Long,
)

data class FloodResult(
    val input: FloodInput,
    val relativeDistances: Map<Coord, Int>,
    val exits: Set<Exit>, // map coord -> chunk coord
    val maxDistance: Int,
    val evenDistances: Int,
    val oddDistances: Int,
) {
    val absoluteMaxDistance: Long
        get() = input.absoluteStartingDistance + maxDistance

    val absoluteEvenDistances: Int
        get() = if (input.absoluteStartingDistance % 2 == 0L) evenDistances else oddDistances

    val absoluteOddDistances: Int
        get() = if (input.absoluteStartingDistance % 2 == 0L) oddDistances else evenDistances

    fun reuse(newInput: FloodInput): FloodResult =
        copy(
            input = newInput,
            exits = exits.map { it.reuse(newInput.chunk) }.toSet()
        )
}

data class Exit(val mapCoord: Coord, val fromChunk: Coord, val toChunk: Coord, val distance: Int) {
    fun reuse(newChunk: Coord): Exit {
        val delta = newChunk - fromChunk
        return copy(
            fromChunk = newChunk,
            toChunk = toChunk + delta,
        )
    }
}

fun infiniteFlood(input: FloodInput): FloodResult {
    val flooded = mutableMapOf<Coord, Int>()
    val exits = mutableListOf<Exit>()
    val toCheck = mutableListOf(input.start to 0)
    val queued = mutableSetOf(input.start)
    var lastSeenDistance = -1
    val xRange = input.map.getHorizontalRange()
    val yRange = input.map.getVerticalRange()

    while (toCheck.isNotEmpty()) {
        val (coord, walkedDistance) = toCheck.removeFirst()

        if (walkedDistance != lastSeenDistance && walkedDistance % 100 == 0) {
//            println("Distance $walkedDistance - flooded ${flooded.size}, queued ${queued.size}, to check ${toCheck.size}")
            lastSeenDistance = walkedDistance
        }

        flooded[coord] = walkedDistance

        val adjCoords = coord.getAdjacentCoords()
        val newDistance = walkedDistance + 1

        for (adjCoord in adjCoords) {
            var newAdjCoord: Coord? = adjCoord

            val exitDirection = when {
                adjCoord.x < xRange.first -> Direction.WEST
                adjCoord.x > xRange.last -> Direction.EAST
                adjCoord.y < yRange.first -> Direction.NORTH
                adjCoord.y > xRange.last -> Direction.SOUTH
                else -> null
            }

            if (exitDirection != null) {
//                println("Coord $adjCoord (dist = $newDistance) is outside of the map!")
                exits.add(Exit(
                    mapCoord = adjCoord,
                    fromChunk = input.chunk,
                    toChunk = input.chunk.move(exitDirection),
                    distance = newDistance,
                ))
                newAdjCoord = null
            } else {
                if (adjCoord !in queued && input.map[adjCoord] != ROCK) {
                    toCheck.add(adjCoord to newDistance)
                    queued.add(adjCoord)
                }
            }

            if (newAdjCoord != null && newAdjCoord !in queued && input.map[newAdjCoord] != ROCK) {
                toCheck.add(newAdjCoord to newDistance)
                queued.add(newAdjCoord)
            }
        }
    }

    return FloodResult(
        input = input,
        relativeDistances = flooded,
        exits = exits
            .groupBy { it.toChunk }
            .map { exit -> exit.value.minByOrNull { it.distance }!! }
            .toSet(),
        maxDistance = flooded.values.max(),
        evenDistances = flooded.count { it.value % 2 == 0 },
        oddDistances = flooded.count { it.value % 2 == 1 },
    )
}

fun <T> wrapAround(coord: Coord, map: Map<Coord, T>): Coord {
    val xRange = map.getHorizontalRange()
    val yRange = map.getVerticalRange()

    return Coord(
        x = coerceWithLoops(coord.x, xRange),
        y = coerceWithLoops(coord.y, yRange),
    )
}

class InfiniteMap(internal val originalMap: Map<Coord, Char>) : Map<Coord, Char> by originalMap {

    internal val xRange = originalMap.getHorizontalRange()
    internal val yRange = originalMap.getVerticalRange()
    internal val xCache = mutableMapOf<Int, Int>()
    internal val yCache = mutableMapOf<Int, Int>()

    override val keys: Set<Coord>
        get() = InfiniteKeys(this)

    override fun get(key: Coord): Char? {
        if (key.x in xRange && key.y in yRange) {
            return originalMap[key]
        }

        val originalX = xCache.computeIfAbsent(key.x) { coerce(key.x, xRange) }
        val originalY = yCache.computeIfAbsent(key.y) { coerce(key.y, yRange) }
        val originalCoord = Coord(originalX, originalY)

        return when (val originalValue = originalMap[originalCoord]) {
            START -> null
            else -> originalValue
        }
    }
}

private class InfiniteKeys(val map: InfiniteMap) : Set<Coord> by map.originalMap.keys {

    override fun contains(element: Coord): Boolean {
        if (element.x in map.xRange && element.y in map.yRange) {
            return map.originalMap.contains(element)
        }

        val originalX = map.xCache.computeIfAbsent(element.x) { coerce(element.x, map.xRange) }
        val originalY = map.yCache.computeIfAbsent(element.y) { coerce(element.y, map.yRange) }
        val originalCoord = Coord(originalX, originalY)

        return map.originalMap.contains(originalCoord)
    }
}

private val coerceCache = mutableMapOf<Pair<Int, IntRange>, Int>()

internal fun rememberingCoerce(value: Int, range: IntRange): Int {
    return coerceCache.computeIfAbsent(value to range) { coerce(value, range) }
}

internal fun coerce(value: Int, range: IntRange): Int {
    val rangeSize = range.last - range.first + 1
    val coerced = if (value > range.last) {
        val tmp = range.first + (value - range.last) % rangeSize
        if (tmp > range.first) {
            tmp - 1
        } else {
            range.last
        }
    } else if (value < range.first) {
        val tmp = range.last - (range.first - value) % rangeSize
        if (tmp < range.last) {
            tmp + 1
        } else {
            range.first
        }
    } else {
        value
    }

    verifyCoercion(value, range, coerced)

    return coerced
}


internal fun coerceWithLoops(value: Int, range: IntRange): Int {
    var newValue = value
    val rangeSize = range.last - range.first + 1
    while (newValue < range.first) {
        newValue += rangeSize
    }
    while (newValue > range.last) {
        newValue -= rangeSize
    }
    return newValue
}

private fun verifyCoercion(value: Int, range: IntRange, coerced: Int) {
    val expected = coerceWithLoops(value, range)
    if (coerced != expected) {
        println("Coercing mismatch! $value -> $range == $expected != $coerced")
    }
}
