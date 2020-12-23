package day21

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class AllergenAssessmentTest {

    private val input = """
        mxmxvkd kfcds sqjhc nhms (contains dairy, fish)
        trh fvjkl sbzzf mxmxvkd (contains dairy)
        sqjhc fvjkl (contains soy)
        sqjhc mxmxvkd sbzzf (contains fish)
    """.trimIndent().lines()

    @Test
    fun `should identify ingredients without allergens`() {
        assertThat(AllergenAssessment().findAllergyFreeIngredients(input)).isEqualTo(5)
    }

    @Test
    fun `should get list of ingredients containing allergens`() {
        assertThat(AllergenAssessment().findUnsafeIngredients(input)).isEqualTo("mxmxvkd,sqjhc,fvjkl")
    }
}