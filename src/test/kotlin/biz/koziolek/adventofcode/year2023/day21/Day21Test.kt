package biz.koziolek.adventofcode.year2023.day21

import biz.koziolek.adventofcode.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@Tag("2023")
internal class Day21Test {

    private val sampleInput = """
            ...........
            .....###.#.
            .###.##..#.
            ..#.#...#..
            ....#.#....
            .##..S####.
            .##..#...#.
            .......##..
            .##.#.####.
            .##..##.##.
            ...........
        """.trimIndent().split("\n")

    @Test
    fun testParse() {
        val map = parseGardenMap(sampleInput)
        assertEquals(11, map.getWidth())
        assertEquals(11, map.getHeight())
        assertEquals(40, map.count { it.value == ROCK })
    }

    @Test
    fun testSampleAnswer1() {
        val map = parseGardenMap(sampleInput)

        val walked1 = walk(map, steps = 1)
        assertEquals(
            """
                ...........
                .....###.#.
                .###.##..#.
                ..#.#...#..
                ....#O#....
                .##.OS####.
                .##..#...#.
                .......##..
                .##.#.####.
                .##..##.##.
                ...........
            """.trimIndent(),
            gardenMapToString(map, walked1)
        )
        assertEquals(2, walked1.size)

        val walked2 = walk(map, steps = 2)
        assertEquals(
            """
                ...........
                .....###.#.
                .###.##..#.
                ..#.#O..#..
                ....#.#....
                .##O.O####.
                .##.O#...#.
                .......##..
                .##.#.####.
                .##..##.##.
                ...........
            """.trimIndent(),
            gardenMapToString(map, walked2)
        )
        assertEquals(4, walked2.size)

        val walked3 = walk(map, steps = 3)
        assertEquals(
            """
                ...........
                .....###.#.
                .###.##..#.
                ..#.#.O.#..
                ...O#O#....
                .##.OS####.
                .##O.#...#.
                ....O..##..
                .##.#.####.
                .##..##.##.
                ...........
            """.trimIndent(),
            gardenMapToString(map, walked3)
        )
        assertEquals(6, walked3.size)

        val walked6 = walk(map, steps = 6)
        assertEquals(
            """
                ...........
                .....###.#.
                .###.##.O#.
                .O#O#O.O#..
                O.O.#.#.O..
                .##O.O####.
                .##.O#O..#.
                .O.O.O.##..
                .##.#.####.
                .##O.##.##.
                ...........
            """.trimIndent(),
            gardenMapToString(map, walked6)
        )
        assertEquals(16, walked6.size)
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val map = parseGardenMap(input)
        assertEquals(3594, walk(map, steps = 64).size)
    }

    @Test
    fun testCoerce() {
        assertEquals(7, coerce(27, 0..9))
        assertEquals(7, coerce(-3, 0..9))
        assertEquals(-7, coerce(3, -9..0))
        assertEquals(-7, coerce(-27, -9..0))
        assertEquals(6, coerce(27, -10..10))
        assertEquals(-6, coerce(-27, -10..10))
        assertEquals(126, coerce(227, 100..200))
        assertEquals(184, coerce(83, 100..200))
        assertEquals(5, coerce(10 + 21 + 21 + 21 + 21 + 10 + 1 + 5, -10..10))
        assertEquals(-6, coerce(-10 - 21 - 21 - 21 - 10 - 1 - 6, -10..10))
        assertEquals(0, coerce(-11, 0..10))
        assertEquals(10, coerce(21, 0..10))
    }

    @Test
    fun testInfiniteMap() {
        val map = InfiniteMap(parseGardenMap(sampleInput))
        assertEquals(
            """
                ...........
                .....###.#.
                .###.##..#.
                ..#.#...#..
                ....#.#....
                .##..S####.
                .##..#...#.
                .......##..
                .##.#.####.
                .##..##.##.
                ...........
            """.trimIndent(),
            gardenMapToString(map)
        )

        val yRange = -11..21
        val xRange = -11..21
        val coords = yRange.flatMap { y -> xRange.map { x -> Coord(x, y) } }
        val newMap = coords
            .mapNotNull { coord -> map[coord]?.let { coord to it } }
            .toMap()

        assertEquals(
            """
                .................................
                .....###.#......###.#......###.#.
                .###.##..#..###.##..#..###.##..#.
                ..#.#...#....#.#...#....#.#...#..
                ....#.#........#.#........#.#....
                .##...####..##...####..##...####.
                .##..#...#..##..#...#..##..#...#.
                .......##.........##.........##..
                .##.#.####..##.#.####..##.#.####.
                .##..##.##..##..##.##..##..##.##.
                .................................
                .................................
                .....###.#......###.#......###.#.
                .###.##..#..###.##..#..###.##..#.
                ..#.#...#....#.#...#....#.#...#..
                ....#.#........#.#........#.#....
                .##...####..##..S####..##...####.
                .##..#...#..##..#...#..##..#...#.
                .......##.........##.........##..
                .##.#.####..##.#.####..##.#.####.
                .##..##.##..##..##.##..##..##.##.
                .................................
                .................................
                .....###.#......###.#......###.#.
                .###.##..#..###.##..#..###.##..#.
                ..#.#...#....#.#...#....#.#...#..
                ....#.#........#.#........#.#....
                .##...####..##...####..##...####.
                .##..#...#..##..#...#..##..#...#.
                .......##.........##.........##..
                .##.#.####..##.#.####..##.#.####.
                .##..##.##..##..##.##..##..##.##.
                .................................
            """.trimIndent(),
            gardenMapToString(newMap)
        )
    }

    @Test
    fun testInfiniteFlood() {
        val input = findInput(object {}).bufferedReader().readLines()
        val map = moveStartTo00(parseGardenMap(input))

        val mapArea = map.getWidth() * map.getHeight()
        val allCoords = map.walkSouth().toSet()
        val rocks = map.filter{ it.value == ROCK }
        val empty = map.walkSouth().filter { it !in rocks }.toList()
        println("area: ${map.getWidth()} x ${map.getWidth()} = $mapArea")
        println("all coords: ${allCoords.size}")
        println("rocks: ${rocks.size}")
        println("empty: ${empty.size}")
        println("empty: ${mapArea - rocks.size}")

        val flooded = infiniteFlood(FloodInput(
            chunk = Coord(0, 0),
            map = map,
            start = findStart(map),
            absoluteStartingDistance = 0,
        ))
        println("Flooded from start: ${flooded.relativeDistances.size}")
        println("  max distance: ${flooded.relativeDistances.values.max()}")

        println("  not flooded:")
        val notFlooded = empty.toSet() - flooded.relativeDistances.keys
        println(notFlooded.joinToString("\n    ", "    "))

        assertEquals(
            setOf(
                Exit(mapCoord = Coord(-66, 0), fromChunk = Coord(0, 0), toChunk = Coord(-1, 0), distance = 66),
                Exit(mapCoord = Coord(0, 66), fromChunk = Coord(0, 0), toChunk = Coord(0, 1), distance = 66),
                Exit(mapCoord = Coord(66, 0), fromChunk = Coord(0, 0), toChunk = Coord(1, 0), distance = 66),
                Exit(mapCoord = Coord(0, -66), fromChunk = Coord(0, 0), toChunk = Coord(0, -1), distance = 66),
            ),
            flooded.exits
        )

        println("From north:")
        val floodedFromNorth = infiniteFlood(FloodInput(Coord(0, 1), map, start = Coord(0, -66), absoluteStartingDistance = 66))
        println("  max distance: ${floodedFromNorth.relativeDistances.values.max()}")

        println("From east:")
        val floodedFromEast = infiniteFlood(FloodInput(Coord(-1, 0), map, start = Coord(66, 0), absoluteStartingDistance = 66))
        println("  max distance: ${floodedFromNorth.relativeDistances.values.max()}")

        println("From south:")
        val floodedFromSouth = infiniteFlood(FloodInput(Coord(0, -1), map, start = Coord(0, 66), absoluteStartingDistance = 66))
        println("  max distance: ${floodedFromNorth.relativeDistances.values.max()}")

        println("From west:")
        val floodedFromWest = infiniteFlood(FloodInput(Coord(1, 0), map, start = Coord(-66, 0), absoluteStartingDistance = 66))
        println("  max distance: ${floodedFromNorth.relativeDistances.values.max()}")

        println("N == E? ${floodedFromNorth == floodedFromEast}")
        println("N == S? ${floodedFromNorth == floodedFromSouth}")
        println("N == W? ${floodedFromNorth == floodedFromWest}")

        val floodFormatter: (Coord, Int?) -> String = { coord, distance ->
            val str = if (distance != null) {
                "%4d".format(distance)
            } else {
                "    "
            }

            if (coord.x == 0 || coord.y == 0) {
                AsciiColor.YELLOW.format(str)
            } else {
                str
            }
        }

        println(flooded.relativeDistances.to2DStringOfStrings(floodFormatter))
        println()
        println(floodedFromNorth.relativeDistances.to2DStringOfStrings(floodFormatter))
        println()
        println(floodedFromEast.relativeDistances.to2DStringOfStrings(floodFormatter))
        println()
        println(floodedFromSouth.relativeDistances.to2DStringOfStrings(floodFormatter))
        println()
        println(floodedFromWest.relativeDistances.to2DStringOfStrings(floodFormatter))

        println(lowestAtEdges(floodedFromNorth))
        println(floodedFromNorth.exits)

        println(lowestAtEdges(floodedFromEast))
        println(floodedFromEast.exits)

        println(lowestAtEdges(floodedFromSouth))
        println(floodedFromSouth.exits)

        println(lowestAtEdges(floodedFromWest))
        println(floodedFromWest.exits)
    }

    private fun lowestAtEdges(flooded: FloodResult): Map<Coord, Int> {
        val (minX, maxX) = flooded.relativeDistances.keys.minAndMaxOrNull { it.x }!!
        val (minY, maxY) = flooded.relativeDistances.keys.minAndMaxOrNull { it.y }!!
        val minDistance = flooded.relativeDistances.values.min()

//        return flooded.filter {
//            it.value == minDistance && (
//                it.key.x == minX
//                || it.key.x == maxX
//                || it.key.y == minY
//                || it.key.y == maxY
//            )
//        }

        return mapOf(
            flooded.relativeDistances.filter { it.key.x == minX }.minBy { it.value }.toPair(),
            flooded.relativeDistances.filter { it.key.x == maxX }.minBy { it.value }.toPair(),
            flooded.relativeDistances.filter { it.key.y == minY }.minBy { it.value }.toPair(),
            flooded.relativeDistances.filter { it.key.y == maxY }.minBy { it.value }.toPair(),
        )
    }

    @ParameterizedTest
    @CsvSource(
//        "6,16",
//        "10,50",
//        "50,1594",
//        "100,6536",
        "500,167004",
//        "1000,668697",
//        "5000,16733044",
    )
    fun testSampleAnswer2(steps: Int, expectedTiles: Int) {
        val map = parseGardenMap(sampleInput)
        val infiniteMap = InfiniteMap(map)
        assertEquals(expectedTiles, walk(infiniteMap, steps).size)
        assertEquals(expectedTiles, infiniteWalk(map, steps))
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val map = moveStartTo00(parseGardenMap(input))
        val infiniteMap = InfiniteMap(map)

        println(walk(infiniteMap, steps = 65).size)
        println(walk(infiniteMap, steps = 65 + 131).size)
        println(walk(infiniteMap, steps = 65 + 131 + 131).size)
//        assertEquals(-1, infiniteWalk(map, steps = 26_501_365))
        println(26_501_365 / map.getWidth())
        // 60 501 212 936 -> too low
        // 604 715 084 751 788 -> too low
        // 608152828731262
        assertEquals(-1, infiniteWalk(map, steps = map.getWidth() + 65))
    }
}
