package biz.koziolek.adventofcode.year2024.day18

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})
    val bytes = parseFallingBytes(inputFile.bufferedReader().readLines())
    val memory = buildMemory(bytes.take(1024))
    val path = findExitPath(memory)
    println("Steps to exit after 1kB: ${path.size - 1}")
}

const val EMPTY = '.'
const val CORRUPTED = '#'
const val PATH = 'O'

fun parseFallingBytes(lines: Iterable<String>): List<Coord> =
    lines.map { Coord.fromString(it) }

fun buildMemory(bytes: List<Coord>, size: Int = 70): Map<Coord, Char> =
    buildMap {
        for (x in 0..size) {
            for (y in 0..size) {
                put(Coord(x, y), EMPTY)
            }
        }
        for (byte in bytes) {
            put(byte, CORRUPTED)
        }
    }

fun findExitPath(memory: Map<Coord, Char>): List<Coord> {
    val start = Coord(0, 0)
    val exit = Coord(memory.getWidth() - 1, memory.getHeight() - 1)

    val graph = buildGraph {
        memory.keys
            .filter { memory[it] == EMPTY }
            .forEach { coord ->
                val node1 = coord.toGraphNode()

                memory.getAdjacentCoords(coord, includeDiagonal = false)
                    .filter { memory[it] == EMPTY }
                    .forEach { adjCoord ->
                        add(
                            BiDirectionalGraphEdge(
                                node1 = node1,
                                node2 = adjCoord.toGraphNode(),
                            )
                        )
                    }
            }
    }

    val path = graph.findShortestPath(start.toGraphNode(), exit.toGraphNode())
    return path.map { it.coord }
}

fun findFirstBlockingByte(bytes: List<Coord>, size: Int = 70): Coord {
    for (i in 1..bytes.size) {
        val memory = buildMemory(bytes.take(i), size)
        val path = findExitPath(memory)
        if (path.isEmpty()) {
            return bytes[i - 1]
        }
    }
    throw IllegalStateException("No blocking byte found")
}

fun drawMemory(memory: Map<Coord, Char>): String = drawPath(memory, emptyList())

fun drawPath(memory: Map<Coord, Char>, path: List<Coord>): String =
    memory.to2DStringOfStrings { coord, c ->
        if (coord in path) {
            AsciiColor.WHITE.format(PATH)
        } else if (c == CORRUPTED) {
            AsciiColor.RED.format(CORRUPTED)
        } else if (c == EMPTY) {
            AsciiColor.BRIGHT_BLACK.format(EMPTY)
        } else {
            throw IllegalArgumentException("Unknown memory value $c at $coord")
        }
    }
