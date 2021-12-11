package biz.koziolek.adventofcode.year2021.day4

import biz.koziolek.adventofcode.findInput

fun main() {
    val inputFile = findInput(object {})
    val lines = inputFile.bufferedReader().readLines()
    val moves = readMoves(lines)
    val boards = readBoards(lines)

    val wonBoards = playBingo(moves, boards)
    println("First won board score: ${wonBoards.first().score}")
    println("Last won board score: ${wonBoards.last().score}")
}

fun playBingo(moves: List<Int>, boards: List<Board>): List<WonBoard> {
    val initial = Pair<List<Board>, List<WonBoard>>(boards, emptyList())

    return moves.fold(initial) { (currentBoards, winningBoards), move ->
        val updatedBoards = currentBoards.map { it.mark(move) }
        val (boardsInProgress, boardsThatWon) = updatedBoards.partition { it.getFullyMarkedRowsOrColumns().isEmpty() }

        Pair(boardsInProgress, winningBoards + boardsThatWon.map { WonBoard(it, it.score(move)) })
    }.second
}

fun readMoves(lines: List<String>): List<Int> = lines[0].split(',').map { it.toInt() }

data class WonBoard(val board: Board,
                    val score: Int)

data class Board(val size: Int,
                 val rows: List<BoardCells>) {

    val cols: List<BoardCells>
        get() = (0 until size).map { i -> BoardCells(cells = rows.map { it[i] }) }

    operator fun get(i: Int) = rows[i]

    fun mark(value: Int): Board =
            copy(rows = rows.map { it.mark(value) })

    fun getFullyMarkedRowsOrColumns() =
            (rows + cols).filter { it.cells.all { cell -> cell.marked } }

    fun score(lastValueMarked: Int): Int {
        val unmarkedValues = rows.flatMap { row ->
            row.cells
                    .filter { cell -> !cell.marked }
                    .map { cell -> cell.value }
        }
        return unmarkedValues.sum() * lastValueMarked
    }
}

data class BoardCells(val cells: List<Cell>) {
    operator fun get(i: Int) = cells[i]

    fun mark(value: Int): BoardCells =
            copy(cells = cells.map { it.copy(marked = it.marked || it.value == value) })
}

data class Cell(val value: Int,
                val marked: Boolean)

fun readBoards(lines: List<String>): List<Board> {
    val boardSize = parseCells(lines[2]).size

    return lines
            .asSequence()
            .drop(2)
            .filter { it.isNotBlank() }
            .chunked(boardSize)
            .map { Board(size = boardSize, rows = parseRows(it)) }
            .toList()
}

private fun parseRows(lines: List<String>): List<BoardCells> =
        lines.asSequence()
                .map { BoardCells(cells = parseCells(it)) }
                .toList()

private fun parseCells(line: String) =
        line.trim()
                .split(Regex(" +"))
                .map { Cell(it.toInt(), false) }
                .toList()
