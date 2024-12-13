package biz.koziolek.adventofcode.year2024.day13

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val machines = parseClawMachines(inputFile.bufferedReader().readLines())
    val winningMoves = winAllPossible(machines)
    println("Total cost: ${winningMoves.sumOf { it.cost }}")
}

data class ClawMachine(
    val a: Coord,
    val b: Coord,
    val prize: Coord,
    val aCost: Int = 3,
    val bCost: Int = 1,
)

data class WinningMove(
    val a: Int,
    val b: Int,
    val machine: ClawMachine,
) {
    val cost: Int = a * machine.aCost + b * machine.bCost
}

fun parseClawMachines(lines: Iterable<String>): List<ClawMachine> =
    buildList {
        val iterator = lines.iterator()
        while (iterator.hasNext()) {
            val a = Regex("Button A: X\\+([0-9]+), Y\\+([0-9]+)").find(iterator.next())!!.groupValues.let { Coord(it[1].toInt(), it[2].toInt()) }
            val b = Regex("Button B: X\\+([0-9]+), Y\\+([0-9]+)").find(iterator.next())!!.groupValues.let { Coord(it[1].toInt(), it[2].toInt()) }
            val prize = Regex("Prize: X=([0-9]+), Y=([0-9]+)").find(iterator.next())!!.groupValues.let { Coord(it[1].toInt(), it[2].toInt()) }
            if (iterator.hasNext()) {
                iterator.next() // skip empty line
            }
            add(ClawMachine(a, b, prize))
        }
    }

fun winAllPossible(machines: List<ClawMachine>): List<WinningMove> =
    machines.mapNotNull { machine ->
        for (a in 0..100) {
            for (b in 0..100) {
                if (machine.a * a + machine.b * b == machine.prize) {
                    return@mapNotNull WinningMove(a, b, machine)
                }
            }
        }
        return@mapNotNull null
    }
