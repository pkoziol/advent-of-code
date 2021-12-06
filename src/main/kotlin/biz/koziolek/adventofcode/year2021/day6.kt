package biz.koziolek.adventofcode.year2021

import java.io.File
import kotlin.math.abs

fun main() {
    val inputFile = File("src/main/resources/year2021/day6/input")
    val line = inputFile.bufferedReader().readLines().first()
    val initialFish = createFish(line)
    
    val fishAfter80Days = (1..80).fold(initialFish) { acc, day -> simulateDay(acc) }
    println("Fish count after 80 days: ${fishAfter80Days.size}")
}

data class Fish(val timer: Int)

fun createFish(line: String): List<Fish> {
    return line.split(',')
            .map { Fish(timer = it.toInt()) }
}

fun simulateDay(fish: List<Fish>): List<Fish> {
    return fish.flatMap {
        when (it.timer) {
            0 -> listOf(Fish(timer = 6), Fish(timer = 8))
            else -> listOf(Fish(timer = it.timer.dec()))
        }
    }
}
