package biz.koziolek.adventofcode.year2024.day14

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput
import java.util.stream.Collectors

fun main() {
    val inputFile = findInput(object {})
    val map = parseRobotMap(inputFile.bufferedReader().readLines())

    val map100 = simulate(map, seconds = 100)
    println("Safety factor after 100s: ${map100.calculateSafetyFactor()}")

    val secondsToATree = findTree(map)
    println("Seconds to a tree: $secondsToATree")
    println(simulate(map, seconds = secondsToATree))
}

data class Robot(val position: Coord, val velocity: Coord) {
    fun move(times: Int): Robot =
        copy(position = position + velocity * times)

    fun teleport(map: RobotMap): Robot =
        copy(position = Coord(
            x = wrap(position.x, map.width),
            y = wrap(position.y, map.height),
        ))

    private fun wrap(value: Int, max: Int): Int =
        if (value < 0) {
            (max - (-value % max)) % max
        } else {
            value % max
        }
}

data class RobotMap(val robots: List<Robot>, val width: Int, val height: Int) {
    fun calculateSafetyFactor(): Int {
        val quadrant1 = robots.filter { it.position.x < width / 2 && it.position.y < height / 2 }
        val quadrant2 = robots.filter { it.position.x > width / 2 && it.position.y < height / 2 }
        val quadrant3 = robots.filter { it.position.x > width / 2 && it.position.y > height / 2 }
        val quadrant4 = robots.filter { it.position.x < width / 2 && it.position.y > height / 2 }
        return quadrant1.size * quadrant2.size * quadrant3.size * quadrant4.size
    }

    override fun toString() =
        buildString {
            for (y in 0 until height) {
                for (x in 0 until width) {
                    val robots = robots.count { it.position == Coord(x, y) }
                    if (robots >= 10) {
                        append("*")
                    } else if (robots > 0) {
                        append(robots)
                    } else {
                        append(".")
                    }
                }
                if (y < height - 1) {
                    append("\n")
                }
            }
        }
}

fun parseRobotMap(lines: Iterable<String>, width: Int = 101, height: Int = 103): RobotMap =
    lines
        .map { line ->
            val (p, v) = line.split(" ")
            Robot(
                position = Coord.fromString(p.drop(2)),
                velocity = Coord.fromString(v.drop(2)),
            )
        }
        .let { RobotMap(it, width, height) }

fun simulate(map: RobotMap, seconds: Int): RobotMap =
    map.copy(
        robots = map.robots.map {
            val move = it.move(seconds)
            val teleport = move.teleport(map)
            teleport
        }
    )

private data class MapWithStat(val seconds: Int, val map: RobotMap, val stat: Int)

fun findTree(map: RobotMap): Int {
    val stats = (1..20_000)
        .toList()
        .parallelStream()
        .map { seconds ->
            val (m, stat) = findLongestConsecutive(map, seconds)
            MapWithStat(seconds, m, stat)
        }
        .takeWhile { it.map != map }
        .filter { it.stat > 3 }
        .collect(Collectors.groupingBy(
            { it.stat },
            Collectors.mapping({ it.seconds }, Collectors.toList())
        ))

    stats.entries
        .sortedByDescending { it.key }
        .forEach { println("Line containing ${it.key} consecutive robots appears ${it.value.size} times") }

    return stats.entries.maxBy { it.key }.value.single()
}

private fun findLongestConsecutive(map: RobotMap, seconds: Int): Pair<RobotMap, Int> {
    val mapI = simulate(map, seconds = seconds)
    val mapString = mapI.toString()
    val longestConsecutive = mapString.lines()
        .maxOf { line ->
            line.split('.').maxOf { it.length }
        }

    return mapI to longestConsecutive
}
