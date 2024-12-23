package biz.koziolek.adventofcode.year2024.day23

import biz.koziolek.adventofcode.findInput
import java.util.Comparator
import java.util.PriorityQueue

fun main() {
    val inputFile = findInput(object {})
    val connections = parseComputerConnections(inputFile.bufferedReader().readLines())
    val triples = findTriples(connections)
    val answer = countTs(triples)
    println("$answer three inter-connected computers contain one starting with T")

    val groups = findFullyConnectedGroups(connections)
    val theBiggest = groups.maxBy { it.size }
    val password = getPassword(theBiggest)
    println("The password is $password")
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

fun findFullyConnectedGroups(connections: List<Pair<String, String>>): Set<Set<String>> {
    val map = connections
        .flatMap { listOf(it, it.second to it.first) }
        .groupBy(
            keySelector = { it.first },
            valueTransform = { it.second },
        )
        .mapValues { it.value.toSet() }

    val toCheck = PriorityQueue(Comparator.comparing<Set<String>, Int> { it.size }.reversed())
    val fullyConnected = mutableSetOf<Set<String>>()
    toCheck.addAll(connections.map { setOf(it.first, it.second) })

    var checked = 0
    var skipped = 0
    while (toCheck.isNotEmpty()) {
        val current = toCheck.poll()
        val largerIsAlreadyFullyConnected = fullyConnected.any { it.containsAll(current) }
        if (largerIsAlreadyFullyConnected) {
            skipped++
            continue
        }

        val initial = current.first().let { setOf(it) + map[it]!! }
        val commonForAll = current.fold(initial) { common, comp ->
            val x = comp.let { setOf(it) + map[it]!! }
            common.intersect(x)
        }
        checked++

        if (commonForAll == current && current.size > 2) {
            fullyConnected.add(current)
            continue
        }

        for (comp in commonForAll) {
            val new = current + comp
            if (new.size == current.size + 1) {
                toCheck.add(new)
            }
        }
    }

    println("Finished finding groups, checked $checked, skipped $skipped, fully connected ${fullyConnected.size}")
    println("Summary:")
    fullyConnected
        .groupingBy { it.size }
        .eachCount()
        .entries
        .sortedByDescending { it.key }
        .forEach { println("${it.key} computers in ${it.value} groups") }

    return fullyConnected
}

fun getPassword(group: Set<String>): String =
    group.sorted().joinToString(",")
