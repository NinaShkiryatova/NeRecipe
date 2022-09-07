package ru.mycompany.recipebook.data

import android.net.Uri
import androidx.lifecycle.LiveData
import ru.mycompany.recipebook.dto.Recipe

interface RecipeRepository {
    fun getAll(): LiveData<List<Recipe>>
    fun addOrRemoveFromFavorite(id: Long)
    fun findBytTitle(titleFragment: String): List<Recipe>?
    fun findByCategory(category: String): List<Recipe>?
    fun removeById(id: Long)
    fun removeStepById(recipeId: Long, stepId: Int)
    fun save(recipe: Recipe)
    fun addPicture(recipeId: Long, stepId: Int, uri: String?)
}