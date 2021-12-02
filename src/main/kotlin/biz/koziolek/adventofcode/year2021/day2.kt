package biz.koziolek.adventofcode.year2021

import java.io.File

fun main() {
    val inputFile = File("src/main/resources/year2021/day2/input")
    val position = calculatePosition(inputFile.bufferedReader().lineSequence())
    println("Position: $position")
    println("Answer 1: ${position.horizontal * position.depth}")

    val positionWithAim = calculatePositionWithAim(inputFile.bufferedReader().lineSequence())
    println("Position with aim: $positionWithAim")
    println("Answer 2: ${positionWithAim.horizontal * positionWithAim.depth}")
}

data class Position(val horizontal: Int,
                    val depth: Int)

data class PositionAndAim(val position: Position,
                          val aim: Int)

fun calculatePosition(lines: Sequence<String>): Position =
        lines
                .map { it.split(" ", limit = 2) }
                .filter { it.size == 2 }
                .fold(Position(0, 0)) { acc, splitCommand ->
                    val direction = splitCommand[0]
                    val value = splitCommand[1].toInt()

                    when (direction) {
                        "forward" -> acc.copy(horizontal = acc.horizontal + value)
                        "down" -> acc.copy(depth = acc.depth + value)
                        "up" -> acc.copy(depth = acc.depth - value)
                        else -> throw IllegalArgumentException("Unknown direction: $direction")
                    }
                }

fun calculatePositionWithAim(lines: Sequence<String>): Position =
        lines
                .map { it.split(" ", limit = 2) }
                .filter { it.size == 2 }
                .fold(PositionAndAim(Position(0, 0), 0)) { acc, splitCommand ->
                    val direction = splitCommand[0]
                    val value = splitCommand[1].toInt()

                    when (direction) {
                        "forward" -> acc.copy(position = acc.position.copy(
                                horizontal = acc.position.horizontal + value,
                                depth = acc.position.depth + acc.aim  * value
                        ))
                        "down" -> acc.copy(aim = acc.aim + value)
                        "up" -> acc.copy(aim = acc.aim - value)
                        else -> throw IllegalArgumentException("Unknown direction: $direction")
                    }
                }
                .position
