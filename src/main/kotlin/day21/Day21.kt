package day21

import java.io.File

class AllergenAssessment {

    private val foodRegex = Regex("""((?:[a-z]+ ?)+) \(contains ((?:[a-z]+(?:, )?)+)\)""")// mxmxvkd kfcds sqjhc nhms (contains dairy, fish)

    fun findAllergyFreeIngredients(input: List<String>): Int {
        val foods = parseInput(input)
        val allergenMap = mapIngredientsAndAllergens(foods)
        val ingredientsContainingAllergens = allergenMap.values.flatten().toSet()
        val allSafeIngredients = foods.map { it.ingredients }.flatten().toMutableList()
        allSafeIngredients.removeAll(ingredientsContainingAllergens)
        return allSafeIngredients.size
    }

    fun findUnsafeIngredients(input: List<String>): String {
        val foods = parseInput(input)
        val ingredientsContainingAllergens = mapIngredientsAndAllergens(foods)
        findUniqueAllergens(ingredientsContainingAllergens)
        val safeIngredients = ingredientsContainingAllergens.toSortedMap().values.flatten()
        return LinkedHashSet(safeIngredients).joinToString(",")
    }

    private fun findUniqueAllergens(ingredientsContainingAllergens: MutableMap<String, Set<String>>) {
        while(ingredientsContainingAllergens.values.any { it.size > 1 }) {
            val uniqueIngredients = ingredientsContainingAllergens.values.filter { it.size == 1 }.flatten()
            ingredientsContainingAllergens.entries.forEach { (key, value) ->
                if (value.size > 1) {
                    ingredientsContainingAllergens[key] = value.filterNot { it in uniqueIngredients }.toSet()
                }
            }
        }
    }

    fun parseInput(input: List<String>): List<Food> {
        return input.map { food ->
            val match = foodRegex.matchEntire(food)
            val ingredients = match!!.groupValues[1].split(" ").toSet()
            val allergens = match.groupValues[2].split(", ").toSet()
            Food(ingredients, allergens)
        }
    }

    private fun mapIngredientsAndAllergens(foods: List<Food>): MutableMap<String, Set<String>> {
        val allergenMap = mutableMapOf<String, Set<String>>()
        foods.forEach { food ->
            food.allergens.forEach { allergen ->
                allergenMap.compute(allergen) { _, value ->
                    val ingredients = (value ?: food.ingredients.toSet())
                    ingredients.intersect(food.ingredients)
                }
            }
        }

        return allergenMap
    }

    data class Food(val ingredients: Set<String>, val allergens: Set<String>)
}

fun main() {
    val input = File("src/main/resources/day21/input.txt").readLines()
    println(AllergenAssessment().findAllergyFreeIngredients(input))
    println(AllergenAssessment().findUnsafeIngredients(input)) // ltbj,chpdjkf,nrfmm,jqnhd,pvhcsn,jxbnb,jtqt,zzkq
}