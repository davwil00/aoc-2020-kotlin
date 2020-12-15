package day15

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class RambunctiousRecitationTest {

    @Test
    fun `play the game`() {
        assertThat(RambunctiousRecitation().playTheGame(2020, 0, 3, 6)).isEqualTo(436)
    }
}