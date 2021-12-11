package biz.koziolek.adventofcode.year2021.day2

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val position = calculatePosition(inputFile.bufferedReader().lineSequence())
    println("Position: $position")
    println("Answer 1: ${position.horizontal * position.depth}")

    val positionWithAim = calculatePositionWithAim(inputFile.bufferedReader().lineSequence())
    println("Position with aim: $positionWithAim")
    println("Answer 2: ${positionWithAim.horizontal * positionWithAim.depth}")
}

data class Position(val horizontal: Int, val depth: Int)

data class PositionAndAim(val position: Position, val aim: Int)

data class Command(val direction: String, val value: Int)

fun calculatePosition(lines: Sequence<String>): Position =
        parseCommands(lines)
                .fold(Position(0, 0)) { acc, command ->
                    when (command.direction) {
                        "forward" -> acc.copy(horizontal = acc.horizontal + command.value)
                        "down" -> acc.copy(depth = acc.depth + command.value)
                        "up" -> acc.copy(depth = acc.depth - command.value)
                        else -> throw IllegalArgumentException("Unknown direction: ${command.direction}")
                    }
                }

fun calculatePositionWithAim(lines: Sequence<String>): Position =
        parseCommands(lines)
                .fold(PositionAndAim(Position(0, 0), 0)) { acc, command ->
                    when (command.direction) {
                        "forward" -> acc.copy(position = acc.position.copy(
                                horizontal = acc.position.horizontal + command.value,
                                depth = acc.position.depth + acc.aim  * command.value
                        ))
                        "down" -> acc.copy(aim = acc.aim + command.value)
                        "up" -> acc.copy(aim = acc.aim - command.value)
                        else -> throw IllegalArgumentException("Unknown direction: ${command.direction}")
                    }
                }
                .position

private fun parseCommands(lines: Sequence<String>) = lines
        .map { it.split(" ", limit = 2) }
        .filter { it.size == 2 }
        .map { Command(direction = it[0], value = it[1].toInt()) }
