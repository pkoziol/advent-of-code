package biz.koziolek.adventofcode.year2023.day20

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2023")
internal class Day20Test {

    private val sampleInput1 = """
            broadcaster -> a, b, c
            %a -> b
            %b -> c
            %c -> inv
            &inv -> a
        """.trimIndent().split("\n")

    private val sampleInput2 = """
            broadcaster -> a
            %a -> inv, con
            &inv -> b
            %b -> con
            &con -> output
        """.trimIndent().split("\n")

    @Test
    fun testParse() {
        val modules1 = parseModules(sampleInput1)
        assertEquals(
            mapOf(
                "broadcaster" to BroadcasterModule(destinations = listOf("a", "b", "c")),
                "a" to FlipFlopModule(name = "a", destinations = listOf("b")),
                "b" to FlipFlopModule(name = "b", destinations = listOf("c")),
                "c" to FlipFlopModule(name = "c", destinations = listOf("inv")),
                "inv" to ConjunctionModule(name = "inv", inputs = setOf("c"), destinations = listOf("a")),
            ),
            modules1
        )

        val modules2 = parseModules(sampleInput2)
        assertEquals(
            mapOf(
                "broadcaster" to BroadcasterModule(destinations = listOf("a")),
                "a" to FlipFlopModule(name = "a", destinations = listOf("inv", "con")),
                "inv" to ConjunctionModule(name = "inv", inputs = setOf("a"), destinations = listOf("b")),
                "b" to FlipFlopModule(name = "b", destinations = listOf("con")),
                "con" to ConjunctionModule(name = "con", inputs = setOf("a", "b"), destinations = listOf("output")),
            ),
            modules2
        )
    }

    @Test
    fun testGeneratePulses() {
        val modules1 = parseModules(sampleInput1)
        val (_, pulses1) = generatePulses(modules1, initialPulse = Pulse.BUTTON_PRESS)
            .take(12)
            .getLatestModulesAndAllPulses()
        assertEquals(
            """
                button -low-> broadcaster
                broadcaster -low-> a
                broadcaster -low-> b
                broadcaster -low-> c
                a -high-> b
                b -high-> c
                c -high-> inv
                inv -low-> a
                a -low-> b
                b -low-> c
                c -low-> inv
                inv -high-> a
            """.trimIndent(),
            pulses1.joinToString("\n") { it.toString() }
        )

        val modules2 = parseModules(sampleInput2)
        val (modules2b, pulses2) = generatePulses(modules2, initialPulse = Pulse.BUTTON_PRESS)
            .getLatestModulesAndAllPulses()
        assertEquals(
            """
                button -low-> broadcaster
                broadcaster -low-> a
                a -high-> inv
                a -high-> con
                inv -low-> b
                con -high-> output
                b -high-> con
                con -low-> output
            """.trimIndent(),
            pulses2.joinToString("\n") { it.toString() }
        )

        val (modules2c, pulses2b) = generatePulses(modules2b, initialPulse = Pulse.BUTTON_PRESS)
            .getLatestModulesAndAllPulses()
        assertEquals(
            """
                button -low-> broadcaster
                broadcaster -low-> a
                a -low-> inv
                a -low-> con
                inv -high-> b
                con -high-> output
            """.trimIndent(),
            pulses2b.joinToString("\n") { it.toString() }
        )

        val (_, pulses2c) = generatePulses(modules2c, initialPulse = Pulse.BUTTON_PRESS)
            .getLatestModulesAndAllPulses()
        assertEquals(
            """
                button -low-> broadcaster
                broadcaster -low-> a
                a -high-> inv
                a -high-> con
                inv -low-> b
                con -low-> output
                b -low-> con
                con -high-> output
            """.trimIndent(),
            pulses2c.joinToString("\n") { it.toString() }
        )
    }

    @Test
    fun testSampleAnswer1() {
        val modules1 = parseModules(sampleInput1)
        assertEquals(32000000, countPulses(modules1, buttonPresses = 1000))

        val modules2 = parseModules(sampleInput2)
        assertEquals(11687500, countPulses(modules2, buttonPresses = 1000))
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val modules = parseModules(input)
        assertEquals(777666211, countPulses(modules, buttonPresses = 1000))
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val input = findInput(object {}).bufferedReader().readLines()
        val modules = parseModules(input)
        assertEquals(243_081_086_866_483, findFewestButtonPresses(modules))
    }
}
