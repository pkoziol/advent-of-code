package biz.koziolek.adventofcode.year2022.day15

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val sensors = parseSensors(inputFile.bufferedReader().readLines())

    println("Positions with y=2000000 without beacons: ${countPositionsWithoutBeacons(sensors, y = 2000000)}")

    val distressBeacon = findDistressBeacon(sensors, maxX = 4000000, maxY = 4000000)
    val tuningFrequency = getTuningFrequency(distressBeacon)
    println("Distress beacon is located at $distressBeacon with tuning frequency: $tuningFrequency")
}

data class Sensor(
    val location: Coord,
    val beacon: Beacon,
) {
    val distanceToBeacon = location.manhattanDistanceTo(beacon.location)
}

data class Beacon(
    val location: Coord,
)

fun parseSensors(lines: Iterable<String>): List<Sensor> =
    lines.map { line ->
        Regex("Sensor at x=([0-9-]+), y=([0-9-]+): closest beacon is at x=([0-9-]+), y=([0-9-]+)")
            .find(line)
            .takeIf { it != null }
            .let { it as MatchResult }
            .let { result ->
                Sensor(
                    location = Coord(
                        x = result.groups[1]!!.value.toInt(),
                        y = result.groups[2]!!.value.toInt(),
                    ),
                    beacon = Beacon(
                        location = Coord(
                            x = result.groups[3]!!.value.toInt(),
                            y = result.groups[4]!!.value.toInt(),
                        ),
                    ),
                )
            }
    }

fun countPositionsWithoutBeacons(sensors: List<Sensor>, y: Int): Int {
    val allCoords = sensors.flatMap { listOf(it.location, it.beacon.location) }
    val minX = allCoords.minOf { it.x }
    val maxX = allCoords.maxOf { it.x }
    val maxBeaconDistance = sensors.maxOf { it.distanceToBeacon }

    return ((minX - maxBeaconDistance)..(maxX + maxBeaconDistance))
        .count { x -> 
            val coord = Coord(x, y)
            sensors.all { sensor -> coord != sensor.beacon.location }
                    && !canBeBeacon(coord, sensors)
        }
}

fun findDistressBeacon(sensors: List<Sensor>,
                       minX: Int = 0, maxX: Int = Int.MAX_VALUE,
                       minY: Int = 0, maxY: Int = Int.MAX_VALUE): Coord =
    sensors.asSequence()
        .flatMap { getPointsAtDistance(it.location, it.distanceToBeacon + 1) }
        .filter { it.x in minX..maxX && it.y in minY..maxY }
        .distinct()
        .single { canBeBeacon(it, sensors) }

fun canBeBeacon(coord: Coord, sensors: List<Sensor>): Boolean =
    sensors.all { sensor -> coord.manhattanDistanceTo(sensor.location) > sensor.distanceToBeacon }

class CoordIterator(
    xRange: Iterable<Int>,
    yRange: Iterable<Int>
) : Iterator<Coord> {
    private val xIter1 = xRange.iterator()
    private val yIter1 = yRange.iterator()

    override fun hasNext() = xIter1.hasNext() && yIter1.hasNext()

    override fun next() = Coord(xIter1.next(), yIter1.next())
}

fun getPointsAtDistance(from: Coord, distance: Int): Sequence<Coord> =
    sequence {
        yieldAll(
            CoordIterator(
                xRange = from.x..from.x + distance,
                yRange = from.y + distance downTo from.y,
            )
        )

        yieldAll(
            CoordIterator(
                xRange = from.x + distance downTo from.x,
                yRange = from.y downTo from.y - distance,
            )
        )

        yieldAll(
            CoordIterator(
                xRange = from.x downTo from.x - distance,
                yRange = from.y - distance..from.y,
            )
        )

        yieldAll(
            CoordIterator(
                xRange = from.x - distance..from.x,
                yRange = from.y..from.y + distance,
            )
        )
    }

fun getTuningFrequency(coord: Coord): Long =
    coord.x * 4000000L + coord.y
