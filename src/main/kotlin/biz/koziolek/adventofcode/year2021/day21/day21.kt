package biz.koziolek.adventofcode.year2021.day21

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})
    val lines = inputFile.bufferedReader().readLines()
    val (player1, player2) = parseDiracDiceStartingPositions(lines)
    val dice = RollCountingDice(DeterministicDice(sides = 100))
    val wonGame = play(DiracDiceGame(player1, player2, dice))
    println("Answer for part 1: ${answerPart1(wonGame)}")
}

data class Player(val position: Int, val score: Int)

interface Dice {
    fun rollNext(): Int
}

class RollCountingDice(val dice: Dice, val start: Int = 0) : Dice {
    private var _rolls = start

    val rolls
        get() = _rolls

    override fun rollNext(): Int {
        _rolls++
        return dice.rollNext()
    }
}

class DeterministicDice(val sides: Int) : Dice {
    private var lastRolled = 0

    override fun rollNext(): Int {
        lastRolled++
        if (lastRolled > sides) {
            lastRolled = 1
        }
        return lastRolled
    }
}

data class DiracDiceGame(
    val player1: Player,
    val player2: Player,
    val dice: Dice,
) {
    val winner = when {
        hasWon(player1) -> player1
        hasWon(player2) -> player2
        else -> null
    }
    val loser = when {
        hasWon(player1) -> player2
        hasWon(player2) -> player1
        else -> null
    }

    fun playNextTurn(): DiracDiceGame {
        val newPlayer1 = rollAndMove(player1)
        val newPlayer2 = if (!hasWon(newPlayer1)) rollAndMove(player2) else player2

        return copy(
            player1 = newPlayer1,
            player2 = newPlayer2,
        )
    }

    private fun hasWon(player: Player) = player.score >= 1000

    private fun rollAndMove(player: Player): Player {
        val rolled = dice.rollNext() + dice.rollNext() + dice.rollNext()
        val newPosition = (player.position + rolled - 1) % 10 + 1
        return player.copy(
            position = newPosition,
            score = player.score + newPosition,
        )
    }
}

fun parseDiracDiceStartingPositions(lines: List<String>): Pair<Player, Player> =
    Pair(
        first = Player(position = lines[0].replace("Player 1 starting position: ", "").toInt(), score = 0),
        second = Player(position = lines[1].replace("Player 2 starting position: ", "").toInt(), score = 0),
    )

fun play(game: DiracDiceGame): DiracDiceGame =
    generateSequence(game) {
        when (it.winner) {
            null -> it.playNextTurn()
            else -> null
        }
    }.last()

fun answerPart1(game: DiracDiceGame): Int =
    (game.loser?.score ?: 0) * ((game.dice as? RollCountingDice)?.rolls ?: 0)
