package day16

import java.io.File

class TicketTranslation {

    fun findTicketScanningErrorRate(input: List<String>): Int {
        val rules = parseRules(input)
        val nearbyTicketsList = input.dropWhile { it != "nearby tickets:" }
        val nearbyTickets = nearbyTicketsList.subList(1, nearbyTicketsList.size).map { parseTicket(it) }

        return nearbyTickets.map { ticket ->
            ticket.map { value ->
                if (rules.any { it.validate(value) }) {
                    0
                } else {
                    value
                }
            }
        }.flatten().sum()
    }

    fun identifyFields(input: List<String>): Long {
        val rules = parseRules(input)
        val myTicket = parseTicket(input.dropWhile { it != "your ticket:" }.take(2)[1])
        val nearbyTicketsList = input.dropWhile { it != "nearby tickets:" }
        val nearbyTickets = nearbyTicketsList.subList(1, nearbyTicketsList.size).map { parseTicket(it) }
        val validTickets = nearbyTickets.filter { ticket -> isValidTicket(ticket, rules) }
        val possibleRuleMatches = mutableMapOf<Int, MutableList<Rule>>()

        myTicket.indices.forEach { idx ->
            val fieldX = validTickets.map { it[idx] }
            rules.forEach { rule ->
                if (fieldX.all { value -> rule.validate(value) }) {
                    val list = possibleRuleMatches.computeIfAbsent(idx) { mutableListOf() }
                    list.add(rule)
                    possibleRuleMatches[idx] = list
                }
            }
        }

        while(possibleRuleMatches.values.any { it.size > 1 }) {
            val singleRuleMatchFields = possibleRuleMatches.values.filter { it.size == 1 }.map { it.first() }
            possibleRuleMatches.values.filter { it.size > 1 }.forEach { field ->
                field.removeAll(singleRuleMatchFields)
            }
        }

        val mappings = possibleRuleMatches.entries.map { (fieldIdx, rules) -> Pair(fieldIdx, rules.first()) }
        return mappings.filter { it.second.name.startsWith("departure") }
                .map { myTicket[it.first] }
                .map { it.toLong() }
                .reduce(Long::times)

    }

    private fun parseTicket(line: String): List<Int> = line.split(",").map{ it.toInt() }

    private fun isValidTicket(ticket: List<Int>, rules: List<Rule>) =
        ticket.all { value -> rules.any { rule -> rule.validate(value) } }

    private fun parseRules(input: List<String>): List<Rule> {
        val rulesRegex = Regex("""([a-z ]*): (\d+)-(\d+) or (\d+)-(\d+)""")
        return input.takeWhile { it.isNotBlank() }
            .map { line ->
                val match = rulesRegex.matchEntire(line) ?: throw IllegalStateException("Unable to parse rule $line")
                val groupVals = match.groupValues
                Rule(groupVals[1], IntRange(groupVals[2].toInt(), groupVals[3].toInt()), IntRange(groupVals[4].toInt(), groupVals[5].toInt()))
            }
    }

    data class Rule(val name: String, val range1: IntRange, val range2: IntRange) {
        fun validate(value: Int): Boolean {
            return value in range1 || value in range2
        }
    }
}

fun main() {
    val input = File("src/main/resources/day16/input.txt").readLines()
    println(TicketTranslation().findTicketScanningErrorRate(input))
    println(TicketTranslation().identifyFields(input))
}