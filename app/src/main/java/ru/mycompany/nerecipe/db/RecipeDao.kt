package ru.mycompany.nerecipe.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import ru.mycompany.nerecipe.dto.Recipe
import ru.mycompany.nerecipe.dto.Step

@Dao
interface RecipeDao {
    @Transaction
    @Query("SELECT * FROM recipes ORDER BY id DESC")
    fun getAllRecipes(): LiveData<List<RecipesWithStepsEntity>>

    @Insert
    fun insertRecipe(recipe: RecipeEntity)
    fun saveRecipe(recipe: Recipe): Long {
        insertRecipe(RecipeEntity.fromDto(recipe))
        return getRecipeIdByTitle(recipe.title)
    }

    @Query("UPDATE recipes SET favourite = :favourite WHERE id = :id")
    fun addOrRemoveFromFavorite(id: Long, favourite: Boolean)

    @Query("SELECT id FROM recipes WHERE title = :title")
    fun getRecipeIdByTitle(title: String): Long

    @Query("DELETE FROM recipes WHERE id = :id")
    fun removeRecipeById(id: Long)

    @Insert
    fun insertStep(step: StepEntity)

    fun saveSteps(steps: List<Step>) {
        for (step in steps) {
            insertStep(StepEntity.fromDto(step))
        }
    }

    @Query("DELETE FROM steps WHERE id = :id")
    fun removeStep(id: Int)
}