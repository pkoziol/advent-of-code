package biz.koziolek.adventofcode.year2023.day20

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val modules = parseModules(inputFile.bufferedReader().readLines())
    println("low pulses * high pulses: ${countPulses(modules, buttonPresses = 1000)}")
    println("It takes ${findFewestButtonPresses(modules)} button presses to send low pulse to rx")
}

sealed interface Module {
    val name: String
    val destinations: List<String>
    fun processPulse(pulse: Pulse): Pair<Module, Pulse.Level?>
}

data class BroadcasterModule(override val destinations: List<String>) : Module {
    override val name = "broadcaster"
    override fun processPulse(pulse: Pulse) = this to pulse.level
}

data class FlipFlopModule(override val name: String,
                          override val destinations: List<String>,
                          val isOn: Boolean = false) : Module {
    private fun flip() = copy(isOn = !isOn)

    override fun processPulse(pulse: Pulse): Pair<Module, Pulse.Level?> =
        when (pulse.level) {
            Pulse.Level.HIGH -> this to null
            Pulse.Level.LOW -> this.flip() to if (isOn) Pulse.Level.LOW else Pulse.Level.HIGH
        }
}

data class ConjunctionModule(override val name: String,
                             val inputs: Set<String>,
                             override val destinations: List<String>,
                             val recentPulses: Map<String, Pulse.Level> = inputs.associateWith { Pulse.Level.LOW }) : Module {
    override fun processPulse(pulse: Pulse): Pair<Module, Pulse.Level?> {
        val newModule = this.update(pulse.from, pulse.level)

        return if (newModule.recentPulses.values.all { it == Pulse.Level.HIGH }) {
            newModule to Pulse.Level.LOW
        } else {
            newModule to Pulse.Level.HIGH
        }
    }

    private fun update(from: String, level: Pulse.Level): ConjunctionModule =
        copy(recentPulses = recentPulses + mapOf(from to level))
}

data class UntypedModule(override val name: String,
                         override val destinations: List<String> = emptyList()) : Module {
    override fun processPulse(pulse: Pulse) = this to pulse.level
}

data class Pulse(val from: String, val to: String, val level: Level) {
    enum class Level { LOW, HIGH }

    override fun toString() = "$from -${level.name.lowercase()}-> $to"

    companion object {
        val BUTTON_PRESS = Pulse(from = "button", to = "broadcaster", level = Level.LOW)
    }
}

fun parseModules(lines: List<String>): Map<String, Module> {
    val inputToOutputMap = lines.associate { line ->
        val (namePart, destPart) = line.split("->")
        val nameAndType = namePart.trim()
        val destinations = destPart.split(',').map { it.trim() }
        nameAndType to destinations
    }

    return inputToOutputMap.map { (nameAndType, destinations) ->
        when {
            nameAndType == "broadcaster" ->
                BroadcasterModule(destinations)

            nameAndType.startsWith('%') ->
                FlipFlopModule(
                    name = getName(nameAndType),
                    destinations = destinations
                )

            nameAndType.startsWith('&') ->
                ConjunctionModule(
                    name = getName(nameAndType),
                    inputs = inputToOutputMap
                        .filterValues { it.contains(getName(nameAndType)) }
                        .keys
                        .map { getName(it) }
                        .toSet(),
                    destinations = destinations
                )

            else ->
                UntypedModule(nameAndType, destinations)
        }
    }.associateBy { it.name }
}

private fun getName(nameAndType: String): String =
    when {
        nameAndType.startsWith('%') -> nameAndType.drop(1)
        nameAndType.startsWith('&') -> nameAndType.drop(1)
        else -> nameAndType
    }

fun generatePulses(modules: Map<String, Module>, initialPulse: Pulse): Sequence<Pair<Pulse, Map<String, Module>>> =
    sequence {
        val pulsesToProcess = mutableListOf(initialPulse)
        var currentModules = modules

        while (pulsesToProcess.isNotEmpty()) {
            val pulse = pulsesToProcess.removeFirst()
            val module = currentModules[pulse.to]

            if (module != null) {
                val (newModule, nextLevel) = module.processPulse(pulse)
                currentModules = currentModules + (newModule.name to newModule)

                if (nextLevel != null) {
                    pulsesToProcess.addAll(newModule.destinations.map {
                        Pulse(
                            from = newModule.name,
                            to = it,
                            level = nextLevel
                        )
                    })
                }
            }

            yield(pulse to currentModules)
        }
    }

fun Sequence<Pair<Pulse, Map<String, Module>>>.getLatestModulesAndAllPulses(): Pair<Map<String, Module>, List<Pulse>> =
    fold(Pair(emptyMap(), emptyList())) { (_, pulses), (pulse, newModules) ->
        newModules to (pulses + pulse)
    }

fun countPulses(modules: Map<String, Module>, buttonPresses: Int): Long {
    var currentModules = modules
    var lowCount = 0L
    var highCount = 0L

    for (i in 1..buttonPresses) {
        for ((pulse, newModules) in generatePulses(currentModules, Pulse.BUTTON_PRESS)) {
            currentModules = newModules

            if (pulse.level == Pulse.Level.LOW) {
                lowCount++
            } else {
                highCount++
            }
        }
    }

    return lowCount * highCount
}

fun findFewestButtonPresses(modules: Map<String, Module>): Long {
    var currentModules = modules
    val rxInputName = currentModules.values.single { it.destinations == listOf("rx") }.name
    val firstHighs: MutableMap<String, Int?> =
        (currentModules[rxInputName] as ConjunctionModule)
            .inputs
            .associateWith { null }
            .toMutableMap()

    var buttonPresses = 0
    while (true) {
        buttonPresses++

        for ((pulse, newModules) in generatePulses(currentModules, Pulse.BUTTON_PRESS)) {
            currentModules = newModules

            if (pulse.to == rxInputName && pulse.level == Pulse.Level.HIGH) {
                firstHighs.computeIfAbsent(pulse.from) { buttonPresses }
            }

            if (firstHighs.all { it.value != null }) {
                return firstHighs.values.fold(1L) { acc, i -> acc * i!! }
            }
        }
    }
}

fun toGraphvizString(modules: Map<String, Module>): String {
    val allModules = modules.values
        .flatMap { it.destinations }
        .distinct()
        .map { modules[it] ?: UntypedModule(it) }

    return modules.values
        .flatMap { module -> module.destinations.map { module.name to it } }
        .joinToString(
            prefix = """
                    digraph G {
                        rankdir=LR
                """.trimIndent() + allModules.joinToString(
                prefix = "\n    ",
                postfix = "\n",
                separator = "\n    "
            ) { "${it.name} [shape=${getGraphvizShape(it)}];" },
            postfix = "\n}",
            separator = "\n"
        ) { "    ${it.first} -> ${it.second}" }
}

private fun getGraphvizShape(module: Module) =
    when (module) {
        is BroadcasterModule -> "hexagon"
        is ConjunctionModule -> "box"
        is FlipFlopModule -> "diamond"
        is UntypedModule -> "oval"
    }
