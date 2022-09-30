package ru.mycompany.nerecipe.adapters

import ru.mycompany.nerecipe.dto.Step

interface StepInteractionListener {
    fun deleteStep(stepId: Int)
    fun editStep(step: Step)
}