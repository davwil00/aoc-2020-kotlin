package day25

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ComboBreakerTest {

    @Test
    fun `should find encryption key`() {
        assertThat(ComboBreaker().findEncryptionKey(5764801L, 17807724L)).isEqualTo(14897079)
    }
}