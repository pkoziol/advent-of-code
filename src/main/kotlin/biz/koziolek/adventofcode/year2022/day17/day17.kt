package biz.koziolek.adventofcode.year2022.day17

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput

fun main() {
    val jetPattern = findInput(object {}).bufferedReader().readLine()
    val chamber = Chamber(width = 7, jetPattern = jetPattern)
    val chamber2022 = chamber.dropManyRocks(count = 2022)
    println(chamber2022.toString())
    println("Tower is ${chamber2022.height} high after 2022 rocks have stopped")
}

const val AIR = '.'
const val ROCK = '#'
const val FALLING_ROCK = '@'
const val WALL = '|'
const val FLOOR = '-'
const val CORNER = '+'

val ROCKS = listOf(
    parseRock("""
        ####
    """.trimIndent()),

    parseRock("""
        .#.
        ###
        .#.
    """.trimIndent()),

    parseRock("""
        ..#
        ..#
        ###
    """.trimIndent()),

    parseRock("""
        #
        #
        #
        #
    """.trimIndent()),

    parseRock("""
        ##
        ##
    """.trimIndent()),
)

fun parseRock(rock: String): Map<Coord, Char> =
    rock.split("\n")
        .reversed()
        .flatMapIndexed { y, line ->
            line.flatMapIndexed { x, char ->
                if (char == ROCK) {
                    listOf(Coord(x, y) to char)
                } else {
                    emptyList()
                }
            }
        }.toMap()

private val LEFT = Coord(x = -1, y = 0)
private val RIGHT = Coord(x = 1, y = 0)
private val DOWN = Coord(x = 0, y = -1)

data class Chamber(
    val rocks: Map<Coord, Char> = emptyMap(),
    val width: Int,
    val jetPattern: String,
    private val jetIndex: Int = 0
) {
    val height = rocks.maxOfOrNull { it.key.y }?.plus(1) ?: 0

    fun dropManyRocks(count: Int, startIndex: Int = 0) =
        (0 until count).fold(this) { currentChamber, rockOffset ->
            currentChamber.dropRock(ROCKS[(startIndex + rockOffset) % ROCKS.size])
        }

    fun dropRock(rock: Map<Coord, Char>): Chamber {
        var tmpRocks = spawn(rock, rocks)
        var movesCount = 0

//        println("init:\n${copy(rocks = tmpRocks)}\n")

        while (tmpRocks.any { it.value == FALLING_ROCK }) {
            val sideDirection = if (jetPattern[(jetIndex + movesCount) % jetPattern.length] == '<') LEFT else RIGHT
            movesCount++

            if (canMove(tmpRocks, sideDirection)) {
                tmpRocks = move(tmpRocks, sideDirection)
            }
//            println("side:\n${copy(rocks = tmpRocks)}\n")

            tmpRocks = if (canMove(tmpRocks, DOWN)) {
                move(tmpRocks, DOWN)
            } else {
                rest(tmpRocks)
            }
//            println("down:\n${copy(rocks = tmpRocks)}\n")
        }

        return copy(
            rocks = tmpRocks,
            jetIndex = jetIndex + movesCount,
        )
    }

    private fun spawn(rock: Map<Coord, Char>, rocks: Map<Coord, Char>): Map<Coord, Char> =
        rocks + rock
            .mapKeys { (coord, _) -> coord + Coord(2, height + 3) }
            .mapValues { FALLING_ROCK }

    private fun canMove(rocks: Map<Coord, Char>, direction: Coord): Boolean {
        return rocks
            .filterValues { it == FALLING_ROCK }
            .keys
            .map { it + direction }
            .all { newCoord ->
                newCoord.x in 0 until width
                        && newCoord.y >= 0
                        && rocks[newCoord] in setOf(null, AIR, FALLING_ROCK)
            }
    }

    private fun move(tmpRocks: Map<Coord, Char>, direction: Coord) =
        tmpRocks.mapKeys { (coord, value) ->
            when (value) {
                FALLING_ROCK -> coord + direction
                else -> coord
            }
        }

    private fun rest(tmpRocks: Map<Coord, Char>) =
        tmpRocks.mapValues { (_, value) ->
            when (value) {
                FALLING_ROCK -> ROCK
                else -> value
            }
        }

    override fun toString() =
        buildString {
            for (y in height - 1 downTo 0) {
                append(WALL)
                for (x in 0 until width) {
                    append(rocks[Coord(x, y)] ?: AIR)
                }
                append("$WALL\n")
            }
            append(CORNER + FLOOR.toString().repeat(width) + CORNER)
        }
}
