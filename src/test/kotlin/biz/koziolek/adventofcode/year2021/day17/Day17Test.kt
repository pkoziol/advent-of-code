package biz.koziolek.adventofcode.year2021.day17

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day17Test {

    private val sampleInput = "target area: x=20..30, y=-10..-5"

    @Test
    fun testParseTargetAreaSample() {
        val targetArea = parseTargetArea(sampleInput)
        assertEquals(Coord(20, -5), targetArea.first)
        assertEquals(Coord(30, -10), targetArea.second)
    }

    @Test
    fun testParseTargetAreaFullInput() {
        val fullInput = findInput(object {}).readLines()
        val targetArea = parseTargetArea(fullInput.single())
        assertEquals(Coord(265, -58), targetArea.first)
        assertEquals(Coord(287, -103), targetArea.second)
    }

    @Test
    fun testSampleTrajectory1() {
        val targetArea = Coord(20, -5) to Coord(30, -10)
        val velocity = Coord(7, 2)

        val trajectory = calculateTrajectory(velocity, targetArea)
        assertEquals(8, trajectory.size)
        assertEquals(Coord(0, 0), trajectory[0])
        assertEquals(Coord(7, 2), trajectory[1])
        assertEquals(Coord(13, 3), trajectory[2])
        assertEquals(Coord(18, 3), trajectory[3])
        assertEquals(Coord(22, 2), trajectory[4])
        assertEquals(Coord(25, 0), trajectory[5])
        assertEquals(Coord(27, -3), trajectory[6])
        assertEquals(Coord(28, -7), trajectory[7])

        val expectedVisualization = """
            .............#....#............
            .......#..............#........
            ...............................
            S........................#.....
            ...............................
            ...............................
            ...........................#...
            ...............................
            ....................TTTTTTTTTTT
            ....................TTTTTTTTTTT
            ....................TTTTTTTT#TT
            ....................TTTTTTTTTTT
            ....................TTTTTTTTTTT
            ....................TTTTTTTTTTT
        """.trimIndent()
        assertEquals(expectedVisualization, visualizeTrajectory(trajectory, targetArea))
    }

    @Test
    fun testSampleTrajectory2() {
        val targetArea = Coord(20, -5) to Coord(30, -10)
        val velocity = Coord(6, 3)

        val trajectory = calculateTrajectory(velocity, targetArea)

        val expectedVisualization = """
            ...............#..#............
            ...........#........#..........
            ...............................
            ......#..............#.........
            ...............................
            ...............................
            S....................#.........
            ...............................
            ...............................
            ...............................
            .....................#.........
            ....................TTTTTTTTTTT
            ....................TTTTTTTTTTT
            ....................TTTTTTTTTTT
            ....................TTTTTTTTTTT
            ....................T#TTTTTTTTT
            ....................TTTTTTTTTTT
        """.trimIndent()
        assertEquals(expectedVisualization, visualizeTrajectory(trajectory, targetArea))
    }

    @Test
    fun testSampleTrajectory3() {
        val targetArea = Coord(20, -5) to Coord(30, -10)
        val velocity = Coord(9, 0)

        val trajectory = calculateTrajectory(velocity, targetArea)

        val expectedVisualization = """
            S........#.....................
            .................#.............
            ...............................
            ........................#......
            ...............................
            ....................TTTTTTTTTTT
            ....................TTTTTTTTTT#
            ....................TTTTTTTTTTT
            ....................TTTTTTTTTTT
            ....................TTTTTTTTTTT
            ....................TTTTTTTTTTT
        """.trimIndent()
        assertEquals(expectedVisualization, visualizeTrajectory(trajectory, targetArea))
    }

    @Test
    fun testSampleTrajectory4() {
        val targetArea = Coord(20, -5) to Coord(30, -10)
        val velocity = Coord(17, -4)

        val trajectory = calculateTrajectory(velocity, targetArea)

        val expectedVisualization = """
            S..............................
            ...............................
            ...............................
            ...............................
            .................#.............
            ....................TTTTTTTTTTT
            ....................TTTTTTTTTTT
            ....................TTTTTTTTTTT
            ....................TTTTTTTTTTT
            ....................TTTTTTTTTTT
            ....................TTTTTTTTTTT
        """.trimIndent()
        assertEquals(expectedVisualization, visualizeTrajectory(trajectory, targetArea))
    }

    @Test
    fun testFindVelocityWithMostStyle() {
        val targetArea = Coord(20, -5) to Coord(30, -10)
        val bestVelocityAndMaxHeight = findVelocityWithMostStyle(targetArea)

        assertEquals(Coord(6, 9) to 45, bestVelocityAndMaxHeight)
    }

    @Test
    fun testAnswer1() {
        val fullInput = findInput(object {}).readLines()
        val targetArea = parseTargetArea(fullInput.single())
        val bestVelocityAndMaxHeight = findVelocityWithMostStyle(targetArea)

        assertEquals(Coord(23, 102) to 5253, bestVelocityAndMaxHeight)
    }

    @Test
    fun testFindAllVelocitiesThatHitTarget() {
        val targetArea = Coord(20, -5) to Coord(30, -10)
        val allVelocitiesThatHitTarget = findAllVelocitiesThatHitTarget(targetArea)

        assertEquals(112, allVelocitiesThatHitTarget.size)
    }

    @Test
    fun testAnswer2() {
        val fullInput = findInput(object {}).readLines()
        val targetArea = parseTargetArea(fullInput.single())
        val allVelocitiesThatHitTarget = findAllVelocitiesThatHitTarget(targetArea)

        assertEquals(1770, allVelocitiesThatHitTarget.size)
    }
}
