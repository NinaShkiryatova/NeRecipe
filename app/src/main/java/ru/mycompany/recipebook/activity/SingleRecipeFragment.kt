package ru.mycompany.recipebook.activity

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.mycompany.recipebook.R
import ru.mycompany.recipebook.adapters.StepAdapter
import ru.mycompany.recipebook.adapters.StepInteractionListener
import ru.mycompany.recipebook.databinding.SingleRecipeFragmentBinding
import ru.mycompany.recipebook.dto.Step
import ru.mycompany.recipebook.util.NumArg
import ru.mycompany.recipebook.util.StringArg
import ru.mycompany.recipebook.viewModel.RecipeViewModel

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
        val adapter = StepAdapter(object : StepInteractionListener {
            private var imageUri: Uri? = null
            val image = registerForActivityResult(ActivityResultContracts.GetContent()){uri->
                if(uri!=null)
                    imageUri = uri
            }

            override fun deleteStepInstruction(recipeId: Long, stepId: Int) {
                viewModel.removeStepById(recipeId, stepId)
            }

            override fun deleteStepIllustration(index: Int) {
                TODO("Not yet implemented")
            }

            override fun addStepIllustration(step: Step) {
                image.launch("image/*")

            }
        })
        val recipeId = arguments?.numArg
        for (requiredRecipe in viewModel.data.value!!) {
            if (requiredRecipe.id == recipeId)
                binding.apply {
                    recipeName.text = requiredRecipe.title
                    categoryName.text = requiredRecipe.category.toString()
                    authorName.text = requiredRecipe.author
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
                                            Bundle().apply { numArg = requiredRecipe.id }
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
            viewModel.currentSteps.observe(viewLifecycleOwner){steps ->
                adapter.submitList(steps)
            }

        }
        return binding.root
    }
}