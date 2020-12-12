package day11

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SeatingSystemTest {
    private val input = """
L.LL.LL.LL
LLLLLLL.LL
L.L.L..L..
LLLL.LL.LL
L.LL.LL.LL
L.LLLLL.LL
..L.L.....
LLLLLLLLLL
L.LLLLLL.L
L.LLLLL.LL
    """.trim().lines()

    @Test
    fun `find occupied seats at stability point part 1`() {
        assertThat(SeatingSystem(Part.PART1).findOccupiedSeats(input)).isEqualTo(37)
    }

    @Test
    fun `find occupied seats at stability point part 2`() {
        assertThat(SeatingSystem(Part.PART2).findOccupiedSeats(input)).isEqualTo(26)
    }
}