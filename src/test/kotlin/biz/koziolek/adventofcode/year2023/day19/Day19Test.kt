package biz.koziolek.adventofcode.year2023.day19

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2023")
internal class Day19Test {

    private val sampleInput = """
            px{a<2006:qkq,m>2090:A,rfg}
            pv{a>1716:R,A}
            lnx{m>1548:A,A}
            rfg{s<537:gd,x>2440:R,A}
            qs{s>3448:A,lnx}
            qkq{x<1416:A,crn}
            crn{x>2662:A,R}
            in{s<1351:px,qqz}
            qqz{s>2770:qs,m<1801:hdj,R}
            gd{a>3333:R,R}
            hdj{m>838:A,pv}

            {x=787,m=2655,a=1222,s=2876}
            {x=1679,m=44,a=2067,s=496}
            {x=2036,m=264,a=79,s=2244}
            {x=2461,m=1339,a=466,s=291}
            {x=2127,m=1623,a=2188,s=1013}
        """.trimIndent().split("\n")

    @Test
    fun testParseXmasSystem() {
        val xmasSystem = parseXmasSystem(sampleInput)

        assertEquals(
            listOf(
                XmasWorkflow(name = "px", rules = listOf(
                    ConditionalXmasRule(variable = 'a', operator = '<', value = 2006, destination = "qkq"),
                    ConditionalXmasRule(variable = 'm', operator = '>', value = 2090, destination = "A"),
                    DefaultXmasRule(destination = "rfg"),
                )),
                XmasWorkflow(name = "pv", rules = listOf(
                    ConditionalXmasRule(variable = 'a', operator = '>', value = 1716, destination = "R"),
                    DefaultXmasRule(destination = "A"),
                )),
                XmasWorkflow(name = "lnx", rules = listOf(
                    ConditionalXmasRule(variable = 'm', operator = '>', value = 1548, destination = "A"),
                    DefaultXmasRule(destination = "A"),
                )),
            ),
            xmasSystem.workflows.take(3)
        )
        assertEquals(11, xmasSystem.workflows.size)

        assertEquals(
            listOf(
                MachinePart(x = 787, m = 2655, a = 1222, s = 2876),
                MachinePart(x = 1679, m = 44, a = 2067, s = 496),
                MachinePart(x = 2036, m = 264, a = 79, s = 2244),
                MachinePart(x = 2461, m = 1339, a = 466, s = 291),
                MachinePart(x = 2127, m = 1623, a = 2188, s = 1013),
            ),
            xmasSystem.parts
        )
    }

    @Test
    fun testSampleAnswer1() {
        val xmasSystem = parseXmasSystem(sampleInput)
        val acceptedParts = xmasSystem.runWorkflows()
        assertEquals(19114, acceptedParts.sumOf { it.propsSum })
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val xmasSystem = parseXmasSystem(input)
        val acceptedParts = xmasSystem.runWorkflows()
        assertEquals(487623, acceptedParts.sumOf { it.propsSum })
    }
}
