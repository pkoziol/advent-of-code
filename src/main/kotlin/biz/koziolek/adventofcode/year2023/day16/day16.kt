package biz.koziolek.adventofcode.year2023.day16

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})
    val mirrors = parseMirrorContraption(inputFile.bufferedReader().readLines())
    val beamPath = simulateBeam(mirrors)
    println("Energized tiles: ${countEnergizedTiles(beamPath)}")
}

const val EMPTY_SPACE = '.'
const val MIRROR_RIGHT = '/'
const val MIRROR_LEFT = '\\'
const val SPLITTER_VERTICAL = '|'
const val SPLITTER_HORIZONTAL = '-'
const val ENERGIZED_TILE = '#'

fun parseMirrorContraption(lines: Iterable<String>): Map<Coord, Char> =
    lines.parse2DMap().filter { it.second != EMPTY_SPACE }.toMap()

fun simulateBeam(mirrors: Map<Coord, Char>,
                 start: Pair<Coord, Direction> = Coord(0, 0) to Direction.EAST): Sequence<Pair<Coord, Direction>> =
    sequence {
        var beams = listOf(start)
        val horizontalRange = mirrors.getHorizontalRange()
        val verticalRange = mirrors.getVerticalRange()
        val seen = mutableSetOf<Pair<Coord, Direction>>()

        while (beams.isNotEmpty()) {
            beams = beams.also { seen.addAll(it) }.flatMap { (beamCoord, beamDirection) ->
                val mirror = mirrors[beamCoord]
                val newDirections: List<Direction> = when (mirror) {
                    MIRROR_RIGHT -> when (beamDirection) {
                        Direction.NORTH -> listOf(Direction.EAST)
                        Direction.SOUTH -> listOf(Direction.WEST)
                        Direction.WEST -> listOf(Direction.SOUTH)
                        Direction.EAST -> listOf(Direction.NORTH)
                    }

                    MIRROR_LEFT -> when (beamDirection) {
                        Direction.NORTH -> listOf(Direction.WEST)
                        Direction.SOUTH -> listOf(Direction.EAST)
                        Direction.WEST -> listOf(Direction.NORTH)
                        Direction.EAST -> listOf(Direction.SOUTH)
                    }

                    SPLITTER_VERTICAL -> when (beamDirection) {
                        Direction.NORTH -> listOf(Direction.NORTH)
                        Direction.SOUTH -> listOf(Direction.SOUTH)
                        Direction.WEST -> listOf(Direction.NORTH, Direction.SOUTH)
                        Direction.EAST -> listOf(Direction.NORTH, Direction.SOUTH)
                    }

                    SPLITTER_HORIZONTAL -> when (beamDirection) {
                        Direction.NORTH -> listOf(Direction.WEST, Direction.EAST)
                        Direction.SOUTH -> listOf(Direction.WEST, Direction.EAST)
                        Direction.WEST -> listOf(Direction.WEST)
                        Direction.EAST -> listOf(Direction.EAST)
                    }

                    else -> listOf(beamDirection)
                }

                newDirections.forEach { yield(beamCoord to it) }
                newDirections
                    .map { beamCoord.move(it) to it }
                    .filter { (coord, _) -> coord.x in horizontalRange && coord.y in verticalRange }
                    .filter { it !in seen }
            }
        }
    }

fun getEnergizedTiles(beamPath: Sequence<Pair<Coord, Direction>>): Map<Coord, List<Direction>> =
    beamPath.fold(mutableMapOf()) { acc, (coord, direction) ->
        acc.compute(coord) { _, existingDirections ->
            if (existingDirections == null) {
                listOf(direction)
            } else {
                existingDirections + direction
            }
        }
        acc
    }

fun showPath(mirrors: Map<Coord, Char>, beamPath: Sequence<Pair<Coord, Direction>>): String {
    val energizedTiles = getEnergizedTiles(beamPath)

    return mirrors
        .to2DString { coord, mirror ->
            mirror
                ?: energizedTiles[coord]?.let {
                    when (it.size) {
                        1 -> it[0].char
                        else -> it.size.digitToChar(radix = 36)
                    }
                }
                ?: EMPTY_SPACE
        }
}

fun showEnergizedTiles(beamPath: Sequence<Pair<Coord, Direction>>): String =
    beamPath
        .map { it.first to ENERGIZED_TILE }
        .toMap()
        .to2DString { _, char -> char ?: EMPTY_SPACE }

fun countEnergizedTiles(beamPath: Sequence<Pair<Coord, Direction>>): Int =
    getEnergizedTiles(beamPath).keys.size

fun findBestBeamStart(mirrors: Map<Coord, Char>): Pair<Coord, Direction> {
    val width = mirrors.getWidth()
    val height = mirrors.getHeight()
    val startsNorth = (0..<width).map { x -> Coord(x, 0) to Direction.SOUTH }
    val startsSouth = (0..<width).map { x -> Coord(x, height - 1) to Direction.NORTH }
    val startsWest = (0..<height).map { y -> Coord(0, y) to Direction.EAST }
    val startsEast = (0..<height).map { y -> Coord(width - 1, y) to Direction.WEST }

    return (startsNorth + startsSouth + startsWest + startsEast)
        .maxBy { countEnergizedTiles(simulateBeam(mirrors, it)) }
}
