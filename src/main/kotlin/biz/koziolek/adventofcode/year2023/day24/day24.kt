package biz.koziolek.adventofcode.year2023.day24

import biz.koziolek.adventofcode.*
import java.time.LocalDateTime

fun main() {
    val inputFile = findInput(object {})
    val hails = parseHails(inputFile.bufferedReader().readLines())
    println("Hails intersect ${countFutureIntersections(hails, testArea)} times in future in the test area")
}

val testArea = LongCoord(200_000_000_000_000, 200_000_000_000_000) to LongCoord(400_000_000_000_000, 400_000_000_000_000)

data class Hail(val position: LongCoord3d,
                val velocity: Coord3d,
                val velocityDelta: Coord3d = Coord3d(0, 0, 0)) {
    override fun toString() = "$position @ $velocity"
}

data class Intersection(val position: DoubleCoord, val time1: Double, val time2: Double)

private fun Pair<LongCoord, LongCoord>.contains(coord: DoubleCoord): Boolean =
    coord.x >= first.x
            && coord.x <= second.x
            && coord.y >= first.y
            && coord.y <= second.y

fun parseHails(lines: Iterable<String>): List<Hail> =
    lines.map { line ->
        val (positionStr, velocityStr) = line.split('@')
        Hail(
            position = LongCoord3d.fromString(positionStr),
            velocity = Coord3d.fromString(velocityStr),
        )
    }

fun countFutureIntersections(hails: List<Hail>, testArea: Pair<LongCoord, LongCoord>): Int =
    generatePairs(hails)
        .count { (hail1, hail2) ->
            val intersection = findIntersection(hail1, hail2)
            intersection != null
                    && intersection.time1 > 0
                    && intersection.time2 > 0
                    && testArea.contains(intersection.position)
        }

private fun generatePairs(hails: List<Hail>): Sequence<Pair<Hail, Hail>> =
    sequence {
        hails.forEachIndexed { index1, hail1 ->
            hails.forEachIndexed { index2, hail2 ->
                if (index2 > index1) {
                    yield(hail1 to hail2)
                }
            }
        }
    }

fun findIntersection(first: Hail, second: Hail): Intersection? {
    // x1 + vx1 * time = x2 + vx2 * time
    // y1 + vy1 * time = y2 + vy2 * time

    // (x1 - x2) / (vx2 - vx1) = time
    // (y1 - y2) / (vy2 - vy1) = time

    // ----------------------------------------

    // y = ax + b
    // a1 = vy1 / vx1
    // b1 = y1 - a1x1

    // a1x + b1 = a2x + b2
    // x = (b1 - b2) / (a2 - a1)

    // x1 + vx1 * time1 = x + vx * time1
    // y1 + vy1 * time1 = y + vy * time1
    // z1 + vz1 * time1 = z + vz * time1

    // x2 + vx2 * time2 = x + vx * time2
    // y2 + vy2 * time2 = y + vy * time2
    // z2 + vz2 * time2 = z + vz * time2

    // x3 + vx3 * time3 = x + vx * time3
    // y3 + vy3 * time3 = y + vy * time3
    // z3 + vz3 * time3 = z + vz * time3

    try {
//        val timeX = (first.position.x.toDouble() - second.position.x) / (second.velocity.x.toDouble() - first.velocity.x)
//        val timeY = (first.position.y.toDouble() - second.position.y) / (second.velocity.y.toDouble() - first.velocity.y)
        val a1 = (first.velocity.y + first.velocityDelta.y) / (first.velocity.x.toDouble() + first.velocityDelta.x)
        val b1 = first.position.y - a1 * first.position.x
        val a2 = (second.velocity.y + second.velocityDelta.y) / (second.velocity.x.toDouble() + second.velocityDelta.x)
        val b2 = second.position.y - a2 * second.position.x

        val x = (b1 - b2) / (a2 - a1)
        val y = a1 * x + b1

        val time1 = (x - first.position.x) / (first.velocity.x.toDouble() + first.velocityDelta.x)
        val time2 = (x - second.position.x) / (second.velocity.x.toDouble() + second.velocityDelta.x)

        return if (x.isFinite() && y.isFinite()) {
            Intersection(
//                positionX = first.position.x + first.velocity.x * timeX,
//                positionY = first.position.y + first.velocity.y * timeY,
//                time = timeX,
                position = DoubleCoord(x, y),
                time1 = time1,
                time2 = time2,
            )
        } else {
            null
        }
    } catch (e: Exception) {
        throw RuntimeException("Error while intersecting $first and $second", e)
    }
}

fun findRockPosition(hails: List<Hail>, x: IntRange, y: IntRange, z: IntRange, logPrefix: String = ""): LongCoord3d {
    val hailsWithDifferentVectors = generateHails(hails, x, y, z)
    var index = 0L
    val points = mutableListOf<LongCoord>()

    for (newHails in hailsWithDifferentVectors) {
        val point = findCommonIntersectsXY(newHails)

        if (index % 999_000_000 == 0L) {
            println("${logPrefix}%,12d @ %s".format(index, LocalDateTime.now()))
        }

        if (point != null) {
            println("${logPrefix}Found $point at $index for dv = ${newHails.first().velocityDelta}")
            points.add(point)
        }

        index++
    }

    // TODO choose Z that matches

    println("${logPrefix}%,12d @ %s".format(index, LocalDateTime.now()))

    return LongCoord3d(0, 0, 0)
}

private fun generateHails(hails: List<Hail>, x: IntRange, y: IntRange, z: IntRange): Sequence<List<Hail>> =
    sequence {
        for (xx in x) {
            for (yy in y) {
                for (zz in z) {
                    yield(Coord3d(xx, yy, zz))
                }
            }
        }
    }
//        .sortedBy { it.distanceTo(Coord3d(0, 0, 0)) }
        .map { delta ->
            hails.map {
                Hail(
                    position = it.position,
                    velocity = it.velocity,
                    velocityDelta = delta
                )
            }
        }

private fun findCommonIntersectsXY(hails: List<Hail>): LongCoord? {
    var commonIntersection: Intersection? = null
    val intersections = mutableMapOf<Intersection, Int>()

    for ((hail1, hail2) in generatePairs(hails)) {
        val intersection = findIntersection(hail1, hail2)
        if (intersection == null) {
            continue
        }

        intersections.compute(intersection) { _, v -> v?.plus(1) ?: 1 }

        if (commonIntersection == null) {
            commonIntersection = intersection
        } else {
            // x = 354954946036320
            // y = 318916597757112
            // z = 112745502066835
            if (commonIntersection.position.distanceTo(intersection.position) > 1) {
                return null
            }
        }
    }

    return commonIntersection?.position?.let {
        LongCoord(it.x.toLong(), it.y.toLong())
    }
}
