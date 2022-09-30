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

internal class StepShowAdapter(
    private val stepInteractionListener: StepInteractionListener
) : ListAdapter<Step, StepShowAdapter.StepViewHolder>(
    DiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val binding =
            RecipeStepCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StepViewHolder(binding, stepInteractionListener)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        val step = getItem(position)
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

                saveStepButton.visibility = View.GONE
                saveStepButton.isClickable = false

                deleteStepButton.visibility = View.GONE
                deleteStepButton.isClickable = false

                editStepButton.visibility = View.GONE
                editStepButton.isClickable = false
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Step>() {
        override fun areItemsTheSame(oldItem: Step, newItem: Step) =
            oldItem.instruction == newItem.instruction

        override fun areContentsTheSame(oldItem: Step, newItem: Step) = oldItem == newItem
    }
}