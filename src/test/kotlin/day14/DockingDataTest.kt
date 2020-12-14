package day14

import org.assertj.core.api.Assertions.`in`
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

internal class DockingDataTest {
    private val input = """
        mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
        mem[8] = 11
        mem[7] = 101
        mem[8] = 0
    """.trimIndent().lines()

    @Test
    fun `calculates the sum of the initialisation values`() {
        assertThat(DockingData().sumInitialisationValues(input)).isEqualTo(165)
    }

    @Test
    fun `generates floating bit combinations`() {
        assertThat(DockingData().generateFloatingBitCombinations("1X1X"))
            .containsExactlyInAnyOrder("1111", "1110", "1011", "1010")
    }

    @Test
    fun combGen() {
        val input = """
            mask = 000000000000000000000000000000X1001X
            mem[42] = 100
            mask = 00000000000000000000000000000000X0XX
            mem[26] = 1
        """.trimIndent().lines()
        assertThat(DockingData().applyV2(input)).isEqualTo(208)
    }
}