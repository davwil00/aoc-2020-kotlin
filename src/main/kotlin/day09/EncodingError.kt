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
        var found: Long? = null
        (2..20).forEach { windowSize ->
            if (found != null) {
                return found as Long
            }
            intList.windowed(windowSize) {
                if (it.sum() == target) {
                    val sortedList = it.sorted()
                    found = sortedList.first() + sortedList.last()
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