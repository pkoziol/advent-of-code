package biz.koziolek.adventofcode.year2024.day18

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})
    val bytes = parseFallingBytes(inputFile.bufferedReader().readLines())
    val memory = Memory.fromBytes(bytes.take(1024))
    val path = findExitPath(memory)
    println("Steps to exit after 1kB: ${path.size - 1}")
}

const val EMPTY = '.'
const val CORRUPTED = '#'
const val PATH = 'O'

fun parseFallingBytes(lines: Iterable<String>): List<Coord> =
    lines.map { Coord.fromString(it) }

data class Memory(val contents: Map<Coord, Char>,
                  val graph: Graph<CoordNode, BiDirectionalGraphEdge<CoordNode>>) {
    fun getWidth(): Int = contents.getWidth()
    fun getHeight(): Int = contents.getHeight()

    fun update(byte: Coord) =
        copy(
            contents = contents + (byte to CORRUPTED),
            graph = graph.removeNodesWithoutSplit(setOf(CoordNode(byte)))
        )

    override fun toString(): String = toString(emptyList())

    fun toString(path: List<Coord>): String =
        contents.to2DStringOfStrings { coord, c ->
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


    companion object {
        fun fromBytes(bytes: List<Coord>, size: Int = 70): Memory {
            val contents = buildContents(bytes, size)
            val graph = buildGraph(contents)
            return Memory(contents, graph)
        }

        private fun buildContents(bytes: List<Coord>, size: Int = 70): Map<Coord, Char> =
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

        private fun buildGraph(contents: Map<Coord, Char>) =
            buildGraph {
                contents.keys
                    .filter { contents[it] == EMPTY }
                    .forEach { coord ->
                        val node1 = coord.toGraphNode()

                        contents.getAdjacentCoords(coord, includeDiagonal = false)
                            .filter { contents[it] == EMPTY }
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
    }
}

fun findExitPath(memory: Memory): List<Coord> {
    val start = Coord(0, 0)
    val exit = Coord(memory.getWidth() - 1, memory.getHeight() - 1)
    val path = memory.graph.findShortestPath(start.toGraphNode(), exit.toGraphNode())
    return path.map { it.coord }
}

fun findFirstBlockingByte(bytes: List<Coord>, size: Int = 70): Coord {
    var memory = Memory.fromBytes(emptyList(), size)
    for (i in bytes.indices) {
        memory = memory.update(bytes[i])
        val path = findExitPath(memory)
        if (path.isEmpty()) {
            return bytes[i]
        }
    }
    throw IllegalStateException("No blocking byte found")
}
