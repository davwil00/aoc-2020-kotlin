package day23

import org.slf4j.LoggerFactory
import java.util.*

class CrabCups {

    private val logger = LoggerFactory.getLogger(CrabCups::class.java)

    fun playTheSimpleGame(input: List<Int>, numberOfMoves: Int): String {
        val cups = playTheGame(input, numberOfMoves)
        val cup1 = cups[1]!!
        var currentCup = cup1
        val answer = StringBuilder()
        while(currentCup.next != cup1) {
            answer.append(currentCup.next.value)
            currentCup = currentCup.next
        }

        return answer.toString()
    }

    fun playTheLongGame(input: List<Int>, numberOfMoves: Int): Long {
        val cups = playTheGame(input, numberOfMoves)
        return cups[1]!!.next.value.toLong() * cups[1]!!.next.next.value.toLong()
    }

    private fun playTheGame(input: List<Int>, numberOfMoves: Int): Map<Int, Cup> {
        val cups = input.map { it to Cup(it) }.toMap()
        var activeCup = cups[input.first()]!!
        cups.values.windowed(2, 1, true)
            .forEach {
                if (it.size == 2) {
                    it[0].next = it[1]
                } else {
                    it[0].next = activeCup
                }
            }
        val highestNum = input.maxOrNull()!!
        repeat(numberOfMoves) {
            logger.debug("-- move {}, active cup {}", it + 1, activeCup.value)
//            logger.debug("cups: $cups")
            activeCup = simulatePlay(cups, highestNum, activeCup)
        }

        return cups
    }

    private fun simulatePlay(cups: Map<Int, Cup>, highestNum: Int, activeCup: Cup): Cup {
        var destinationCupVal = activeCup.value - 1
        val lastPickedUpCup = activeCup.next.next.next
        val cupsRemoved = mutableListOf(activeCup.next.value, activeCup.next.next.value, lastPickedUpCup.value)
        logger.debug("pick up: {}", cupsRemoved)
        while (destinationCupVal == 0 || destinationCupVal in cupsRemoved || destinationCupVal == activeCup.value) {
            destinationCupVal--
            if (destinationCupVal <= 0) {
                destinationCupVal = highestNum
            }
        }
        logger.debug("destination: {}", destinationCupVal)
        val nextActiveCup = lastPickedUpCup.next
        val destinationCup = cups[destinationCupVal]!!
        lastPickedUpCup.next = destinationCup.next
        destinationCup.next = activeCup.next
        activeCup.next = nextActiveCup

        return nextActiveCup
    }

    data class Cup(val value: Int): Iterable<Cup> {
        lateinit var next: Cup

        override fun iterator(): Iterator<Cup> {
            return object: Iterator<Cup> {
                override fun hasNext() = true

                override fun next() = next
            }
        }
    }
}

fun main() {
    println(CrabCups().playTheSimpleGame(listOf(5,8,6,4,3,9,1,7,2), 100))
    val input = mutableListOf(5,8,6,4,3,9,1,7,2).also { it.addAll((10..1_000_000).toList()) }
    println(CrabCups().playTheLongGame(input, 10_000_000))
}