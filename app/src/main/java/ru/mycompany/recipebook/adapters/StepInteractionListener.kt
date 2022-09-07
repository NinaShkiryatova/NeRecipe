package ru.mycompany.recipebook.adapters

import android.net.Uri
import ru.mycompany.recipebook.dto.Step

interface StepInteractionListener {
    fun deleteStepInstruction(recipeId: Long, stepId: Int)
    fun deleteStepIllustration(index: Int)
    fun addStepIllustration(step: Step)
}