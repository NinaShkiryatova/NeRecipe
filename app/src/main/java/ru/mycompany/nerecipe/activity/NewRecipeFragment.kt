package ru.mycompany.nerecipe.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.mycompany.nerecipe.R
import ru.mycompany.nerecipe.activity.EditRecipeFragment.Companion.numArg
import ru.mycompany.nerecipe.activity.EditStepFragment.Companion.intArg
import ru.mycompany.nerecipe.adapters.StepAdapter
import ru.mycompany.nerecipe.adapters.StepInteractionListener
import ru.mycompany.nerecipe.databinding.NewRecipeFragmentBinding
import ru.mycompany.nerecipe.dto.Step
import ru.mycompany.nerecipe.viewModel.RecipeViewModel

class NewRecipeFragment : Fragment() {

    @SuppressLint("ResourceType", "NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)
        val binding = NewRecipeFragmentBinding.inflate(
            inflater, container, false
        )
        val adapter = StepAdapter(object : StepInteractionListener {

            override fun deleteStep(stepId: Int) {
                viewModel.removeStepById(stepId)
            }

            override fun editStep(step: Step) {
                findNavController().navigate(R.id.action_newRecipeFragment_to_editStepFragment,
                    Bundle().apply {
                        intArg = step.id
                        numArg = step.recipeId
                    })
            }
        })
        binding.stepsList.adapter = adapter
        binding.apply {
            categoriesMenu.setOnClickListener {
                PopupMenu(context, categoriesMenu).apply {
                    inflate(R.menu.recipe_category_options)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.european_cuisine -> {
                                chooseCategoriesText.setText(R.string.european_cuisine_name)
                                true
                            }
                            R.id.asian_cuisine -> {
                                chooseCategoriesText.setText(R.string.asian_cuisine_name)
                                true
                            }
                            R.id.pan_asian_cuisine -> {
                                chooseCategoriesText.setText(R.string.pan_asian_cuisine_name)
                                true
                            }
                            R.id.oriental_cuisine -> {
                                chooseCategoriesText.setText(R.string.oriental_cuisine_name)
                                true
                            }
                            R.id.american_cuisine -> {
                                chooseCategoriesText.setText(R.string.american_cuisine_name)
                                true
                            }
                            R.id.russian_cuisine -> {
                                chooseCategoriesText.setText(R.string.russian_cuisine_name)
                                true
                            }
                            R.id.mediterranean_cuisine -> {
                                chooseCategoriesText.setText(R.string.mediterranean_cuisine_name)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }

            viewModel.currentSteps.observe(viewLifecycleOwner) { steps ->
                adapter.submitList(steps)
            }

            binding.addStepButton.setOnClickListener {
                findNavController().navigate(R.id.action_newRecipeFragment_to_editStepFragment,
                    Bundle().apply {
                        intArg = -1
                        numArg = 0
                    })
            }

            binding.addRecipeButton.setOnClickListener {
                if (!binding.recipeTitle.text.isEmpty() && !binding.authorName.text.isEmpty() && binding.chooseCategoriesText.text.toString() != resources.getString(
                        R.string.choose_the_category
                    ) && viewModel.currentSteps.value!!.isNotEmpty()
                ) {
                    viewModel.addRecipe(
                        0L,
                        binding.recipeTitle.text.toString(),
                        binding.authorName.text.toString(),
                        binding.chooseCategoriesText.text.toString(),
                        false,
                        viewModel.currentSteps.value!!.toList()
                    )
                    viewModel.backDefault()
                    findNavController().navigateUp()
                } else {
                    val text: String = if (binding.recipeTitle.text.isEmpty()) {
                        "Fill the title field"
                    } else if (binding.authorName.text.isEmpty()) {
                        "Fill the author name field"
                    } else if (viewModel.currentSteps.value!!.isEmpty()) {
                        "Add at least one step"
                    } else {
                        "Choose the category"
                    }
                    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
                }
            }
            return binding.root
        }
    }
}