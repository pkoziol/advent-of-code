package biz.koziolek.adventofcode

data class Coord(val x: Int, val y: Int) {
    operator fun plus(other: Coord) = Coord(x + other.x, y + other.y)
    operator fun minus(other: Coord) = Coord(x - other.x, y - other.y)
    operator fun unaryMinus() = Coord(-x, -y)
}

fun <T> Map<Coord, T>.getWidth() = keys.maxOfOrNull { it.x }?.plus(1) ?: 0

fun <T> Map<Coord, T>.getHeight() = keys.maxOfOrNull { it.y }?.plus(1) ?: 0

fun <T> Map<Coord, T>.getAdjacentCoords(coord: Coord, includeDiagonal: Boolean): Set<Coord> {
    return sequence {
        yield(Coord(-1, 0))
        yield(Coord(0, -1))
        yield(Coord(1, 0))
        yield(Coord(0, 1))

        if (includeDiagonal) {
            yield(Coord(-1, -1))
            yield(Coord(1, -1))
            yield(Coord(-1, 1))
            yield(Coord(1, 1))
        }
    }
        .map { coord + it }
        .filter { containsKey(it) }
        .toSet()
}

fun Map<Coord, Int>.toGraph(includeDiagonal: Boolean): Graph<CoordNode, UniDirectionalGraphEdge<CoordNode>> =
    buildGraph {
        this@toGraph.forEach { (coord, value) ->
            val node2 = coord.toGraphNode()

            getAdjacentCoords(coord, includeDiagonal)
                .forEach { adjCoord ->
                    add(UniDirectionalGraphEdge(
                        node1 = adjCoord.toGraphNode(),
                        node2 = node2,
                        weight = value,
                    ))
                }
        }
    }

fun Coord.toGraphNode() = CoordNode(this)

data class CoordNode(val coord: Coord) : GraphNode {

    override val id = "x${coord.x}_y${coord.y}"

    override fun toGraphvizString() = id
}
