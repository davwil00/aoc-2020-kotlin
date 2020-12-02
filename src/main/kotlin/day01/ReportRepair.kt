package day01

import java.io.File
import java.lang.IllegalStateException

class ReportRepair {

    fun findPairs(input: List<String>, total: Int): Int {
        val parsedInput = parseInput(input)
        parsedInput.forEach { first ->
            if (parsedInput.contains(total - first)) {
                return first * (total - first)
            }
        }

        throw IllegalStateException("Could not find solution")
    }

    fun findTriple(input: List<String>, total: Int): Int {
        val parsedInput = parseInput(input)
        parsedInput.forEach { first ->
            parsedInput.forEach { second ->
                if (first + second < total &&
                    parsedInput.contains(total - first - second)) {
                        return first * second * (total - first - second)
                }
            }
        }

        throw IllegalStateException("Could not find solution")
    }

    private fun parseInput(input: List<String>): Set<Int> {
        return input.map {
            it.toInt()
        }.toSet()
    }
}

fun main() {
    val input = File("src/main/resources/day01/input.txt")
        .readLines()
    println(ReportRepair().findPairs(input, 2020))
    println(ReportRepair().findTriple(input, 2020))
}