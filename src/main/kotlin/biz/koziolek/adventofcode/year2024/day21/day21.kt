package biz.koziolek.adventofcode.year2024.day21

import biz.koziolek.adventofcode.*
import kotlin.math.sign

fun main() {
    val inputFile = findInput(object {})
    val codes = parseCodes(inputFile.bufferedReader().readLines())

    val keypads = listOf(
        NUMERIC_KEYPAD,
        DIRECTIONAL_KEYPAD,
        DIRECTIONAL_KEYPAD,
    )
    val buttonsSeq = findButtonPresses(codes, keypads)
    val complexityScore = calculateComplexityScore(codes, buttonsSeq)
    println("The complexity score is $complexityScore")
}

fun parseCodes(lines: Iterable<String>): List<String> =
    lines.toList()

val NUMERIC_KEYPAD = Keypad.fromString("""
    789
    456
    123
     0A
""".trimIndent())

val DIRECTIONAL_KEYPAD = Keypad.fromString("""
     ^A
    <v>
""".trimIndent())

data class Keypad(val buttons: Map<Coord, Char>) {

    operator fun get(char: Char): Coord =
        buttons.entries.single { it.value == char }.key

    fun pressButtons(buttonsSeq: String, start: Char = 'A'): String =
        buttonsSeq.fold(this[start] to "") { (currentPos, remoteButtonsSeq), command ->
            if (currentPos !in buttons) {
                throw IllegalArgumentException("Invalid position $currentPos (buttons sequence so far: $remoteButtonsSeq)")
            }

            if (command == 'A') {
                val c = buttons[currentPos]
                currentPos to (remoteButtonsSeq + c)
            } else {
                val direction = Direction.fromChar(command)
                val nextPos = currentPos.move(direction)
                nextPos to remoteButtonsSeq
            }
        }.second

    fun isValidSequence(buttonsSeq: String, start: Char = 'A'): Boolean =
        buttonsSeq.fold(this[start] to true) { (currentPos, isValid), command ->
            if (currentPos !in buttons) {
                currentPos to false
            } else if (command == 'A') {
                currentPos to isValid
            } else {
                val direction = Direction.fromChar(command)
                val nextPos = currentPos.move(direction)
                nextPos to isValid
            }
        }.second

    override fun toString(): String =
        buttons.to2DString(' ')

    companion object {
        fun fromString(string: String): Keypad =
            string.split("\n")
                .parse2DMap()
                .filter { it.second != ' ' }
                .toMap()
                .let { Keypad(it) }
    }
}

private val cache = mutableMapOf<Pair<String, Int>, String>()

fun findButtonPresses(targetButtonsSeq: String, keypads: List<Keypad>): String =
    findButtonPresses(listOf(targetButtonsSeq), keypads).single()

fun findButtonPresses(targetButtonsSeq: List<String>, keypads: List<Keypad>): List<String> {
    cache.clear()
    return targetButtonsSeq.map { findButtonPressesInternalMemorized(it, keypads) }
}

private fun findButtonPressesInternalMemorized(targetButtonsSeq: String,
                                               keypads: List<Keypad>,
                                               keypadIndex: Int = 0): String {
    val key = Pair(targetButtonsSeq, keypadIndex)
    var ans = cache[key]

    if (ans == null) {
        ans = findButtonPressesInternal(targetButtonsSeq, keypads, keypadIndex)
        cache[key] = ans
    }

    return ans
}

private fun findButtonPressesInternal(targetButtonsSeq: String,
                                      keypads: List<Keypad>,
                                      keypadIndex: Int = 0): String {
    val targetPermutations =
        if (shouldBePermuted(targetButtonsSeq)) {
            targetButtonsSeq
                .split('A')
                .let { permuteButtonGroups(it.dropLast(1)) }
                .map { it.joinToString("A", postfix = "A") }
                .filter { keypadIndex - 1 !in keypads.indices || keypads[keypadIndex - 1].isValidSequence(it) }
        }
        else {
            listOf(targetButtonsSeq)
        }

    val allAnswers = targetPermutations
        .map { findButtonPresses(it, keypads[keypadIndex]) }

    if (keypadIndex == keypads.size - 1) {
        return allAnswers.minBy { it.length }
    }

    val moreAnswers = allAnswers
        .map { outer -> findButtonPressesInternalMemorized(outer, keypads, keypadIndex + 1) }
        .minBy { it.length }

    return moreAnswers
}

private fun shouldBePermuted(string: String): Boolean =
    "0123456789".none { string.contains(it) }

private fun permuteButtonGroups(groups: List<String>): List<List<String>> {
    if (groups.isEmpty()) {
        return emptyList()
    }

    val head = groups.first()
    val tail = groups.drop(1)

    val headPerms = if (head.isNotEmpty()) head.permutations().toList() else listOf("")
    val tailPerms = permuteButtonGroups(tail)

    if (tailPerms.isEmpty()) {
        return headPerms.map { listOf(it) }
    }

    return headPerms.flatMap { headPerm ->
        tailPerms.map { tailPerm ->
            listOf(headPerm) + tailPerm
        }
    }
}

private fun String.permutations(): Sequence<String> =
    if (length == 1) sequenceOf(this)
    else asSequence().flatMapIndexed { index, c -> removeRange(index..index).permutations().map { c + it } }

private fun findButtonPresses(targetButtonsSeq: String,
                              keypad: Keypad): String =
    targetButtonsSeq.fold(keypad['A'] to "") { (currentPos, buttonsSeq), buttonToPress ->
        val targetPos = keypad[buttonToPress]
        var xDiff = targetPos.x - currentPos.x
        var yDiff = targetPos.y - currentPos.y
        var newPos = currentPos
        var newButtonsSeq = ""

        fun move(delta: Coord): Boolean {
            val nextPos = newPos + delta
            if (keypad.buttons.containsKey(nextPos)) {
                newPos = nextPos
                newButtonsSeq += Direction.fromCoord(delta).char
                xDiff = targetPos.x - nextPos.x
                yDiff = targetPos.y - nextPos.y
                return true
            } else {
                throw IllegalStateException("Cannot move to $nextPos")
            }
        }

        var restart = false
        while (xDiff != 0) {
            val delta = Coord(xDiff.sign, 0)
            val nextPos = newPos + delta
            if (!keypad.buttons.containsKey(nextPos)) {
                restart = true
                break
            }
            move(delta)
        }

        if (restart) {
            xDiff = targetPos.x - currentPos.x
            yDiff = targetPos.y - currentPos.y
            newPos = currentPos
            newButtonsSeq = ""
        }

        while (yDiff != 0) {
            move(Coord(0, yDiff.sign))
        }

        if (restart) {
            while (xDiff != 0) {
                move(Coord(xDiff.sign, 0))
            }
        }

        newButtonsSeq += 'A'

        newPos to (buttonsSeq + newButtonsSeq)
    }.second

fun calculateComplexityScore(codes: List<String>, buttonsSeq: List<String>): Int =
    codes.zip(buttonsSeq).sumOf { (code, buttons) ->
        val numericPart = code.replace("A", "").toInt()
        numericPart * buttons.length
    }
