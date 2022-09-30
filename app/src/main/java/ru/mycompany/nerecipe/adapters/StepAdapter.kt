package ru.mycompany.nerecipe.adapters

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.mycompany.nerecipe.databinding.RecipeStepCardBinding
import ru.mycompany.nerecipe.dto.Step

internal class StepAdapter(
    private val stepInteractionListener: StepInteractionListener
) : ListAdapter<Step, StepAdapter.StepViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val binding =
            RecipeStepCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StepViewHolder(binding, stepInteractionListener)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        var step = getItem(position)
        holder.bind(step)
    }

    inner class StepViewHolder(
        private val binding: RecipeStepCardBinding,
        private val listener: StepInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("NotifyDataSetChanged")
        fun bind(step: Step) {
            binding.apply {
                stepNumber.text = (adapterPosition + 1).toString()

                stepDescription.text = step.instruction
                stepDescription.visibility = View.VISIBLE

                editedStepDescription.setText(step.instruction)
                editedStepDescription.visibility = View.GONE

                stepIllustration.visibility =
                    if (step.illustration != null) View.VISIBLE else View.GONE
                if (step.illustration != null) stepIllustration.setImageURI(Uri.parse(step.illustration))

                editStepButton.visibility = View.VISIBLE
                editStepButton.isClickable = true
                editStepButton.setOnClickListener {
                    listener.editStep(step)
                }

                saveStepButton.visibility = View.GONE
                saveStepButton.isClickable = false
                saveStepButton.setOnClickListener {
                    stepDescription.text = editedStepDescription.text
                    step.instruction = editedStepDescription.text.toString()
                    editedStepDescription.visibility = View.GONE
                    stepDescription.visibility = View.VISIBLE
                    saveStepButton.visibility = View.GONE
                    saveStepButton.isClickable = false
                    editStepButton.visibility = View.VISIBLE
                    editStepButton.isClickable = true
                }

                deleteStepButton.visibility = View.VISIBLE
                deleteStepButton.isClickable = true
                deleteStepButton.setOnClickListener {
                    listener.deleteStep(step.id)
                    notifyDataSetChanged()
                }
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Step>() {
        override fun areItemsTheSame(oldItem: Step, newItem: Step) =
            oldItem.instruction == newItem.instruction

        override fun areContentsTheSame(oldItem: Step, newItem: Step) = oldItem == newItem
    }
}