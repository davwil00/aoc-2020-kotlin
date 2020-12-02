package day02

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PasswordPhilosophyTest {
    private val input = """
1-3 a: abcde
1-3 b: cdefg
2-9 c: ccccccccc
        """.trim().lines()

    @Test
    fun `should count valid passwords`() {
        assertThat(PasswordPhilosophy().countValidPasswords(input)).isEqualTo(2)
    }

    @Test
    fun `should count valid passwords for toboggan policy`() {
        assertThat(PasswordPhilosophy().countValidTobogganPasswords(input)).isEqualTo(1)
    }
}