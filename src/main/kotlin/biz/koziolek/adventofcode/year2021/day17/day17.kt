package biz.koziolek.adventofcode.year2021.day17

import biz.koziolek.adventofcode.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    val inputFile = findInput(object {})
    val line = inputFile.bufferedReader().readLine()
    val targetArea = parseTargetArea(line)

    val (bestVelocity, maxHeight) = findVelocityWithMostStyle(targetArea)
    println("Best velocity is: $bestVelocity that gives max height: $maxHeight")

    val allVelocitiesThatHitTarget = findAllVelocitiesThatHitTarget(targetArea)
    println("There is ${allVelocitiesThatHitTarget.size} velocities that hit target")
}

fun parseTargetArea(line: String): Pair<Coord, Coord> =
    Regex("target area: x=(-?[0-9]+)\\.\\.(-?[0-9]+), y=(-?[0-9]+)\\.\\.(-?[0-9]+)")
        .find(line)
        ?.let {
            val x1 = it.groupValues[1].toInt()
            val x2 = it.groupValues[2].toInt()
            val y1 = it.groupValues[3].toInt()
            val y2 = it.groupValues[4].toInt()

            Coord(min(x1, x2), max(y1, y2)) to Coord(max(x1, x2), min(y1, y2))
        }
        ?: throw IllegalArgumentException("Could not parse target area: $line")

fun findVelocityWithMostStyle(targetArea: Pair<Coord, Coord>): Pair<Coord, Int> =
    generateVelocities(0, targetArea.second.x, 0, abs(targetArea.second.y))
        .map { velocity -> velocity to calculateTrajectory(velocity, targetArea) }
        .filter { (_, trajectory) -> trajectory.last() in targetArea }
        .map { (velocity, trajectory) -> velocity to trajectory.maxOf { it.y } }
        .sortedByDescending { (_, maxHeight) -> maxHeight }
        .first()

fun findAllVelocitiesThatHitTarget(targetArea: Pair<Coord, Coord>): Set<Coord> =
    generateVelocities(0, targetArea.second.x, targetArea.second.y, abs(targetArea.second.y))
        .map { velocity -> velocity to calculateTrajectory(velocity, targetArea) }
        .filter { (_, trajectory) -> trajectory.last() in targetArea }
        .map { (velocity, _) -> velocity }
        .toSet()

private fun generateVelocities(minX: Int, maxX: Int, minY: Int, maxY: Int): Sequence<Coord> =
    sequence {
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                yield(Coord(x, y))
            }
        }
    }

fun calculateTrajectory(velocity: Coord, targetArea: Pair<Coord, Coord>, limit: Int = 1_000_000): List<Coord> =
    generateSequence(Triple(Coord(0, 0), velocity, 0)) { (coord, velocity, iteration) ->
        if (iteration > limit) {
            throw IllegalStateException("Trajectory calculation interrupted by limit")
        } else if (coord in targetArea) {
            null
        } else {
            val newCoord = coord + velocity
            val newVelocity = Coord(
                x = when {
                    velocity.x > 0 -> velocity.x - 1
                    velocity.x < 0 -> velocity.x + 1
                    else -> velocity.x
                },
                y = velocity.y - 1
            )

            if (velocity.y < 0 && newCoord.y < targetArea.second.y
                || velocity.x > 0 && newCoord.x > targetArea.second.x) {
                null
            } else {
                Triple(newCoord, newVelocity, iteration + 1)
            }
        }
    }
        .map { it.first }
        .toList()

fun visualizeTrajectory(trajectory: List<Coord>, targetArea: Pair<Coord, Coord>): String =
    buildString {
        val allX = trajectory.map { it.x }.plus(targetArea.first.x).plus(targetArea.second.x)
        val allY = trajectory.map { it.y }.plus(targetArea.first.y).plus(targetArea.second.y)
        val minX = allX.minOrNull() ?: 0
        val maxX = allX.maxOrNull() ?: 0
        val minY = allY.minOrNull() ?: 0
        val maxY = allY.maxOrNull() ?: 0
        
        for (y in maxY downTo minY) {
            for (x in minX..maxX) {
                val coord = Coord(x, y)
                val trajectoryIndex = trajectory.indexOf(coord)
                
                if (trajectoryIndex == 0) {
                    append('S')
                } else if (trajectoryIndex > 0) {
                    append('#')
                } else if (coord in targetArea) {
                    append('T')
                } else {
                    append('.')
                }
            }

            if (y != minY) {
                append('\n')
            }
        }
    }

private operator fun Pair<Coord, Coord>.contains(coord: Coord): Boolean {
    val minX = min(first.x, second.x)
    val maxX = max(first.x, second.x)
    val minY = min(first.y, second.y)
    val maxY = max(first.y, second.y)

    return coord.x in minX..maxX && coord.y in minY..maxY
}
