package biz.koziolek.adventofcode.year2024.day15

import biz.koziolek.adventofcode.AsciiColor
import biz.koziolek.adventofcode.findInput
import biz.koziolek.adventofcode.getHeight
import biz.koziolek.adventofcode.getWidth
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2024")
internal class Day15Test {

    private val sampleInputSmall = """
            ########
            #..O.O.#
            ##@.O..#
            #...O..#
            #.#.O..#
            #...O..#
            #......#
            ########

            <^^>>>vv<v>>v<<
        """.trimIndent().split("\n")

    private val sampleInputLarger = """
            ##########
            #..O..O.O#
            #......O.#
            #.OO..O.O#
            #..O@..O.#
            #O#..O...#
            #O..O..O.#
            #.OO.O.OO#
            #....O...#
            ##########

            <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
            vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
            ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
            <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
            ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
            ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
            >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
            <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
            ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
            v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
        """.trimIndent().split("\n")

    @Test
    fun testParse() {
        val (warehouse, moves) = parseWarehouse(sampleInputSmall)
        assertEquals(8, warehouse.map.getWidth())
        assertEquals(8, warehouse.map.getHeight())
        assertEquals(15, moves.size)
        assertEquals("""
            ########
            #..O.O.#
            ##@.O..#
            #...O..#
            #.#.O..#
            #...O..#
            #......#
            ########
        """.trimIndent(), AsciiColor.cleanUp(warehouse.toString()))
    }

    @Test
    fun testMovementSmall() {
        val (warehouse, moves) = parseWarehouse(sampleInputSmall)
        val allMovesString = buildString {
            append("Initial state:\n")
            append("$warehouse")
            moves.fold(warehouse) { acc, direction ->
                val newWarehouse = acc.move(direction)
                append("\n\nMove ${direction.char}:\n")
                append("$newWarehouse")
                newWarehouse
            }
        }
        assertEquals("""
            Initial state:
            ########
            #..O.O.#
            ##@.O..#
            #...O..#
            #.#.O..#
            #...O..#
            #......#
            ########

            Move <:
            ########
            #..O.O.#
            ##@.O..#
            #...O..#
            #.#.O..#
            #...O..#
            #......#
            ########

            Move ^:
            ########
            #.@O.O.#
            ##..O..#
            #...O..#
            #.#.O..#
            #...O..#
            #......#
            ########

            Move ^:
            ########
            #.@O.O.#
            ##..O..#
            #...O..#
            #.#.O..#
            #...O..#
            #......#
            ########

            Move >:
            ########
            #..@OO.#
            ##..O..#
            #...O..#
            #.#.O..#
            #...O..#
            #......#
            ########

            Move >:
            ########
            #...@OO#
            ##..O..#
            #...O..#
            #.#.O..#
            #...O..#
            #......#
            ########

            Move >:
            ########
            #...@OO#
            ##..O..#
            #...O..#
            #.#.O..#
            #...O..#
            #......#
            ########

            Move v:
            ########
            #....OO#
            ##..@..#
            #...O..#
            #.#.O..#
            #...O..#
            #...O..#
            ########

            Move v:
            ########
            #....OO#
            ##..@..#
            #...O..#
            #.#.O..#
            #...O..#
            #...O..#
            ########

            Move <:
            ########
            #....OO#
            ##.@...#
            #...O..#
            #.#.O..#
            #...O..#
            #...O..#
            ########

            Move v:
            ########
            #....OO#
            ##.....#
            #..@O..#
            #.#.O..#
            #...O..#
            #...O..#
            ########

            Move >:
            ########
            #....OO#
            ##.....#
            #...@O.#
            #.#.O..#
            #...O..#
            #...O..#
            ########

            Move >:
            ########
            #....OO#
            ##.....#
            #....@O#
            #.#.O..#
            #...O..#
            #...O..#
            ########

            Move v:
            ########
            #....OO#
            ##.....#
            #.....O#
            #.#.O@.#
            #...O..#
            #...O..#
            ########

            Move <:
            ########
            #....OO#
            ##.....#
            #.....O#
            #.#O@..#
            #...O..#
            #...O..#
            ########

            Move <:
            ########
            #....OO#
            ##.....#
            #.....O#
            #.#O@..#
            #...O..#
            #...O..#
            ########
        """.trimIndent(), AsciiColor.cleanUp(allMovesString))
    }

    @Test
    fun testMovementLarger() {
        val (warehouse, moves) = parseWarehouse(sampleInputLarger)
        val finalWarehouse = warehouse.move(moves)
        assertEquals(
            """
            ##########
            #.O.O.OOO#
            #........#
            #OO......#
            #OO@.....#
            #O#.....O#
            #O.....OO#
            #O.....OO#
            #OO....OO#
            ##########
        """.trimIndent(), AsciiColor.cleanUp(finalWarehouse.toString())
        )
    }

    @Test
    fun testSampleAnswer1() {
        val (warehouseS, movesS) = parseWarehouse(sampleInputSmall)
        val finalWarehouseS = warehouseS.move(movesS)
        assertEquals(2028, finalWarehouseS.sumBoxesCoords())

        val (warehouseL, movesL) = parseWarehouse(sampleInputSmall)
        val finalWarehouseL = warehouseL.move(movesL)
        assertEquals(2028, finalWarehouseL.sumBoxesCoords())
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val (warehouse, moves) = parseWarehouse(input)
        val finalWarehouse = warehouse.move(moves)
        assertEquals(1463715, finalWarehouse.sumBoxesCoords())
    }
}
