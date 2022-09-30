package ru.mycompany.nerecipe.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.mycompany.nerecipe.R
import ru.mycompany.nerecipe.activity.SingleRecipeFragment.Companion.numArg
import ru.mycompany.nerecipe.adapters.RecipeAdapter
import ru.mycompany.nerecipe.adapters.RecipeInteractionListener
import ru.mycompany.nerecipe.databinding.RecipeListFragmentBinding
import ru.mycompany.nerecipe.dto.RecipeWithSteps
import ru.mycompany.nerecipe.util.AndroidUtils
import ru.mycompany.nerecipe.util.StringArg
import ru.mycompany.nerecipe.viewModel.RecipeViewModel
import java.util.*

class RecipeListFragment : Fragment() {
    companion object {
        var Bundle.textArg: String? by StringArg
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)
        val binding = RecipeListFragmentBinding.inflate(
            inflater, container, false
        )

        val adapter = RecipeAdapter(object : RecipeInteractionListener {
            override fun showRecipe(recipe: RecipeWithSteps) {
                viewModel.showRecipe(recipe)
                findNavController().navigate(
                    R.id.action_recipeListFragment_to_singleRecipeFragment,
                    Bundle().apply { numArg = recipe.recipe.id }
                )
            }

            override fun editRecipe(recipe: RecipeWithSteps) {
                viewModel.setCurrentStepsForRecipe(recipe.recipe.id)
                findNavController().navigate(
                    R.id.action_recipeListFragment_to_editRecipeFragment,
                    Bundle().apply { numArg = recipe.recipe.id }
                )
            }

            override fun removeRecipe(id: Long) {
                viewModel.removeById(id)
            }

            override fun addOrRemoveFromFavourite(recipe: RecipeWithSteps) {
                viewModel.addOrRemoveFromFavourite(recipe)
            }

        })

        fun hideNoInfoLayout(infoFound: Boolean) {
            binding.recipesList.visibility = if (infoFound) View.VISIBLE else View.GONE
            binding.noInfoLayout.visibility = if (infoFound) View.GONE else View.VISIBLE
        }

        binding.noInfoLayout.visibility = View.GONE
        val filters = arguments?.textArg?.split(", ")
        binding.recipesList.adapter = adapter

        val simpleCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.DOWN,
            0
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                Collections.swap(viewModel.recipeData.value, fromPosition, toPosition)

                adapter.notifyItemMoved(fromPosition, toPosition)

                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.recipesList)

        if (filters.isNullOrEmpty()) {
            viewModel.recipeData.observe(viewLifecycleOwner) { recipes ->
                adapter.submitList(recipes)
            }
        } else {
            viewModel.getFilteredListOfRecipes(filters)
            if (viewModel.filteredRecipes.value.isNullOrEmpty()) {
                hideNoInfoLayout(false)
            } else {
                hideNoInfoLayout(true)
                viewModel.filteredRecipes.observe(viewLifecycleOwner) { recipes ->
                    adapter.submitList(recipes)
                }
            }
        }
        binding.fab.setOnClickListener {
            viewModel.currentSteps.value?.clear()
            findNavController().navigate(R.id.action_recipeListFragment_to_newRecipeFragment)
        }
        binding.filtersButton.setOnClickListener {
            viewModel.filteredRecipes.value?.clear()
            findNavController().navigate(R.id.action_recipeListFragment_to_filtersFragment)
        }
        binding.nameSearchButton.setOnClickListener {
            if (binding.nameSearch.text.isNullOrEmpty()) {
                Toast.makeText(context, "Enter the recipe title to search", Toast.LENGTH_LONG)
                    .show()
            } else {
                viewModel.getFilteredListByTitle(binding.nameSearch.text.toString())
                if (viewModel.filteredRecipes.value.isNullOrEmpty()) {
                    hideNoInfoLayout(false)
                    AndroidUtils.hideKeyboard(requireView())
                } else {
                    hideNoInfoLayout(true)
                    viewModel.filteredRecipes.observe(viewLifecycleOwner) { recipes ->
                        adapter.submitList(recipes)
                        AndroidUtils.hideKeyboard(requireView())
                    }
                }
            }
        }
        binding.allRecipes.setOnClickListener {
            binding.nameSearch.setText("")
            binding.recipesList.visibility = View.VISIBLE
            binding.noInfoLayout.visibility = View.INVISIBLE
            viewModel.recipeData.observe(viewLifecycleOwner) { recipes ->
                adapter.submitList(recipes)
            }
        }

        binding.favouriteRecipes.setOnClickListener {
            viewModel.getListOfFavourites()
            if (viewModel.filteredRecipes.value.isNullOrEmpty()) hideNoInfoLayout(false) else hideNoInfoLayout(
                true
            )
            viewModel.filteredRecipes.observe(viewLifecycleOwner) { recipes ->
                adapter.submitList(recipes)
            }
        }
        return binding.root
    }
}
