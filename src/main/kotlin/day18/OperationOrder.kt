package day18

import java.io.File
import java.lang.IllegalStateException

class OperationOrder() {

    fun sumEquationResults(input: List<String>): Long {
        return input.map { calculate(it) }.sum()
    }

    fun calculate(line: String): Long {
        val input = line.replace(" ", "").toCharArray()
        val equation = parseInput(input)
        return equation.solve()
    }

    private fun parseInput(charArray: CharArray): Equation {
        val char0 = charArray[0]
        if (charArray.size == 1) {
            return Equation(char0.intVal())
        }
        return if (isBracket(char0)) {
            val (bracketedEquationInput, idx) = findMatchingBracket(charArray.sliceArray(1 until charArray.size))
            val bracketedEquation = parseInput(bracketedEquationInput)
            val restOfEqnChars = charArray.sliceArray(idx + 3 until charArray.size)
            return if (restOfEqnChars.isNotEmpty()) {
                val restOfEquation = parseInput(restOfEqnChars)
                Equation(bracketedEquation.solve(), restOfEquation, Operator.fromChar(charArray[idx + 2]))
            } else {
                Equation(bracketedEquation.solve())
            }
        } else {
            if (isOperator(charArray[1])) {
                val left = char0.intVal()
                val operator = Operator.fromChar(charArray[1])
                val right = parseInput(charArray.sliceArray(2 until charArray.size))
                Equation(left, right, operator)
            } else {
                throw IllegalStateException("Unable to parse input $charArray")
            }
        }
    }

    private fun findMatchingBracket(charArray: CharArray): Pair<CharArray, Int> {
        var brackets = 1
        charArray.forEachIndexed { idx, char ->
            when (char) {
                '(' -> brackets++
                ')' -> brackets--
            }

            if (brackets == 0) {
                return Pair(charArray.sliceArray(0 until idx), idx)
            }
        }

        throw IllegalStateException("Unable to find matching bracket")
    }

    private fun isBracket(char: Char) = char == '(' || char == ')'
    private fun isOperator(char: Char) = char == '+' || char == '*'

    enum class Operator(val sign: Char, val fn: (Long, Long) -> Long) {
        ADD('+', Long::plus), MULTIPLY('*', Long::times);

        fun applyLtoR(left: Long, right: Equation): Long {
            println("applying $left $sign $right")
            return when {
                right.right == null -> {
                    println("right is false processing $left $sign ${right.left}")
                    fn(left, right.left)
                }
                right.operator != null -> {
                    println("processing $left $sign ${right.left} ${right.operator.sign} ${right.right}")
                    right.operator.applyLtoR(fn(left, right.left), right.right)
                }
                else -> throw IllegalStateException("Unable to apply operator")
            }
        }

        fun applyPlusPrecedence(left: Long, right: Equation): Long {
            println("applying $left $sign $right")
            return when {
                right.right == null -> {
                    println("right is false processing $left $sign ${right.left}")
                    fn(left, right.left)
                }
                right.operator != null -> {
                    println("processing $left $sign ${right.left} ${right.operator.sign} ${right.right}")
                    if (this == ADD) {
                        right.operator.applyPlusPrecedence(fn(left, right.left), right.right)
                    } else {
                        fn(left, right.solve())
                    }
                }
                else -> throw IllegalStateException("Unable to apply operator")
            }
        }

        companion object {
            fun fromChar(char: Char): Operator {
                return values().firstOrNull { it.sign == char }
                    ?: throw IllegalArgumentException("Unknown operator: $char")
            }
        }
    }

    data class Equation(val left: Long,
                        val right: Equation? = null,
                        val operator: Operator? = null) {

        fun solve(): Long {
            return if (operator == null || right == null) {
                left
            } else {
                operator.applyPlusPrecedence(left, right)
                //operator.applyLtoR(left, right)
            }
        }
    }

    private fun Char.intVal() = Character.digit(this, 10).toLong()
}

fun main() {
    val input = File("src/main/resources/day18/input.txt").readLines()
    println(OperationOrder().sumEquationResults(input)) // 18213007238947
}