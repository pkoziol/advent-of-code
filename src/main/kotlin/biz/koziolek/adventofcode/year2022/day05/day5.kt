package biz.koziolek.adventofcode.year2022.day05

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val (stacks, instructions) = parseCargo(inputFile.bufferedReader().readLines())

    val movedStacks = moveCargo(stacks, instructions)
    println(movedStacks)

    val movedStacksAtOnce = moveCargo(stacks, instructions, atOnce = true)
    println(movedStacksAtOnce)
}

data class Stacks(private val stacks: List<List<Char>>) {

    val maxHeight = stacks.maxOf { it.size }

    fun findTopOfEachStack() = stacks.map { it.last() }

    fun execute(instruction: Instruction, atOnce: Boolean): Stacks {
        val srcStack = stacks[instruction.from - 1]
        val bottom = srcStack.subList(0, srcStack.size - instruction.count)
        val top = srcStack.subList(srcStack.size - instruction.count, srcStack.size)

        val new = stacks.mapIndexed { index, chars ->
            when (index) {
                instruction.from - 1 -> bottom
                instruction.to - 1 -> chars + if (atOnce) top else top.reversed()
                else -> chars
            }
        }

        return Stacks(new)
    }

    override fun toString(): String {
        val footer = (1..stacks.size).joinToString(separator = " ") {
            " $it "
        }

        return (maxHeight - 1 downTo 0)
            .joinToString(separator = "\n", postfix = "\n$footer") { rowIndex ->
                stacks.joinToString(separator = " ") { stack ->
                    if (rowIndex < stack.size) {
                        "[${stack[rowIndex]}]"
                    } else {
                        "   "
                    }
                }
            }
    }
}

data class Instruction(
    val count: Int,
    val from: Int,
    val to: Int,
)

fun parseCargo(lines: Iterable<String>): Pair<Stacks, List<Instruction>> {
    val stacks = lines
        .takeWhile { it.isNotEmpty() }
        .reversed()
        .drop(1)
        .map { it.chunked(4) }
        .fold((1..1000).map { emptyList<Char>() }.toList()) { acc, strings ->
            acc.zip(strings) { a, b ->
                if (b.isNotBlank()) {
                    a + listOf(b[1])
                } else {
                    a
                }
            }
        }
        .let { Stacks(it) }

    val instructions = lines
        .drop(stacks.maxHeight + 2)
        .mapNotNull { line ->
            Regex("move ([0-9]+) from ([0-9]+) to ([0-9]+)")
                .find(line)
                ?.let {
                    val count = it.groupValues[1].toInt()
                    val from = it.groupValues[2].toInt()
                    val to = it.groupValues[3].toInt()
                    Instruction(count, from, to)
                }
        }

    return Pair(stacks, instructions)
}

fun moveCargo(stacks: Stacks, instructions: List<Instruction>, atOnce: Boolean = false): Stacks =
    instructions.fold(stacks) { acc, instruction ->
        acc.execute(instruction, atOnce)
    }

fun readTopOfEachStack(stacks: Stacks): String =
    stacks.findTopOfEachStack().joinToString("")
