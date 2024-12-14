package biz.koziolek.adventofcode.year2024.day13

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.LongCoord
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
    val prize: LongCoord,
    val aCost: Int = 3,
    val bCost: Int = 1,
)

data class WinningMove(
    val a: Long,
    val b: Long,
    val machine: ClawMachine,
) {
    val cost: Long = a * machine.aCost + b * machine.bCost
}

fun parseClawMachines(lines: Iterable<String>): List<ClawMachine> =
    buildList {
        val iterator = lines.iterator()
        while (iterator.hasNext()) {
            val a = Regex("Button A: X\\+([0-9]+), Y\\+([0-9]+)").find(iterator.next())!!.groupValues.let { Coord(it[1].toInt(), it[2].toInt()) }
            val b = Regex("Button B: X\\+([0-9]+), Y\\+([0-9]+)").find(iterator.next())!!.groupValues.let { Coord(it[1].toInt(), it[2].toInt()) }
            val prize = Regex("Prize: X=([0-9]+), Y=([0-9]+)").find(iterator.next())!!.groupValues.let { LongCoord(it[1].toInt(), it[2].toInt()) }
            if (iterator.hasNext()) {
                iterator.next() // skip empty line
            }
            add(ClawMachine(a, b, prize))
        }
    }

fun winAllPossible(machines: List<ClawMachine>): List<WinningMove> =
    machines.mapNotNull { machine ->
        val aNumerator = machine.prize.x * machine.b.y - machine.prize.y * machine.b.x
        val aDenominator = machine.a.x * machine.b.y - machine.a.y * machine.b.x

        if (aDenominator == 0 || aNumerator % aDenominator != 0L) {
            return@mapNotNull null
        }

        val a = aNumerator / aDenominator
        val bNumerator = machine.prize.y - machine.a.y * a
        val bDenominator = machine.b.y

        if (bDenominator == 0 || bNumerator % bDenominator != 0L) {
            return@mapNotNull null
        }

        val b = bNumerator / machine.b.y
        return@mapNotNull WinningMove(a, b, machine)
    }

fun fixConversionError(machines: List<ClawMachine>): List<ClawMachine> =
    machines.map {
        it.copy(
            prize = LongCoord(
                x = it.prize.x + 10000000000000L,
                y = it.prize.y + 10000000000000L,
            )
        )
    }
