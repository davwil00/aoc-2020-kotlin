package day19

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MonsterMessagesTest {

    @Test
    fun `should handle simple rules`() {
        val input = """
            0: 1 2
            1: "a"
            2: 1 3 | 3 1
            3: "b"
            
            aab
            aba
        """.trimIndent()
        assertThat(MonsterMessages().validateMessages(input)).isEqualTo(2)
    }
    @Test
    fun `should handle more complex rules`() {
        val input = """
            0: 4 1 5
            1: 2 3 | 3 2
            2: 4 4 | 5 5
            3: 4 5 | 5 4
            4: "a"
            5: "b"
            
            ababbb
            bababa
            abbbab
            aaabbb
            aaaabbb
        """.trimIndent()
        assertThat(MonsterMessages().validateMessages(input)).isEqualTo(2)
    }
}