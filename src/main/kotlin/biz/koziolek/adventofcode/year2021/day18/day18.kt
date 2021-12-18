package biz.koziolek.adventofcode.year2021.day18

import biz.koziolek.adventofcode.*
import java.util.*
import kotlin.math.ceil
import kotlin.math.floor

fun main() {
    val inputFile = findInput(object {})
    val lines = inputFile.bufferedReader().readLines()
    val numbers = parseSnailfishNumbers(lines)

    val sum = addAndReduceAll(numbers)
    println("Sum magnitude is: ${sum.magnitude}")

    val (_, _, maxMagnitude) = findMaxPossibleMagnitudeOfTwo(numbers)
    println("Max possible magnitude of sum of two numbers is: $maxMagnitude")
}

sealed interface SnailfishNumber {
    val level: Int
    val magnitude: Int
    fun asText(): String
    fun increaseLevel(): SnailfishNumber
    fun reduce(): SnailfishNumber
    fun reduceOnce(): SnailfishNumber
    fun explode(): SnailfishNumber
    fun split(): SnailfishNumber
}

data class SnailfishPair(
    val left: SnailfishNumber,
    val right: SnailfishNumber,
    override val level: Int
) : SnailfishNumber {
    override val magnitude by lazy { 3 * left.magnitude + 2 * right.magnitude }

    override fun asText() = "[${left.asText()},${right.asText()}]"

    override fun increaseLevel(): SnailfishPair = copy(
        left = left.increaseLevel(),
        right = right.increaseLevel(),
        level = level + 1
    )

    operator fun plus(other: SnailfishPair): SnailfishPair =
        SnailfishPair(
            left = this.increaseLevel(),
            right = other.increaseLevel(),
            level = 0
        )

    override fun reduce(): SnailfishPair {
        var prev: SnailfishNumber = this
        var next = reduceOnce()
        while (next != prev) {
            prev = next
            next = next.reduceOnce()
        }
        return next
    }

    override fun reduceOnce(): SnailfishPair {
        val newThis = this.explode()
        if (newThis != this) {
            return newThis
        }

        val newThis2 = this.split()
        if (newThis2 != this) {
            return newThis2
        }

        return this
    }

    override fun explode(): SnailfishPair {
        return when (val explosionResult = explodeInternal(NoExplosion)) {
            is NoExplosion -> this
            is LeftAndRight -> throw IllegalStateException("Not consumed left nor right should never happen (I think?)")
            is OnlyLeft -> explosionResult.number
            is OnlyRight -> explosionResult.number as SnailfishPair
            is ExplosionComplete -> explosionResult.number as SnailfishPair
        }
    }

    private fun explodeInternal(result: ExplosionResult): ExplosionResult =
        when (result) {
            is NoExplosion -> {
                if (level >= 4 && left is SnailfishRegular && right is SnailfishRegular) {
                    LeftAndRight(left.value, right.value, SnailfishRegular(value = 0, level = level))
                } else {
                    val leftResult = if (left is SnailfishPair) {
                        left.explodeInternal(result)
                    } else {
                        NoExplosion
                    }

                    when (leftResult) {
                        is NoExplosion -> {
                            val rightResult = if (right is SnailfishPair) {
                                right.explodeInternal(leftResult)
                            } else {
                                NoExplosion
                            }

                            when (rightResult) {
                                is NoExplosion -> NoExplosion
                                is LeftAndRight -> {
                                    val leftResult2 = when (left) {
                                        is SnailfishRegular -> OnlyRight(rightResult.right, left.copy(value = left.value + rightResult.left))

                                        // BTW, this is impossible and won't work if called.
                                        // 1) Left cannot be a pair and not be exploded at the same time when right was exploded.
                                        //    They are at the same level, so left or something inside it must have been exploded.
                                        // 2) Left receives LeftAndRight as argument, which is not supported by outer-most when.
                                        is SnailfishPair -> left.explodeInternal(rightResult)
                                    }
                                    when (leftResult2) {
                                        is NoExplosion -> throw IllegalStateException("Impossible")
                                        is ExplosionComplete -> throw IllegalStateException("Impossible")
                                        is LeftAndRight -> throw IllegalStateException("Impossible")
                                        is OnlyLeft -> throw IllegalStateException("Impossible")
                                        is OnlyRight -> OnlyRight(right = rightResult.right, number = copy(
                                            left = leftResult2.number,
                                            right = rightResult.number,
                                        ))
                                    }
                                }
                                is OnlyLeft -> {
                                    val leftResult2 = when (left) {
                                        is SnailfishRegular -> ExplosionComplete(left.copy(value = left.value + rightResult.left))
                                        is SnailfishPair -> left.explodeInternal(rightResult)
                                    }
                                    when (leftResult2) {
                                        is NoExplosion -> throw IllegalStateException("Impossible")
                                        is ExplosionComplete -> ExplosionComplete(number = copy(
                                            left = leftResult2.number,
                                            right = rightResult.number,
                                        ))
                                        is LeftAndRight -> throw IllegalStateException("Impossible")
                                        is OnlyLeft -> throw IllegalStateException("Impossible")
                                        is OnlyRight -> throw IllegalStateException("Impossible")
                                    }
                                }
                                is OnlyRight -> rightResult.copy(number = copy(
                                    right = rightResult.number,
                                ))
                                is ExplosionComplete -> rightResult.copy(number = copy(
                                    right = rightResult.number,
                                ))
                            }
                        }
                        is LeftAndRight -> {
                            val rightResult = when (right) {
                                is SnailfishRegular -> ExplosionComplete(right.copy(value = right.value + leftResult.right))
                                is SnailfishPair -> right.explodeInternal(OnlyRight(right = leftResult.right, number = this))
                            }
                            when (rightResult) {
                                is NoExplosion -> throw IllegalStateException("Impossible")
                                is ExplosionComplete -> OnlyLeft(left = leftResult.left, number = copy(
                                    left = leftResult.number,
                                    right = rightResult.number,
                                ))
                                is LeftAndRight -> throw IllegalStateException("Impossible")
                                is OnlyLeft -> throw IllegalStateException("Impossible")
                                is OnlyRight -> throw IllegalStateException("Impossible")
                            }
                        }
                        is ExplosionComplete -> leftResult.copy(number = copy(
                            left = leftResult.number,
                        ))
                        is OnlyLeft -> leftResult.copy(number = copy(
                            left = leftResult.number,
                        ))
                        is OnlyRight -> {
                            val rightResult = when (right) {
                                is SnailfishRegular -> ExplosionComplete(right.copy(value = right.value + leftResult.right))
                                is SnailfishPair -> right.explodeInternal(leftResult)
                            }
                            when (rightResult) {
                                is NoExplosion -> throw IllegalStateException("Impossible")
                                is ExplosionComplete -> ExplosionComplete(copy(
                                    left = leftResult.number,
                                    right = rightResult.number,
                                ))
                                is LeftAndRight -> throw IllegalStateException("Impossible")
                                is OnlyLeft -> throw IllegalStateException("Impossible")
                                is OnlyRight -> throw IllegalStateException("Impossible")
                            }
                        }
                    }
                }
            }
            is LeftAndRight -> throw IllegalStateException("Don't know what to do - please do not pass me that")
            is OnlyLeft -> {
                val rightResult = when (right) {
                    is SnailfishRegular -> ExplosionComplete(right.copy(value = right.value + result.left))
                    is SnailfishPair -> right.explodeInternal(result)
                }
                when (rightResult) {
                    is NoExplosion -> throw IllegalStateException("Impossible")
                    is ExplosionComplete -> rightResult.copy(number = copy(
                        right = rightResult.number,
                    ))
                    is LeftAndRight -> throw IllegalStateException("Impossible")
                    is OnlyLeft -> throw IllegalStateException("Impossible")
                    is OnlyRight -> throw IllegalStateException("Impossible")
                }
            }
            is OnlyRight -> {
                val leftResult = when (left) {
                    is SnailfishRegular -> ExplosionComplete(left.copy(value = left.value + result.right))
                    is SnailfishPair -> left.explodeInternal(result)
                }
                when (leftResult) {
                    is NoExplosion -> throw IllegalStateException("Impossible")
                    is ExplosionComplete -> leftResult.copy(number = copy(
                        left = leftResult.number,
                    ))
                    is LeftAndRight -> throw IllegalStateException("Impossible")
                    is OnlyLeft -> throw IllegalStateException("Impossible")
                    is OnlyRight -> throw IllegalStateException("Impossible")
                }
            }
            is ExplosionComplete -> throw IllegalStateException("Impossible")
        }

    override fun split(): SnailfishPair {
        val newLeft = left.split()
        if (newLeft != left) {
            return copy(left = newLeft)
        }

        val newRight = right.split()
        if (newRight != left) {
            return copy(right = newRight)
        }

        return this
    }

    private sealed interface ExplosionResult

    private object NoExplosion : ExplosionResult

    private data class LeftAndRight(val left: Int, val right: Int, val number: SnailfishNumber) : ExplosionResult

    private data class OnlyLeft(val left: Int, val number: SnailfishPair) : ExplosionResult

    private data class OnlyRight(val right: Int, val number: SnailfishNumber) : ExplosionResult

    private data class ExplosionComplete(val number: SnailfishNumber) : ExplosionResult
}

data class SnailfishRegular(
    val value: Int,
    override val level: Int
) : SnailfishNumber {
    override val magnitude = value

    override fun asText() = value.toString()

    override fun increaseLevel(): SnailfishRegular = copy(level = level + 1)

    override fun reduce() = reduceOnce()

    override fun reduceOnce() = split()

    override fun explode() = this

    override fun split(): SnailfishNumber =
        if (value < 10) {
            this
        } else {
            SnailfishPair(
                left = SnailfishRegular(
                    value = floor(value / 2.0).toInt(),
                    level = level + 1
                ),
                right = SnailfishRegular(
                    value = ceil(value / 2.0).toInt(),
                    level = level + 1
                ),
                level = level
            )
        }
}

fun parseSnailfishNumbers(lines: Iterable<String>): List<SnailfishPair> =
    lines.map { parseSnailfishNumber(it) }

fun parseSnailfishNumber(line: String): SnailfishPair {
    try {
        val stack: Deque<SnailfishNumber> = ArrayDeque()
        var currentLevel = 0
        var readingRegular = false

        for (char in line) {
            when (char) {
                '[' -> {
                    currentLevel++
                }
                ']' -> {
                    readingRegular = false
                    currentLevel--

                    check(stack.size >= 2) { "Stack does not have at least 2 elements" }

                    val right = stack.pop()
                    val left = stack.pop()
                    stack.push(SnailfishPair(left, right, currentLevel))
                }
                ',' -> {
                    readingRegular = false
                }
                else -> {
                    val regular = if (readingRegular) {
                        check(stack.peek() is SnailfishRegular) { "Stack does not have regular number at the top" }
                        stack.pop() as SnailfishRegular
                    } else {
                        SnailfishRegular(0, currentLevel)
                    }

                    stack.push(SnailfishRegular(regular.value * 10 + char.digitToInt(), currentLevel))
                    readingRegular = true
                }
            }
        }

        check(stack.size == 1) { "Stack does not have exactly 1 element, but ${stack.size}" }
        check(stack.peek() is SnailfishPair) { "Stack does not have pair at the top" }

        return stack.first as SnailfishPair
    } catch (e: Exception) {
        throw IllegalArgumentException("Could not parse: $line", e)
    }
}

fun addAndReduceAll(numbers: Iterable<SnailfishPair>): SnailfishPair =
    numbers.reduce { a, b -> (a + b).reduce() }

fun findMaxPossibleMagnitudeOfTwo(numbers: Iterable<SnailfishPair>): Triple<SnailfishPair, SnailfishPair, Int> =
    sequence {
        for (a in numbers) {
            for (b in numbers) {
                if (a != b) {
                    yield(a to b)
                }
            }
        }
    }
        .map { (a, b) -> Triple(a, b, (a + b).reduce().magnitude) }
        .sortedByDescending { it.third }
        .first()
