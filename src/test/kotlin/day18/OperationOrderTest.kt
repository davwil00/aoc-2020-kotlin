package day18

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

internal class OperationOrderTest {

    @Test
    fun `should calculate simple correctly`() {
        assertThat(OperationOrder().calculate("1 + 2 * 3 + 4 * 5 + 6").isEqualTo(71)
    }
}