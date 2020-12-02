package day02

import java.io.File
import java.lang.IllegalStateException

class PasswordPhilosophy {

    fun countValidPasswords(input: List<String>): Int =
        parseInput(input).count{ isValidPassword(it) }

    fun countValidTobogganPasswords(input: List<String>): Int =
        parseInput(input).count{ isValidTobogganPassword(it) }

    private fun isValidPassword(passwordAndPolicy: PasswordAndPolicy): Boolean {
        val charCount = passwordAndPolicy.password.count { it == passwordAndPolicy.letter }
        return charCount <= passwordAndPolicy.number2 && charCount >= passwordAndPolicy.number1
    }

    private fun isValidTobogganPassword(passwordAndPolicy: PasswordAndPolicy): Boolean {
        val pos1ContainsChar = passwordAndPolicy.password[passwordAndPolicy.number1 - 1] == passwordAndPolicy.letter
        val pos2ContainsChar = passwordAndPolicy.password[passwordAndPolicy.number2 - 1] == passwordAndPolicy.letter

        return pos1ContainsChar xor pos2ContainsChar
    }

    private fun parseInput(input: List<String>): List<PasswordAndPolicy> = input.map { PasswordAndPolicy.parse(it) }
}

fun main() {
    val input = File("src/main/resources/day02/input.txt").readLines()
    println(PasswordPhilosophy().countValidPasswords(input))
    println(PasswordPhilosophy().countValidTobogganPasswords(input))
}

data class PasswordAndPolicy(val number1: Int, val number2: Int, val letter: Char, val password: String) {
    companion object {
        private val policyRegex = Regex("""(\d+)-(\d+) ([a-z]): (.*)""")

        fun parse(input: String): PasswordAndPolicy {
            val match = policyRegex.find(input)
            if (match != null) {
                val groupValues = match.groupValues
                return PasswordAndPolicy(groupValues[1].toInt(), groupValues[2].toInt(), groupValues[3][0], groupValues[4])
            }

            throw IllegalStateException("Unable to parse policy")
        }
    }
}