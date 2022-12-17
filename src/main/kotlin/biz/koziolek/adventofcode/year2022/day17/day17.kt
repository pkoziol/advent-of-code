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
        var tmpRock = spawn(rock)
        var movesCount = 0
        var rested = false

//        println("init:\n${copy(rocks = tmpRocks)}\n")

        while (!rested) {
            val sideDirection = if (jetPattern[(jetIndex + movesCount) % jetPattern.length] == '<') LEFT else RIGHT
            movesCount++

            if (canMove(tmpRock, sideDirection)) {
                tmpRock = move(tmpRock, sideDirection)
            }
//            println("side:\n${copy(rocks = tmpRocks)}\n")

            if (canMove(tmpRock, DOWN)) {
                tmpRock = move(tmpRock, DOWN)
            } else {
                rested = true
            }
//            println("down:\n${copy(rocks = tmpRocks)}\n")
        }

        return copy(
            rocks = rocks + tmpRock.mapValues { ROCK },
            jetIndex = jetIndex + movesCount,
        )
    }

    private fun spawn(rock: Map<Coord, Char>): Map<Coord, Char> =
        rock
            .map { (coord, _) -> (coord + Coord(2, height + 3)) to FALLING_ROCK }
            .toMap()

    private fun canMove(rock: Map<Coord, Char>, direction: Coord): Boolean {
        return rock.keys
            .all { coord ->
                val newCoord = coord + direction
                newCoord.x in 0 until width
                        && newCoord.y >= 0
                        && rocks[newCoord] in setOf(null, AIR, FALLING_ROCK)
            }
    }

    private fun move(tmpRock: Map<Coord, Char>, direction: Coord) =
        tmpRock.mapKeys { (coord, _) -> coord + direction }

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
