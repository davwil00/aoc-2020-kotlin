package day14

import java.io.File

class DockingData {

    private val instructionRegex = Regex("""mem\[(\d+)] = (\d+)""")

    fun sumInitialisationValues(input: List<String>): Long {
        val programs = parseInput(input)
        val memory = mutableMapOf<Long, Long>()
        programs.map { program ->
            program.applyMasks()
        }.forEach { instructions ->
            instructions.forEach {
                memory[it.address] = it.value
            }
        }

        return memory.values.sum()
    }

    fun applyV2(input: List<String>): Long {
        val programs = parseInput(input)
        val memory = mutableMapOf<Long, Long>()
        programs.map { program ->
            program.applyMasksV2()
        }.forEach { maskVals ->
            maskVals.forEach { maskVal ->
                generateFloatingBitCombinations(maskVal.first).forEach {
                    memory[it.toLong(2)] = maskVal.second
                }
            }
        }

        return memory.values.sum()
    }

    fun generateFloatingBitCombinations(bitmask: String, combinations: List<String> = listOf()): List<String> {
        if (!bitmask.contains("X")) {
            return listOf(bitmask)
        }

        val idx = bitmask.indexOf('X')
        val newList = mutableListOf<String>()
        newList.addAll(generateFloatingBitCombinations(bitmask.replaceAt(idx, '0'), combinations))
        newList.addAll(generateFloatingBitCombinations(bitmask.replaceAt(idx, '1'), combinations))
        return newList
    }

    private fun String.replaceAt(idx: Int, newValue: Char): String {
        return this.substring(0, idx) + newValue + this.substring(idx + 1)
    }

    private fun parseInput(input: List<String>): List<Program> {
        val programs = mutableListOf<Program>()
        var bitmask = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
        var instructions = mutableListOf<Instruction>()
        input.forEach {
            if (it.startsWith("mask")) {
                programs.add(Program(bitmask, instructions))
                bitmask = it.substring(7)
                instructions = mutableListOf()
            } else {
                val matchResult = instructionRegex.find(it)!!
                instructions.add(Instruction(matchResult.groupValues[1].toLong(), matchResult.groupValues[2].toLong()))
            }
        }

        programs.add(Program(bitmask, instructions))

        return programs.subList(1, programs.size)
    }

    data class Program(val bitmask: String, val instructions: List<Instruction>) {
        fun applyMasks(): List<Instruction> {
            return instructions.map { instruction ->
                val binValue = java.lang.Long.toBinaryString(instruction.value).padStart(36, '0')
                val maskedValue = binValue.zip(bitmask).map { (binVal, maskVal) ->
                    if (maskVal == 'X') {
                        binVal
                    } else {
                        maskVal
                    }
                }.joinToString("").toLong(2)
                Instruction(instruction.address, maskedValue)
            }
        }

        fun applyMasksV2(): List<Pair<String, Long>> {
            return instructions.map { instruction ->
                val binValue = java.lang.Long.toBinaryString(instruction.address).padStart(36, '0')
                val maskedValue = binValue.zip(bitmask).map { (binVal, maskVal) ->
                    when (maskVal) {
                        '0' -> binVal
                        '1' -> '1'
                        else -> 'X'
                    }
                }.joinToString("")
                Pair(maskedValue, instruction.value)
            }
        }
    }

    data class Instruction(val address: Long,val value: Long)
}

fun main() {
    val input = File("src/main/resources/day14/input.txt").readLines()
    println(DockingData().sumInitialisationValues(input)) // 10035335144067
    println(DockingData().applyV2(input)) // 3817372618036
}