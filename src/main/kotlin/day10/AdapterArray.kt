package day10

import java.io.File

class AdapterArray {

    fun findJolts(input: List<String>): Int {
        val adapters = input.map { it.toInt() } + 0
        val usedAdapters = findAdapterChain(adapters, listOf(0), 0)
        val ones = usedAdapters.zipWithNext { curr, next ->
            if (next - curr == 1) 1 else 0
        }.sum()
        return ones * (adapters.size - ones)
    }

    fun findAdapterNodes(input: List<String>): Int {
        val adapters = input.map { it.toInt() } + 0
        val combs = findAdapterCombinations(adapters)
        println(combs)
        return countCombs(combs, combs[0]!!, 0, mutableMapOf())
    }

    private fun countCombs(
        combs: Map<Int, List<Int>>,
        toVisit: List<Int>,
        count: Int,
        visited: MutableMap<Int, Int>
    ): Int {
        if (toVisit.isEmpty()) {
            return 1
        }

        if (toVisit.size == 1 && visited.containsKey(toVisit[0])) {
            return visited[toVisit[0]]!!
        }

        return toVisit.map {
            val sum = countCombs(combs, combs[it]!!, count, visited)
            visited[it] = sum
            sum
        }.sum()
    }

    private fun findAdapterCombinations(availableAdapters: List<Int>): Map<Int, List<Int>> {
        val map = mutableMapOf<Int, List<Int>>().toSortedMap(Comparator.reverseOrder())
        availableAdapters.forEach { adapter ->
            map[adapter] = findCompatibleAdapters(adapter, availableAdapters)
        }

        return map
    }

    private tailrec fun findAdapterChain(availableAdapters: List<Int>, usedAdapters: List<Int>, joltage: Int): List<Int> {
        if (availableAdapters.isEmpty()) {
            return usedAdapters
        }
        val adapterToUse = findCompatibleAdapters(joltage, availableAdapters).minOrNull()!!
        return findAdapterChain(
            availableAdapters.filterNot { it <= adapterToUse },
            usedAdapters + adapterToUse,
            adapterToUse
        )
    }

    private fun findCompatibleAdapters(joltage: Int, adapters: List<Int>): List<Int> {
        return adapters.filter {
            it in joltage + 1..joltage + 3
        }
    }
}

fun main() {
    val input = File("src/main/resources/day10/input.txt").readLines()
    println(AdapterArray().findJolts(input)) // 1656
    println(AdapterArray().findAdapterNodes(input)) // 344068096 too low
}
