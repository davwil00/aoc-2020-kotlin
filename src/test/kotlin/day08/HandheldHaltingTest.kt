package day08

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class HandheldHaltingTest {

    private val input = """
nop +0
acc +1
jmp +4
acc +3
jmp -3
acc -99
acc +1
jmp -4
acc +6        
    """.trim().lines()

    @Test
    fun `should parse instructions and return accumulator`() {
        assertThat(HandheldHalting().executeBootCode(input)).isEqualTo(5)
    }

    @Test
    fun `should identify incorrect instruction`() {
        assertThat(HandheldHalting().fixProgram(input)).isEqualTo(8)
    }
}