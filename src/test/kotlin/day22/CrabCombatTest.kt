package day22

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class CrabCombatTest {

    private val input = """
        Player 1:
        9
        2
        6
        3
        1

        Player 2:
        5
        8
        4
        7
        10
    """.trimIndent().lines()

    @Test
    fun `should simulate gameplay`() {
        assertThat(CrabCombat().simulateGameplay(input)).isEqualTo(306)
    }

    @Test
    fun `should prevent infinite game`() {
        val input = """
            Player 1:
            43
            19

            Player 2:
            2
            29
            14
        """.trimIndent().lines()
        assertThat(CrabCombat().simulateGameplayPartB(input))
    }

    @Test
    fun `should simulate gameplay for part B`() {
        assertThat(CrabCombat().simulateGameplayPartB(input)).isEqualTo(291)
    }
}