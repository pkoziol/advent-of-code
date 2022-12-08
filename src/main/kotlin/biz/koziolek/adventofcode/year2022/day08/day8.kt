package biz.koziolek.adventofcode.year2022.day08

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val trees = parseTrees(inputFile.bufferedReader().readLines())
    println("Trees visible from edges: ${countVisibleTreesFromEdges(trees)}")
    println("Highest scenic score: ${findHighestScenicScore(trees)}")
}

fun parseTrees(lines: List<String>): Map<Coord, Int> =
    lines.flatMapIndexed { y, line ->
        line.mapIndexed { x, char ->
            Coord(x, y) to char.toString().toInt()
        }
    }.toMap()

fun countVisibleTreesFromEdges(trees: Map<Coord, Int>) =
    trees.count { (treeCoord, treeHeight) ->
        listOf(
            getNorthTrees(trees, treeCoord),
            getEastTrees(trees, treeCoord),
            getSouthTrees(trees, treeCoord),
            getWestTrees(trees, treeCoord),
        ).any { otherTrees ->
            isVisible(treeHeight, otherTrees.map { it.value })
        }
    }

fun isVisible(checkedTree: Int, otherTrees: List<Int>) =
    otherTrees.all { it < checkedTree }

fun <T> getNorthTrees(trees: Map<Coord, T>, start: Coord): List<Map.Entry<Coord, T>> =
    trees.filter { it.key.x == start.x && it.key.y < start.y }
        .entries
        .sortedByDescending { it.key.y }

fun <T> getEastTrees(trees: Map<Coord, T>, start: Coord): List<Map.Entry<Coord, T>> =
    trees.filter { it.key.y == start.y && it.key.x > start.x }
        .entries
        .sortedBy { it.key.x }

fun <T> getSouthTrees(trees: Map<Coord, T>, start: Coord): List<Map.Entry<Coord, T>> =
    trees.filter { it.key.x == start.x && it.key.y > start.y }
        .entries
        .sortedBy { it.key.y }

fun <T> getWestTrees(trees: Map<Coord, T>, start: Coord): List<Map.Entry<Coord, T>> =
    trees.filter { it.key.y == start.y && it.key.x < start.x }
        .entries
        .sortedByDescending { it.key.x }

fun findHighestScenicScore(trees: Map<Coord, Int>) =
    trees.maxOf { getScenicScore(trees, it.key) }

fun getScenicScore(trees: Map<Coord, Int>, checkedCoord: Coord): Int =
    listOf(
        getNorthTrees(trees, checkedCoord),
        getEastTrees(trees, checkedCoord),
        getSouthTrees(trees, checkedCoord),
        getWestTrees(trees, checkedCoord),
    )
        .map { otherTrees ->
            getViewingDistance(trees[checkedCoord]!!, otherTrees.map { it.value })
        }
        .reduce(Int::times)

fun getViewingDistance(checkedTree: Int, otherTrees: List<Int>): Int {
    val shorterTrees = otherTrees.takeWhile { it < checkedTree }.count()
    val blockTree = if (shorterTrees < otherTrees.size) 1 else 0
    return shorterTrees + blockTree
}
