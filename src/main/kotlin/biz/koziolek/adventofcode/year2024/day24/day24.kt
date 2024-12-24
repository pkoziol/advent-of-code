package biz.koziolek.adventofcode.year2024.day24

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
        .map { it[0] to (it[1] == "1") }
        .toMap()

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
