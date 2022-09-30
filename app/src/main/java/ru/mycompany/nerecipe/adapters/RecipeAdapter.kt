package ru.mycompany.nerecipe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.mycompany.nerecipe.R
import ru.mycompany.nerecipe.databinding.RecipeCardBinding
import ru.mycompany.nerecipe.dto.RecipeWithSteps

internal class RecipeAdapter(
    private val interactionListener: RecipeInteractionListener
) : ListAdapter<RecipeWithSteps, RecipeAdapter.RecipeViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = RecipeCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe)
    }

    inner class RecipeViewHolder(
        private val binding: RecipeCardBinding,
        private val listener: RecipeInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: RecipeWithSteps) {
            binding.apply {
                basicRecipeLayout.isClickable = true
                basicRecipeLayout.setOnClickListener {
                    listener.showRecipe(recipe)
                }
                author.setText(R.string.author)
                category.setText(R.string.category)
                recipeName.text = recipe.recipe.title
                authorName.text = recipe.recipe.author
                categoryName.text = recipe.recipe.category.toString()
                menu.setOnClickListener {
                    PopupMenu(itemView.context, binding.menu).apply {
                        inflate(R.menu.options_recipe)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.delete -> {
                                    listener.removeRecipe(recipe.recipe.id)
                                    true
                                }
                                R.id.edit -> {
                                    listener.editRecipe(recipe)
                                    true
                                }
                                else -> false
                            }
                        }
                    }.show()
                }

                favouriteButton.isChecked = recipe.recipe.favourite
                favouriteButton.isClickable = true
                favouriteButton.setOnCheckedChangeListener { _, isChecked: Boolean ->
                    recipe.recipe.favourite = isChecked
                    listener.addOrRemoveFromFavourite(recipe)

                }
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<RecipeWithSteps>() {
        override fun areItemsTheSame(oldItem: RecipeWithSteps, newItem: RecipeWithSteps) =
            oldItem.recipe.id == newItem.recipe.id

        override fun areContentsTheSame(oldItem: RecipeWithSteps, newItem: RecipeWithSteps) =
            oldItem == newItem
    }
}