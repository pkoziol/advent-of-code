package biz.koziolek.adventofcode.year2024.day24

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2024")
internal class Day24Test {

    private val sampleInputSmall = """
            x00: 1
            x01: 1
            x02: 1
            y00: 0
            y01: 1
            y02: 0

            x00 AND y00 -> z00
            x01 XOR y01 -> z01
            x02 OR y02 -> z02
        """.trimIndent().split("\n")

    private val sampleInputLarge = """
            x00: 1
            x01: 0
            x02: 1
            x03: 1
            x04: 0
            y00: 1
            y01: 1
            y02: 1
            y03: 1
            y04: 1

            ntg XOR fgs -> mjb
            y02 OR x01 -> tnw
            kwq OR kpj -> z05
            x00 OR x03 -> fst
            tgd XOR rvg -> z01
            vdt OR tnw -> bfw
            bfw AND frj -> z10
            ffh OR nrd -> bqk
            y00 AND y03 -> djm
            y03 OR y00 -> psh
            bqk OR frj -> z08
            tnw OR fst -> frj
            gnj AND tgd -> z11
            bfw XOR mjb -> z00
            x03 OR x00 -> vdt
            gnj AND wpb -> z02
            x04 AND y00 -> kjc
            djm OR pbm -> qhw
            nrd AND vdt -> hwm
            kjc AND fst -> rvg
            y04 OR y02 -> fgs
            y01 AND x02 -> pbm
            ntg OR kjc -> kwq
            psh XOR fgs -> tgd
            qhw XOR tgd -> z09
            pbm OR djm -> kpj
            x03 XOR y03 -> ffh
            x00 XOR y04 -> ntg
            bfw OR bqk -> z06
            nrd XOR fgs -> wpb
            frj XOR qhw -> z04
            bqk OR frj -> z07
            y03 OR x01 -> nrd
            hwm AND bqk -> z03
            tgd XOR rvg -> z12
            tnw OR pbm -> gnj
        """.trimIndent().split("\n")

    @Test
    fun testParseGates() {
        val (initValues, gateConnections) = parseGates(sampleInputSmall)
        assertEquals(6, initValues.size)
        assertEquals(3, gateConnections.size)

        val (initValues2, gateConnections2) = parseGates(sampleInputLarge)
        assertEquals(10, initValues2.size)
        assertEquals(36, gateConnections2.size)
    }

    @Test
    fun testEvaluate() {
        val (initValues, gateConnections) = parseGates(sampleInputSmall)
        val values = evaluate(gateConnections, initValues)
        assertEquals(mapOf(
            "x00" to true,
            "x01" to true,
            "x02" to true,
            "y00" to false,
            "y01" to true,
            "y02" to false,
            "z00" to false,
            "z01" to false,
            "z02" to true,
        ), values)

        val (initValues2, gateConnections2) = parseGates(sampleInputLarge)
        val values2 = evaluate(gateConnections2, initValues2)
        assertEquals(initValues2 + mapOf(
            "bfw" to true,
            "bqk" to true,
            "djm" to true,
            "ffh" to false,
            "fgs" to true,
            "frj" to true,
            "fst" to true,
            "gnj" to true,
            "hwm" to true,
            "kjc" to false,
            "kpj" to true,
            "kwq" to false,
            "mjb" to true,
            "nrd" to true,
            "ntg" to false,
            "pbm" to true,
            "psh" to true,
            "qhw" to true,
            "rvg" to false,
            "tgd" to false,
            "tnw" to true,
            "vdt" to true,
            "wpb" to false,
            "z00" to false,
            "z01" to false,
            "z02" to false,
            "z03" to true,
            "z04" to false,
            "z05" to true,
            "z06" to true,
            "z07" to true,
            "z08" to true,
            "z09" to true,
            "z10" to true,
            "z11" to false,
            "z12" to false,
        ), values2)
    }

    @Test
    fun testSampleAnswer1() {
        val (initValues, gateConnections) = parseGates(sampleInputSmall)
        val values = evaluate(gateConnections, initValues)
        val number = getNumber(values)
        assertEquals(4, number)

        val (initValues2, gateConnections2) = parseGates(sampleInputLarge)
        val values2 = evaluate(gateConnections2, initValues2)
        val number2 = getNumber(values2)
        assertEquals(2024, number2)
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val input = findInput(object {}).bufferedReader().readLines()
        val (initValues, gateConnections) = parseGates(input)
        val values = evaluate(gateConnections, initValues)
        val number = getNumber(values)
        assertEquals(59619940979346, number)
    }
}
