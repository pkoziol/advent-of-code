package biz.koziolek.adventofcode.year2021.day24

import java.util.*

@Suppress("JoinDeclarationAndAssignment", "DuplicatedCode", "VARIABLE_WITH_REDUNDANT_INITIALIZER")
fun verifySerialNumberSimplified(serialNumber: Long): Boolean {
    val input = ArrayDeque(serialNumber.toString().map { it.digitToInt().toLong() })

    var x = 0L
    var z = 0L

    val z1: Long
    val z2: Long
    val z3: Long
    val z4: Long
    val z5: Long
    val z6: Long
    val z7: Long
    val z8: Long
    val z9: Long
    val z10: Long
    val z11: Long
    val z12: Long
    val z13: Long
    @Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
    val z14: Long

    val w1: Long
    val w2: Long
    val w3: Long
    val w4: Long
    val w5: Long
    val w6: Long
    val w7: Long
    val w8: Long
    val w9: Long
    val w10: Long
    val w11: Long
    val w12: Long
    val w13: Long
    val w14: Long

    w1 = input.poll()
//    z = w1 + 1
    z1 = w1 + 1

    w2 = input.poll()
//    z *= 26
//    z += w2 + 7
    z2 = 26 * z1 + (w2 + 7)

    w3 = input.poll()
//    z *= 26
//    z += w3 + 13
    z3 = 26 * z2 + (w3 + 13)
    z = z3

    w4 = input.poll()
    x = (z % 26) - 6
    // w4 == w3 + 7
    z4 = if (x != w4)
        26 * (z3 / 26) + (w4 + 10)
    else
        z3 / 26
    // z4 = z2
    z /= 26
    x = if (x != w4) 1 else 0
    z *= 25 * x + 1
    z += (w4 + 10) * x

    w5 = input.poll()
    z *= 26
    z += w5
    z5 = 26 * z4 + w5

    w6 = input.poll()
    x = (z % 26) - 4
    // w6 == w5 - 4
    z6 = if (x != w6)
        26 * (z5 / 26) + (w6 + 13)
    else
        z5 / 26
    // z6 = z4
    z /= 26
    x = if (x != w6) 1 else 0
    z *= 25 * x + 1
    z += (w6 + 13) * x

    w7 = input.poll()
    z7 = 26 * z6 + (w7 + 11)
    z *= 26
    z += (w7 + 11)

    w8 = input.poll()
    z8 = 26 * z7 + (w8 + 6)
    z *= 26
    z += (w8 + 6)

    w9 = input.poll()
    z9 = 26 * z8 + (w9 + 1)
    z *= 26
    z += (w9 + 1)

    w10 = input.poll()
    x = (z % 26) + 0
    // w10 == w9 + 1
    z10 = if (x != w10)
        26 * (z9 / 26) + (w10 + 7)
    else
        z9 / 26
    // z10 = z8
    z /= 26
    x = if (x != w10) 1 else 0
    z *= 25 * x + 1
    z += (w10 + 7) * x

    w11 = input.poll()
    x = (z % 26) + 0
    // w11 == w8 + 6
    z11 = if (x != w11)
        26 * (z10 / 26) + (w11 + 11)
    else
        z10 / 26
    // z11 = z7
    z /= 26
    x = if (x != w11) 1 else 0
    z *= 25 * x + 1
    z += (w11 + 11) * x

    w12 = input.poll()
    x = (z % 26) - 3
    // w12 == w7 + 8
    z12 = if (x != w12)
        26 * (z11 / 26) + (w12 + 14)
    else
        z11 / 26
    // z12 = z6
    z /= 26
    x = if (x != w12) 1 else 0
    z *= 25 * x + 1
    z += (w12 + 14) * x

    w13 = input.poll()
    x = (z % 26) - 9
    // w13 == (w2 + 7) - 9 == w2 - 2
    z13 = if (x != w13)
        26 * (z12 / 26) + (w13 + 4)
    else
        z12 / 26
    // z13 = z1
    z /= 26
    x = if (x != w13) 1 else 0
    z *= 25 * x + 1
    z += (w13 + 4) * x

    w14 = input.poll()
    x = (z % 26) - 9
    // w14 == (w1 + 1) - 9 == w1 - 8
    @Suppress("UNUSED_VALUE")
    z14 = if (x != w14)
        26 * (z13 / 26) + (w14 + 10)
    else
        z13 / 26
    // z14 = 0
    z /= 26
    x = if (x != w14) 1 else 0
    z *= 25 * x + 1
    z += (w14 + 10) * x

//    println("z1=$z1")
//    println("z2=$z2")
//    println("z3=$z3")
//    println("z4=$z4")
//    println("z5=$z5")
//    println("z6=$z6")
//    println("z7=$z7")
//    println("z8=$z8")
//    println("z9=$z9")
//    println("z10=$z10")
//    println("z11=$z11")
//    println("z12=$z12")
//    println("z13=$z13")
//    println("z14=$z14")
//
//    println("x=$x")
//    println("z=$z")

    return z == 0L
}
