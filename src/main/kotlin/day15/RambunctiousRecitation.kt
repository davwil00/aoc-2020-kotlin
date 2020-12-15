package day15

import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit
import java.util.concurrent.LinkedBlockingDeque

class RambunctiousRecitation {

    fun playTheGame(target: Int, vararg startingNumbers: Int): Int {
        val start = Instant.now()
        val seenNumbers = startingNumbers.mapIndexed { idx, num ->
            val vals = LinkedBlockingDeque<Int>(2)
            vals.offer(idx + 1)
            num to vals
        }.toMap().toMutableMap()
        var lastSpokenNumber = startingNumbers.last()

        (startingNumbers.size + 1..target).forEach { turn ->
            if (!seenNumbers.containsKey(lastSpokenNumber) || seenNumbers[lastSpokenNumber]!!.size == 1) {
                lastSpokenNumber = 0
                seenNumbers[0]!!.offerLast(turn)
            } else {
                val previousOccurrences = seenNumbers[lastSpokenNumber]!!
                lastSpokenNumber = previousOccurrences.last - previousOccurrences.pop()

                if (lastSpokenNumber !in seenNumbers) {
                    seenNumbers[lastSpokenNumber] = LinkedBlockingDeque<Int>(2)
                }

                seenNumbers.getValue(lastSpokenNumber).offerLast(turn)
            }
        }
        val end = Instant.now()
        println(Duration.between(start, end).toSeconds())
        return lastSpokenNumber
    }
}



fun main() {
    println(RambunctiousRecitation().playTheGame(2020, 0,12,6,13,20,1,17))
    println(RambunctiousRecitation().playTheGame(30000000, 0,12,6,13,20,1,17))
}