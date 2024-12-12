package biz.koziolek.adventofcode.year2024.day12

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput
import biz.koziolek.adventofcode.parse2DMap

fun main() {
    val inputFile = findInput(object {})
    val regions = parseRegions(inputFile.bufferedReader().readLines())
    val prices = regions.map { it.price }
    println("Total price: ${prices.sum()}")
}

data class Region(val plant: Char, val coords: Set<Coord>, val map: Map<Coord, Char>) {
    val area: Int = coords.size
    val perimeter: Int by lazy {
        coords.sumOf { coord ->
            coord.getAdjacentCoords(includeDiagonal = false)
                .count { it !in coords || map[it] != plant }
        }
    }
    val price: Int by lazy {
        area * perimeter
    }
}

fun parseRegions(lines: Iterable<String>): List<Region> {
    val map = lines.parse2DMap().toMap()
    val regions = mutableListOf<Region>()

    for ((coord, plant) in map) {
        if (regions.any { it.coords.contains(coord) }) {
            continue
        }

        val coords = findAllAdjacentPlants(coord, plant, map)
        val region = Region(plant, coords, map)
        regions.add(region)
    }

    return regions
}

fun findAllAdjacentPlants(start: Coord, plant: Char, map: Map<Coord, Char>): Set<Coord> {
    val result = mutableSetOf<Coord>()
    val toVisit = mutableListOf(start)
    val visited = mutableSetOf<Coord>()

    while (toVisit.isNotEmpty()) {
        val current = toVisit.removeFirst()
        visited.add(current)

        if (map[current] == plant) {
            result.add(current)
            current.getAdjacentCoords(includeDiagonal = false)
                .filter { it !in visited && it !in toVisit }
                .filter { it in map }
                .filter { map[it] == plant }
                .forEach { toVisit.add(it) }
        }
    }

    return result
}
