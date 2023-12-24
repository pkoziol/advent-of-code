package biz.koziolek.adventofcode.year2023.day24

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})
    val hails = parseHails(inputFile.bufferedReader().readLines())
    println("Hails intersect ${countFutureIntersections(hails, testArea)} times in future in the test area")
}

val testArea = LongCoord(200_000_000_000_000, 200_000_000_000_000) to LongCoord(400_000_000_000_000, 400_000_000_000_000)

data class Hail(val position: LongCoord3d, val velocity: Coord3d, val time: Long = 1) {
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
    hails
        .flatMapIndexed { index1, hail1 ->
            hails.mapIndexedNotNull { index2, hail2 ->
                if (index2 > index1) {
                    hail1 to hail2
                } else {
                    null
                }
            }
        }
        .count { (hail1, hail2) ->
            val intersection = findIntersection(hail1, hail2)
            intersection != null
                    && intersection.time1 > 0
                    && intersection.time2 > 0
                    && testArea.contains(intersection.position)
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

    try {
//        val timeX = (first.position.x.toDouble() - second.position.x) / (second.velocity.x.toDouble() - first.velocity.x)
//        val timeY = (first.position.y.toDouble() - second.position.y) / (second.velocity.y.toDouble() - first.velocity.y)
        val a1 = first.velocity.y / first.velocity.x.toDouble()
        val b1 = first.position.y - a1 * first.position.x
        val a2 = second.velocity.y / second.velocity.x.toDouble()
        val b2 = second.position.y - a2 * second.position.x

        val x = (b1 - b2) / (a2 - a1)
        val y = a1 * x + b1

        val time1 = (x - first.position.x) / first.velocity.x.toDouble()
        val time2 = (x - second.position.x) / second.velocity.x.toDouble()

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
