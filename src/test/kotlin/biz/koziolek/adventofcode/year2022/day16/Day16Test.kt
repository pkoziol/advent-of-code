package biz.koziolek.adventofcode.year2022.day16

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2022")
internal class Day16Test {

    private val sampleInput = """
            Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
            Valve BB has flow rate=13; tunnels lead to valves CC, AA
            Valve CC has flow rate=2; tunnels lead to valves DD, BB
            Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
            Valve EE has flow rate=3; tunnels lead to valves FF, DD
            Valve FF has flow rate=0; tunnels lead to valves EE, GG
            Valve GG has flow rate=0; tunnels lead to valves FF, HH
            Valve HH has flow rate=22; tunnel leads to valve GG
            Valve II has flow rate=0; tunnels lead to valves AA, JJ
            Valve JJ has flow rate=21; tunnel leads to valve II
        """.trimIndent().split("\n")

    @Test
    fun testParseValvesGraph() {
        val valvesGraph = parseValvesGraph(sampleInput)
        assertEquals(10, valvesGraph.nodes.size)
        assertEquals(0, valvesGraph.nodes.single { it.id == "AA" }.flowRate)
        assertEquals(13, valvesGraph.nodes.single { it.id == "BB" }.flowRate)
        assertEquals(2, valvesGraph.nodes.single { it.id == "CC" }.flowRate)
        assertEquals(20, valvesGraph.nodes.single { it.id == "DD" }.flowRate)
        assertEquals(3, valvesGraph.nodes.single { it.id == "EE" }.flowRate)
        assertEquals(0, valvesGraph.nodes.single { it.id == "FF" }.flowRate)
        assertEquals(0, valvesGraph.nodes.single { it.id == "GG" }.flowRate)
        assertEquals(22, valvesGraph.nodes.single { it.id == "HH" }.flowRate)
        assertEquals(0, valvesGraph.nodes.single { it.id == "II" }.flowRate)
        assertEquals(21, valvesGraph.nodes.single { it.id == "JJ" }.flowRate)
    }

    @Disabled
    @Test
    fun testPlayground() {
        val valvesGraph = parseValvesGraph(sampleInput)

        val distanceMatrix = buildDistanceMatrix(valvesGraph)

//        distanceMatrix.forEach { (srcValve, map) ->
//            println("${srcValve.id}:")
//            map.forEach { (dstValve, distance) ->
//                println("  ${dstValve.id}: $distance")
//            }
//        }

        val valveIDs = valvesGraph.nodes.map { it.id }.distinct().sorted()

        print("  ")
        valveIDs.forEach { dstID ->
            print(" $dstID")
        }
        println()

        valveIDs.forEach { srcID ->
            val srcValve = valvesGraph.nodes.single { it.id == srcID }
            print(srcID)
            valveIDs.forEach { dstID ->
                val dstValve = valvesGraph.nodes.single { it.id == dstID }
                print(" %2d".format(distanceMatrix[srcValve]!![dstValve]!!))
            }
            println()
        }

        generatePaths(valvesGraph, timeLimit = 30)
    }

    @Test
    fun testValvePath() {
        val valvesGraph = parseValvesGraph(sampleInput)
        val aa = valvesGraph.nodes.single { it.id == "AA" }
        val bb = valvesGraph.nodes.single { it.id == "BB" }
        val cc = valvesGraph.nodes.single { it.id == "CC" }
        val dd = valvesGraph.nodes.single { it.id == "DD" }
        val ee = valvesGraph.nodes.single { it.id == "EE" }
        val ff = valvesGraph.nodes.single { it.id == "FF" }
        val gg = valvesGraph.nodes.single { it.id == "GG" }
        val hh = valvesGraph.nodes.single { it.id == "HH" }
        val ii = valvesGraph.nodes.single { it.id == "II" }
        val jj = valvesGraph.nodes.single { it.id == "JJ" }

        val path = ValvePath(aa, false)
            .addValve(dd, true)
            .addValve(cc, false)
            .addValve(bb, true)
            .addValve(aa, false)
            .addValve(ii, false)
            .addValve(jj, true)
            .addValve(ii, false)
            .addValve(aa, false)
            .addValve(dd, false)
            .addValve(ee, false)
            .addValve(ff, false)
            .addValve(gg, false)
            .addValve(hh, true)
            .addValve(gg, false)
            .addValve(ff, false)
            .addValve(ee, true)
            .addValve(dd, false)
            .addValve(cc, true)

        assertEquals("AA->(DD)->CC->(BB)->AA->II->(JJ)->II->AA->DD->EE->FF->GG->(HH)->GG->FF->(EE)->DD->(CC)", path.toString())
        assertEquals(24, path.time)
        assertEquals(1165, path.releasedPressure)
        assertEquals(1651, path.potentialPressure(6))
    }

    @Test
    fun testFindBestPath() {
        val valvesGraph = parseValvesGraph(sampleInput)
        val timeLimit = 30
        val bestPath = findBestPath(valvesGraph, timeLimit)
        assertEquals(1651, bestPath.potentialPressure(timeLimit - bestPath.time))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val valvesGraph = parseValvesGraph(input)
        val timeLimit = 30
        val bestPath = findBestPath(valvesGraph, timeLimit)
        val potentialPressure = bestPath.potentialPressure(timeLimit - bestPath.time)
        assertEquals(1862, potentialPressure)
        assertEquals("AA->QD->(UK)->HU->(CO)->ED->XN->(IJ)->WE->(NA)->OS->YR->(SE)->AD->JW->(KF)->AL->TR->(CS)->GT->(MN)", bestPath.toString())
    }
}
