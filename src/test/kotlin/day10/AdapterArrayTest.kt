package day10

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class AdapterArrayTest {

    private val smallInput = """
16
10
15
5
1
11
7
19
6
12
4
""".trim().lines()

    val largeInput = """
28
33
18
42
31
14
46
20
48
47
24
23
49
45
19
38
39
11
1
32
25
35
8
17
7
9
4
2
34
10
3
""".trim().lines()

    @Test
    fun `should find joltages`() {
        assertThat(AdapterArray().findJolts(smallInput)).isEqualTo(35)
    }

    @Test
    fun `should find joltages in larger input`() {
        assertThat(AdapterArray().findJolts(largeInput)).isEqualTo(220)
    }

    @Test
    fun run() {
        AdapterArray().findAdapterNodes(smallInput)
    }

    @Test
    fun `should find combinations in small input`() {
        assertThat(AdapterArray().findAdapterNodes(smallInput)).isEqualTo(8)
    }

    @Test
    fun `should find combinations in large input`() {
        assertThat(AdapterArray().findAdapterNodes(largeInput)).isEqualTo(19208)
    }
}