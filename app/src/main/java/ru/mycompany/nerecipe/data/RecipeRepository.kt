package ru.mycompany.nerecipe.data

import androidx.lifecycle.LiveData
import ru.mycompany.nerecipe.dto.Recipe
import ru.mycompany.nerecipe.dto.RecipeWithSteps
import ru.mycompany.nerecipe.dto.Step

interface RecipeRepository {
    fun getAllRecipesWithSteps(): LiveData<List<RecipeWithSteps>>
    fun saveRecipe(recipe: Recipe): Long
    fun removeRecipeById(recipeId: Long)
    fun addOrRemoveRecipeFromFavorite(id: Long, favourite: Boolean)
    fun getRecipeIdByTitle(title: String): Long

    fun addSteps(steps: List<Step>)
    fun removeSteps(steps: List<Step>)
}

