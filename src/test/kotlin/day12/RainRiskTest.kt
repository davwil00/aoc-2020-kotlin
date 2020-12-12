package day12

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class RainRiskTest {

    private val input = """
F10
N3
F7
R90
F11
    """.trim().lines()

    @Test
    fun `find Manhattan distance`() {
        assertThat(RainRisk().findManhattanDistance(input)).isEqualTo(25)
    }

    @Test
    fun `find Manhattan distance part 2`() {
        assertThat(RainRisk().findManhattanDistancePart2(input)).isEqualTo(286)
    }

    @Test
    fun `turn waypoint`() {
        assertThat(RainRisk.WaypointAndShipLocation(Pair(10, 4)).turnWaypoint(90)).isEqualTo(Pair(4, -10))
        assertThat(RainRisk.WaypointAndShipLocation(Pair(10, 4)).turnWaypoint(-90)).isEqualTo(Pair(-4, 10))
    }
}