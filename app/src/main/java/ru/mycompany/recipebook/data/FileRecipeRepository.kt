package ru.mycompany.recipebook.data

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.mycompany.recipebook.R
import ru.mycompany.recipebook.dto.Categories
import ru.mycompany.recipebook.dto.Recipe
import ru.mycompany.recipebook.dto.Step

class FileRecipeRepository(
    private val context: Context
) : RecipeRepository {

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Recipe::class.java).type
    private val fileName = "recipes.json"
    private var nextId = 1L
    private var recipes = emptyList<Recipe>()
    private val data = MutableLiveData(recipes)

    init {
        val file = context.filesDir.resolve(fileName)
        if (file.exists()) {
            context.openFileInput(fileName).bufferedReader().use {
                recipes = gson.fromJson(it, type)
                data.value = recipes
            }
        } else {
            sync()
        }
    }

    override fun getAll(): LiveData<List<Recipe>> = data

    override fun save(recipe: Recipe) {
        if (recipe.id == 0L) {
            recipes = listOf(
                recipe.copy(
                    id = nextId,
                    title = recipe.title,
                    author = recipe.author,
                    category = recipe.category,
                    favourite = recipe.favourite,
                    steps = recipe.steps
                )
            ) + recipes
            nextId++
            data.value = recipes
            sync()
            return
        }
        recipes = recipes.map {
            if (it.id != recipe.id) it else it.copy(
                title = recipe.title,
                author = recipe.author,
                category = recipe.category,
                favourite = recipe.favourite,
                steps = recipe.steps
            )
        }
        data.value = recipes
        sync()
    }

    override fun addPicture(recipeId: Long, stepId: Int, uri: String?) {
        for(recipe in recipes){
            if(recipe.id == recipeId){
                for(step in recipe.steps){
                    if(step.id == stepId){
                        step.illustration = uri
                    }
                }
            }
        }
    }

    override fun addOrRemoveFromFavorite(id: Long) {
        for(recipe in recipes){
            if(recipe.id==id) recipe.favourite = !recipe.favourite
        }
        sync()
    }

    override fun findBytTitle(titleFragment: String): List<Recipe>? {
        return recipes.filter{!it.title.contains(titleFragment)}
    }

    override fun findByCategory(category: String): List<Recipe>? {
        return recipes.filter { it.category != category }
    }

    override fun removeById(id: Long) {
        recipes = recipes.filter { it.id != id }
        data.value = recipes
        sync()
    }

    override fun removeStepById(recipeId: Long, stepId: Int) {
        for(recipe in recipes){
            if(recipe.id == recipeId){
                recipe.steps.filter { it.id != stepId }
            }
        }
        data.value = recipes
        sync()
    }

    private fun sync() {
        context.openFileOutput(fileName, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(recipes))
        }
    }


}