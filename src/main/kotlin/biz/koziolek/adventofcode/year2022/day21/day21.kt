package biz.koziolek.adventofcode.year2022.day21

import biz.koziolek.adventofcode.findInput
import java.util.*

fun main() {
    val inputFile = findInput(object {})
    val yellingMonkeys = parseYellingMonkeys(inputFile.bufferedReader().readLines())
    println("'root' monkey yells: ${findYelledNumber("root", yellingMonkeys)}")
}

sealed interface YellingMonkey {
    val id: String
}

data class SpecificNumberYellingMonkey(
    override val id: String,
    val number: Long,
) : YellingMonkey

data class MathOperationYellingMonkey(
    override val id: String,
    val operation: Char,
    val operand1: String,
    val operand2: String,
) : YellingMonkey

fun parseYellingMonkeys(lines: Iterable<String>): List<YellingMonkey> =
    lines.map { line ->
        Regex("^([a-z]+): (([0-9]+)|(([a-z]+) ([/*+-]) ([a-z]+)))$")
            .find(line)
            ?.let { result ->
                val id = result.groups[1]!!.value

                if (result.groups[3] != null) {
                    val number = result.groups[3]!!.value.toLong()
                    SpecificNumberYellingMonkey(id, number)
                } else {
                    val operand1 = result.groups[5]!!.value
                    val operation = result.groups[6]!!.value.single()
                    val operand2 = result.groups[7]!!.value
                    MathOperationYellingMonkey(id, operation, operand1, operand2)
                }
            }
            ?: throw IllegalArgumentException("Cannot parse '$line'")
    }

fun findYelledNumber(monkeyID: String, yellingMonkeys: List<YellingMonkey>): Long {
    val waitingMonkeys: Stack<String> = Stack()
    waitingMonkeys.push(monkeyID)

    val monkeyNumbers = hashMapOf<String, Long>()

    while (waitingMonkeys.isNotEmpty()) {
        val currentMonkeyID = waitingMonkeys.pop()
        val currentMonkey = yellingMonkeys.single { it.id == currentMonkeyID }

        if (currentMonkey.id in monkeyNumbers) {
            continue
        }

        when (currentMonkey) {
            is SpecificNumberYellingMonkey -> {
                monkeyNumbers[currentMonkey.id] = currentMonkey.number
            }
            is MathOperationYellingMonkey -> {
                if (currentMonkey.operand1 in monkeyNumbers && currentMonkey.operand2 in monkeyNumbers) {
                    val number1 = monkeyNumbers[currentMonkey.operand1]!!
                    val number2 = monkeyNumbers[currentMonkey.operand2]!!

                    val number = when (currentMonkey.operation) {
                        '+' -> number1 + number2
                        '-' -> number1 - number2
                        '*' -> number1 * number2
                        '/' -> number1 / number2
                        else -> throw IllegalArgumentException("Unsupported operation: ${currentMonkey.operation}")
                    }
                    monkeyNumbers[currentMonkey.id] = number
                } else {
                    waitingMonkeys.push(currentMonkey.id)
                    waitingMonkeys.push(currentMonkey.operand1)
                    waitingMonkeys.push(currentMonkey.operand2)
                }
            }
        }
    }

    return monkeyNumbers[monkeyID]!!
}
