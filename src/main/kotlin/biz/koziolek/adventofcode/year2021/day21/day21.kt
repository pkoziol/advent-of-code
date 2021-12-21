package biz.koziolek.adventofcode.year2021.day21

import biz.koziolek.adventofcode.*

fun main() {
    val inputFile = findInput(object {})
    val lines = inputFile.bufferedReader().readLines()
    val (player1, player2) = parseDiracDiceStartingPositions(lines)
    val dice = RollCountingDice(DeterministicDice(sides = 100))
    val wonGame = play(DiracDiceGame(player1, player2, dice))
    println("Losing player score * total dice rolls: ${answerPart1(wonGame)}")

    val (player1Wins, player2Wins) = playQuantum(DiracDiceGame(player1, player2, QuantumDice(sides = 3)))
    if (player1Wins > player2Wins) {
        println("Player 1 wins in $player1Wins universes")
    } else {
        println("Player 2 wins in $player2Wins universes")
    }
}

data class Player(val position: Int, val score: Int)

interface Dice {
    val sides: Int
    fun rollNext(): Int
}

class RollCountingDice(private val dice: Dice, start: Int = 0) : Dice {
    private var _rolls = start

    val rolls
        get() = _rolls

    override val sides = dice.sides

    override fun rollNext(): Int {
        _rolls++
        return dice.rollNext()
    }
}

class DeterministicDice(override val sides: Int) : Dice {
    private var lastRolled = 0

    override fun rollNext(): Int {
        lastRolled++
        if (lastRolled > sides) {
            lastRolled = 1
        }
        return lastRolled
    }
}

class QuantumDice(override val sides: Int) : Dice {
    override fun rollNext() = throw IllegalStateException("Quantum dice does not return one value")
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

fun playQuantum(game: DiracDiceGame): Pair<Long, Long> {
    val (player1WinsPerTurn, player1NotYetWinsPerTurn) = countWinsPerTurn(game.player1.position, score = 0, turn = 0, diceSides = game.dice.sides)
    val (player2WinsPerTurn, player2NotYetWinsPerTurn) = countWinsPerTurn(game.player2.position, score = 0, turn = 0, diceSides = game.dice.sides)

    var player1Wins = 0L
    for (player1Turn in 1 until player1WinsPerTurn.size) {
        player1Wins += player1WinsPerTurn[player1Turn] * player2NotYetWinsPerTurn[player1Turn - 1]
    }

    var player2Wins = 0L
    for (player2Turn in 1 until player2WinsPerTurn.size) {
        player2Wins += player2WinsPerTurn[player2Turn] * player1NotYetWinsPerTurn[player2Turn]
    }

    return Pair(player1Wins, player2Wins)
}

fun countWinsPerTurn(position: Int, score: Int, turn: Int, diceSides: Int = 3, multiplier: Long = 1): Pair<LongArray, LongArray> {
    val arraySize = 11
    val winningScore = 21

    val winsPerTurn = LongArray(arraySize)
    val notYetWinsPerTurn = LongArray(arraySize)
    val newTurn = turn + 1

    val rollRepeats = buildMap<Int, Int> {
        for (roll1 in 1..diceSides) {
            for (roll2 in 1..diceSides) {
                for (roll3 in 1..diceSides) {
                    val rolled = roll1 + roll2 + roll3
                    merge(rolled, 1) { a, b -> a + b }
                }
            }
        }
    }

    for ((rolled, repeats) in rollRepeats) {
        val newPosition = (position + rolled - 1) % 10 + 1
        val newScore = score + newPosition

        if (newScore >= winningScore) {
            winsPerTurn[newTurn] += multiplier * repeats
        } else {
            notYetWinsPerTurn[newTurn] += multiplier * repeats

            val (otherWinsPerTurn, otherNotYetWinsPerTurn) = countWinsPerTurn(
                position = newPosition,
                score = newScore,
                turn = newTurn,
                diceSides = diceSides,
                multiplier = multiplier * repeats,
            )

            for (i in 0 until arraySize) {
                winsPerTurn[i] += otherWinsPerTurn[i]
            }
            for (i in 0 until arraySize) {
                notYetWinsPerTurn[i] += otherNotYetWinsPerTurn[i]
            }
        }
    }

    return winsPerTurn to notYetWinsPerTurn
}
