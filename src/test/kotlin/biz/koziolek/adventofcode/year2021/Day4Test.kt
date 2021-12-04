package biz.koziolek.adventofcode.year2021

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class Day4Test {

    private val sampleInput = """
            7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

            22 13 17 11  0
             8  2 23  4 24
            21  9 14 16  7
             6 10  3 18  5
             1 12 20 15 19
            
             3 15  0  2 22
             9 18 13 17  5
            19  8  7 25 23
            20 11 10 24  4
            14 21 16 12  6
            
            14 21 17 24  4
            10 16 15  9 19
            18  8 23 26 20
            22 11 13  6  5
             2  0 12  3  7
        """.trimIndent()

    @Test
    fun testParsingSampleInput() {
        val lines = sampleInput.split("\n")

        val expectedMoves = listOf(7, 4, 9, 5, 11, 17, 23, 2, 0, 14, 21, 24, 10, 16, 13, 6, 15, 25, 12, 22, 18, 20, 8, 19, 3, 26, 1)
        val moves = readMoves(lines)
        assertEquals(expectedMoves, moves)

        val boards = readBoards(lines)
        assertEquals(3, boards.size)

        assertNewBoard(boards, 0)
        assertNewBoard(boards, 1)
        assertNewBoard(boards, 2)
        
        assertEquals(listOf(6, 10, 3, 18, 5), boards[0].rows[3].cells.map { it.value })
        assertEquals(listOf(0, 24, 7, 5, 19), boards[0].cols[4].cells.map { it.value })
    }

    @Test
    fun testParsingFullInput() {
        val lines = File("src/main/resources/year2021/day4/input").readLines()

        val moves = readMoves(lines)
        assertEquals(100, moves.size)

        val boards = readBoards(lines)
        assertEquals(100, boards.size)

        for (i in 0 until 100) {
            assertNewBoard(boards, i)
        }
    }
    
    private fun assertNewBoard(boards: List<Board>, index: Int) {
        val board = boards[index]

        assertEquals(5, board.size)

        for (i in 0 until board.size) {
            for (j in 0 until board.size) {
                assertFalse(board[i][j].marked, "Board #$index, cell $i,$j should not be marked")
            }
        }

        assertEquals(0, board.getFullyMarkedRowsOrColumns().size)
    }

    @Test
    fun testMarkingCells() {
        val lines = sampleInput.split("\n")
        val boards = readBoards(lines)

        var board = boards[0]

        assertFalse(board[2][4].marked)
        board = board.mark(7)
        assertTrue(board[2][4].marked)

        assertFalse(board[1][3].marked)
        board = board.mark(4)
        assertTrue(board[1][3].marked)

        assertFalse(board[2][1].marked)
        board = board.mark(9)
        assertTrue(board[2][1].marked)
    }

    @Test
    fun testFindingFullyMarkedRowsOrColumns() {
        val lines = sampleInput.split("\n")
        val boards = readBoards(lines)
        val board = boards[0]

        // 4th row
        var moves = listOf(6, 10, 3, 18, 5)
        var markedBoard = moves.fold(board, Board::mark)
        var markedCells = markedBoard.getFullyMarkedRowsOrColumns().single()
        assertEquals(moves, markedCells.cells.map { it.value })

        // 3rd column
        moves = listOf(17, 23, 14, 3, 20)
        markedBoard = moves.fold(board, Board::mark)
        markedCells = markedBoard.getFullyMarkedRowsOrColumns().single()
        assertEquals(moves, markedCells.cells.map { it.value })

        // Main diagonal
        moves = listOf(22, 2, 14, 18, 19)
        markedBoard = moves.fold(board, Board::mark)
        assertEquals(0, markedBoard.getFullyMarkedRowsOrColumns().size)

        // 5th column, out of order
        moves = listOf(0, 5, 7, 19, 24)
        markedBoard = moves.fold(board, Board::mark)
        markedCells = markedBoard.getFullyMarkedRowsOrColumns().single()
        assertEquals(moves, markedCells.cells.map { it.value }.sorted())
    }

    @Test
    fun testScoringBoard() {
        val lines = sampleInput.split("\n")
        val moves = listOf(7, 4, 9, 5, 11, 17, 23, 2, 0, 14, 21, 24)
        val boards = readBoards(lines)
        val board = boards[2]

        val markedBoard = moves.fold(board, Board::mark)
        val score = markedBoard.score(moves.last())
        assertEquals(4512, score)
    }

    @Test
    fun testPlaySampleBingo() {
        val lines = sampleInput.split("\n")
        val moves = readMoves(lines)
        val boards = readBoards(lines)

        val score = playBingo(moves, boards)
        assertEquals(4512, score)
    }

    @Test
    fun testPlayRealBingo() {
        val lines = File("src/main/resources/year2021/day4/input").readLines()
        val moves = readMoves(lines)
        val boards = readBoards(lines)

        val score = playBingo(moves, boards)
        assertEquals(87456, score)
    }
}
