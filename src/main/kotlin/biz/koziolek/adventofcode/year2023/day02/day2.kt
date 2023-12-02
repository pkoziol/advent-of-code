package biz.koziolek.adventofcode.year2023.day02

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val games = parseGames(inputFile.bufferedReader().readLines())
    val possible = findPossibleGames(games, FULL_SET)
    println("Possible games are: ${possible.map { it.id }} with sum of IDs: ${sumGameIds(possible)}")
    println("Sum of powers of minimum sets: ${sumPowersOfMinimumSets(games)}")
}

val FULL_SET = CubeSet(red = 12, green = 13, blue = 14)

data class Game(val id: Int, val sets: List<CubeSet>) {
    val minimumSet: CubeSet
        get() = CubeSet(
            red = sets.maxOf { it.red },
            green = sets.maxOf { it.green },
            blue = sets.maxOf { it.blue },
        )

    fun isPossible(fullSet: CubeSet) =
        sets.all { set -> set.isSubSetOf(fullSet) }
}

data class CubeSet(val red: Int = 0, val green: Int = 0, val blue: Int = 0) {
    val power: Int
        get() = red * green * blue

    fun add(count: Int, color: String): CubeSet {
        val (newRed, newGreen, newBlue) = when (color) {
            "red" -> Triple(count, 0, 0)
            "green" -> Triple(0, count, 0)
            "blue" -> Triple(0, 0, count)
            else -> throw IllegalArgumentException("Unknown color: $color")
        }
        return copy(
            red = red + newRed,
            green = green + newGreen,
            blue = blue + newBlue,
        )
    }

    fun isSubSetOf(other: CubeSet) =
        this.red <= other.red
                && this.green <= other.green
                && this.blue <= other.blue
}

fun parseGames(lines: Iterable<String>): List<Game> =
    lines.map { line ->
        val parts = line.split(Regex("[:;]"))
        val id = parts[0].replace("Game ", "").toInt()
        val sets = parts.drop(1).map { setStr ->
            setStr.split(",").fold(CubeSet()) { acc, colorStr ->
                val (count, color) = colorStr.trim().split(" ")
                acc.add(count.toInt(), color)
            }
        }
        Game(id, sets)
    }

fun findPossibleGames(games: List<Game>, fullSet: CubeSet): List<Game> =
    games.filter { it.isPossible(fullSet) }

fun sumGameIds(games: List<Game>) = games.sumOf { it.id }

fun sumPowersOfMinimumSets(games: List<Game>): Int =
    games.sumOf { it.minimumSet.power }