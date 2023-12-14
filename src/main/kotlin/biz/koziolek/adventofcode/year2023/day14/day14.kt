package biz.koziolek.adventofcode.year2023.day14

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})
    val platform = parsePlatform(inputFile.bufferedReader().readLines())
    println("Total load after sliding north: ${platform.slideNorth().totalLoad}")
    println("Total load after 1 000 000 000 cycles: ${platform.cycle(n = 1_000_000_000).totalLoad}")
}

const val ROUND_ROCK = 'O'
const val CUBE_ROCK = '#'
const val EMPTY_SPACE = '.'

data class Platform(val rocks: Map<Coord, Char>) {

    val totalLoad: Int =
        rocks
            .filter { it.value == ROUND_ROCK }
            .map { rocks.getHeight() - it.key.y }
            .sum()

    override fun toString() = toString(color = false)

    fun toString(color: Boolean = false) =
        buildString {
            val width = rocks.getWidth()
            val height = rocks.getHeight()

            for (y in 0 until height) {
                for (x in 0 until width) {
                    val coord = Coord(x, y)
                    val symbol = rocks[coord] ?: EMPTY_SPACE

                    if (color) {
                        when (symbol) {
                            ROUND_ROCK -> append(AsciiColor.BRIGHT_WHITE.format(symbol))
                            CUBE_ROCK -> append(AsciiColor.WHITE.format(symbol))
                            EMPTY_SPACE -> append(AsciiColor.BLACK.format(symbol))
                        }
                    } else {
                        append(symbol)
                    }
                }

                if (y != height - 1) {
                    append("\n")
                }
            }
        }

    private val seenPlatforms = mutableMapOf<Platform, MutableList<Int>>()

    fun cycle(n: Int): Platform {
        seenPlatforms.clear()

        var newPlatform = this
        var startOffset: Int? = null
        var cyclePeriod: Int? = null

        for (cycle in 1..n) {
            newPlatform = cycleInternal(newPlatform, cycle)

            if (newPlatform in seenPlatforms) {
                val seenCycles = seenPlatforms[newPlatform]!!
                seenCycles.add(cycle)
                println(seenCycles)

                if (seenCycles.size == 2) {
                    startOffset = seenCycles[0]
                    cyclePeriod = seenCycles[1] - seenCycles[0]
                    break
                }
            } else {
                seenPlatforms[newPlatform] = mutableListOf(cycle)
            }
        }

        if (startOffset == null || cyclePeriod == null) {
            println("Warning: finished without finding startOffset or cyclePeriod")
            return newPlatform
        }

        val wholePeriods = (n - startOffset) / cyclePeriod
        val endRemainder = (n - startOffset) % cyclePeriod
        println("Start offset: $startOffset")
        println("Cycle period: $cyclePeriod")
        println("Whole periods: $wholePeriods")
        println("End remainder: $endRemainder")

        newPlatform = this
        for (cycle in 1..startOffset) {
//            println("Cycle $cycle")
            newPlatform = cycleInternal(newPlatform, cycle)
        }
        for (cycle in (startOffset + wholePeriods * cyclePeriod + 1)..(startOffset + wholePeriods * cyclePeriod + endRemainder)) {
//            println("Cycle $cycle")
            newPlatform = cycleInternal(newPlatform, cycle)
        }
        return newPlatform
    }

    private fun cycleInternal(newPlatform: Platform, cycle: Int) =
        newPlatform
//            .also { println("Cycle $cycle:\n${it.toString(color = true)}\n") }
            .slideNorth()
//            .also { println("North:\n${it.toString(color = true)}\n") }
            .slideWest()
//            .also { println("West:\n${it.toString(color = true)}\n") }
            .slideSouth()
//            .also { println("South:\n${it.toString(color = true)}\n") }
            .slideEast()
//            .also { println("East:\n${it.toString(color = true)}\n") }

    fun slideNorth(): Platform {
        val newRocks = rocks.toMutableMap()

        for (y in 0..rocks.getHeight()) {
            for (x in 0..rocks.getWidth()) {
                val coord = Coord(x, y)
                if (newRocks[coord] == ROUND_ROCK) {
                    var lastFreeY = y
                    for (yy in y-1 downTo 0) {
                        if (newRocks[Coord(x, yy)] == null) {
                            lastFreeY = yy
                        } else {
                            break
                        }
                    }

                    newRocks.remove(coord)
                    newRocks[Coord(x, lastFreeY)] = ROUND_ROCK
                }
            }
        }

        return Platform(newRocks)
    }

    fun slideWest(): Platform {
        val newRocks = rocks.toMutableMap()

        for (x in 0..rocks.getWidth()) {
            for (y in 0..rocks.getHeight()) {
                val coord = Coord(x, y)
                if (newRocks[coord] == ROUND_ROCK) {
                    var lastFreeX = x
                    for (xx in x-1 downTo 0) {
                        if (newRocks[Coord(xx, y)] == null) {
                            lastFreeX = xx
                        } else {
                            break
                        }
                    }

                    newRocks.remove(coord)
                    newRocks[Coord(lastFreeX, y)] = ROUND_ROCK
                }
            }
        }

        return Platform(newRocks)
    }

    fun slideSouth(): Platform {
        val newRocks = rocks.toMutableMap()

        for (y in rocks.getHeight()-1 downTo 0) {
            for (x in 0..rocks.getWidth()) {
                val coord = Coord(x, y)
                if (newRocks[coord] == ROUND_ROCK) {
                    var lastFreeY = y
                    for (yy in y+1 until rocks.getHeight()) {
                        if (newRocks[Coord(x, yy)] == null) {
                            lastFreeY = yy
                        } else {
                            break
                        }
                    }

                    newRocks.remove(coord)
                    newRocks[Coord(x, lastFreeY)] = ROUND_ROCK
                }
            }
        }

        return Platform(newRocks)
    }

    fun slideEast(): Platform {
        val newRocks = rocks.toMutableMap()

        for (x in rocks.getWidth()-1 downTo 0) {
            for (y in 0..rocks.getHeight()) {
                val coord = Coord(x, y)
                if (newRocks[coord] == ROUND_ROCK) {
                    var lastFreeX = x
                    for (xx in x+1 until   rocks.getWidth()) {
                        if (newRocks[Coord(xx, y)] == null) {
                            lastFreeX = xx
                        } else {
                            break
                        }
                    }

                    newRocks.remove(coord)
                    newRocks[Coord(lastFreeX, y)] = ROUND_ROCK
                }
            }
        }

        return Platform(newRocks)
    }
}

fun parsePlatform(lines: Iterable<String>): Platform =
    Platform(lines.parse2DMap { if (it != EMPTY_SPACE) it else null }.toMap())
