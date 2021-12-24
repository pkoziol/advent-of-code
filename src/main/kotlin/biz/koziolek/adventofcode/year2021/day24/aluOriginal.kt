package biz.koziolek.adventofcode.year2021.day24

import java.util.*

fun main() {
    val goodNums = mutableListOf<List<Int>>()


    val goodNumsAsLongs = goodNums.map { it.joinToString(separator = "").toLong() }

    val max = goodNumsAsLongs.maxOf { it }
    val min = goodNumsAsLongs.minOf { it }
    println(max)
    println(min)
}

fun verifySerialNumber(serialNumber: Long, debug: Boolean = false): Boolean {
    val input = ArrayDeque(serialNumber.toString().map { it.digitToInt().toLong() })

    var x = 0L
    var y = 0L
    var z = 0L
    var w = 0L

    w = input.poll()
    x *= 0
    x += z
    x %= 26
    z /= 1
    x += 14
    x = if (x == w) 1 else 0
    x = if (x == 0L) 1 else 0
    y *= 0
    y += 25
    y *= x
    y += 1
    z *= y
    y *= 0
    y += w
    y += 1
    y *= x
    z += y
    if (debug) println("z1=$z")

    w = input.poll()
    x *= 0
    x += z
    x %= 26
    z /= 1
    x += 15
    x = if (x == w) 1 else 0
    x = if (x == 0L) 1 else 0
    y *= 0
    y += 25
    y *= x
    y += 1
    z *= y
    y *= 0
    y += w
    y += 7
    y *= x
    z += y
    if (debug) println("z2=$z")

    w = input.poll()
    x *= 0
    x += z
    x %= 26
    z /= 1
    x += 15
    x = if (x == w) 1 else 0
    x = if (x == 0L) 1 else 0
    y *= 0
    y += 25
    y *= x
    y += 1
    z *= y
    y *= 0
    y += w
    y += 13
    y *= x
    z += y
    if (debug) println("z3=$z")

    w = input.poll()
    x *= 0
    x += z
    x %= 26
    z /= 26
    x += -6
    x = if (x == w) 1 else 0
    x = if (x == 0L) 1 else 0
    y *= 0
    y += 25
    y *= x
    y += 1
    z *= y
    y *= 0
    y += w
    y += 10
    y *= x
    z += y
    if (debug) println("z4=$z")

    w = input.poll()
    x *= 0
    x += z
    x %= 26
    z /= 1
    x += 14
    x = if (x == w) 1 else 0
    x = if (x == 0L) 1 else 0
    y *= 0
    y += 25
    y *= x
    y += 1
    z *= y
    y *= 0
    y += w
    y += 0
    y *= x
    z += y
    if (debug) println("z5=$z")

    w = input.poll()
    x *= 0
    x += z
    x %= 26
    z /= 26
    x += -4
    x = if (x == w) 1 else 0
    x = if (x == 0L) 1 else 0
    y *= 0
    y += 25
    y *= x
    y += 1
    z *= y
    y *= 0
    y += w
    y += 13
    y *= x
    z += y
    if (debug) println("z6=$z")

    w = input.poll()
    x *= 0
    x += z
    x %= 26
    z /= 1
    x += 15
    x = if (x == w) 1 else 0
    x = if (x == 0L) 1 else 0
    y *= 0
    y += 25
    y *= x
    y += 1
    z *= y
    y *= 0
    y += w
    y += 11
    y *= x
    z += y
    if (debug) println("z7=$z")

    w = input.poll()
    x *= 0
    x += z
    x %= 26
    z /= 1
    x += 15
    x = if (x == w) 1 else 0
    x = if (x == 0L) 1 else 0
    y *= 0
    y += 25
    y *= x
    y += 1
    z *= y
    y *= 0
    y += w
    y += 6
    y *= x
    z += y
    if (debug) println("z8=$z")

    w = input.poll()
    x *= 0
    x += z
    x %= 26
    z /= 1
    x += 11
    x = if (x == w) 1 else 0
    x = if (x == 0L) 1 else 0
    y *= 0
    y += 25
    y *= x
    y += 1
    z *= y
    y *= 0
    y += w
    y += 1
    y *= x
    z += y
    if (debug) println("z9=$z")

    w = input.poll()
    x *= 0
    x += z
    x %= 26
    z /= 26
    x += 0
    x = if (x == w) 1 else 0
    x = if (x == 0L) 1 else 0
    y *= 0
    y += 25
    y *= x
    y += 1
    z *= y
    y *= 0
    y += w
    y += 7
    y *= x
    z += y
    if (debug) println("z10=$z")

    w = input.poll()
    x *= 0
    x += z
    x %= 26
    z /= 26
    x += 0
    x = if (x == w) 1 else 0
    x = if (x == 0L) 1 else 0
    y *= 0
    y += 25
    y *= x
    y += 1
    z *= y
    y *= 0
    y += w
    y += 11
    y *= x
    z += y
    if (debug) println("z11=$z")

    w = input.poll()
    x *= 0
    x += z
    x %= 26
    z /= 26
    x += -3
    x = if (x == w) 1 else 0
    x = if (x == 0L) 1 else 0
    y *= 0
    y += 25
    y *= x
    y += 1
    z *= y
    y *= 0
    y += w
    y += 14
    y *= x
    z += y
    if (debug) println("z12=$z")

    w = input.poll()
    x *= 0
    x += z
    x %= 26
    z /= 26
    x += -9
    x = if (x == w) 1 else 0
    x = if (x == 0L) 1 else 0
    y *= 0
    y += 25
    y *= x
    y += 1
    z *= y
    y *= 0
    y += w
    y += 4
    y *= x
    z += y
    if (debug) println("z13=$z")

    w = input.poll()
    x *= 0
    x += z
    x %= 26
    z /= 26
    x += -9
    x = if (x == w) 1 else 0
    x = if (x == 0L) 1 else 0
    y *= 0
    y += 25
    y *= x
    y += 1
    z *= y
    y *= 0
    y += w
    y += 10
    y *= x
    z += y
    if (debug) println("z14=$z")

    return z == 0L
}
