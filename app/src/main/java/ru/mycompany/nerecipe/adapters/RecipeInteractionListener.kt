package ru.mycompany.nerecipe.adapters

import ru.mycompany.nerecipe.dto.RecipeWithSteps

interface RecipeInteractionListener {
    fun showRecipe(recipe: RecipeWithSteps)
    fun editRecipe(recipe: RecipeWithSteps)
    fun removeRecipe(id: Long)
    fun addOrRemoveFromFavourite(recipe: RecipeWithSteps)
}