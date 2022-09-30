package ru.mycompany.nerecipe.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.mycompany.nerecipe.R
import ru.mycompany.nerecipe.adapters.StepInteractionListener
import ru.mycompany.nerecipe.adapters.StepShowAdapter
import ru.mycompany.nerecipe.databinding.SingleRecipeFragmentBinding
import ru.mycompany.nerecipe.dto.Step
import ru.mycompany.nerecipe.util.NumArg
import ru.mycompany.nerecipe.util.StringArg
import ru.mycompany.nerecipe.viewModel.RecipeViewModel

class SingleRecipeFragment : Fragment() {

    companion object {
        var Bundle.numArg: Long by NumArg
        var Bundle.textArg: String? by StringArg
    }

    private val viewModel: RecipeViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = SingleRecipeFragmentBinding.inflate(inflater, container, false)
        val adapter = StepShowAdapter(object : StepInteractionListener {

            override fun deleteStep(stepId: Int) {
            }

            override fun editStep(step: Step) {
            }
        })
        val recipeId = arguments?.numArg
        for (requiredRecipe in viewModel.recipeData.value!!) {
            if (requiredRecipe.recipe.id == recipeId)
                binding.apply {
                    author.setText(R.string.author)
                    category.setText(R.string.category)
                    recipeName.text = requiredRecipe.recipe.title
                    categoryName.text = requiredRecipe.recipe.category.toString()
                    authorName.text = requiredRecipe.recipe.author
                    menu.setOnClickListener {
                        PopupMenu(context, menu).apply {
                            inflate(R.menu.options_recipe)
                            setOnMenuItemClickListener { item ->
                                when (item.itemId) {
                                    R.id.delete -> {
                                        viewModel.removeById(recipeId)
                                        findNavController().navigateUp()
                                        true
                                    }
                                    R.id.edit -> {
                                        viewModel.editRecipe(requiredRecipe)
                                        findNavController().navigate(
                                            R.id.action_singleRecipeFragment_to_editRecipeFragment,
                                            Bundle().apply { numArg = requiredRecipe.recipe.id }
                                        )
                                        true
                                    }
                                    else -> false
                                }
                            }
                        }.show()
                    }
                    stepsList.adapter = adapter
                }
            viewModel.setCurrentStepsForRecipe(recipeId!!)

            viewModel.currentSteps.observe(viewLifecycleOwner) { steps ->
                adapter.submitList(steps)
            }
        }
        return binding.root
    }
}