package day11

import Part
import plus
import times
import java.io.File
import java.lang.IndexOutOfBoundsException

class SeatingSystem(private val part: Part) {

    fun findOccupiedSeats(seatLayout: List<String>): Int {
        var currentSeatLayout = seatLayout
        var newSeatLayout = applyRules(seatLayout)
        while (currentSeatLayout != newSeatLayout) {
            currentSeatLayout = newSeatLayout
            newSeatLayout = applyRules(currentSeatLayout)
        }

        return countOccupiedSeats(newSeatLayout)
    }

    private fun applyRules(seatLayout: List<String>): List<String> {
        val newLayout = MutableList(seatLayout.size) { "" }
        seatLayout.forEachIndexed { rowNo, row ->
            row.forEachIndexed { seatNo, seat ->
                newLayout[rowNo] += getNewSeatState(seatLayout, seat, rowNo, seatNo)
            }
        }

        return newLayout
    }

    private fun getNewSeatState(seatLayout: List<String>, currentSeatState: Char, rowNo: Int, seatNo: Int): String {
        if (currentSeatState == '.') {
            return currentSeatState.toString()
        }

        val isSeatOccupied = currentSeatState == '#'
        val occupiedAdjacentSeats = countOccupiedAdjacentSeats(seatLayout, Pair(rowNo, seatNo))
        if (occupiedAdjacentSeats == 0 && !isSeatOccupied) {
            return "#"
        }

        val threshold = if (part == Part.PART1) 4 else 5
        if (isSeatOccupied && occupiedAdjacentSeats >= threshold) {
            return "L"
        }

        return currentSeatState.toString()
    }

    fun countOccupiedAdjacentSeats(seatLayout: List<String>, seatCoord: Pair<Int, Int>): Int {
        val seatCoordsToCheck = listOf(
            Pair(-1, - 1),
            Pair(-1, 0),
            Pair(-1, 1),
            Pair(0, -1),
            Pair(0, 1),
            Pair(1, -1),
            Pair(1, 0),
            Pair(1, 1)
        )
        return seatCoordsToCheck.map { coord ->
            if (part == Part.PART1) {
                getSeatState(seatLayout, seatCoord + coord) == '#'
            } else {
                (1..seatLayout.size).forEach {
                    val seatState = getSeatState(seatLayout, seatCoord + (coord * it))
                    if (seatState != '.') {
                        return@map seatState == '#'
                    }
                }
                false
            }
        }.count { it }
    }

    private fun getSeatState(seatLayout: List<String>, seatCoord: Pair<Int, Int>): Char? {
        return try {
            seatLayout[seatCoord.first][seatCoord.second]
        } catch (e: IndexOutOfBoundsException) {
            null
        }
    }

    private fun countOccupiedSeats(seatLayout: List<String>) =
        seatLayout.map { row -> row.count { it == '#' } }.sum()
}

fun main() {
    val input = File("src/main/resources/day11/input.txt").readLines()
    println(SeatingSystem(Part.PART1).findOccupiedSeats(input)) // 2324
    println(SeatingSystem(Part.PART2).findOccupiedSeats(input)) // 2068
}
