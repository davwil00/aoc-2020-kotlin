package day09

import java.io.File
import java.lang.IllegalStateException

class EncodingError {

    fun findInvalidNumber(input: List<String>, preambleSize: Int): Long {
        val intList = input.map { it.toLong() }
        intList.windowed(preambleSize + 1)
            .forEach { window ->
                val target = window.last()
                val invalid = window.find { window.contains(target - it) } == null
                if (invalid) {
                    return target
                }
            }

        throw IllegalStateException("All inputs are valid")
    }

    fun breakEncryption(input: List<String>, target: Long): Long {
        val intList = input.map{ it.toLong() }
        (2..input.size).forEach { windowSize ->
            intList.windowed(windowSize).forEach {
                if (it.sum() == target) {
                    val sortedList = it.sorted()
                    return sortedList.first() + sortedList.last()
                }
            }
        }

        throw IllegalStateException("Unable to break encryption")
    }
}

fun main() {
    val input = File("src/main/resources/day09/input.txt").readLines()
    val invalidNumber= EncodingError().findInvalidNumber(input, 25)
    println(invalidNumber)
    println(EncodingError().breakEncryption(input, invalidNumber)) // 2466556
}