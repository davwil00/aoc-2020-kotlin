package day02

import java.io.File
import java.lang.IllegalStateException

class PasswordPhilosophy {

    fun countValidPasswords(input: List<String>): Int =
        parseInput(input).count{ isValidPassword(it.first, it.second) }

    fun countValidTobogganPasswords(input: List<String>): Int =
        parseInput(input).count{ isValidTobogganPassword(it.first, it.second) }

    private fun isValidPassword(passwordPolicy: PasswordPolicy, password: String): Boolean {
        val charCount = password.count { it == passwordPolicy.letter }
        return charCount <= passwordPolicy.maxOccurrences && charCount >= passwordPolicy.minOccurrences
    }

    private fun isValidTobogganPassword(passwordPolicy: PasswordPolicy, password: String): Boolean {
        val pos1ContainsChar = password[passwordPolicy.minOccurrences - 1] == passwordPolicy.letter
        val pos2ContainsChar = password[passwordPolicy.maxOccurrences - 1] == passwordPolicy.letter

        return pos1ContainsChar xor pos2ContainsChar
    }

    private fun parseInput(input: List<String>): List<Pair<PasswordPolicy, String>> =
        input.map {
            val splits = it.split(": ")
            val passwordPolicy = PasswordPolicy.parse(splits[0])
            Pair(passwordPolicy, splits[1])
        }
}

fun main() {
    val input = File("src/main/resources/day02/input.txt").readLines()
    val validPasswords = PasswordPhilosophy().countValidPasswords(input)
    val validTobogganPasswords = PasswordPhilosophy().countValidTobogganPasswords(input)
    println(validPasswords)
    println(validTobogganPasswords)
}

data class PasswordPolicy(val minOccurrences: Int, val maxOccurrences: Int, val letter: Char) {
    companion object {
        private val policyRegex = Regex("""(\d+)-(\d+) ([a-z])""")

        fun parse(input: String): PasswordPolicy {
            val match = policyRegex.find(input)
            if (match != null) {
                val groupValues = match.groupValues
                return PasswordPolicy(groupValues[1].toInt(), groupValues[2].toInt(), groupValues[3][0])
            }

            throw IllegalStateException("Unable to parse policy")
        }
    }
}