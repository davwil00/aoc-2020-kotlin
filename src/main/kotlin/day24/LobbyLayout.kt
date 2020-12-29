package day24

import java.io.File

class LobbyLayout {

    fun findBlackTiles(input: List<String>): Int {
        val tiles = layTiles(input)
        return tiles.values.count { it.isBlack }
    }

    fun findBlackTilesAfter100Days(input: List<String>): Int {
        var tiles = layTiles(input)
        repeat(100){
            tiles = tick(tiles)
        }

        return tiles.values.count { it.isBlack }
    }

    private fun layTiles(input: List<String>): Map<HexCoordinate, Tile> {
        val tiles = mutableMapOf<HexCoordinate, Tile>()
        input.forEach { stepsRaw ->
            val steps = parseSteps(stepsRaw)
            followSteps(steps, tiles)
        }

        return tiles
    }

    private fun parseSteps(stepsRaw: String): List<Direction> {
        val steps = mutableListOf<Direction>()
        val iterator = stepsRaw.iterator()
        while (iterator.hasNext()) {
            val char1 = iterator.nextChar().toString()
            steps.add(if (Direction.isDirection(char1)) {
                Direction.from(char1)
            } else {
                Direction.from("$char1${iterator.nextChar()}")
            })
        }

        return steps
    }

    private tailrec fun followSteps(steps: List<Direction>, tiles: MutableMap<HexCoordinate, Tile>, coordinates: HexCoordinate = Pair(0.0 ,0.0)) {
        if (steps.isEmpty()) {
            tiles.merge(coordinates, Tile(coordinates, true)) { existingTile, _ -> existingTile.also{ it.flip() } }
            return
        }

        val nextStep = steps.first()
        val nextCoordinates = coordinates + nextStep.coordinateDelta

        return followSteps(steps.subList(1, steps.size), tiles, nextCoordinates)
    }

    private fun tick(tiles: Map<HexCoordinate, Tile>): Map<HexCoordinate, Tile> {
        val coordinatesToCheck = tiles.values.filter { it.isBlack }
            .flatMap { getAdjacentTiles(it.coordinate) }

        return coordinatesToCheck.map { coordinate ->
            val isTileBlack = tiles[coordinate]?.isBlack ?: false
            val adjacentBlackTiles = getAdjacentTiles(coordinate)
                .map { tiles[it]?.isBlack ?: false }
                .count { it }
            val newState = if (isTileBlack && adjacentBlackTiles == 0 || adjacentBlackTiles > 2) {
                false
            } else if (!isTileBlack && adjacentBlackTiles == 2) {
                true
            } else {
                isTileBlack
            }
            coordinate to Tile(coordinate, newState)
        }.toMap()
    }

    private fun getAdjacentTiles(coordinate: HexCoordinate) =
        Direction.values().map { direction ->
            coordinate + direction.coordinateDelta
        }

    enum class Direction(val direction: String, val coordinateDelta: HexCoordinate) {
        E("e", Pair(0.0, 1.0)),
        SE("se", Pair(-1.0, 0.5)),
        SW("sw", Pair(-1.0, -0.5)),
        W("w", Pair(0.0, -1.0)),
        NW("nw", Pair(1.0, -0.5)),
        NE("ne", Pair(1.0, 0.5));

        companion object {
            fun isDirection(direction: String) = values().any { it.direction == direction }
            fun from(direction: String) = values().find { it.direction == direction }!!
        }
    }

    data class Tile(val coordinate: HexCoordinate, var isBlack: Boolean) {
        fun flip() {
            isBlack = !isBlack
        }
    }

}

operator fun HexCoordinate.plus(other: HexCoordinate): HexCoordinate = HexCoordinate(this.first + other.first, this.second + other.second)

typealias HexCoordinate = Pair<Double, Double>

fun main() {
    val input = File("src/main/resources/day24/input.txt").readLines()
    println(LobbyLayout().findBlackTiles(input))
    println(LobbyLayout().findBlackTilesAfter100Days(input))
}