package biz.koziolek.adventofcode.year2023.day19

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val xmasSystem = parseXmasSystem(inputFile.bufferedReader().readLines())
    val acceptedParts = xmasSystem.runWorkflows()
    println("Sum of accepted parts: ${acceptedParts.sumOf { it.propsSum }}")
}

data class XmasSystem(val workflows: List<XmasWorkflow>,
                      val parts: List<MachinePart>) {
    fun runWorkflows(): List<MachinePart> =
        parts.filter { isAccepted(it) }

    private fun isAccepted(machinePart: MachinePart): Boolean {
        var workflow = workflows.single { it.name == "in" }

        while (true) {
            val rule = workflow.matches(machinePart)

            when {
                rule.isAccepted() -> return true
                rule.isRejected() -> return false
                else -> workflow = workflows.single { it.name == rule.destination }
            }
        }
    }
}

data class XmasWorkflow(val name: String, val rules: List<XmasRule>) {
    fun matches(machinePart: MachinePart): XmasRule =
        rules.first { it.matches(machinePart) }
}

sealed interface XmasRule {
    val destination: String
    fun isAccepted() = destination == "A"
    fun isRejected() = destination == "R"
    fun matches(machinePart: MachinePart): Boolean
}

data class ConditionalXmasRule(val variable: Char,
                               val operator: Char,
                               val value: Int,
                               override val destination: String) : XmasRule {
    override fun matches(machinePart: MachinePart): Boolean {
        val partValue = when (variable) {
            'x' -> machinePart.x
            'm' -> machinePart.m
            'a' -> machinePart.a
            's' -> machinePart.s
            else -> throw IllegalArgumentException("Unexpected variable: $variable")
        }

        return when (operator) {
            '>' -> partValue > value
            '<' -> partValue < value
            else -> throw IllegalArgumentException("Unexpected operator: $operator")
        }
    }
}

data class DefaultXmasRule(override val destination: String) : XmasRule {
    override fun matches(machinePart: MachinePart) = true
}

data class MachinePart(val x: Int, val m: Int, val a: Int, val s: Int) {
    val propsSum = x + m + a + s
}

fun parseXmasSystem(lines: Iterable<String>): XmasSystem {
    val workflows = lines
        .takeWhile { line -> line.isNotBlank() }
        .map { line ->
            val (name, rulesStr, _) = line.split('{', '}')

            XmasWorkflow(
                name = name,
                rules = Regex("(([xmas])([<>])([0-9]+):)?([a-z]+|A|R)").findAll(rulesStr)
                    .map { match ->
                        if (match.groups[1]?.value != null) {
                            ConditionalXmasRule(
                                variable = match.groups[2]!!.value.single(),
                                operator = match.groups[3]!!.value.single(),
                                value = match.groups[4]!!.value.toInt(),
                                destination = match.groups[5]!!.value,
                            )
                        } else {
                            DefaultXmasRule(
                                destination = match.groups[5]!!.value,
                            )
                        }
                    }
                    .toList()
            )
        }

    val parts = lines
        .dropWhile { line -> line.isNotBlank() }
        .drop(1)
        .mapNotNull { line ->
            Regex("\\{x=([0-9]+),m=([0-9]+),a=([0-9]+),s=([0-9]+)}")
                .find(line)
                ?.let { match ->
                    MachinePart(
                        x = match.groups[1]!!.value.toInt(),
                        m = match.groups[2]!!.value.toInt(),
                        a = match.groups[3]!!.value.toInt(),
                        s = match.groups[4]!!.value.toInt(),
                    )
                }
        }

    return XmasSystem(workflows, parts)
}
