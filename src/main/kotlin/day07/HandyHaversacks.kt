package day07

import java.io.File

class HandyHaversacks {

    private val bagMap = mutableMapOf<String, List<BagNumber>>()
    private val inverseBagMap = mutableMapOf<String, MutableList<String>>()

    private val instructionRegex = Regex("""(.+) bags contain (\d.+,?)+[.]""")
    private val containsRegex = Regex("""(\d) (.+) bag[s]?""")

    fun findContainingBags1(input: List<String>, targetColour: String): Int {
        populateBagMap(input)
        var bagsNeeded = 0
        val bagsToCheck = mutableListOf(BagNumber(targetColour, 1))

        while(bagsToCheck.isNotEmpty()) {
            val bagToCheck = bagsToCheck.removeAt(0)
            println("adding ${bagToCheck.number} ${bagToCheck.colour} bags")
            bagsNeeded += bagToCheck.number
            val innerBags = bagMap[bagToCheck.colour] ?: emptyList()
            val multipliedBags = innerBags.map { bag -> BagNumber(bag.colour, bag.number * bagToCheck.number) }
            bagsToCheck.addAll(multipliedBags )
        }

        return bagsNeeded -1
    }

    private fun reverseBagMap() {
        bagMap.forEach { (colour, containsColours) ->
            containsColours.forEach { containsColour ->
                inverseBagMap.computeIfAbsent(containsColour.colour) { mutableListOf() }.add(colour)
            }
        }
    }

    fun findContainingBags(input: List<String>, targetColour: String): Int {
        populateBagMap(input)
        reverseBagMap()
        val bagsFound = mutableSetOf<String>()
        val bagsToCount = mutableListOf<String>()
        bagsFound.addAll(inverseBagMap[targetColour] ?: emptyList())
        bagsToCount.addAll(inverseBagMap[targetColour] ?: emptyList())

        while(bagsToCount.isNotEmpty()) {
            val bagToCount = bagsToCount.removeAt(0)
            val bags = inverseBagMap[bagToCount] ?: emptyList()
            bagsFound.addAll(bags)
            bagsToCount.addAll(bags)
        }

        return bagsFound.size
    }

    private fun populateBagMap(input: List<String>) {
        val bags = parseInstructions(input)
        bags.forEach { bag ->
            val bagColour = bag.first
            bagMap[bagColour] = bag.second
        }
    }

    private fun parseInstructions(input: List<String>): List<Pair<String, List<BagNumber>>> {
        return input.mapNotNull { instruction ->
            val parsedInstruction = instructionRegex.find(instruction)
            parsedInstruction?.let {
                val bagColour = parsedInstruction.groupValues[1]
                val containsInstructions = parsedInstruction.groupValues[2]
                val containsBagItems = containsInstructions.split(",")
                    .mapNotNull { containsInstruction ->
                        containsRegex.find(containsInstruction)?.let {
                            val containsBagGroups = it.groupValues
                            val containsBagNum = containsBagGroups[1].toInt()
                            val containsBagColour = containsBagGroups[2]
                            BagNumber(containsBagColour, containsBagNum)
                        }
                    }
                Pair(bagColour, containsBagItems)
            }
        }
    }

    data class BagNumber(val colour: String, val number: Int)
}

fun main() {
    val input = File("src/main/resources/day07/input.txt").readLines()
    println(HandyHaversacks().findContainingBags(input, "shiny gold")) //287
    println(HandyHaversacks().findContainingBags1(input, "shiny gold")) //48160
}