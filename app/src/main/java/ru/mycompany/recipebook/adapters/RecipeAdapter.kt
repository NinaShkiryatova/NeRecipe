package ru.mycompany.recipebook.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.mycompany.recipebook.R
import ru.mycompany.recipebook.databinding.RecipeCardBinding
import ru.mycompany.recipebook.dto.Recipe

internal class RecipeAdapter (
    private val interactionListener: RecipeInteractionListener
    ) : ListAdapter<Recipe, RecipeAdapter.RecipeViewHolder>(DiffCallback) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
            val binding = RecipeCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return RecipeViewHolder(binding, interactionListener)
        }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int){
        var recipe = getItem(position)
        holder.bind(recipe)
    }

    inner class RecipeViewHolder(
        private val binding: RecipeCardBinding,
        private val listener: RecipeInteractionListener
    ) : RecyclerView.ViewHolder(binding.root){

        fun bind(recipe: Recipe){
            binding.apply{
                basicRecipeLayout.isClickable = true
                basicRecipeLayout.setOnClickListener {
                    listener.showRecipe(recipe)
                }
                recipeName.text = recipe.title
                authorName.text = recipe.author
                categoryName.text = recipe.category.toString()
                menu.setOnClickListener{
                    PopupMenu(itemView.context, binding.menu).apply {
                        inflate(R.menu.options_recipe)
                        setOnMenuItemClickListener { item->
                            when(item.itemId){
                                R.id.delete -> {
                                    listener.removeRecipe(recipe.id)
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
            }
        }
    }



    private object DiffCallback : DiffUtil.ItemCallback<Recipe>(){
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe) = oldItem == newItem
    }

}