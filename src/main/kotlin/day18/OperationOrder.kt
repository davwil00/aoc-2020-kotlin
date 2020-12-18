package day18

class OperationOrder {

    fun calculate(line: String) {
        val input = line.replace(" ", "").toCharArray()
        val equation = parseInput(input)

    }

    fun parseInput(charArray: CharArray): Equation {
        val char0 = charArray[0]
        if (charArray.size == 1) {
            return Equation(char0.toInt())
        }
//        var equations = mutableListOf<Equation>()
        if (!isBracket(char0)) {
            val currentEquation = Equation(char0.toInt())
            if (isOperator(charArray[1])) {
                currentEquation.operator = Operator.fromChar(charArray[1])
                currentEquation.right = parseInput(charArray.sliceArray(2..charArray.size))
            }
        }
    }

    private fun isBracket(char: Char) = char == '(' || char == ')'
    private fun isOperator(char: Char) = char == '+' && char == '*'

    enum class Operator(val sign: Char) {
        ADD('+'), MULTIPLY('*');

        companion object {
            fun fromChar(char: Char): Operator {
                return values().firstOrNull { it.sign == char }
                    ?: throw IllegalArgumentException("Unknown operator: $char")
            }
        }
    }

    data class Equation(val left: Int, var right: Equation? = null, var operator: Operator? = null)
}