package ru.mycompany.recipebook.adapters

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.mycompany.recipebook.R
import ru.mycompany.recipebook.databinding.RecipeStepEditCardBinding
import ru.mycompany.recipebook.dto.Step
import ru.mycompany.recipebook.util.AndroidUtils

internal class StepAdapter(
    private val stepInteractionListener: StepInteractionListener
) : ListAdapter<Step, StepAdapter.StepViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val binding =
            RecipeStepEditCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StepViewHolder(binding, stepInteractionListener)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        var step = getItem(position)
        holder.bind(step)
    }

    inner class StepViewHolder(
        private val binding: RecipeStepEditCardBinding,
        private val listener: StepInteractionListener
    ) : RecyclerView.ViewHolder(binding.root){

        @SuppressLint("NotifyDataSetChanged")
        fun bind(step: Step) {
            binding.apply {
                stepNumber.text = "Step ${(adapterPosition + 1).toString()}"

                stepDescription.text = step.instruction
                stepDescription.visibility = View.VISIBLE

                editedStepDescription.setText(step.instruction)
                editedStepDescription.visibility = View.GONE

                //if (step.illustration!=null) stepIllustration.setImageURI(Uri.parse(step.illustration))
                stepIllustration.visibility = if (step.illustration != null) View.VISIBLE else View.GONE
                //if (step.illustration!=null) stepIllustration.setImageURI(Uri.parse(step.illustration))

                editStepButton.visibility = View.VISIBLE
                editStepButton.isClickable = true
                editStepButton.setOnClickListener {
                    stepDescription.visibility = View.GONE
                    editedStepDescription.visibility = View.VISIBLE
                    saveStepButton.visibility = View.VISIBLE
                    saveStepButton.isClickable = true
                    editStepButton.visibility = View.GONE
                    editStepButton.isClickable = false
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
                    listener.deleteStepInstruction(step.recipeId, step.id)
                    notifyDataSetChanged()
                }

                deleteIllustrationButton.visibility = if(step.illustration == null) View.GONE else View.VISIBLE
                deleteIllustrationButton.isClickable = step.illustration == null
                deleteIllustrationButton.setOnClickListener {
                    /*TODO("СДЕЛАТЬ удаление картинки")*/
                }

                addIllustrationButton.visibility = if(step.illustration != null) View.GONE else View.VISIBLE
                addIllustrationButton.isClickable = step.illustration != null
                addIllustrationButton.setOnClickListener {
                    listener.addStepIllustration(step)
                    notifyDataSetChanged()
                    stepIllustration.setImageURI(Uri.parse(step.illustration))
                    stepIllustration.visibility = View.VISIBLE
                    addIllustrationButton.visibility = if(step.illustration != null) View.GONE else View.VISIBLE
                    deleteIllustrationButton.visibility = if(step.illustration == null) View.GONE else View.VISIBLE


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