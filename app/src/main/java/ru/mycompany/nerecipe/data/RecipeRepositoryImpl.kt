package ru.mycompany.nerecipe.data

import androidx.lifecycle.Transformations
import ru.mycompany.nerecipe.db.RecipeDao
import ru.mycompany.nerecipe.dto.Recipe
import ru.mycompany.nerecipe.dto.Step

class RecipeRepositoryImpl(
    private val recipeDao: RecipeDao
) : RecipeRepository {

    override fun getAllRecipesWithSteps() = Transformations.map(recipeDao.getAllRecipes()) { list ->
        list.map {
            it.toDto(it)
        }
    }

    override fun saveRecipe(recipe: Recipe): Long {
        return recipeDao.saveRecipe(recipe)
    }

    override fun removeRecipeById(recipeId: Long) {
        recipeDao.removeRecipeById(recipeId)
    }

    override fun getRecipeIdByTitle(title: String): Long {
        return recipeDao.getRecipeIdByTitle(title)
    }

    override fun addOrRemoveRecipeFromFavorite(id: Long, favourite: Boolean) {
        recipeDao.addOrRemoveFromFavorite(id, favourite)
    }

    override fun addSteps(steps: List<Step>) = recipeDao.saveSteps(steps)

    override fun removeSteps(steps: List<Step>) {
        for (step in steps) {
            recipeDao.removeStep(step.id)
        }
    }
}

