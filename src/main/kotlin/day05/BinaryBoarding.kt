package day05

import java.io.File
import kotlin.math.ceil
import kotlin.math.floor


class BinaryBoarding {

    fun findHighestSeatId(boardingPasses: List<String>): Int? {
        return boardingPasses.map { boardingPass ->
            calculateSeatId(boardingPass)
        }.maxOrNull()
    }

    fun findSeat(boardingPasses: List<String>): Int {
        boardingPasses.map { boardingPass ->
            calculateSeatId(boardingPass)
        }
        .sorted()
        .zipWithNext { curr, next ->
            if (curr + 1 != next) {
                return(curr + 1)
            }
        }

        throw IllegalArgumentException("Unable to find seat")
    }

    fun calculateSeatId(boardingPass: String): Int {
        val row = decodeNumber(boardingPass.substring(0, 7), 0..127)
        println("Row: $row")
        val seat = decodeNumber(boardingPass.substring(7), 0..7)
        println("seat: $seat")
        return (row * 8) + seat
    }

    private tailrec fun decodeNumber(chars: String, range: IntRange): Int {
        println("using range $range")
        val currentChar = chars[0]
        return if (chars.length == 1) {
            if (currentChar == 'F' || currentChar == 'L') range.first else range.last
        } else {
            val half = (range.last - range.first) / 2.0
            if (currentChar == 'F' || currentChar == 'L') {
                decodeNumber(chars.substring(1), range.first..range.first + floor(half).toInt())
            } else {
                decodeNumber(chars.substring(1), (range.first + ceil(half).toInt())..range.last)
            }
        }
    }
}

fun main() {
    val input = File("src/main/resources/day05/input.txt").readLines()
    println(BinaryBoarding().findHighestSeatId(input)) // 818
    println(BinaryBoarding().findSeat(input)) // 559
}