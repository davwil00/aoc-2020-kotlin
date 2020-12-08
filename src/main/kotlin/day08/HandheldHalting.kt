package day08

import java.io.File
import java.lang.UnsupportedOperationException

class HandheldHalting {

    fun executeBootCode(input: List<String>): Int {
        val parsedInstructions = input.mapIndexed { idx, instruction -> parseInstruction(idx, instruction) }
        return executeBootCode(parsedInstructions)
    }

    fun fixProgram(input: List<String>): Int {
        val parsedInstructions = input.mapIndexed { idx, instruction -> parseInstruction(idx, instruction) }
        return fixProgram(parsedInstructions.toMutableList())
    }

    private tailrec fun executeBootCode(parsedInstructions: List<Instruction>,
                                        accumulator: Int = 0,
                                        idx: Int = 0,
                                        runInstructions: Set<Int> = setOf()): Int {
        if (runInstructions.contains(idx)) {
            return accumulator
        }

        val nextInstruction = try {
            runProgram(parsedInstructions[idx], accumulator, idx)
        } catch (e: java.lang.IndexOutOfBoundsException) {
            throw ProgramCompletedException(accumulator)
        }

        return executeBootCode(parsedInstructions, nextInstruction.first, nextInstruction.second, runInstructions + idx)
    }

    private fun runProgram(parsedInstruction: Instruction, accumulator: Int, idx: Int): AccIdx {
//        println("$parsedInstruction acc: $accumulator idx: $idx")
        return when (parsedInstruction.operation) {
            "nop" -> AccIdx(accumulator, idx + 1)
            "acc" -> AccIdx(accumulator + parsedInstruction.argument, idx + 1)
            "jmp" -> AccIdx(accumulator, idx + parsedInstruction.argument)
            else -> throw UnsupportedOperationException(parsedInstruction.operation)
        }
    }

    private tailrec fun fixProgram(parsedInstructions: MutableList<Instruction>, attemptedIdx: Int = -1): Int {
        val instructionToReplace = parsedInstructions.filter {
            it.idx > attemptedIdx && (it.operation == "nop" || it.operation == "jmp")
        }.first()
//        println("fixing instruction ${instructionToReplace.idx}")
        val newInstruction = Instruction(instructionToReplace.idx, flipOp(instructionToReplace.operation), instructionToReplace.argument)
        parsedInstructions[instructionToReplace.idx] = newInstruction

        try {
            executeBootCode(parsedInstructions)
        } catch (e: ProgramCompletedException) {
            return e.accumulator
        }

        parsedInstructions[instructionToReplace.idx] = instructionToReplace // reset list
        return fixProgram(parsedInstructions, instructionToReplace.idx)
    }

    private fun flipOp(op: String) = if (op == "nop") "jmp" else if (op == "jmp") "nop" else op

    private fun parseInstruction(idx: Int, instruction: String): Instruction {
        val splitInstruction = instruction.split(" ")
        return Instruction(idx, splitInstruction[0], splitInstruction[1].toInt())
    }

    data class Instruction(val idx: Int, var operation: String, val argument: Int)
}

typealias AccIdx = Pair<Int, Int>

class ProgramCompletedException(val accumulator: Int): RuntimeException()

fun main() {
    val input = File("src/main/resources/day08/input.txt").readLines()
    println(HandheldHalting().executeBootCode(input)) // 1654
    println(HandheldHalting().fixProgram(input)) // 833
}