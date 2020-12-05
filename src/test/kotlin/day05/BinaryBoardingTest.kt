package day05

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class BinaryBoardingTest {

    @MethodSource("generateInput")
    @ParameterizedTest
    fun `should decode seat number`(boardingPass: String, expectedSeatId: Int) {
        assertThat(BinaryBoarding().calculateSeatId(boardingPass))
            .isEqualTo(expectedSeatId)
    }


    companion object {

        @JvmStatic
        fun generateInput() = Stream.of(
            Arguments.of("FBFBBFFRLR", 357),
            Arguments.of("BFFFBBFRRR", 567),
            Arguments.of("FFFBBBFRRR", 119),
            Arguments.of("BBFFBBFRLL", 820)
        )
    }
}