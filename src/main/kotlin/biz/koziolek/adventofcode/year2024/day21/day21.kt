package biz.koziolek.adventofcode.year2024.day21

import biz.koziolek.adventofcode.*
import kotlin.math.sign

fun main() {
    val inputFile = findInput(object {})
    val codes = parseCodes(inputFile.bufferedReader().readLines())

    val keypads2 = listOf(NUMERIC_KEYPAD, DIRECTIONAL_KEYPAD, DIRECTIONAL_KEYPAD)
    val seqLem2 = findSequenceLengths(codes, keypads2)
    val complexityScore2 = calculateComplexityScore(codes, seqLem2)
    println("The complexity score for 2 directional keypads: $complexityScore2")

    val keypads25 = listOf(NUMERIC_KEYPAD) + List(25) { DIRECTIONAL_KEYPAD }
    val seqLen25 = findSequenceLengths(codes, keypads25)
    val complexityScore25 = calculateComplexityScore(codes, seqLen25)
    println("The complexity score for 25 directional keypads: $complexityScore25")
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

    fun isValidSequence(buttonsSeq: String, startPos: Coord = this['A']): Boolean =
        buttonsSeq.fold(startPos to true) { (currentPos, isValid), command ->
            if (!isValid) {
                currentPos to false
            } else if (currentPos !in buttons) {
                currentPos to false
            } else if (command == 'A') {
                currentPos to true
            } else {
                val direction = Direction.fromChar(command)
                val nextPos = currentPos.move(direction)
                nextPos to true
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

fun findSequenceLengths(codes: List<String>, keypads: List<Keypad>): List<Long> =
    codes.map { findSequenceLength(it, keypads) }

fun findSequenceLength(code: String, keypads: List<Keypad>): Long {
    return minKeyPressesCached(code, keypads, startPos = NUMERIC_KEYPAD['A']).first
}

private val cache = mutableMapOf<Triple<String, Int, Coord>, Pair<Long, Coord>>()

private fun minKeyPressesCached(buttonsSeq: String, keypads: List<Keypad>, startPos: Coord): Pair<Long, Coord> {
    val key = Triple(buttonsSeq, keypads.size, startPos)
    var res = cache[key]

    if (res == null) {
        res = minKeyPresses(buttonsSeq, keypads, startPos)
        cache[key] = res
    }

    return res
}

private fun minKeyPresses(buttonsSeq: String, keypads: List<Keypad>, startPos: Coord): Pair<Long, Coord> {
    require(buttonsSeq.last() == 'A') { "Button sequence has to end with A" }

    if (keypads.isEmpty()) {
        return buttonsSeq.length.toLong() to startPos
    }

    val parts = splitButtonGroups(buttonsSeq)

    if (parts.size > 1) {
        return parts.fold(0L to startPos) { (length, currentPos), part ->
            val (newLen, newPos) = minKeyPressesCached(part, keypads, currentPos)
            (length + newLen) to newPos
        }
    }

    val keypad = keypads.first()
    val encoded = findButtonPresses(parts.single(), keypad, startPos = keypad['A'])
    val permutations = permuteButtonGroups(encoded.first)
        .filter { keypad.isValidSequence(it, startPos = keypad['A']) }
        .toList()

    return permutations
        .map { seq -> minKeyPressesCached(seq, keypads.drop(1), encoded.second) }
        .minBy { (len, _) -> len }
}

private fun splitButtonGroups(buttonsSeq: String): List<String> =
    buttonsSeq
        .split('A')
        .dropLast(1)
        .map { it + "A" }

private fun permuteButtonGroups(buttonsSeq: String): List<String> {
    if (buttonsSeq.isEmpty()) {
        return listOf("")
    }

    val head = buttonsSeq.takeWhile { it != 'A' }
    val tail = buttonsSeq.dropWhile { it != 'A' }.drop(1)

    val headPerms = when {
        head.isEmpty() -> listOf("A")
        else -> head.permutations().map { it + "A" }.toList()
    }

    val tailPerms = permuteButtonGroups(tail)
    return when {
        tailPerms.isEmpty() -> headPerms
        else -> headPerms.flatMap { headPerm ->
                    tailPerms.map { tailPerm ->
                        headPerm + tailPerm
                    }
                }
    }

}

private fun String.permutations(): Sequence<String> =
    if (isEmpty()) sequenceOf(this)
    else asSequence().flatMapIndexed { index, c -> removeRange(index..index).permutations().map { c + it } }

fun findButtonPresses(targetButtonsSeq: String,
                      keypad: Keypad,
                      startPos: Coord): Pair<String, Coord> =
    targetButtonsSeq.fold("" to startPos) { (buttonsSeq, currentPos), buttonToPress ->
        val (newSeq, newPos) = findButtonPresses(buttonToPress, keypad, currentPos)
        (buttonsSeq + newSeq) to newPos
    }

fun findButtonPresses(buttonToPress: Char,
                      keypad: Keypad,
                      currentPos: Coord): Pair<String, Coord> {
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

    return newButtonsSeq to newPos
}

fun calculateComplexityScore(codes: List<String>, seqLens: List<Long>): Long =
    codes.zip(seqLens).sumOf { (code, seqLen) ->
        val numericPart = code.replace("A", "").toInt()
        numericPart * seqLen
    }
