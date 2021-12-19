package biz.koziolek.adventofcode.year2021.day19

import biz.koziolek.adventofcode.*
import kotlin.math.absoluteValue

fun main() {
    val inputFile = findInput(object {})
    val lines = inputFile.bufferedReader().readLines()

    val scanners = parseScannerReport(lines)

    val allBeacons = findAllBeaconsRelativeTo0(scanners)
    println("There are ${allBeacons.size} beacons")

    val maxDistance = findMaxScannerDistance(scanners)
    println("Max distance between two scanners is: $maxDistance")
}

fun parseScannerReport(lines: Iterable<String>): List<RandomScanner> =
    buildList {
        var beacons: MutableSet<Coord3d>? = null

        for (line in lines) {
            if (line.startsWith("--- scanner")) {
                if (beacons != null) {
                    add(RandomScanner(beacons))
                }
                beacons = mutableSetOf()
            } else if (line.isNotBlank()) {
                beacons?.add(Coord3d.fromString(line))
            }
        }

        if (beacons != null) {
            add(RandomScanner(beacons))
        }
    }

interface Scanner {
    val beacons: Set<Coord3d>

    fun distanceToAllBeacons(from: Coord3d): Set<Double> =
        beacons.map { from.distanceTo(it) }.toSet()
}

data class RandomScanner(override val beacons: Set<Coord3d>) : Scanner {

    fun normalize(scannerRotation: Rotation, scannerCoord: Coord3d): NormalizedScanner {
        return NormalizedScanner(
            position = scannerCoord,
            beacons = beacons.map { it.rotate(scannerRotation) + scannerCoord }.toSet(),
        )
    }
}

data class NormalizedScanner(val position: Coord3d, override val beacons: Set<Coord3d>) : Scanner

fun findMaxScannerDistance(scanners: List<RandomScanner>): Int {
    val normalizedScanners = matchScanners(scanners)
    
    return sequence {
        for (scannerA in normalizedScanners) {
            for (scannerB in normalizedScanners) {
                yield(scannerA to scannerB)
            }
        }
    }.maxOf { it.first.position.manhattanDistanceTo(it.second.position) }
}

fun findAllBeaconsRelativeTo0(scanners: List<RandomScanner>): Set<Coord3d> =
    matchScanners(scanners)
        .flatMap { it.beacons }
        .toSet()

fun matchScanners(scanners: List<RandomScanner>): List<NormalizedScanner> {
    val normalizedScanners: MutableList<NormalizedScanner?> = scanners.map { null }.toMutableList()
    normalizedScanners[0] = scanners[0].normalize(Rotation("+x", "+y", "+z"), Coord3d(0, 0, 0))

    while (normalizedScanners.any { it == null }) {
        var matchedAnythingThisIteration = false

        for ((scannerToMatchIndex, scannerToMatch) in scanners.withIndex()) {
            if (normalizedScanners[scannerToMatchIndex] != null) {
                // Already matched
                continue
            }

            for (normalizedScanner in normalizedScanners) {
                if (normalizedScanner == null) {
                    // Not yet matched and normalized
                    continue
                }

                val sameCoords = findSameCoords(normalizedScanner, scannerToMatch)

                if (sameCoords.isNotEmpty()) {
                    val rotation = findSecondScannerRotation(sameCoords)
                    val scannerCenter = findSecondScannerCoordRelativeToFirst(sameCoords)
                    normalizedScanners[scannerToMatchIndex] = scannerToMatch.normalize(rotation, scannerCenter)

                    matchedAnythingThisIteration = true
                    break
                }
            }
        }

        if (!matchedAnythingThisIteration) {
            val stillUnmatchedIndexes = scanners.indices.filter { index -> normalizedScanners[index] == null }
            throw IllegalStateException("Cannot match scanners: $stillUnmatchedIndexes")
        }
    }

    return normalizedScanners.filterNotNull()
}

fun findSameCoords(scannerA: Scanner, scannerB: Scanner, minInCommon: Int = 12): Map<Coord3d, Coord3d> {
    val distancesA = scannerA.beacons.associateWith { scannerA.distanceToAllBeacons(it) }
    val distancesB = scannerB.beacons.associateWith { scannerB.distanceToAllBeacons(it) }

    return buildMap {
        for ((beaconA, thisDistancesA) in distancesA.entries) {
            for ((beaconB, thisDistancesB) in distancesB.entries) {
                val inCommon = thisDistancesA.intersect(thisDistancesB)
                if (inCommon.size >= minInCommon) {
                    put(beaconA, beaconB)
                }
            }
        }
    }
}

fun findSecondScannerCoordRelativeToFirst(sameCoords: Map<Coord3d, Coord3d>): Coord3d {
    val scannerBRotation = findSecondScannerRotation(sameCoords)
    return sameCoords
        .map { (a, b) -> a to b.rotate(scannerBRotation) }
        .map { it.first - it.second }
        .distinct()
        .single()
}

private fun Coord3d.rotate(rotation: Rotation) = Coord3d(
    x = rotateSingle(this, rotation.x),
    y = rotateSingle(this, rotation.y),
    z = rotateSingle(this, rotation.z),
)

private fun rotateSingle(coord3d: Coord3d, singleRotation: String): Int =
    when (singleRotation) {
        "+x" -> coord3d.x
        "-x" -> -coord3d.x
        "+y" -> coord3d.y
        "-y" -> -coord3d.y
        "+z" -> coord3d.z
        "-z" -> -coord3d.z
        else -> throw IllegalArgumentException("Unknown rotation: $singleRotation")
    }

private fun findSingleRotation(singleAxisValue: Int, otherCoord: Coord3d): String =
    when (singleAxisValue) {
        otherCoord.x -> "+x"
        -otherCoord.x -> "-x"
        otherCoord.y -> "+y"
        -otherCoord.y -> "-y"
        otherCoord.z -> "+z"
        -otherCoord.z -> "-z"
        else -> throw IllegalArgumentException("Cannot find single rotation - $singleAxisValue was not found in $otherCoord")
    }

data class Rotation(val x: String, val y: String, val z: String)

fun findSecondScannerRotation(sameCoords: Map<Coord3d, Coord3d>): Rotation =
    sameCoords.entries.asSequence()
        .zipWithNext()
        .map { (prev, next) ->
            val (prevA, prevB) = prev
            val (nextA, nextB) = next

            val vectorA = prevA - nextA
            val vectorB = prevB - nextB

            vectorA to vectorB
        }
        .filter { (vectorA, vectorB) ->
            val absValuesA = setOf(vectorA.x.absoluteValue, vectorA.y.absoluteValue, vectorA.z.absoluteValue)
            val absValuesB = setOf(vectorB.x.absoluteValue, vectorB.y.absoluteValue, vectorB.z.absoluteValue)
            absValuesA == absValuesB
        }
        .map { (vectorA, vectorB) ->
            val rotationX = findSingleRotation(vectorA.x, vectorB)
            val rotationY = findSingleRotation(vectorA.y, vectorB)
            val rotationZ = findSingleRotation(vectorA.z, vectorB)

            Rotation(rotationX, rotationY, rotationZ)
        }
        .groupBy { it }
        .map { it.key to it.value.size }
        .sortedByDescending { it.second }
        .map { it.first }
        .take(1)
        .first()
