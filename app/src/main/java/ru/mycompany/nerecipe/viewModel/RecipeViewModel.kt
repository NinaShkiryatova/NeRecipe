package ru.mycompany.nerecipe.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.mycompany.nerecipe.data.RecipeRepository
import ru.mycompany.nerecipe.data.RecipeRepositoryImpl
import ru.mycompany.nerecipe.db.RecipeDb
import ru.mycompany.nerecipe.dto.Recipe
import ru.mycompany.nerecipe.dto.RecipeWithSteps
import ru.mycompany.nerecipe.dto.Step

private val defaultRecipe = RecipeWithSteps(
    Recipe(
        id = 0,
        title = "",
        author = "",
        category = "",
        favourite = false
    ), emptyList()
)

private val defaultSteps = mutableListOf<Step>()
private val defaultRecipeList = mutableListOf<RecipeWithSteps>()

private val defaultPicture: String? = null


class RecipeViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val recipeRepository: RecipeRepository = RecipeRepositoryImpl(
        RecipeDb.getInstance(context = application).recipeDao()
    )


    val recipeData = recipeRepository.getAllRecipesWithSteps()
    val filteredRecipes = MutableLiveData(defaultRecipeList)
    private var currentRecipe = MutableLiveData(defaultRecipe)
    var currentSteps = MutableLiveData(defaultSteps)
    var currentPicture = MutableLiveData(defaultPicture)

    fun backDefault() {
        currentSteps.value?.clear()
        currentRecipe.value = defaultRecipe
        currentPicture.value = defaultPicture
    }

    fun getFilteredListOfRecipes(list: List<String>) {
        filteredRecipes.value?.clear()
        for (item in list) {
            filteredRecipes.value?.addAll((recipeData.value?.filter { it.recipe.category == item })!!.toMutableList())
        }
    }

    fun getFilteredListByTitle(titleFragment: String) {
        filteredRecipes.value =
            recipeData.value?.filter { it.recipe.title.contains(titleFragment) }?.toMutableList()
    }

    fun getListOfFavourites() {
        filteredRecipes.value = recipeData.value?.filter {
            it.recipe.favourite
        }?.toMutableList()
    }

    fun setCurrentStepsForRecipe(recipeId: Long) {
        currentSteps.value?.clear()
        for (recipe in recipeData.value!!) {
            if (recipe.recipe.id == recipeId) {
                currentSteps.value = recipe.steps?.toMutableList()
            }
        }
    }

    fun saveStep(step: Step) {
        if (step.id == -1) {
            step.id =
                if (!currentSteps.value.isNullOrEmpty()) currentSteps.value!!.last().id + 1 else 1
            currentSteps.value?.add(step)
        } else currentSteps.value = currentSteps.value?.map {
            if (it.id != step.id) it else it.copy(
                instruction = step.instruction,
                illustration = step.illustration
            )
        }?.toMutableList()
    }

    fun removeStepById(stepId: Int) {
        currentSteps.value = currentSteps.value?.filter { it.id != stepId }?.toMutableList()
    }

    fun addRecipe(
        id: Long,
        title: String,
        author: String,
        category: String,
        favourite: Boolean,
        steps: List<Step>
    ) {
        recipeRepository.saveRecipe(Recipe(id, title, author, category, favourite))
        val newId = recipeRepository.getRecipeIdByTitle(title)
        for (step in steps) {
            step.recipeId = newId
        }
        recipeRepository.addSteps(steps)
        backDefault()
    }

    fun addOrRemoveFromFavourite(recipe: RecipeWithSteps) =
        recipeRepository.addOrRemoveRecipeFromFavorite(recipe.recipe.id, recipe.recipe.favourite)

    fun editRecipe(recipe: RecipeWithSteps) {
        currentRecipe.value = recipe
        currentSteps.value = recipe.steps.toMutableList()
    }

    fun removeById(id: Long) {
        for (recipe in recipeData.value!!) {
            if (recipe.recipe.id == id) {
                recipeRepository.removeSteps(recipe.steps)
            }
        }
        recipeRepository.removeRecipeById(id)
    }

    fun showRecipe(recipe: RecipeWithSteps) {
        currentRecipe.value = recipe
        currentSteps.value = recipe.steps.toMutableList()
    }
}