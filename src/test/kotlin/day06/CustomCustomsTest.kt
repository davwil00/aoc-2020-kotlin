package day06

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class CustomCustomsTest {
    private val input = """
abc

a
b
c

ab
ac

a
a
a
a

b
""".trim()

    @Test
    fun `should count unique group answers`() {
        assertThat(CustomCustoms().readUniqueGroupsAnswers(input)).isEqualTo(11)
    }

    @Test
    fun `should count answers where everyone in group answers yes`() {
        assertThat(CustomCustoms().findQuestionsAnsweredByAllInGroup(input)).isEqualTo(6)
    }
}