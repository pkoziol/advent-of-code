package biz.koziolek.adventofcode.year2022.day18

import biz.koziolek.adventofcode.Coord3d
import biz.koziolek.adventofcode.findInput
import biz.koziolek.adventofcode.getAdjacentCoords

fun main() {
    val inputFile = findInput(object {})
    val droplet = parseDroplet(inputFile.bufferedReader().readLines())
    println("Droplet surface area: ${getSurfaceArea(droplet)}")
}

fun parseDroplet(lines: Iterable<String>): Set<Coord3d> =
    lines.map { Coord3d.fromString(it) }.toSet()

fun getSurfaceArea(droplet: Set<Coord3d>): Int =
    droplet.sumOf { 6 - droplet.getAdjacentCoords(it).size }
