package day10

import java.io.File

class AdapterArray {

    fun findJolts(input: List<String>): Long {
        val adapters = input.map { it.toLong() } + 0
        val usedAdapters = findAdapterChain(adapters, listOf(0), 0)
        val ones = usedAdapters.zipWithNext { curr, next ->
            if (next - curr == 1L) 1L else 0
        }.sum()
        return ones * (adapters.size - ones)
    }

    fun findAdapterNodes(input: List<String>): Long {
        val adapters = input.map { it.toLong() } + 0
        val combs = findAdapterCombinations(adapters)
        println(combs)
        return countCombs(combs, combs[0]!!, 0, mutableMapOf())
    }

    private fun countCombs(
        combs: Map<Long, List<Long>>,
        toVisit: List<Long>,
        count: Long,
        visited: MutableMap<Long, Long>
    ): Long {
        if (toVisit.isEmpty()) {
            return 1
        }

        if (toVisit.size == 1 && visited.containsKey(toVisit[0])) {
            return visited[toVisit[0]]!!
        }

        val sum = toVisit.map {
            val sum = countCombs(combs, combs[it]!!, count, visited)
            visited[it] = sum
            sum
        }.sum()
//        println(visited)
        return sum
    }

    private fun findAdapterCombinations(availableAdapters: List<Long>): Map<Long, List<Long>> {
        val map = mutableMapOf<Long, List<Long>>().toSortedMap(Comparator.reverseOrder())
        availableAdapters.forEach { adapter ->
            map[adapter] = findCompatibleAdapters(adapter, availableAdapters)
        }

        return map
    }

    private tailrec fun findAdapterChain(availableAdapters: List<Long>, usedAdapters: List<Long>, joltage: Long): List<Long> {
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

    private fun findCompatibleAdapters(joltage: Long, adapters: List<Long>): List<Long> {
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
