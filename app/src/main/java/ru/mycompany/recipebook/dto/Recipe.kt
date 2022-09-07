package ru.mycompany.recipebook.dto

import android.net.Uri
import kotlinx.serialization.Serializable

@Serializable
data class Recipe (
    val id : Long,
    val title: String,
    val author: String,
    val category: String,
    var favourite: Boolean = false,
    val steps: List<Step>
        )

@Serializable
data class Step(
    var id: Int,
    val recipeId: Long,
    var instruction: String,
    var illustration: String?
)

