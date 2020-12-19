package day18

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class OperationOrderTest {

    @Test
    fun `should calculate simple correctly`() {
        assertThat(OperationOrder().calculate("1 + 2 * 3 + 4 * 5 + 6")).isEqualTo(71L)
    }

    @Test
    fun `should calculate with brackets`() {
        assertThat(OperationOrder().calculate("1 + (2 * 3) + (4 * (5 + 6))")).isEqualTo(51L)
    }

    @ParameterizedTest
    @CsvSource("2 * 3 + (4 * 5),26",
        "5 + (8 * 3 + 9 + 3 * 4 * 3),437",
        "5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4)),12240",
        "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2,13632"
    )
    fun `additional checks`(input: String, expected: Long) {
        assertThat(OperationOrder().calculate(input)).isEqualTo(expected)
    }

    @Test
    fun `should calculate simple correctly with plus precedence`() {
        assertThat(OperationOrder().calculate("1 + 2 * 3 + 4 * 5 + 6")).isEqualTo(231L)
    }


    @ParameterizedTest
    @CsvSource("1 + (2 * 3) + (4 * (5 + 6)),51",
        "2 * 3 + (4 * 5),46",
        "5 + (8 * 3 + 9 + 3 * 4 * 3),1445",
        "5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4)),669060",
        "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2,23340"
    )
    fun `additional checks with plus precedence`(input: String, expected: Long) {
        assertThat(OperationOrder().calculate(input)).isEqualTo(expected)
    }
}