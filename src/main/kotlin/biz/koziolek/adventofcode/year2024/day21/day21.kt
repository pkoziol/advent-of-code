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

fun findButtonPresses(targetButtonsSeq: List<String>, keypads: List<Keypad>): List<String> =
    targetButtonsSeq.map {
        findButtonPresses(it, keypads)
            .filter { buttonsSequences ->
                val allValid = buttonsSequences.reversed()
                    .zip(keypads.reversed())
                    .all { (buttonsSeq, keypad) ->
                        isValidButtonsSequence(buttonsSeq, keypad)
                    }
                allValid
            }
            .minBy { it.last().length }
            .last()
    }

fun isValidButtonsSequence(buttonsSeq: String, keypad: Keypad): Boolean =
    try {
        keypad.pressButtons(buttonsSeq)
        true
    } catch (e: IllegalArgumentException) {
        false
    }

fun findButtonPresses(targetButtonsSeq: String, keypad: Keypad, debug: Boolean = false): String {
//    val minAnswer = allAnswers.minBy { it.last().length }
//    println("targetButtonsSeq: $targetButtonsSeq")
//    println("org: $originalAnswer")
//    println("min: $minAnswer")

    val allAnswers = findButtonPresses(targetButtonsSeq, listOf(keypad))
    if (debug) {
        allAnswers
            .map { it to it.last().length }
            .sortedByDescending { it.second }
            .forEach { println(it) }
    }

    return allAnswers.minBy { it.last().length }.last()
}

fun findButtonPresses(targetButtonsSeq: String, keypads: List<Keypad>, debug: Boolean = false): List<List<String>> {
//    println("abc".permutations())
//    return ""

//    println("--------------------")
//    // <Av<AA>>^A<A>AvAA<A>^A<vA>^A
//    permuteButtonGroups(listOf("b", "xy", "", "123", "q", "G", "n", "", "!", "ty", "KL", "FV"))
//        .forEach { println(it) }
//    println("--------------------")

    if (debug) {
        println("targetButtonsSeq: $targetButtonsSeq - keypads: ${keypads.size}")
    }

//    if (keypads == 0) {
//        return listOf(
//            listOf(_findButtonPresses(targetButtonsSeq, keypad))
//        )
//    }

    val targetPermutations =
        if (shouldBePermuted(targetButtonsSeq)) {
            targetButtonsSeq
                .split('A')
                .let { permuteButtonGroups(it.dropLast(1)) }
                .map { it.joinToString("A", postfix = "A") }
        }
        else {
            listOf(targetButtonsSeq)
        }

    val allAnswers = targetPermutations
        .map { listOf(it, findButtonPresses(it, keypads.first())) }

    if (keypads.size == 1) {
//        println("allAnswers size: ${allAnswers.size}")
        return allAnswers
    }

    val remainingKeypads = keypads.drop(1)
    val moreAnswers = allAnswers
        .flatMap { outer -> findButtonPresses(outer.last(), remainingKeypads).map { outer.dropLast(1) + it } }

//    println("moreAnswers size: ${moreAnswers.size}")

    return moreAnswers
}

private fun shouldBePermuted(string: String): Boolean =
    "0123456789".none { string.contains(it) }

private fun permuteButtonGroups(groups: List<String>): List<List<String>> {
//    println(groups)
    if (groups.isEmpty()) {
        return emptyList()
    }

    val head = groups.first()
    val tail = groups.drop(1)

    val headPerms = if (head.isNotEmpty()) head.permutations().toList() else listOf("")
    val tailPerms = permuteButtonGroups(tail)

    if (tailPerms.isEmpty()) {
//        println("tailPerms is empty - returning $headPerms")
        return headPerms.map { listOf(it) }
    }

    return headPerms.flatMap { headPerm ->
//        println("headPerm: $headPerm - tailPerms: $tailPerms")
        tailPerms.map { tailPerm ->
//            println("headPerm: $headPerm, tailPerm: $tailPerm")
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
//        println("Need to press $buttonToPress going from ${keypad.buttons[currentPos]} ($currentPos). Previously pressed: $buttonsSeq")

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

        try {
            while (xDiff != 0) {
                move(Coord(xDiff.sign, 0))
            }
            while (yDiff != 0) {
                move(Coord(0, yDiff.sign))
            }
        } catch (e: IllegalStateException) {
            xDiff = targetPos.x - currentPos.x
            yDiff = targetPos.y - currentPos.y
            newPos = currentPos
            newButtonsSeq = ""

            while (yDiff != 0) {
                move(Coord(0, yDiff.sign))
            }
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
//        println("$numericPart: ${buttons.length}")
        numericPart * buttons.length
    }
