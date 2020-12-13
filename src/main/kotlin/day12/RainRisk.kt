package day12

import plus
import times
import java.io.File
import java.lang.IllegalArgumentException
import kotlin.math.abs

class RainRisk {

    fun findManhattanDistance(instructions: List<String>): Int {
        var location = Location()
        instructions.forEach { instruction ->
            val action = Action.valueOf(instruction[0].toString())
            val value = instruction.substring(1).toInt()
            location = location.move(action, value)
        }
        return abs(location.eastWest) + abs(location.northSouth)
    }

    fun findManhattanDistancePart2(instructions: List<String>): Int {
        var location = WaypointAndShipLocation()
        instructions.forEach { instruction ->
            val action = Action.valueOf(instruction[0].toString())
            val value = instruction.substring(1).toInt()
            location = location.processAction(action, value)
        }
        return abs(location.ship.first) + abs(location.ship.second)
    }

    data class Location(val eastWest: Int = 0, val northSouth: Int = 0, val direction: Direction = Direction.E) {
        fun move(action: Action, value: Int): Location {
            return when(action) {
                Action.N -> Location(eastWest, northSouth + value, direction)
                Action.S -> Location(eastWest, northSouth - value, direction)
                Action.E -> Location(eastWest + value, northSouth, direction)
                Action.W -> Location(eastWest - value, northSouth, direction)
                Action.L -> Location(eastWest, northSouth, direction.turn(-value))
                Action.R -> Location(eastWest, northSouth, direction.turn(value))
                Action.F -> {
                    when (direction) {
                        Direction.N -> Location(eastWest, northSouth + value, direction)
                        Direction.S -> Location(eastWest, northSouth - value, direction)
                        Direction.E -> Location(eastWest + value, northSouth, direction)
                        Direction.W -> Location(eastWest - value, northSouth, direction)
                    }
                }
            }
        }
    }

    data class WaypointAndShipLocation(val waypoint: Pair<Int, Int> = Pair(10, 1), val ship: Pair<Int, Int> = Pair(0, 0)) {
        fun processAction(action: Action, value: Int): WaypointAndShipLocation {
            return when(action) {
                Action.N -> WaypointAndShipLocation(waypoint + Pair(0, value), ship)
                Action.S -> WaypointAndShipLocation(waypoint + Pair(0, -value), ship)
                Action.E -> WaypointAndShipLocation(waypoint + Pair(value, 0), ship)
                Action.W -> WaypointAndShipLocation(waypoint + Pair(-value, 0), ship)
                Action.L -> WaypointAndShipLocation(turnWaypoint(-value), ship)
                Action.R -> WaypointAndShipLocation(turnWaypoint(value), ship)
                Action.F -> WaypointAndShipLocation(waypoint, ship + (waypoint * value))
            }
        }

        fun turnWaypoint(value: Int): Pair<Int, Int> {
            return when (value) {
                90, -270 -> Pair(waypoint.second, -waypoint.first)
                180, -180 -> Pair(-waypoint.first, -waypoint.second)
                270, -90 -> Pair(-waypoint.second, waypoint.first)
                else -> throw IllegalArgumentException("Unexpected rotation $value")
            }
        }
    }

    enum class Action {
        N, S, E, W, L, R, F
    }

    enum class Direction {
        N, E, S, W;

        fun turn(degrees: Int): Direction {
            val rules = mapOf(
                N to listOf(E, S, W),
                E to listOf(S, W, N),
                S to listOf(W, N, E),
                W to listOf(N, E, S)
            )

            val idx = if (degrees > 0) (degrees / 90) - 1 else 3 - (abs(degrees) / 90)
            return rules[this]!![idx]
        }
    }
}

fun main() {
    val input = File("src/main/resources/day12/input.txt").readLines()
    println(RainRisk().findManhattanDistance(input)) // 845
    println(RainRisk().findManhattanDistancePart2(input)) // 27016
}