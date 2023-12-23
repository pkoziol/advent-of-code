package biz.koziolek.adventofcode.year2022.day16

import biz.koziolek.adventofcode.*
import java.util.Comparator
import java.util.PriorityQueue
import java.util.Queue

fun main() {
    val inputFile = findInput(object {})
    val valvesGraph = parseValvesGraph(inputFile.bufferedReader().readLines())
    val timeLimit = 30
    val bestPath = findBestPath(valvesGraph, timeLimit)
    val potentialPressure = bestPath.potentialPressure(timeLimit - bestPath.time)

    println("Best path is: $bestPath with total pressure released: $potentialPressure")
}

const val TIME_PER_DISTANCE = 1
const val TIME_TO_OPEN = 1

data class Valve(
    override val id: String,
    val flowRate: Int
) : GraphNode {
    override fun toGraphvizString(exactXYPosition: Boolean, xyPositionScale: Float) = id
}

fun parseValvesGraph(lines: List<String>): Graph<Valve, UniDirectionalGraphEdge<Valve>> =
    buildGraph {
        val regex = Regex("Valve ([a-zA-Z0-9]+) has flow rate=([0-9]+); tunnels? leads? to valves? (.*)$")
        val valves = lines
            .mapNotNull { regex.find(it) }
            .map {
                Valve(
                    id = it.groups[1]!!.value,
                    flowRate = it.groups[2]!!.value.toInt(),
                )
            }
            .associateBy { it.id }

        lines
            .mapNotNull { regex.find(it) }
            .flatMap { result ->
                val currentId = result.groups[1]!!.value
                val currentValve = valves[currentId] ?: throw IllegalStateException("Valve '$currentId' not found")
                val neighbors = result.groups[3]!!.value.split(',')
                    .map { it.trim() }
                    .map { valves[it] ?: throw IllegalStateException("Valve '$it' not found") }

                neighbors.map { UniDirectionalGraphEdge(currentValve, it) }
            }
            .forEach { add(it) }
    }

fun buildDistanceMatrix(valvesGraph: Graph<Valve, UniDirectionalGraphEdge<Valve>>): Map<Valve, Map<Valve, Int>> =
    valvesGraph.nodes.associateWith { srcValve ->
        valvesGraph.nodes.associateWith { dstValve ->
            valvesGraph.findShortestPath(srcValve, dstValve).size - 1
        }
    }

fun findBestPath(valvesGraph: Graph<Valve, UniDirectionalGraphEdge<Valve>>, timeLimit: Int): ValvePath =
    generatePaths(valvesGraph, timeLimit)
        .maxByOrNull { it.potentialPressure(timeLimit - it.time) }!!

fun generatePaths(valvesGraph: Graph<Valve, UniDirectionalGraphEdge<Valve>>, timeLimit: Int): Sequence<ValvePath> =
    sequence {
//        val distanceMatrix = buildDistanceMatrix(valvesGraph)
        val toCheck: Queue<Pair<ValvePath, Int>> = PriorityQueue(
            Comparator.comparing { (path, timeLeft) -> -path.potentialPressure(timeLeft) }
        )

        toCheck.add(
            Pair(
                ValvePath(
                    valvesGraph.nodes.single { it.id == "AA" },
                    open = false
                ),
                timeLimit,
            )
        )

        while (toCheck.isNotEmpty()) {
//            println("--------------------------------------------------------------------------------")

            val (currentPath, timeLeft) = toCheck.poll()

            if (timeLeft <= 0) {
                yield(currentPath)
                continue
            }
            
            val srcValve = currentPath.lastValve

//            val currentPotentialPressure = currentPath.potentialPressure(timeLeft)
//            println("Time left: $timeLeft")
//            println("Current: $currentPath (${currentPath.releasedPressure}/$currentPotentialPressure)")
            
            val unopenedValves = valvesGraph.nodes
                .filter { dstValve -> dstValve.flowRate > 0 && !currentPath.isOpened(dstValve) }

            if (unopenedValves.isEmpty()) {
                yield(currentPath)
                continue
            }

            val newPaths = unopenedValves.flatMap { dstValve ->
                val newPathFragment = valvesGraph.findShortestPath(srcValve, dstValve)
//                val distance = distanceMatrix[srcValve]!![dstValve]!!
                val distance = newPathFragment.size - 1
                val newTimeLeft = timeLeft - distance * TIME_PER_DISTANCE - TIME_TO_OPEN
//                val newPotentialPressure = dstValve.flowRate * newTimeLeft
//                println("  ${dstValve.id}: potential pressure: $currentPotentialPressure + $newPotentialPressure")

                if (newTimeLeft > 0) {
                    listOf(
                        Pair(
                            newPathFragment.drop(1).fold(currentPath) { newPath, valve ->
                                newPath.addValve(valve, open = valve == dstValve)
                            },
                            newTimeLeft,
                        )
                    )
                } else {
                    emptyList()
                }
            }

            if (newPaths.isNotEmpty()) {
                toCheck.addAll(newPaths)
            } else {
                yield(currentPath)
            }
        }
    }


data class ValvePath(
    private val path: List<Pair<Valve, Boolean>>
) {
    constructor(valve: Valve, open: Boolean) : this(listOf(valve to open))

    val time = path.fold(-1) { time, (_, open) -> time + TIME_PER_DISTANCE + if (open) TIME_TO_OPEN else 0 }
    val lastValve = path.last().first
    val releasedPressure: Int
        get() {
            val (released, _) = calculatePressure()
            return released
        }

    fun potentialPressure(timeLeft: Int): Int {
        val (released, lastTotalFlowRate) = calculatePressure()
        return released + lastTotalFlowRate * timeLeft
    }

    private fun calculatePressure() =
        path.fold(0 to 0) { (totalPressure, flowRate), (valve, open) ->
            when {
                open -> Pair(
                    totalPressure + flowRate * (TIME_PER_DISTANCE + TIME_TO_OPEN),
                    flowRate + valve.flowRate
                )
                else -> Pair(
                    totalPressure + flowRate * TIME_PER_DISTANCE,
                    flowRate
                )
            }
        }

    fun isOpened(valve: Valve) = path.any { it.first == valve && it.second }

    fun addValve(valve: Valve, open: Boolean) =
        if (open && isOpened(valve)) {
            throw IllegalStateException("Valve ${valve.id} is already opened")
        } else if (open && valve.flowRate == 0) {
            throw IllegalArgumentException("Valve ${valve.id} is zero flow rate - why would you waste time opening it?!")
        } else {
            copy(path = path + (valve to open))
        }

    override fun toString() =
        path.joinToString("->") { (valve, open) ->
            if (open) {
                "(${valve.id})"
            } else {
                valve.id
            }
        }
}
