package biz.koziolek.adventofcode.year2024.day23

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val connections = parseComputerConnections(inputFile.bufferedReader().readLines())
    val triples = findTriples(connections)
    val answer = countTs(triples)
    println("$answer three inter-connected computers contain one starting with T")
}

fun parseComputerConnections(lines: Iterable<String>): List<Pair<String, String>> =
    lines
        .map { it.split("-") }
        .map { it[0] to it[1] }

fun findTriples(connections: List<Pair<String, String>>): Set<Triple<String, String, String>> {
    val map = connections
        .flatMap { listOf(it, it.second to it.first) }
        .groupBy(
            keySelector = { it.first },
            valueTransform = { it.second },
        )

    return map.entries
        .flatMap { (comp1, conns1) ->
            conns1
                .flatMap { comp2 ->
                    val conns2 = map[comp2]!!
                    conns2
                        .filter { comp3 -> comp3 != comp1 && comp3 in conns1 }
                        .map { comp3 ->
                            val sorted = listOf(comp1, comp2, comp3).sorted()
                            Triple(sorted[0], sorted[1], sorted[2])
                        }
                }
        }
        .toSet()
}

fun countTs(triples: Set<Triple<String, String, String>>): Int =
    triples.count { it.first[0] == 't' || it.second[0] == 't' || it.third[0] == 't' }
