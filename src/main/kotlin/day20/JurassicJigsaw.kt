package day20

import countMatches
import flipVertical
import rotate
import java.io.File
import java.lang.IllegalArgumentException
import kotlin.IllegalStateException
import kotlin.math.min
import kotlin.math.sqrt

class JurassicJigsaw {

    private val monsterLine1Regex = Regex(".{18}#")
    private val monsterLine2Regex = Regex("#....##....##....###")
    private val monsterLine3Regex = Regex(".#..#..#..#..#..#...")

    fun findCorners(input: String): Long {
        val tiles = parseInput(input)
        matchTiles(tiles)
        val corners = tiles.filter { it.isCorner() }
        return corners.map { it.tileNum }.reduce(Long::times)
    }

    fun checkForMonsters(input: String): Long {
        val tiles = parseInput(input)
        matchTiles(tiles)
        val image = assembleImage(tiles)
        val stitchedImage = stitchImage(image)
        val numberOfMonsters = findMonsters(stitchedImage)
        val hashes = stitchedImage.joinToString("").count { it == '#' }
        return hashes - (numberOfMonsters * 15)
    }


    fun generateImage(input: String): List<String> {
        val tiles = parseInput(input)
        matchTiles(tiles)
        val image = assembleImage(tiles)
        return stitchImage(image)
    }

    private fun assembleImage(tiles: List<Tile>): Map<Pair<Int, Int>, Tile> {
        val image = mutableMapOf<Pair<Int, Int>, Tile>()
        val tilesPlaced = mutableSetOf<Tile>()

        val firstTile = tiles.minByOrNull { it.bordersWith.size }!!
        image[Pair(0, 0)] = firstTile
        tilesPlaced.add(firstTile)

        while(tilesPlaced.size < tiles.size) {
            tiles.filterNot { it in tilesPlaced }
                .forEach { tileToPlace ->
                    val tileToPlaceBorders = tileToPlace.getBorders()
                    val adjacentTile = image.entries.find { (_, imageTile) ->
                        val imageTileBorders = imageTile.getBorders()
                        imageTileBorders.any { border ->
                            tileToPlaceBorders.contains(border) || tileToPlaceBorders.contains(border.reversed())
                        }
                    }

                    adjacentTile?.value?.getBorders()?.forEachIndexed placeTile@{ imageBorderIdx, border ->
                        val borderIdx = tileToPlaceBorders.indexOf(border)
                        val flippedBorderIdx = tileToPlaceBorders.indexOf(border.reversed())
                        if (borderIdx >= 0 || flippedBorderIdx >= 0) {
                            placeTile(image, adjacentTile.key, imageBorderIdx, borderIdx, flippedBorderIdx, tileToPlace)
                            tilesPlaced.add(tileToPlace)
                            return@placeTile
                        }
                    }
                }
        }

        return image
    }

    private fun placeTile(image: MutableMap<Pair<Int, Int>, Tile>,
                          candidateTileCoordinates: Pair<Int, Int>,
                          candidateBorderIdx: Int,
                          borderIdx: Int,
                          flippedBorderIdx: Int,
                          tile: Tile) {
        val coords = when (candidateBorderIdx) {
            0 -> Pair(candidateTileCoordinates.first - 1, candidateTileCoordinates.second) // tile placed above
            1 -> Pair(candidateTileCoordinates.first, candidateTileCoordinates.second + 1) // tile placed right
            2 -> Pair(candidateTileCoordinates.first + 1, candidateTileCoordinates.second) // tile placed below
            3 -> Pair(candidateTileCoordinates.first, candidateTileCoordinates.second - 1) // tile placed left
            else -> throw IllegalStateException("Unexpected candidateBorderIdx $candidateBorderIdx")
        }

        when {
            (candidateBorderIdx == 0 && borderIdx == 0) ||
                    (candidateBorderIdx == 2 && borderIdx == 2) -> {
                tile.rotate(2)
                tile.flipVertical()
            }
            (candidateBorderIdx == 0 && borderIdx == 1) ||
                    (candidateBorderIdx == 2 && borderIdx == 3) -> {
                tile.rotate(1)
                tile.flipVertical()
            }
            (candidateBorderIdx == 0 && borderIdx == 3) ||
                    (candidateBorderIdx == 2 && borderIdx == 1) -> tile.rotate(3)
            (candidateBorderIdx == 1 && borderIdx == 0) ||
                    (candidateBorderIdx == 3 && borderIdx == 2) -> {
                tile.rotate(3)
                tile.flipHorizontal()
            }
            (candidateBorderIdx == 1 && borderIdx == 1) ||
                    (candidateBorderIdx == 3 && borderIdx == 3) -> tile.flipVertical()
            (candidateBorderIdx == 1 && borderIdx == 2) ||
                    (candidateBorderIdx == 3 && borderIdx == 0) -> tile.rotate(1)

            (candidateBorderIdx == 0 && flippedBorderIdx == 0) -> tile.rotate(2)
            (candidateBorderIdx == 0 && flippedBorderIdx == 1) -> tile.rotate(1)
            (candidateBorderIdx == 0 && flippedBorderIdx == 2) -> tile.flipVertical()
            (candidateBorderIdx == 0 && flippedBorderIdx == 3) -> {
                tile.rotate(3)
                tile.flipVertical()
            }
            (candidateBorderIdx == 1 && flippedBorderIdx == 0) -> tile.rotate(3)
            (candidateBorderIdx == 1 && flippedBorderIdx == 1) -> tile.rotate(2)
            (candidateBorderIdx == 1 && flippedBorderIdx == 2) -> {
                tile.rotate(1)
                tile.flipHorizontal()
            }
            (candidateBorderIdx == 1 && flippedBorderIdx == 3) -> tile.flipHorizontal()
            (candidateBorderIdx == 2 && flippedBorderIdx == 0) -> tile.flipVertical()
            (candidateBorderIdx == 2 && flippedBorderIdx == 1) -> {
                tile.rotate(3)
                tile.flipVertical()
            }
            (candidateBorderIdx == 2 && flippedBorderIdx == 2) -> tile.rotate(2)
            (candidateBorderIdx == 2 && flippedBorderIdx == 3) -> tile.rotate(1)
            (candidateBorderIdx == 3 && flippedBorderIdx == 0) -> {
                tile.rotate(1)
                tile.flipHorizontal()
            }
            (candidateBorderIdx == 3 && flippedBorderIdx == 1) -> tile.flipHorizontal()
            (candidateBorderIdx == 3 && flippedBorderIdx == 2) -> tile.rotate(3)
            (candidateBorderIdx == 3 && flippedBorderIdx == 3) -> tile.rotate(2)
        }

        image[coords] = tile
    }

    private fun stitchImage(image: Map<Pair<Int, Int>, Tile>): List<String> {
        val imageWidthHeight = sqrt(image.size.toDouble()).toInt()
        val finalImage = mutableListOf<String>()
        val minRow = image.keys.minByOrNull { it.first }!!.first
        val minCol = image.keys.minByOrNull { it.second }!!.second
        (minRow until minRow + imageWidthHeight).forEach { row ->
            val rowData = mutableListOf<List<String>>()
            (minCol until minCol + imageWidthHeight).forEach { col ->
                rowData.add(image[Pair(row, col)]!!.getBorderlessImageData())
            }

            rowData[0].indices.forEach { rowIdx ->
                finalImage.add(rowData.joinToString("") { it[rowIdx] })
            }
        }

        return finalImage
    }

    private fun findMonsters(image: List<String>): Long {
        var rotatedImage = image.toList()
        var monstersFound = 0L
        var rotations = 0
        while(monstersFound == 0L && rotations <= 4) {
            monstersFound = countMonsters(rotatedImage)
            rotatedImage = rotatedImage.rotate()
            rotations++
        }

        return if (monstersFound > 0) {
            monstersFound
        } else {
            findMonsters(image.flipVertical())
        }
    }

    private fun countMonsters(image: List<String>): Long {
        var monstersFound = 0L
        image.windowed(3).forEach { window ->
            val monsterCount = minOf(
                monsterLine1Regex.countMatches(window[0]),
                monsterLine2Regex.countMatches(window[1]),
                monsterLine3Regex.countMatches(window[2])
            )
            monstersFound += monsterCount

        }

        return monstersFound
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
            return data.subList(1, data.size - 1).map { it.substring(1, it.length -1) }
        }

        fun rotate(turns: Int) = repeat(turns) { rotate() }

        private fun rotate() {
            data = data.rotate()
        }

        fun flipVertical() {
            data = data.flipVertical()
        }

        fun flipHorizontal() {
            data = data.reversed()
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

    data class Border(val borderDirection: BorderDirection, val tileNum: Long)

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
    println(JurassicJigsaw().findCorners(input)) // 17148689442341
    println(JurassicJigsaw().checkForMonsters(input)) // 2089
}