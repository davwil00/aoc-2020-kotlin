package day17

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

internal class ConwayCubesTest {
    private val input = """
.#.
..#
###
""".trim().lines()

    @Test
    fun `should correctly simulate 6 cycles`() {
        assertThat(ConwayCubes().simulate(input)).isEqualTo(112)
    }

    @Test
    fun `should correctly simulate 6 cycles for 4D`() {
        assertThat(ConwayCubes().simulate4D(input)).isEqualTo(848)
    }

}