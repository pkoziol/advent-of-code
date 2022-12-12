package biz.koziolek.adventofcode.year2022.day11

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val monkeys = parseMonkeys(inputFile.bufferedReader().readLines())
    val monkeys20 = playKeepAway(monkeys, rounds = 20)
    println("Monkey business after 20 rounds: ${getMonkeyBusiness(monkeys20)}")
}

data class Monkey(
    val id: Int,
    val items: List<Item>,
    val operation: (Item) -> Item,
    val test: (Item) -> Boolean,
    val targets: Map<Boolean, Int>,
    val inspectedItems: Int = 0,
) {
    fun inspectItems(worryDivider: Int): Pair<Monkey, List<Pair<Item, Int>>> =
        copy(
            items = emptyList(),
            inspectedItems = inspectedItems + items.size,
        ) to items.map { item ->
            val worryLevel = operation(item) / worryDivider
            val testResult = test(worryLevel)
            val targetID = targets[testResult]
                ?: throw IllegalStateException("No target found for $worryLevel at $this")

            worryLevel to targetID
        }

    fun addItem(item: Item): Monkey = copy(items = items + item)
}

sealed interface Item {
    operator fun plus(number: Int): Item
    operator fun times(number: Int): Item
    operator fun div(number: Int): Item
    fun square(): Item
    fun isDivisible(factor: Int): Boolean
}

data class SimpleItem(
    val value: Int
) : Item {
    override fun plus(number: Int): Item =
        copy(value = value + number)

    override fun times(number: Int): Item =
        copy(value = value * number)

    override fun div(number: Int): Item =
        copy(value = value / number)

    override fun square(): Item =
        copy(value = value * value)

    override fun isDivisible(factor: Int): Boolean =
        value % factor == 0
}

data class Multiply(val value: Int) : (Item) -> Item {
    override fun invoke(item: Item): Item = item * value
}

data class Add(val value: Int) : (Item) -> Item {
    override fun invoke(item: Item): Item = item + value
}

object Square : (Item) -> Item {
    override fun invoke(item: Item): Item = item.square()
}

data class Divisible(val value: Int) : (Item) -> Boolean {
    override fun invoke(item: Item): Boolean = item.isDivisible(value)
}

fun parseMonkeys(lines: Iterable<String>): List<Monkey> =
    buildList {
        val iterator = lines.iterator()
        while (iterator.hasNext()) {
            val firstLine = iterator.next()

            if (firstLine.startsWith("Monkey ")) {
                val id = firstLine.let { line ->
                    Regex("Monkey ([0-9]+)")
                        .find(line)
                        ?.groups?.get(1)?.value
                        ?.toInt()
                        ?: throw IllegalStateException("Could not find monkey's ID in: '$line'")
                }

                val items = iterator.next().let { line ->
                    Regex("Starting items: (.+)$")
                        .find(line)
                        ?.groups?.get(1)?.value
                        ?.split(',')
                        ?.map { SimpleItem(it.trim().toInt()) }
                        ?: throw IllegalStateException("Could parse starting items in: '$line'")
                }

                val operation = iterator.next().let { line ->
                    Regex("Operation: new = old ([+*]) ([0-9]+|old)$")
                        .find(line)
                        ?.let { result ->
                            val operator = result.groups[1]?.value
                            val operand = result.groups[2]?.value

                            if (operator == "*" && operand == "old") {
                                Square
                            } else if (operator == "*") {
                                Multiply(operand!!.toInt())
                            } else {
                                Add(operand!!.toInt())
                            }
                        }
                        ?: throw IllegalStateException("Could parse operation in: '$line'")
                }

                val test = iterator.next().let { line ->
                    Regex("Test: divisible by ([0-9]+)$")
                        .find(line)
                        ?.groups?.get(1)?.value
                        ?.toInt()
                        ?.let { Divisible(it) }
                        ?: throw IllegalStateException("Could parse test in: '$line'")
                }

                val targets = listOf(iterator.next(), iterator.next())
                    .associate { line ->
                        Regex("If (true|false): throw to monkey ([0-9]+)")
                            .find(line)
                            ?.let { result ->
                                val boolean = result.groups[1]!!.value.toBoolean()
                                val monkeyID = result.groups[2]!!.value.toInt()
                                boolean to monkeyID
                            }
                            ?: throw IllegalStateException("Could not parse target in: '$line'")
                    }

                add(
                    Monkey(
                        id = id,
                        items = items,
                        operation = operation,
                        test = test,
                        targets = targets,
                    )
                )
            }
        }
    }

fun playKeepAway(monkeys: List<Monkey>, rounds: Int, worryDivider: Int = 3): List<Monkey> =
    (1..rounds)
        .fold(monkeys) { currentMonkeys, _ -> playKeepAway(currentMonkeys, worryDivider) }

fun playKeepAway(monkeys: List<Monkey>, worryDivider: Int = 3): List<Monkey> =
    monkeys.map { it.id }
        .sorted()
        .fold(monkeys) { currentMonkeys, currentID ->
            val currentMonkey = currentMonkeys.find { it.id == currentID }!!
            val (updatedMonkey, itemsToTargets) = currentMonkey.inspectItems(worryDivider)

            currentMonkeys
                .map { monkey ->
                    when (monkey.id) {
                        currentID -> updatedMonkey
                        else -> monkey
                    }
                }
                .map { monkey ->
                    itemsToTargets.fold(monkey) { m, (item, targetID) ->
                        when (m.id) {
                            targetID -> m.addItem(item)
                            else -> m
                        }
                    }
                }
        }

fun getMonkeyBusiness(monkeys: List<Monkey>): Int =
    monkeys
        .map { it.inspectedItems }
        .sortedDescending()
        .take(2)
        .reduce(Int::times)
