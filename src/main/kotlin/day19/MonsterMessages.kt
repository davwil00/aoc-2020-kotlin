package day19

import org.slf4j.LoggerFactory
import java.io.File
import java.lang.IllegalStateException

class MonsterMessages {

    private val logger = LoggerFactory.getLogger(MonsterMessages::class.java)

    fun validateMessages(input: String): Int {
        val (rules, messages) = input.split("\n\n")
        val ruleMap = processRules(rules.lines())
        val patterns = resolveRules(ruleMap).map { it }.toSet()
        return messages.lines()
            .count {
                patterns.contains(it)
            }
    }

    fun validateMessagesWithLoops(input: String): Int {
        val (rules, messages) = input.split("\n\n")
        val ruleMap = processRules(rules.lines()).toMutableMap()
        val resolvedRules = mutableMapOf<Int, List<String>>()
        resolveRules(ruleMap, resolvedRules = resolvedRules).toSet()
        val chunkSize = minOf(resolvedRules.getValue(42).first().length)
        return messages.lines().count { message ->
            val chunks = message.chunked(chunkSize)
            val numberOfRule42Matches = chunks.takeWhile { it in resolvedRules.getValue(42) }.size
            val numberOfRule31Matches =  chunks.dropWhile { it in resolvedRules.getValue(42) }
                .takeWhile { it in resolvedRules.getValue(31) }.size
            (numberOfRule42Matches + numberOfRule31Matches == message.length / chunkSize &&
                    numberOfRule42Matches > numberOfRule31Matches &&
                    numberOfRule31Matches > 0).also {
                if (it) {
                    logger.debug(message)
                }
            }
        }
    }

    private fun processRules(rules: List<String>): Map<Int, Rule> {
        val ruleMap = mutableMapOf<Int, Rule>()
        rules.forEach {
            when {
                LinkRule.matches(it) -> {
                    val rule = LinkRule.from(it)
                    ruleMap[rule.ruleNum] = rule
                }
                AbsoluteRule.matches(it) -> {
                    val rule = AbsoluteRule.from(it)
                    ruleMap[rule.ruleNum] = rule
                }
                else -> throw IllegalStateException("Unmatched input $it")
            }
        }

        return ruleMap
    }

    private fun resolveRules(rules: Map<Int, Rule>, currentRule: Rule = rules[0]!!, resolvedRules: MutableMap<Int, List<String>> = mutableMapOf()): List<String> {
        if (currentRule.ruleNum in resolvedRules) {
            return resolvedRules.getValue(currentRule.ruleNum)
        }
        return when (currentRule) {
            is AbsoluteRule -> listOf(currentRule.value)
            is LinkRule -> {
                val resolvedLinkRules = currentRule.linkedRules.map { linkedRule ->
                    resolveRules(rules, rules[linkedRule]!!, resolvedRules)
                }.reduce(mergeLists())

                if (currentRule.orLinkedRules != null) {
                    val resolvedOrLinkRules = currentRule.orLinkedRules.map { orLinkedRule ->
                        resolveRules(rules, rules[orLinkedRule]!!, resolvedRules)
                    }.reduce(mergeLists())

                    val resolvedRulesAndOrRules = listOf(resolvedLinkRules, resolvedOrLinkRules).flatten()
                    resolvedRules[currentRule.ruleNum] = resolvedRulesAndOrRules
                    return resolvedRulesAndOrRules
                }
                resolvedRules[currentRule.ruleNum] = resolvedLinkRules
                return resolvedLinkRules
            }
            else -> throw IllegalStateException("Unknown rule")
        }
    }

    private fun mergeLists() = { acc: List<String>, curr: List<String> ->
        acc.flatMap { outer ->
            curr.map { inner ->
                outer + inner
            }
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
    println(MonsterMessages().validateMessages(input)) // 151
    println(MonsterMessages().validateMessagesWithLoops(input)) // 386
}