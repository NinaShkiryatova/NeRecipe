package ru.mycompany.nerecipe.dto

import kotlinx.serialization.Serializable


@Serializable
data class Step(
    var id: Int,
    var recipeId: Long,
    var instruction: String,
    var illustration: String?
)

@Serializable
data class Recipe(
    val id : Long,
    val title: String,
    val author: String,
    val category: String,
    var favourite: Boolean = false
)

@Serializable
data class RecipeWithSteps(
    val recipe: Recipe,
    var steps: List<Step>
)


