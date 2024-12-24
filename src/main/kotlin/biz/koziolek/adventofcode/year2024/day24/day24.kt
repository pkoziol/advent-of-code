package biz.koziolek.adventofcode.year2024.day24

import biz.koziolek.adventofcode.GraphNode
import biz.koziolek.adventofcode.UniDirectionalGraphEdge
import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val (initValues, gateConnections) = parseGates(inputFile.bufferedReader().readLines())
    val values = evaluate(gateConnections, initValues)
    val number = getNumber(values)
    println("The number is $number")
}

enum class Operation {
    AND, OR, XOR,
}

data class GateConnection(
    val input1: String,
    val input2: String,
    val operation: Operation,
    val output: String,
) {
    fun input1IsKnown(values: Map<String, Boolean>) = values[input1] != null

    fun input2IsKnown(values: Map<String, Boolean>) = values[input2] != null

    fun getOutput(values: Map<String, Boolean>) =
        when (operation) {
            Operation.AND -> values[input1]!! and values[input2]!!
            Operation.OR -> values[input1]!! or values[input2]!!
            Operation.XOR -> values[input1]!! xor values[input2]!!
        }
}

fun parseGates(lines: Iterable<String>): Pair<Map<String, Boolean>, List<GateConnection>> {
    val initValues = lines
        .takeWhile { it.isNotBlank() }
        .map { it.split(": ") }
        .associate { it[0] to (it[1] == "1") }

    val gateConnections = lines
        .dropWhile { it.isNotBlank() }
        .drop(1)
        .map { it.split(" ") }
        .map {
            GateConnection(
                input1 = it[0],
                input2 = it[2],
                operation = Operation.valueOf(it[1]),
                output = it[4],
            )
        }

    return initValues to gateConnections
}

fun evaluate(gateConnections: List<GateConnection>, initValues: Map<String, Boolean>): Map<String, Boolean> {
    val values = initValues.toMutableMap()

    val toCompute = gateConnections
        .map { it.output }
        .filter { it[0] == 'z' }
        .filter { !values.containsKey(it) }
        .toMutableList()

    val connectionsByOut = gateConnections
        .groupingBy { it.output }
        .aggregate { key, _: GateConnection?, element, first ->
            if (first) {
                element
            } else {
                throw IllegalStateException("Multiple connections for $key")
            }
        }

    while (toCompute.isNotEmpty()) {
        val gate = toCompute.last()

        if (values[gate] != null) {
            toCompute.removeLast()
            continue
        }

        val conn = connectionsByOut[gate]
            ?: throw IllegalStateException("No connection for $gate")

        if (conn.input1IsKnown(values)) {
            if (conn.input2IsKnown(values)) {
                values[gate] = conn.getOutput(values)
                toCompute.removeLast()
            } else {
                toCompute.add(conn.input2)
            }
        } else {
            toCompute.add(conn.input1)
        }
    }

    return values
}

fun getNumber(values: Map<String, Boolean>): Long {
    return values
        .filterKeys { it.startsWith("z") }
        .entries
        .sortedByDescending { it.key }
        .map { if (it.value) 1L else 0L }
        .fold(0L) { acc, i -> acc * 2 + i }
}

fun getAnswerPart2(swaps: List<Pair<String, String>>) =
    swaps.flatMap { listOf(it.first, it.second) }
        .sorted()
        .joinToString(",")

fun swapOutputs(first: String, second: String, gateConnections: List<GateConnection>): List<GateConnection> {
    val newConnections = gateConnections.toMutableList()
    val firstConn = newConnections.single { it.output == first }
    val secondConn = newConnections.single { it.output == second }

    newConnections.remove(firstConn)
    newConnections.remove(secondConn)

    newConnections.add(firstConn.copy(output = second))
    newConnections.add(secondConn.copy(output = first))

    return newConnections
}

fun buildGraph(gateConnections: List<GateConnection>) =
    biz.koziolek.adventofcode.buildGraph {
        gateConnections.forEach { conn ->
            add(createEdge(conn.input1, conn, gateConnections))
            add(createEdge(conn.input2, conn, gateConnections))
        }
    }

private fun createEdge(from: String, to: GateConnection, connections: List<GateConnection>): UniDirectionalGraphEdge<GateNode> {
    val fromConn = connections.singleOrNull { it.output == from }
    val color = when {
        fromConn != null && isXXorY(fromConn) && isZFromXor(to) -> "red"
        fromConn != null && isXAndY(fromConn) -> "blue"
        else -> "black"
    }
    return UniDirectionalGraphEdge(ValueNode(from), ConnectionNode(to), color = color)
}

interface GateNode : GraphNode

private data class ValueNode(override val id: String) : GateNode {
    override fun toGraphvizString(exactXYPosition: Boolean, xyPositionScale: Float) = id
}

private data class ConnectionNode(val conn: GateConnection) : GateNode {
    override val id: String
        get() = conn.output

    override fun toGraphvizString(exactXYPosition: Boolean, xyPositionScale: Float): String {
        val color = when {
            isXXorY(conn) -> "red"
            isZFromXor(conn) -> "red"
            isZOut(conn) -> "orange"
            isXAndY(conn) -> "blue"
            else -> "black"
        }
        return "\"$id\" [label=\"${conn.output} = ${conn.input1} ${conn.operation} ${conn.input2}\" color=$color]"
    }
}

private fun isXXorY(conn: GateConnection) =
    (conn.input1[0] == 'x' || conn.input1[0] == 'y')
            && (conn.input2[0] == 'x' || conn.input2[0] == 'y')
            && conn.operation == Operation.XOR

private fun isXAndY(conn: GateConnection) =
    (conn.input1[0] == 'x' || conn.input1[0] == 'y')
            && (conn.input2[0] == 'x' || conn.input2[0] == 'y')
            && conn.operation == Operation.AND

private fun isZFromXor(conn: GateConnection) =
    conn.output[0] == 'z' && conn.operation == Operation.XOR

private fun isZOut(conn: GateConnection) =
    conn.output[0] == 'z'
