package ru.mycompany.recipebook.viewModel

import android.app.Application
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.mycompany.recipebook.data.FileRecipeRepository
import ru.mycompany.recipebook.data.RecipeRepository
import ru.mycompany.recipebook.dto.Recipe
import ru.mycompany.recipebook.dto.Step

private val defaultRecipe = Recipe(
    id = 0,
    title = "",
    author = "",
    category = "",
    favourite = false,
    steps = listOf(Step(0, 0, "", null))
)

private val defaultSteps = mutableListOf<Step>()

private val defaultPicture: Uri? = null


class RecipeViewModel(
    application: Application
) : AndroidViewModel (application){

    private val repository: RecipeRepository = FileRecipeRepository(application)
    val data = repository.getAll()
    var currentRecipe = MutableLiveData(defaultRecipe)
    var currentSteps = MutableLiveData(defaultSteps)
    var currentPicture = MutableLiveData(defaultPicture)

    private fun backDefault(){
        currentSteps.value = defaultSteps
        currentRecipe.value = defaultRecipe
    }

    fun saveStep(step: Step){
        if(step.id == 0 || step.id == currentSteps.value?.last()!!.id+1)
            currentSteps.value?.add(Step(step.id, step.recipeId, step.instruction, step.illustration))
        else currentSteps.value = currentSteps.value?.map {
            if(it.id != step.id) it else it.copy(
                instruction = step.instruction
            )
        }?.toMutableList()
    }

    fun removeStepById(recipeId: Long, stepId: Int) {
        currentSteps.value = currentSteps.value?.filter{it.id != stepId}?.toMutableList()
        repository.removeStepById(recipeId, stepId)
    }

    fun addRecipe(id: Long, title: String, author: String, category: String, favourite: Boolean, steps: List<Step>){
        currentRecipe.value = Recipe(id, title, author, category, favourite, steps)
        currentRecipe.value?.let{
            repository.save(it)
        }
        backDefault()
    }

    fun addPicture(step: Step){
        for(editedStep in currentSteps.value!!){
            if(editedStep.id == step.id){
                editedStep.illustration = currentPicture.value.toString()
            }
        }
        currentPicture.value = defaultPicture
    }

    fun editRecipe(recipe: Recipe){
        currentRecipe.value = recipe
        currentSteps.value = recipe.steps.toMutableList()
    }

    fun removeById(id: Long) = repository.removeById(id)

    fun showRecipe(recipe: Recipe){
        currentRecipe.value = recipe
        currentSteps.value = recipe.steps.toMutableList()
    }
}