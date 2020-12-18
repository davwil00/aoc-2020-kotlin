package day17

import java.io.File

class ConwayCubes {

    fun simulate(input: List<String>): Int {
        val initialState = parseInput(input)
        var pocketDimension: PocketDimension = initialState

        repeat(6) {
            pocketDimension = runCycle(pocketDimension)
        }

        return pocketDimension.size
    }

    fun simulate4D(input: List<String>): Int {
        val initialState = parse4DInput(input)
        var pocketDimension: Pocket4Dimension = initialState

        repeat(6) {
            pocketDimension = runCycle4D(pocketDimension)
        }

        return pocketDimension.size
    }

    private fun parseInput(input: List<String>): PocketDimension {
        val activeCubes = mutableSetOf<Coordinate>()
        val zIdx = 0
        input.forEachIndexed { yIdx, row->
            row.forEachIndexed { xIdx, state ->
                if (state == '#') {
                    activeCubes.add(Coordinate(xIdx, yIdx, zIdx))
                }
            }
        }

        return activeCubes
    }

    private fun parse4DInput(input: List<String>): Pocket4Dimension {
        val activeCubes = mutableSetOf<Cofourdinate>()
        val wIdx = 0
        val zIdx = 0
        input.forEachIndexed { yIdx, row->
            row.forEachIndexed { xIdx, state ->
                if (state == '#') {
                    activeCubes.add(Cofourdinate(wIdx, xIdx, yIdx, zIdx))
                }
            }
        }

        return activeCubes
    }

    private fun runCycle(activeCubes: PocketDimension): PocketDimension {
        val cubesToCheck = activeCubes.flatMap { activeCube ->
            getNeighbours(activeCube)
        }.toSet()

        return cubesToCheck.map { cube ->
            Pair(cube, calculateNewState(cube, activeCubes))
        }.filter { it.second }.map { it.first }.toSet()
    }

    private fun runCycle4D(activeCubes: Pocket4Dimension): Pocket4Dimension {
        val cubesToCheck = activeCubes.flatMap { activeCube ->
            getNeighbours(activeCube)
        }.toSet()

        return cubesToCheck.map { cube ->
            Pair(cube, calculateNewState(cube, activeCubes))
        }.filter { it.second }.map { it.first }.toSet()
    }

    private fun calculateNewState(coordinate: Coordinate, activeCubes: PocketDimension): Boolean {
        val totalActiveNeighbours = getNeighbours(coordinate).count { activeCubes.contains(it) }
        return when {
            activeCubes.contains(coordinate) -> totalActiveNeighbours in 2..3
            totalActiveNeighbours == 3 -> true
            else -> false
        }
    }

    private fun calculateNewState(coordinate: Cofourdinate, activeCubes: Pocket4Dimension): Boolean {
        val totalActiveNeighbours = getNeighbours(coordinate).count { activeCubes.contains(it) }
        return when {
            activeCubes.contains(coordinate) -> totalActiveNeighbours in 2..3
            totalActiveNeighbours == 3 -> true
            else -> false
        }
    }

    private fun getNeighbours(coordinate: Coordinate): Set<Coordinate> {
        val coordinates = mutableSetOf<Coordinate>()
        (coordinate.x - 1..coordinate.x + 1).forEach { x ->
            (coordinate.y - 1..coordinate.y + 1).forEach { y ->
                (coordinate.z - 1..coordinate.z + 1).forEach { z ->
                    coordinates.add(Coordinate(x, y, z))
                }
            }
        }

        coordinates.remove(coordinate)
        return coordinates
    }

    private fun getNeighbours(cofourdinate: Cofourdinate): Set<Cofourdinate> {
        val cofourdinates = mutableSetOf<Cofourdinate>()
        (cofourdinate.w - 1..cofourdinate.w + 1).forEach { w ->
            (cofourdinate.x - 1..cofourdinate.x + 1).forEach { x ->
                (cofourdinate.y - 1..cofourdinate.y + 1).forEach { y ->
                    (cofourdinate.z - 1..cofourdinate.z + 1).forEach { z ->
                        cofourdinates.add(Cofourdinate(w, x, y, z))
                    }
                }
            }
        }

        cofourdinates.remove(cofourdinate)
        return cofourdinates
    }
}

data class Coordinate(val x: Int, val y: Int, val z: Int)
data class Cofourdinate(val w: Int, val x: Int, val y: Int, val z: Int)
typealias PocketDimension = Set<Coordinate>
typealias Pocket4Dimension = Set<Cofourdinate>

fun main() {
    val input = File("src/main/resources/day17/input.txt").readLines()
    println(ConwayCubes().simulate(input))  // 298
    println(ConwayCubes().simulate4D(input))  // 1792
}