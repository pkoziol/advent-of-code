package biz.koziolek.adventofcode.year2022.day18

import biz.koziolek.adventofcode.Coord3d
import biz.koziolek.adventofcode.findInput
import biz.koziolek.adventofcode.getAdjacentCoords
import java.util.ArrayDeque
import java.util.Queue

fun main() {
    val inputFile = findInput(object {})
    val droplet = parseDroplet(inputFile.bufferedReader().readLines())
    println("Droplet surface area: ${getSurfaceArea(droplet)}")
    val lavaAirMap = mapOutside(droplet)
    println("Droplet outside surface area: ${getOutsideSurfaceArea(lavaAirMap)}")
}

fun parseDroplet(lines: Iterable<String>): Set<Coord3d> =
    lines.map { Coord3d.fromString(it) }.toSet()

fun getSurfaceArea(droplet: Set<Coord3d>): Int =
    droplet.sumOf { 6 - droplet.getAdjacentCoords(it).size }

const val LAVA = 'L'
const val OUTSIDE = 'O'

fun mapOutside(droplet: Set<Coord3d>): Map<Coord3d, Char> =
    buildMap {
        putAll(droplet.map { it to LAVA })

        val minX = droplet.minOf { it.x }
        val maxX = droplet.maxOf { it.x }
        val minY = droplet.minOf { it.y }
        val maxY = droplet.maxOf { it.y }
        val minZ = droplet.minOf { it.z }
        val maxZ = droplet.maxOf { it.z }
        val xRange = (minX - 1)..(maxX + 1)
        val yRange = (minY - 1)..(maxY + 1)
        val zRange = (minZ - 1)..(maxZ + 1)

        val toCheck: Queue<Coord3d> = ArrayDeque()

        fun shouldBeChecked(coord: Coord3d) =
            coord.x in xRange
                    && coord.y in yRange
                    && coord.z in zRange
                    && !contains(coord)
                    && !toCheck.contains(coord)

        for (x in (minX-1)..(maxX+1)) {
            for (y in (minY - 1)..(maxY + 1)) {
                val coord = Coord3d(x, y, z = minZ - 1)
                put(coord, OUTSIDE)

                coord.getAdjacentCoords()
                    .filter { shouldBeChecked(it) }
                    .forEach { toCheck.add(it) }
            }
        }

        while (toCheck.isNotEmpty()) {
            val coord = toCheck.poll()
            val adjCoords = coord.getAdjacentCoords()

            if (adjCoords.any { get(it) == OUTSIDE }) {
                put(coord, OUTSIDE)
            }

            adjCoords
                .filter { shouldBeChecked(it) }
                .forEach { toCheck.add(it) }
        }
    }

fun getOutsideSurfaceArea(lavaAirMap: Map<Coord3d, Char>): Int =
    lavaAirMap
        .filterValues { it == LAVA }
        .keys
        .sumOf { coord ->
            lavaAirMap.keys.getAdjacentCoords(coord).count { adjCoord ->
                lavaAirMap[adjCoord] == OUTSIDE
            }
        }
