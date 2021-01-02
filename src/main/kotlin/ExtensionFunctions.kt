operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> = Pair(this.first + other.first, this.second + other.second)
operator fun Pair<Int, Int>.times(multiplier: Int): Pair<Int, Int> = Pair(this.first * multiplier, this.second * multiplier)

fun List<String>.rotate(): List<String> {
    val newData = mutableListOf<String>()
    this.indices.forEach { col ->
        val line = mutableListOf<Char>()
        (this.size - 1 downTo 0).forEach { row ->
            line.add(this[row][col])
        }
        newData.add(line.joinToString(""))
    }

    return newData
}

fun List<String>.flipVertical() = this.map { line -> line.reversed() }

fun Regex.countMatches(input: String) = this.findAll(input).toList().size