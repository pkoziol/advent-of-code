package biz.koziolek.adventofcode.year2024.day23

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2024")
internal class Day23Test {

    private val sampleInput = """
            kh-tc
            qp-kh
            de-cg
            ka-co
            yn-aq
            qp-ub
            cg-tb
            vc-aq
            tb-ka
            wh-tc
            yn-cg
            kh-ub
            ta-co
            de-co
            tc-td
            tb-wq
            wh-td
            ta-ka
            td-qp
            aq-cg
            wq-ub
            ub-vc
            de-ta
            wq-aq
            wq-vc
            wh-yn
            ka-de
            kh-ta
            co-tc
            wh-qp
            tb-vc
            td-yn
        """.trimIndent().split("\n")

    @Test
    fun testParseComputerConnections() {
        val connections = parseComputerConnections(sampleInput)
        assertEquals(32, connections.size)
        assertTrue(Pair("td", "yn") in connections)
    }

    @Test
    fun testFindTriples() {
        val connections = parseComputerConnections(sampleInput)
        val triples = findTriples(connections)
        val expectedTriples = """
            aq,cg,yn
            aq,vc,wq
            co,de,ka
            co,de,ta
            co,ka,ta
            de,ka,ta
            kh,qp,ub
            qp,td,wh
            tb,vc,wq
            tc,td,wh
            td,wh,yn
            ub,vc,wq
        """.trimIndent()
            .split("\n")
            .map { it.split(",") }
            .map { Triple(it[0], it[1], it[2]) }
            .toSet()
        assertEquals(expectedTriples, triples)
    }

    @Test
    fun testSampleAnswer1() {
        val connections = parseComputerConnections(sampleInput)
        val triples = findTriples(connections)
        val answer = countTs(triples)
        assertEquals(7, answer)
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val connections = parseComputerConnections(input)
        val triples = findTriples(connections)
        val answer = countTs(triples)
        assertEquals(1327, answer)
    }

    @Test
    fun testSampleAnswer2() {
        val connections = parseComputerConnections(sampleInput)
        val groups = findFullyConnectedGroups(connections)
        val theBiggest = groups.maxBy { it.size }
        val password = getPassword(theBiggest)
        assertEquals("co,de,ka,ta", password)
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val connections = parseComputerConnections(input)
        val groups = findFullyConnectedGroups(connections)
        val theBiggest = groups.maxBy { it.size }
        val password = getPassword(theBiggest)
        assertEquals("df,kg,la,mp,pb,qh,sk,th,vn,ww,xp,yp,zk", password)
    }
}
