package day22

import org.slf4j.LoggerFactory
import java.io.File
import java.util.*

class CrabCombat {

    private val logger = LoggerFactory.getLogger(CrabCombat::class.java)

    fun simulateGameplay(input: List<String>): Long {
        val (player1Deck, player2Deck) = parseInput(input)
        return playTheGame(player1Deck, player2Deck)
    }

    fun simulateGameplayPartB(input: List<String>): Long {
        val (player1Deck, player2Deck) = parseInput(input)
        return playTheGame2(player1Deck, player2Deck).second
    }

    private tailrec fun playTheGame(player1Deck: Hand, player2Deck: Hand): Long {
        if (player1Deck.isEmpty()) {
            return calculateScore(player2Deck)
        } else if (player2Deck.isEmpty()) {
            return calculateScore(player1Deck)
        }

        val player1Card = player1Deck.pop()
        val player2Card = player2Deck.pop()

        if (player1Card > player2Card) {
            player1Deck.addLast(player1Card)
            player1Deck.addLast(player2Card)
        } else {
            player2Deck.addLast(player2Card)
            player2Deck.addLast(player1Card)
        }

        return playTheGame(player1Deck, player2Deck)
    }

    private tailrec fun playTheGame2(player1Deck: Hand, player2Deck: Hand, previousGameStates: GameState = GameState()): Pair<Int, Long> {
        logger.debug("-- Round ${previousGameStates.roundNum} --")
        logger.debug("Player 1's deck: $player1Deck")
        logger.debug("Player 2's deck: $player2Deck")
        if (player2Deck.isEmpty() || player1Deck.toList() in previousGameStates.player1PreviousDecks || player2Deck.toList() in previousGameStates.player2PreviousDecks) {
            return Pair(1, calculateScore(player1Deck))
        } else if (player1Deck.isEmpty()) {
            return Pair(2, calculateScore(player2Deck))
        }

        previousGameStates.player1PreviousDecks.add(player1Deck.toList())
        previousGameStates.player2PreviousDecks.add(player2Deck.toList())
        previousGameStates.roundNum++

        val player1Card = player1Deck.pop()
        logger.debug("Player 1 plays: $player1Card")
        val player2Card = player2Deck.pop()
        logger.debug("Player 2 plays: $player2Card")

        val winner = if (player1Deck.size >= player1Card && player2Deck.size >= player2Card) {
            logger.debug("Playing a subgame to determine the winner...\n")
            val newPlayer1Deck = ArrayDeque(player1Deck.toList().subList(0, player1Card))
            val newPlayer2Deck = ArrayDeque(player2Deck.toList().subList(0, player2Card))
            val (winner, _) = playTheGame2(newPlayer1Deck, newPlayer2Deck)
            winner
        } else if (player1Card > player2Card) {
            1
        } else 2

        if (winner == 1) {
            logger.debug("Player 1 wins the round!\n")
            player1Deck.addLast(player1Card)
            player1Deck.addLast(player2Card)
        } else {
            logger.debug("Player 2 wins the round!\n")
            player2Deck.addLast(player2Card)
            player2Deck.addLast(player1Card)
        }

        return playTheGame2(player1Deck, player2Deck, previousGameStates)
    }

    private fun calculateScore(deck: Deque<Int>) =
        deck.reversed()
            .mapIndexed() { index, card -> (index + 1) * card.toLong() }
            .sum()

    private fun parseInput(input: List<String>): Pair<Hand, Hand> {
        val player2Start = input.indexOf("Player 2:")
        val player1 = input.subList(1,player2Start - 1).map { it.toInt() }
        val player2 = input.subList(player2Start + 1, input.size).map { it.toInt() }

        return Pair(ArrayDeque(player1), ArrayDeque(player2))
    }

    class GameState(val player1PreviousDecks: MutableSet<List<Int>> = mutableSetOf(),
                    val player2PreviousDecks: MutableSet<List<Int>> = mutableSetOf(),
                    var roundNum: Long = 1)
}

typealias Hand = Deque<Int>

fun main() {
    val input = File("src/main/resources/day22/input.txt").readLines()
    println(CrabCombat().simulateGameplay(input)) // 35370
    println(CrabCombat().simulateGameplayPartB(input)) // 31662 too low //36246
}