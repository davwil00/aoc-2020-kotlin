package day19

import java.io.File
import java.lang.IllegalStateException

class MonsterMessages {

    fun validateMessages(input: String): Int {
        val (rules, messages) = input.split("\n\n")
        val ruleMap = processRules(rules.split("\n"))
        val pattern = resolveRules(ruleMap[0]!!, ruleMap, "")
        val regex = Regex(pattern)
        return messages.split("\n").count { regex.matches(it) }
    }

    private fun processRules(rules: List<String>): Map<Int, Rule> {
        val ruleMap = mutableMapOf<Int, Rule>()
        rules.forEach {
            if (LinkRule.matches(it)) {
                val rule = LinkRule.from(it)
                ruleMap[rule.ruleNum] = rule
            } else if (AbsoluteRule.matches(it)) {
                val rule = AbsoluteRule.from(it)
                ruleMap[rule.ruleNum] = rule
            } else {
                throw IllegalStateException("Unmatched input $it")
            }
        }

        return ruleMap
    }

    private fun resolveRules(currentRule: Rule, rules: Map<Int, Rule>, pattern: String): String {
        return when (currentRule) {
            is AbsoluteRule -> pattern + currentRule.value
            is LinkRule -> {
                val resolvedLinkRule = currentRule.linkedRules.map { linkedRule ->
                    resolveRules(rules[linkedRule]!!, rules, pattern)
                }.joinToString("")
                if (currentRule.orLinkedRules != null) {
                    val resolvedOrLinkRule = currentRule.orLinkedRules.map { orLinkedRule ->
                        resolveRules(rules[orLinkedRule]!!, rules, pattern)
                    }.joinToString("")
                    return "($resolvedLinkRule|$resolvedOrLinkRule)"
                }
                return resolvedLinkRule
            }
            else -> throw IllegalStateException("Unknown rule")
        }
    }

    abstract class Rule(val ruleNum: Int)

    class LinkRule(ruleNum: Int, val linkedRules: List<Int>, val orLinkedRules: List<Int>? = null): Rule(ruleNum) {
        companion object {
            private val regex = Regex("""(\d+): ((?:\d+ ?)+)(?:\| ((?:\d+ ?)+))?""")
            fun matches(rule: String) = regex.matches(rule)
            fun from(rule: String): LinkRule {
                val match = regex.find(rule)
                val ruleNum = match!!.groupValues[1].toInt()
                val links = match.groupValues[2].trim().split(" ").map { it.toInt() }
                return if (match.groupValues[3].isNotBlank()) {
                    val orLinks = match.groupValues[3].split(" ").map { it.toInt() }
                    LinkRule(ruleNum, links, orLinks)
                } else {
                    LinkRule(ruleNum, links)
                }
            }
        }
    }

    class AbsoluteRule(ruleNum: Int, val value: String): Rule(ruleNum) {
        companion object {
            private val regex = Regex("""(\d+): "([ab])"""")
            fun matches(rule: String) = regex.matches(rule)
            fun from(rule: String): AbsoluteRule {
                val match = regex.find(rule)
                val ruleNum = match!!.groupValues[1].toInt()
                val value = match.groupValues[2]
                return AbsoluteRule(ruleNum, value)
            }
        }
    }
}

fun main() {
    val input = File("src/main/resources/day19/input.txt").readText()
    println(MonsterMessages().validateMessages(input))
}