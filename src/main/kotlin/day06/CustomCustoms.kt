package day06

import java.io.File

class CustomCustoms {

    fun readUniqueGroupsAnswers(input: String): Int {
        return input.split("\n\n")
            .map { groupAnswers ->
                groupAnswers.replace(Regex("\\s"), "")
                    .split("")
                    .distinct()
                    .joinToString("")
            }
            .sumBy { it.length }
    }

    fun findQuestionsAnsweredByAllInGroup(input: String): Int {
        val groupsAnswers = input.split("\n\n")
        return groupsAnswers.map { groupAnswers ->
            groupAnswers.split("\n")
                .map { it.toSet() }
                .reduce{ acc, curr -> acc.intersect(curr) }
                .size
        }.sum()
    }
}

fun main() {
    val input = File("src/main/resources/day06/input.txt").readText()
    println(CustomCustoms().readUniqueGroupsAnswers(input)) //6506
    println(CustomCustoms().findQuestionsAnsweredByAllInGroup(input)) //3243
}
