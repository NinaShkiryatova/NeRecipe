package ru.mycompany.nerecipe.db

import androidx.room.*
import ru.mycompany.nerecipe.dto.Recipe
import ru.mycompany.nerecipe.dto.RecipeWithSteps
import ru.mycompany.nerecipe.dto.Step


@Entity(tableName = "recipes")
data class RecipeEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "author")
    val author: String,
    @ColumnInfo(name = "category")
    val category: String,
    @ColumnInfo(name = "favourite")
    var favourite: Boolean
) {
    fun toDto() = Recipe(id, title, author, category, favourite)

    companion object {
        fun fromDto(dto: Recipe) =
            RecipeEntity(dto.id, dto.title, dto.author, dto.category, dto.favourite)
    }
}

@Entity(tableName = "steps")
data class StepEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "recipeId")
    val recipeId: Long,
    @ColumnInfo(name = "instruction")
    val instruction: String,
    @ColumnInfo(name = "illustration")
    val illustration: String?
) {
    fun toDto() = Step(id, recipeId, instruction, illustration)

    companion object {
        fun fromDto(dto: Step) =
            StepEntity(0, dto.recipeId, dto.instruction, dto.illustration)
    }
}

data class RecipesWithStepsEntity(
    @Embedded
    val recipeEntity: RecipeEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val stepsEntity: List<StepEntity>
) {

    fun toDto(recipeWithStepsEntity: RecipesWithStepsEntity): RecipeWithSteps{
        val recipe = recipeWithStepsEntity.recipeEntity.toDto()
        val steps = mutableListOf<Step>()
        for(stepEntity in recipeWithStepsEntity.stepsEntity){steps.add(stepEntity.toDto())}
        return RecipeWithSteps(recipe, steps)
    }
}







