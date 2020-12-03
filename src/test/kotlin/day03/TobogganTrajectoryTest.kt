package day03

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.streams.toList

internal class TobogganTrajectoryTest {
    private val input = """
..##.......
#...#...#..
.#....#..#.
..#.#...#.#
.#...##..#.
..#.##.....
.#.#.#....#
.#........#
#.##...#...
#...##....#
.#..#...#.#
        """.trim().lines()

    @MethodSource("getParams")
    @ParameterizedTest
    fun `should find trees on path5`(path: Pair<Int, Int>, expected: Long) {
        assertThat(TobogganTrajectory(11).findTreesOnPaths(input, listOf(path))).isEqualTo(expected)
    }

    @Test
    fun `should find trees on paths`() {
        val paths = getParams().map {it.get()[0] as Pair<Int, Int>}.toList()
        assertThat(TobogganTrajectory(11).findTreesOnPaths(input, paths)).isEqualTo(336)
    }

    companion object {
        @JvmStatic
        fun getParams() = Stream.of(
            Arguments.of(Pair(1, 1), 2L),
            Arguments.of(Pair(3, 1), 7L),
            Arguments.of(Pair(5, 1), 3L),
            Arguments.of(Pair(7, 1), 4L),
            Arguments.of(Pair(1, 2), 2L),
        )
    }
}