package biz.koziolek.adventofcode.year2023.day05

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val almanac = parseAlmanacV1(inputFile.bufferedReader().readLines())
    println("Lowest location is: ${almanac.mapAll(from = "seed", to = "location", items = almanac.seeds).min()}")
}

fun parseAlmanacV1(lines: Iterable<String>): Almanac =
    parseAlmanac(lines, ::parseSeedsV1)

fun parseAlmanacV2(lines: Iterable<String>): Almanac =
    parseAlmanac(lines, ::parseSeedsV2)

private fun parseAlmanac(lines: Iterable<String>, seedParser: (String) -> Sequence<Long>): Almanac {
    var seeds: Sequence<Long> = emptySequence()
    val maps: MutableList<AlmanacMap> = mutableListOf()

    val iterator = lines.iterator()
    while (iterator.hasNext()) {
        val line = iterator.next()

        if (line.startsWith("seeds:")) {
            seeds = seedParser(line)
        } else if (line.contains("map:")) {
            val (from, _, to, _) = line.split("-", " ")
            val map = buildList {
                while (iterator.hasNext()) {
                    val mappingLine = iterator.next()
                    if (mappingLine.isBlank()) {
                        break
                    }

                    val mapping = mappingLine
                        .split(" ")
                        .map { it.trim().toLong() }
                        .let { (destination, source, length) ->
                            AlmanacMapping(
                                destination = destination,
                                source = source,
                                length = length,
                            )
                        }

                    add(mapping)
                }
            }

            maps.add(AlmanacMap(from, to, map))
        }
    }

    return Almanac(seeds, maps)
}

private fun parseSeedsV1(line: String) =
    line
        .replace("seeds:", "")
        .trim()
        .split(" ")
        .map { it.trim().toLong() }
        .asSequence()

private fun parseSeedsV2(line: String) =
    line
        .replace("seeds:", "")
        .trim()
        .split(" ")
        .map { it.trim().toLong() }
        .chunked(2)
        .asSequence()
        .flatMap { (start, length) ->
            start until start + length
        }

data class Almanac(val seeds: Sequence<Long>, val maps: List<AlmanacMap>) {
    fun findMap(from: String, to: String? = null): AlmanacMap? =
        maps.singleOrNull { it.sourceCategory == from
                && (to == null || it.destinationCategory == to) }

    fun mapAll(from: String, to: String, items: Sequence<Long>): Sequence<Long> {
        val mapsToUse = buildList {
            var next: String? = from

            while (next != null) {
                val map = findMap(next) ?: throw IllegalArgumentException("Cannot map $from to $to - no map for $next")
                add(map)

                next = if (map.destinationCategory == to) {
                    null
                } else {
                    map.destinationCategory
                }
            }
        }

        return items.map { item ->
            mapsToUse.fold(item) { acc, map -> map.map(acc) }
        }
    }
}

data class AlmanacMap(
    val sourceCategory: String,
    val destinationCategory: String,
    val mapping: List<AlmanacMapping>
) {
    fun map(from: Long): Long =
        mapping
            .singleOrNull { it.covers(from) }
            ?.map(from)
            ?: from
}

data class AlmanacMapping(val destination: Long, val source: Long, val length: Long) {
    val sourceRange = source until source + length
    val destinationRange = destination until destination + length

    fun map(from: Long): Long =
        when {
            covers(from) -> destination + (from - source)
            else -> from
        }

    fun covers(from: Long) = from in sourceRange
}
