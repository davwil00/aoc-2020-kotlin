package day13

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ShuttleSearchTest {

    private val input = """
        939
        7,13,x,x,59,x,31,19
    """.trimIndent().lines()

    @Test
    fun `should find earliest bus`() {
        assertThat(ShuttleSearch().findEarliestBus(input)).isEqualTo(295)
    }

    @Test
    fun `find earliest timestamp for subsequent departures`() {
        assertThat(ShuttleSearch().findEarliestTimestampForSubsequentDepartures(input[1].split(","))).isEqualTo(1068781)
        assertThat(ShuttleSearch().findEarliestTimestampForSubsequentDepartures(listOf("67","7","59","61"))).isEqualTo(754018)
        assertThat(ShuttleSearch().findEarliestTimestampForSubsequentDepartures(listOf("67","x","7","59","61"))).isEqualTo(779210)
        assertThat(ShuttleSearch().findEarliestTimestampForSubsequentDepartures(listOf("67","7","x","59","61"))).isEqualTo(1261476)
        assertThat(ShuttleSearch().findEarliestTimestampForSubsequentDepartures(listOf("1789","37","47","1889"))).isEqualTo(1202161486)
    }
}