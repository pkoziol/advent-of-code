package biz.koziolek.adventofcode.year2024.day02

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val reports = parseReports(inputFile.bufferedReader().readLines())
    println("Safe reports: ${reports.count { isSafeReport(it) }}")
    println("Safe reports 2: ${reports.count { isSafeReport2(it) }}")
}

fun parseReports(lines: Iterable<String>): List<List<Int>> =
    lines.map { line -> line.split(" ").map { it.toInt() } }

fun isSafeReport(report: List<Int>): Boolean {
    val diffs = report.zipWithNext().map { it.first - it.second }
    return diffs.all { it in 1..3 } || diffs.all { it in -3..-1 }
}

fun isSafeReport2(report: List<Int>): Boolean =
    report.indices
        .map { report.subList(0, it) + report.subList(it + 1, report.size) }
        .any { isSafeReport(it) }
