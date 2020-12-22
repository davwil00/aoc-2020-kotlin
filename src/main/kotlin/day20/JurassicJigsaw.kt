package day20

import java.io.File
import java.lang.IllegalArgumentException
import kotlin.IllegalStateException

class JurassicJigsaw {

    fun findCorners(input: String): Long {
        val tiles = parseInput(input)
        matchTiles(tiles)
        val corners = tiles.filter { it.isCorner() }
        return corners.map { it.tileNum }.reduce(Long::times)
    }

    fun checkForMonsters(input: String) {
        val tiles = parseInput(input)
        matchTiles(tiles)
        assembleImage(tiles)
    }

    private fun assembleImage(tiles: List<Tile>) {
        val image = mutableMapOf<Pair<Int, Int>, Tile>()
        val tilesPlaced = mutableSetOf<Tile>()

        val firstTile = tiles.sortedBy { it.bordersWith.size }.first()
        image[Pair(0, 0)] = firstTile
        tilesPlaced.add(firstTile)

        while(tilesPlaced.size < tiles.size) {
            val placedTilesWithCoords = image.entries.toList()
            placedTilesWithCoords.forEach { (candidateTileCoordinate, candidateTile) ->
                candidateTile.getBorders().forEachIndexed { candidateTileBorderIdx, border ->
                    val tilesToPlace = tiles.toMutableList()
                    tilesToPlace.removeAll(tilesPlaced)
                    tilesToPlace.sortBy { it.bordersWith.size }
                    tilesToPlace.forEach { tile ->
                        val borderIdx = tile.getBorders().indexOf(border)
                        if (borderIdx >= 0) {
                            placeTile(image, candidateTileCoordinate, candidateTileBorderIdx, borderIdx, tile)
                            tilesPlaced.add(tile)
                        } else {
                            val flippedBorderIdx = tile.getBorders().indexOf(border.reversed())
                            if (flippedBorderIdx >= 0) {
                                tile.flip()
                                placeTile(
                                    image,
                                    candidateTileCoordinate,
                                    candidateTileBorderIdx,
                                    flippedBorderIdx,
                                    tile
                                )
                                tilesPlaced.add(tile)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun placeTile(image: MutableMap<Pair<Int, Int>, Tile>, candidateTileCoordinates: Pair<Int, Int>, candidateBorderIdx: Int, borderIdx: Int, tileToPlace: Tile) {
        when (candidateBorderIdx) {
            0 -> {
                val turnsToRotate = when(borderIdx) {
                    0 -> 2
                    1 -> 1
                    2 -> 0
                    3 -> 3
                    else -> throw IllegalStateException("Unexpected borderIdx $borderIdx")
                }
                tileToPlace.rotate(turnsToRotate)
                image[Pair(candidateTileCoordinates.first - 1, candidateTileCoordinates.second)] = tileToPlace // tile placed above
            }
            1 -> {
                val turnsToRotate = 3 - borderIdx
                tileToPlace.rotate(turnsToRotate)
                image[Pair(candidateTileCoordinates.first, candidateTileCoordinates.second + 1)] = tileToPlace // tile placed right
            }
            2 -> {
                val turnsToRotate = when(borderIdx) {
                    0 -> 0
                    1 -> 3
                    2 -> 2
                    3 -> 1
                    else -> throw IllegalStateException("Unexpected borderIdx $borderIdx")
                }
                tileToPlace.rotate(turnsToRotate)
                image[Pair(candidateTileCoordinates.first + 1, candidateTileCoordinates.second)] = tileToPlace // tile placed below
            }
            3 -> {
                val turnsToRotate = when(borderIdx) {
                    0 -> 1
                    1 -> 0
                    2 -> 3
                    3 -> 2
                    else -> throw IllegalStateException("Unexpected borderIdx $borderIdx")
                }
                tileToPlace.rotate(turnsToRotate)
                image[Pair(candidateTileCoordinates.first, candidateTileCoordinates.second - 1)] = tileToPlace // tile placed left
            }
            else -> throw IllegalStateException("Unexpected candidateBorderIdx $candidateBorderIdx")
        }
    }

    private fun matchTiles(tiles: List<Tile>) {
        tiles.forEachIndexed { index1, tile1 ->
            tiles.subList(index1, tiles.size).forEach { tile2 ->
                if (tile1.tileNum != tile2.tileNum) {
                    matchBorder(tile1, tile2)
                }
            }
        }
    }

    private fun matchBorder(tile1: Tile, tile2: Tile) {
        val tile2Borders = tile2.getBorders()
        tile1.getBorders() .mapIndexed { tile1Idx, border ->
            var borderIdx = tile2Borders.indexOf(border)
            var flipped = false
            if (borderIdx < 0) {
                borderIdx = tile2Borders.indexOf(border.reversed())
                flipped = true
            }
            if (borderIdx >= 0) {
                tile1.bordersWith.add(Border(BorderDirection.fromIdx(tile1Idx, flipped), tile2.tileNum))
                tile2.bordersWith.add(Border(BorderDirection.fromIdx(borderIdx, flipped), tile1.tileNum))
            }
        }
    }

    private fun parseInput(input: String): List<Tile> {
        val tiles = input.split("\n\n")
        return tiles.map { tile -> Tile.fromString(tile) }
    }

    data class Tile(val tileNum: Long, var data: List<String>) {
        val bordersWith = mutableListOf<Border>()

        fun getBorders(): List<String> {
            return listOf(
                data.first(), // top
                data.joinToString("") { it.last().toString() }, // right
                data.last(), // bottom
                data.joinToString("") { it.first().toString() } //left
            )
        }

        fun isCorner() = bordersWith.size == 2

        fun getBorderlessImageData(): List<String> {
            return data.subList(1, data.size - 1).map { it.substring(1) }
        }

        fun rotate(turns: Int) = repeat(turns) { rotate() }

        private fun rotate() {
            val newData = mutableListOf<String>()
            data.indices.forEach { col ->
                val line = mutableListOf<Char>()
                (data.size - 1 downTo 0).forEach { row ->
                    line.add(data[row][col])
                }
                newData.add(line.joinToString(""))
            }

            this.data = newData
            this.bordersWith.map { it.rotate() }
        }

        fun flip() {
            data = data.map { line -> line.reversed() }
        }

        companion object {
            private val tileNumRegex = Regex("""Tile (\d+):""")

            fun fromString(str: String): Tile {
                val tileRows = str.lines()
                val match = tileNumRegex.matchEntire(tileRows.first())
                val tileNum = match?.groupValues?.get(1)?.toLong() ?: throw IllegalStateException("Unable to parse tile")
                val tileData = tileRows.subList(1, tileRows.size)
                return Tile(tileNum, tileData)
            }
        }
    }

    data class Border(val borderDirection: BorderDirection, val tileNum: Long) {
        fun rotate() {
//            return Border(BorderDirection.fromIdx(borderDirection.idx ++)
        }
    }
    enum class BorderDirection(val idx: Int) {
        TOP(0),
        RIGHT(1),
        BOTTOM(2),
        LEFT(3);

        companion object {
            fun fromIdx(idx: Int, flipped: Boolean): BorderDirection {
                return if (flipped) {
                    values().find { it.idx == 3 - idx } ?: throw IllegalArgumentException("Unknown flipped index $idx")
                } else {
                    values().find { it.idx == idx } ?: throw IllegalArgumentException("Unknown index $idx")
                }
            }
        }
    }
}

fun main() {
    val input = File("src/main/resources/day20/input.txt").readText()
    println(JurassicJigsaw().findCorners(input))
}