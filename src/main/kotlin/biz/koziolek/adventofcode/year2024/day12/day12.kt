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

    val sides: Int by lazy {
        val visitedOutside = mutableSetOf<Pair<Coord, Coord>>()
        var sides = 0

        for (coord in coords) {
            for (outsideCoord in coord.getAdjacentCoords(includeDiagonal = false)) {
                val diffToInside = coord - outsideCoord

                if (outsideCoord in coords || (outsideCoord to diffToInside) in visitedOutside) {
                    continue
                }

                sides++

                val diffToWalk = Coord(x = diffToInside.y, y = diffToInside.x)
                val diffToWalkBack = Coord(x = -diffToInside.y, y = -diffToInside.x)
                var current = outsideCoord

                while (true) {
                    val currentIn = current + diffToInside
                    if (current in coords || currentIn !in coords) {
                        break
                    }
                    visitedOutside.add(current to diffToInside)
                    current += diffToWalk
                }

                current = outsideCoord
                while (true) {
                    val currentIn = current + diffToInside
                    if (current in coords || currentIn !in coords) {
                        break
                    }
                    visitedOutside.add(current to diffToInside)
                    current += diffToWalkBack
                }
            }
        }

        sides
    }

    val price: Int by lazy {
        area * perimeter
    }

    val discountedPrice: Int by lazy {
        area * sides
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
