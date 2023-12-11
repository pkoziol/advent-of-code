package biz.koziolek.adventofcode.year2022.day08

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput
import biz.koziolek.adventofcode.parse2DMap

fun main() {
    val inputFile = findInput(object {})
    val trees = parseTrees(inputFile.bufferedReader().readLines())
    println("Trees visible from edges: ${countVisibleTreesFromEdges(trees)}")
    println("Highest scenic score: ${findHighestScenicScore(trees)}")
}

fun parseTrees(lines: List<String>): Map<Coord, Int> =
    lines.parse2DMap { it.toString().toInt() }.toMap()

fun countVisibleTreesFromEdges(trees: Map<Coord, Int>) =
    trees.count { (treeCoord, treeHeight) ->
        listOf(
            getNorthTrees(trees, treeCoord),
            getEastTrees(trees, treeCoord),
            getSouthTrees(trees, treeCoord),
            getWestTrees(trees, treeCoord),
        ).any { otherTrees ->
            isVisible(treeHeight, otherTrees.map { it.second })
        }
    }

fun isVisible(checkedTree: Int, otherTrees: List<Int>) =
    otherTrees.all { it < checkedTree }

fun <T> getNorthTrees(trees: Map<Coord, T>, start: Coord): List<Pair<Coord, T>> =
    (start.y - 1 downTo 0)
        .filter { it >= 0 }
        .map { start.copy(y = it) }
        .map { it to trees[it]!! }

fun <T> getEastTrees(trees: Map<Coord, T>, start: Coord): List<Pair<Coord, T>> =
    generateSequence(start.x + 1) { it + 1 }
        .map { start.copy(x = it) }
        .takeWhile { trees[it] != null }
        .map { it to trees[it]!! }
        .toList()

fun <T> getSouthTrees(trees: Map<Coord, T>, start: Coord): List<Pair<Coord, T>> =
    generateSequence(start.y + 1) { it + 1 }
        .map { start.copy(y = it) }
        .takeWhile { trees[it] != null }
        .map { it to trees[it]!! }
        .toList()

fun <T> getWestTrees(trees: Map<Coord, T>, start: Coord): List<Pair<Coord, T>> =
    (start.x - 1 downTo 0)
        .filter { it >= 0 }
        .map { start.copy(x = it) }
        .map { it to trees[it]!! }

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
            getViewingDistance(trees[checkedCoord]!!, otherTrees.map { it.second })
        }
        .reduce(Int::times)

fun getViewingDistance(checkedTree: Int, otherTrees: List<Int>): Int {
    val shorterTrees = otherTrees.takeWhile { it < checkedTree }.count()
    val blockTree = if (shorterTrees < otherTrees.size) 1 else 0
    return shorterTrees + blockTree
}
