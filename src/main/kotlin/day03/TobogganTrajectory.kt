package day03

import java.io.File

class TobogganTrajectory(private val mapWidth: Int) {

    fun findTreesOnPaths(input: List<String>, paths: List<Pair<Int, Int>>): Long {
        val counts = paths.map { path ->
            val parsedInput = parseInput(input)
            (0 .. input.size step path.second).filter {
                val coords = Pair(it / path.second * path.first, it)
                isTree(coords, parsedInput)
            }.size.toLong()
        }
        return counts.reduce(Long::times)
    }

    private fun isTree(coords: Pair<Int, Int>, map: Map<Int, List<Int>>) =
        map[coords.second]?.contains(coords.first % mapWidth) ?: false

    private fun parseInput(input: List<String>): Map<Int, List<Int>> {
        val map = mutableMapOf<Int, List<Int>>()
        input.forEachIndexed { rowIdx, line ->
            val treeIndexes = mutableListOf<Int>()
            line.forEachIndexed { idx, char ->
                if (char == '#') {
                    treeIndexes.add(idx)
                }
                map[rowIdx] = treeIndexes
            }
        }

        return map
    }
}

fun main() {
    val input = File("src/main/resources/day03/input.txt").readLines()
    val tobogganTrajectory = TobogganTrajectory(31)
    println(tobogganTrajectory.findTreesOnPaths(input, listOf(Pair(3, 1)))) // 299
    val routes = listOf(
        Pair(1, 1),
        Pair(3, 1),
        Pair(5, 1),
        Pair(7, 1),
        Pair(1, 2)
    )
    println(tobogganTrajectory.findTreesOnPaths(input, routes)) // 3621285278
}