package day01

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ReportRepairTest {
    private val testInput = """1721
979
366
299
675
1456""".split("\n")

    @Test
    fun findPairsShouldFindTwoNumbersThatSumToAnAnswer() {
        val answer = ReportRepair().findPairs(testInput, 2020)
        assertThat(answer).isEqualTo(514579)
    }

    @Test
    fun findTriplesShouldFindThreeNumbersThatSumToAnAnswer() {
        val answer = ReportRepair().findTriple(testInput, 2020)
        assertThat(answer).isEqualTo(241861950)
    }
}