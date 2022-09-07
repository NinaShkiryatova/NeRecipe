package ru.mycompany.recipebook.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.mycompany.recipebook.R
import ru.mycompany.recipebook.activity.SingleRecipeFragment.Companion.numArg
import ru.mycompany.recipebook.adapters.RecipeAdapter
import ru.mycompany.recipebook.adapters.RecipeInteractionListener
import ru.mycompany.recipebook.databinding.RecipeListFragmentBinding
import ru.mycompany.recipebook.dto.Recipe
import ru.mycompany.recipebook.viewModel.RecipeViewModel

class RecipeListFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)
        val binding = RecipeListFragmentBinding.inflate(
            inflater, container, false)
        val adapter = RecipeAdapter(object  : RecipeInteractionListener{
            override fun showRecipe(recipe: Recipe) {
                viewModel.showRecipe(recipe)
                findNavController().navigate(
                    R.id.action_recipeListFragment_to_singleRecipeFragment,
                    Bundle().apply{numArg = recipe.id}
                )
            }

            override fun editRecipe(recipe: Recipe) {
                viewModel.currentRecipe.value = recipe
                viewModel.currentSteps.value = recipe.steps.toMutableList()
                findNavController().navigate(
                    R.id.action_recipeListFragment_to_editRecipeFragment,
                    Bundle().apply{numArg = recipe.id}
                )
            }

            override fun removeRecipe(id: Long) {
                viewModel.removeById(id)
            }

            override fun editStep(id: Long, stepIndex: Int) {
                //TODO("Not yet implemented")
            }

        })
        binding.recipesList.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner){recipes ->
            adapter.submitList(recipes)
        }
        binding.fab.setOnClickListener {
            viewModel.currentSteps.value?.clear()
            findNavController().navigate(R.id.action_recipeListFragment_to_newRecipeFragment)
        }


        return binding.root
    }
}