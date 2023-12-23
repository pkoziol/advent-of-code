package biz.koziolek.adventofcode.year2022.day12

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})
    val heightMap = parseHeightMap(inputFile.bufferedReader().readLines())
    val elevationGraph = buildElevationGraph(heightMap)
    println("Fewest steps from S to E: ${findFewestStepsFromStartToEnd(elevationGraph)}")
    println("Fewest steps from any a to E: ${findFewestStepsFromZeroToEnd(elevationGraph)}")
}

fun parseHeightMap(lines: Iterable<String>): Map<Coord, Char> =
    lines.parse2DMap().toMap()

data class ElevationNode(
    val label: Char,
    val height: Int,
    val coord: Coord,
) : GraphNode {
    override val id = "${label}__${coord.x}_${coord.y}"
    override fun toGraphvizString(exactXYPosition: Boolean, xyPositionScale: Float): String = id
}

fun buildElevationGraph(heightMap: Map<Coord, Char>): Graph<ElevationNode, UniDirectionalGraphEdge<ElevationNode>> =
    buildGraph {
        heightMap.forEach { (coord, label) ->
            val currentHeight = labelToHeight(label)
            val currentNode = ElevationNode(label, currentHeight, coord)

            heightMap.getAdjacentCoords(coord, includeDiagonal = false)
                .map { adjCoord ->
                    val adjLabel = heightMap[adjCoord]!!
                    val adjHeight = labelToHeight(adjLabel)
                    ElevationNode(adjLabel, adjHeight, adjCoord)
                }
                .filter { it.height <= currentHeight + 1 }
                .forEach { add(UniDirectionalGraphEdge(currentNode, it)) }
        }
    }

fun labelToHeight(label: Char): Int =
    when (label) {
        'S' -> labelToHeight('a')
        'E' -> labelToHeight('z')
        in 'a'..'z' -> label - 'a'
        else -> throw IllegalArgumentException("Unexpected heightMap label: '$label'")
    }

fun findFewestStepsFromStartToEnd(elevationGraph: Graph<ElevationNode, UniDirectionalGraphEdge<ElevationNode>>): Int {
    val start = elevationGraph.nodes.find { it.label == 'S' }
        ?: throw IllegalStateException("Start not found")
    val end = elevationGraph.nodes.find { it.label == 'E' }
        ?: throw IllegalStateException("End not found")
    val shortestPath = elevationGraph.findShortestPath(start, end)

    return shortestPath.size - 1
}

fun findFewestStepsFromZeroToEnd(elevationGraph: Graph<ElevationNode, UniDirectionalGraphEdge<ElevationNode>>): Int {
    val starts = elevationGraph.nodes.filter { it.height == 0 }.toSet()
    val end = elevationGraph.nodes.find { it.label == 'E' }
        ?: throw IllegalStateException("End not found")
    val shortestPath = elevationGraph.findShortestPath(starts, end)

    return shortestPath.size - 1
}
