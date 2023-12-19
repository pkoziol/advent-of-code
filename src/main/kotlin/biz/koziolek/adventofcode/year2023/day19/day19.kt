package biz.koziolek.adventofcode.year2023.day19

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val xmasSystem = parseXmasSystem(inputFile.bufferedReader().readLines())
    val acceptedParts = xmasSystem.runWorkflows()
    println("Sum of accepted parts: ${acceptedParts.sumOf { it.propsSum }}")
    println("Sum of ALL accepted parts: ${xmasSystem.countAllAccepted()}")
}

data class XmasSystem(val workflows: List<XmasWorkflow>,
                      val parts: List<MachinePart>) {
    private val startingWorkflow = findWorkflow("in")

    private fun findWorkflow(name: String) = workflows.single { it.name == name }

    fun runWorkflows(): List<MachinePart> =
        parts.filter { isAccepted(it) }

    private fun isAccepted(machinePart: MachinePart): Boolean {
        var workflow = startingWorkflow

        while (true) {
            val rule = workflow.matches(machinePart)

            when {
                rule.isAccepted() -> return true
                rule.isRejected() -> return false
                else -> workflow = findWorkflow(rule.destination)
            }
        }
    }

    fun countAllAccepted(): Long {
        var accepted = 0L
        var rejected = 0L
        var checked = 0L

        val toCheck = mutableListOf<Pair<QuantumMachinePart, XmasWorkflow>>()
        toCheck.add(
            QuantumMachinePart(
                x = 1..4000,
                m = 1..4000,
                a = 1..4000,
                s = 1..4000,
            ) to startingWorkflow
        )

        while (toCheck.isNotEmpty()) {
            checked++

            val (part, workflow) = toCheck.removeFirst()

            for (processedPart in workflow.process(part)) {
                when {
                    processedPart.rule.isAccepted() -> accepted += processedPart.part.propsSum
                    processedPart.rule.isRejected() -> rejected += processedPart.part.propsSum
                    else -> toCheck.add(processedPart.part to findWorkflow(processedPart.rule.destination))
                }
            }
        }

        val total = accepted + rejected
        println("Checked $checked for total of $total of which $accepted (${accepted * 100 / total}%) were accepted")

        return accepted
    }
}

data class XmasWorkflow(val name: String, val rules: List<XmasRule>) {
    fun matches(machinePart: MachinePart): XmasRule =
        rules.first { it.matches(machinePart) }

    fun process(machinePart: QuantumMachinePart, debug: Boolean = false): List<MatchedQuantumPart> {
        if (debug) println("Workflow $name:")

        val processedParts = mutableListOf<MatchedQuantumPart>()
        var pendingParts = listOf(machinePart)

        for (rule in rules) {
            val newPendingParts = mutableListOf<QuantumMachinePart>()

            for (part in pendingParts) {
                if (debug) println("  $part + $rule =")

                for (processedPart in rule.process(part)) {
                    if (debug) println("    ${processedPart::class.simpleName} & ${processedPart.part}")

                    when (processedPart) {
                        is MatchedQuantumPart -> processedParts.add(processedPart)
                        is NotMatchedQuantumPart -> newPendingParts.add(processedPart.part)
                    }
                }
            }

            pendingParts = newPendingParts
        }

        return processedParts
    }
}

sealed interface XmasRule {
    val destination: String
    fun isAccepted() = destination == "A"
    fun isRejected() = destination == "R"
    fun matches(machinePart: MachinePart): Boolean
    fun process(machinePart: QuantumMachinePart): List<ProcessedQuantumPart>
}


sealed interface ProcessedQuantumPart {
    val part: QuantumMachinePart
}
data class MatchedQuantumPart(override val part: QuantumMachinePart, val rule: XmasRule) : ProcessedQuantumPart
data class NotMatchedQuantumPart(override val part: QuantumMachinePart) : ProcessedQuantumPart

data class ConditionalXmasRule(val property: Char,
                               val operator: Char,
                               val value: Int,
                               override val destination: String) : XmasRule {
    override fun matches(machinePart: MachinePart): Boolean {
        val partValue = machinePart.getProperty(property)

        return when (operator) {
            '>' -> partValue > value
            '<' -> partValue < value
            else -> throw IllegalArgumentException("Unexpected operator: $operator")
        }
    }

    override fun process(machinePart: QuantumMachinePart): List<ProcessedQuantumPart> {
        val valuesToCheck = machinePart.getProperty(property)

        return when {
            value in valuesToCheck ->
                when (operator) {
                    '<' -> listOf(
                        MatchedQuantumPart(machinePart.copy(property, valuesToCheck.first..<value), this),
                        NotMatchedQuantumPart(machinePart.copy(property, value..valuesToCheck.last)),
                    )

                    '>' -> listOf(
                        NotMatchedQuantumPart(machinePart.copy(property, valuesToCheck.first..value)),
                        MatchedQuantumPart(machinePart.copy(property, value+1..valuesToCheck.last), this),
                    )

                    else -> throw IllegalArgumentException("Unexpected operator: $operator")
                }

            (operator == '<' && valuesToCheck.last < value) || (operator == '>' && valuesToCheck.first > value) ->
                listOf(MatchedQuantumPart(machinePart, this))

            else -> listOf(NotMatchedQuantumPart(machinePart))
        }
    }

    override fun toString() = "Conditional{$property $operator $value -> $destination}"
}

data class DefaultXmasRule(override val destination: String) : XmasRule {
    override fun matches(machinePart: MachinePart) = true

    override fun process(machinePart: QuantumMachinePart): List<MatchedQuantumPart> =
        listOf(MatchedQuantumPart(machinePart, this))

    override fun toString() = "Always{-> $destination}"
}

data class MachinePart(val x: Int, val m: Int, val a: Int, val s: Int) {
    val propsSum = x + m + a + s

    fun getProperty(name: Char) =
        when (name) {
            'x' -> x
            'm' -> m
            'a' -> a
            's' -> s
            else -> throw IllegalArgumentException("Unexpected property: $name")
        }

    override fun toString() = "{x=$x,m=$m,a=$a,s=$s}"
}

data class QuantumMachinePart(val x: IntRange, val m: IntRange, val a: IntRange, val s: IntRange) {
    val propsSum = x.count().toLong() * m.count().toLong() * a.count().toLong() * s.count().toLong()

    fun getProperty(name: Char) =
        when (name) {
            'x' -> x
            'm' -> m
            'a' -> a
            's' -> s
            else -> throw IllegalArgumentException("Unexpected property: $name")
        }

    fun copy(property: Char, newValues: IntRange): QuantumMachinePart =
        when (property) {
            'x' -> copy(x = newValues)
            'm' -> copy(m = newValues)
            'a' -> copy(a = newValues)
            's' -> copy(s = newValues)
            else -> throw IllegalArgumentException("Unexpected property: $property")
        }

    override fun toString() = "{x=$x,m=$m,a=$a,s=$s}"
}

private fun IntRange.count(): Int {
    return last - first + 1
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
                                property = match.groups[2]!!.value.single(),
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
