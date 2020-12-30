package day23

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class CrabCupsTest {

    @Test
    fun `play the game 10 times`() {
        assertThat(CrabCups().playTheSimpleGame(listOf(3,8,9,1,2,5,4,6,7), 10)).isEqualTo("92658374")
    }

    @Test
    fun `play the game 100 times`() {
        assertThat(CrabCups().playTheSimpleGame(listOf(3,8,9,1,2,5,4,6,7), 100)).isEqualTo("67384529")
    }

    @Test
    fun `play the massive game`() {
        val input = mutableListOf(3,8,9,1,2,5,4,6,7)
        input.addAll((10..1_000_000).toList())
        assertThat(CrabCups().playTheLongGame(input, 10_000_000)).isEqualTo(149245887792)
    }
}