package day04

import java.io.File

object PassportProcessing {

    private val YEAR_REGEX = Regex("[0-9]{4}")
    private val HEIGHT_REGEX = Regex("[0-9]+(?:cm|in)")
    private val HAIR_COLOUR_REGEX = Regex("#[0-9a-f]{6}")
    private val EYE_COLOUR_REGEX = Regex("amb|brn|blu|gry|grn|hzl|oth")
    private val PID_REGEX = Regex("[0-9]{9}")

    fun parseInput(input: String): Int {
        return input.split("\n\n")
                .map { passport ->
                    passport.replace("\n", " ").split(" ").map {
                        val fieldValuePair = it.split(":")
                        fieldValuePair[0] to fieldValuePair[1]
                    }.toMap()
                }.filter { isValid(it) }
                .count()
    }

    private fun isValid(passport: Map<String, String>): Boolean {
        return (passport.size == 8 || (!passport.containsKey("cid") && passport.size == 7)) &&
                passport.entries.all { (key, value) ->
                    isValidField(key, value)
                }
    }

    fun isValidField(key: String, value: String): Boolean {
        return when (key) {
            "byr" -> YEAR_REGEX.matches(value) && value.toInt() in 1920..2002
            "iyr" -> YEAR_REGEX.matches(value) && value.toInt() in 2010..2020
            "eyr" -> YEAR_REGEX.matches(value) && value.toInt() in 2020..2030
            "hgt" -> HEIGHT_REGEX.matches(value) &&
                    (value.endsWith("cm") && value.substringBefore("cm").toInt() in 150..193) ||
                    (value.endsWith("in") && value.substringBefore("in").toInt() in 59..76)
            "hcl" -> HAIR_COLOUR_REGEX.matches(value)
            "ecl" -> EYE_COLOUR_REGEX.matches(value)
            "pid" -> PID_REGEX.matches(value) && value.length == 9
            "cid" -> true
            else -> false
        }
    }
}

fun main() {
    val input = File("src/main/resources/day04/input.txt")
    println(PassportProcessing.parseInput(input.readText())) // 242 and 186
}