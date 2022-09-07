package ru.mycompany.recipebook.adapters

import ru.mycompany.recipebook.dto.Recipe

interface RecipeInteractionListener {
    fun showRecipe(recipe: Recipe)
    fun editRecipe(recipe: Recipe)
    fun removeRecipe(id: Long)
    fun editStep(id: Long, stepIndex: Int)
}