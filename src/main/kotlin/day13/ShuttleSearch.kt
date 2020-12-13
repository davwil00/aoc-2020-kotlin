package day13

import java.io.File

class ShuttleSearch {
    fun findEarliestBus(input: List<String>): Int {
        val arrivalTime = input[0].toInt()
        val busIds = input[1].split(",").filterNot { it == "x"}.map { it.toInt() }
        val result = busIds.map { Pair(it, it - (arrivalTime % it)) }.minByOrNull { it.second }!!
        return result.first * result.second
    }

    fun findEarliestTimestampForSubsequentDepartures(input: List<String>): Long {
        val busIds = input.mapIndexedNotNull { idx, busId ->
            if (busId != "x") Pair(idx, busId.toLong()) else null
        }
        var incrementer = 1L
        var time = 0L

        // Iterate through the bus ids
        busIds.forEach { (timeOffset, busId) ->
            // Add product of previously satisfied bus ids (incrementer) until the next condition is satisfied
            // Condition is satisfied when bus departs at exactly timeOffset mins after the initial bus
            while ((time + timeOffset) % busId != 0L) {
                time += incrementer
            }

            // By increasing the incrementer by the product of the previous busIds, we know
            // that next time the condition is satisfied, it will also be satisfied for the bus ids before it
            incrementer *= busId
        }
        return time
    }
}

fun main() {
    val input = File("src/main/resources/day13/input.txt").readLines()
    println(ShuttleSearch().findEarliestBus(input)) // 156
    println(ShuttleSearch().findEarliestTimestampForSubsequentDepartures(input[1].split(","))) // 404517869995362
}