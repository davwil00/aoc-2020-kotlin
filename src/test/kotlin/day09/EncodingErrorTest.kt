package day09

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class EncodingErrorTest {
    private val input = """
35
20
15
25
47
40
62
55
65
95
102
117
150
182
127
219
299
277
309
576
    """.trim().lines()

    @Test
    fun `should find invalid number`() {
        assertThat(EncodingError().findInvalidNumber(input, 5)).isEqualTo(127)
    }

    @Test
    fun `should find encryption key`() {
        assertThat(EncodingError().breakEncryption(input, 127)).isEqualTo(62)
    }
}