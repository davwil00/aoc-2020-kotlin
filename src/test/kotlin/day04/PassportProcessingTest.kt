package day04

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class PassportProcessingTest {
    val input = """
ecl:gry pid:860033327 eyr:2020 hcl:#fffffd
byr:1937 iyr:2017 cid:147 hgt:183cm

iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884
hcl:#cfa07d byr:1929

hcl:#ae17e1 iyr:2013
eyr:2024
ecl:brn pid:760753108 byr:1931
hgt:179cm

hcl:#cfa07d eyr:2025 pid:166559648
iyr:2011 ecl:brn hgt:59in
""".trim()

    @Test
    fun `should read passports correctly`() {
        assertThat(PassportProcessing.parseInput(input)).isEqualTo(2)
    }

    @Test
    fun `should identify invalid passwords`() {
        val input = """
eyr:1972 cid:100
hcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926

iyr:2019
hcl:#602927 eyr:1967 hgt:170cm
ecl:grn pid:012533040 byr:1946

hcl:dab227 iyr:2012
ecl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277

hgt:59cm ecl:zzz
eyr:2038 hcl:74454a iyr:2023
pid:3556412378 byr:2007
""".trim()
        assertThat(PassportProcessing.parseInput(input)).isEqualTo(0)
    }

    @Test
    fun `should identify valid passwords`() {
        val input = """
pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980
hcl:#623a2f

eyr:2029 ecl:blu cid:129 byr:1989
iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm

hcl:#888785
hgt:164cm byr:2001 iyr:2015 cid:88
pid:545766238 ecl:hzl
eyr:2022

iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719
""".trim()
        assertThat(PassportProcessing.parseInput(input)).isEqualTo(4)
    }

    @MethodSource("getFields")
    @ParameterizedTest
    fun isValidField(key: String, value: String, expected: Boolean) {
        assertThat(PassportProcessing.isValidField(key, value)).isEqualTo(expected)
    }

    companion object {

        @JvmStatic
        fun getFields() = Stream.of(
                Arguments.of("byr", "2002", true),
                Arguments.of("byr", "2003", false),

                Arguments.of("hgt", "60in", true),
                Arguments.of("hgt", "190cm", true),
                Arguments.of("hgt", "190in", false),
                Arguments.of("hgt", "190", false),

                Arguments.of("hcl", "#123abc", true),
                Arguments.of("hcl", "#123abz", false),
                Arguments.of("hcl", "123abc", false),

                Arguments.of("ecl", "brn", true),
                Arguments.of("ecl", "wat", false),

                Arguments.of("pid", "000000001", true),
                Arguments.of("pid", "0123456789", false),
        )

    }
}