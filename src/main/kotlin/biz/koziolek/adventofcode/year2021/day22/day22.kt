package biz.koziolek.adventofcode.year2021.day22

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})
    val lines = inputFile.bufferedReader().readLines()
    val rebootSteps = parseReactorRebootSteps(lines)
    val onCubes = executeRebootSteps(rebootSteps, min = -50, max = 50)
    println("On cubes: $onCubes")
}

data class RebootStep(val turnOn: Boolean, val from: Coord3d, val to: Coord3d) {
    fun getCubes(): Sequence<Coord3d> =
        (from.x..to.x).asSequence().flatMap { x ->
            (from.y..to.y).asSequence().flatMap { y ->
                (from.z..to.z).asSequence().map { z ->
                    Coord3d(x, y, z)
                }
            }
        }
}

fun parseReactorRebootSteps(lines: List<String>): List<RebootStep> =
    lines.map { line ->
        Regex("(on|off) x=(-?[0-9]+)\\.\\.(-?[0-9]+),y=(-?[0-9]+)\\.\\.(-?[0-9]+),z=(-?[0-9]+)\\.\\.(-?[0-9]+)")
            .find(line)
            ?.let { result ->
                RebootStep(
                    turnOn = result.groupValues[1] == "on",
                    from = Coord3d(
                        x = result.groupValues[2].toInt(),
                        y = result.groupValues[4].toInt(),
                        z = result.groupValues[6].toInt(),
                    ),
                    to = Coord3d(
                        x = result.groupValues[3].toInt(),
                        y = result.groupValues[5].toInt(),
                        z = result.groupValues[7].toInt(),
                    ),
                )
            }
            ?: throw IllegalArgumentException("Could not parse reboot step: $line")
    }

fun executeRebootSteps(rebootSteps: List<RebootStep>, min: Int, max: Int): Int =
    rebootSteps
        .filter {
            min <= it.from.x && it.to.x <= max
                    && min <= it.from.y && it.to.y <= max
                    && min <= it.from.z && it.to.z <= max
        }
        .fold(mutableMapOf<Coord3d, Boolean>()) { map, rebootStep ->
            rebootStep.getCubes().fold(map) { map2, coord3d -> 
                map2[coord3d] = rebootStep.turnOn
                map2
            }
        }
        .count { it.value }
