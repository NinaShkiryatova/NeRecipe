package ru.mycompany.recipebook.activity

import android.annotation.SuppressLint
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
import ru.mycompany.recipebook.databinding.EditRecipeFragmentBinding
import ru.mycompany.recipebook.dto.Step
import ru.mycompany.recipebook.util.AndroidUtils
import ru.mycompany.recipebook.util.NumArg
import ru.mycompany.recipebook.viewModel.RecipeViewModel

class EditRecipeFragment : Fragment() {

    companion object {
        var Bundle.numArg: Long by NumArg
    }

    private val viewModel: RecipeViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val image = registerForActivityResult(ActivityResultContracts.GetContent()){uri: Uri? ->
            if(uri!=null)
                viewModel.currentPicture.value = uri
        }
        val adapter = StepAdapter(object : StepInteractionListener{


            override fun deleteStepInstruction(recipeId: Long, stepId: Int) {
                viewModel.removeStepById(recipeId, stepId)
            }

            override fun deleteStepIllustration(index: Int) {
                TODO("Not yet implemented")
            }

            override fun addStepIllustration(step: Step) {
                image.launch("image/*")
                viewModel.addPicture(step)
            }
        })
        val binding = EditRecipeFragmentBinding.inflate(inflater, container, false)
        val recipeId = arguments?.numArg
        for (editedRecipe in viewModel.data.value!!) {
            if (editedRecipe.id == recipeId)
                binding.apply {
                    recipeTitle.setText(editedRecipe.title)
                    categoryName.text = editedRecipe.category
                    chooseCategoryMenu.setOnClickListener {
                        PopupMenu(context, chooseCategoryMenu).apply{
                            inflate(R.menu.recipe_category_options)
                            setOnMenuItemClickListener { item->
                                when(item.itemId){
                                    R.id.european_cuisine ->{
                                        categoryName.setText(R.string.european_cuisine_name)
                                        true
                                    }
                                    R.id.asian_cuisine ->{
                                        categoryName.setText(R.string.asian_cuisine_name)
                                        true
                                    }
                                    R.id.pan_asian_cuisine ->{
                                        categoryName.setText(R.string.pan_asian_cuisine_name)
                                        true
                                    }
                                    R.id.oriental_cuisine ->{
                                        categoryName.setText(R.string.oriental_cuisine_name)
                                        true
                                    }
                                    R.id.american_cuisine ->{
                                        categoryName.setText(R.string.american_cuisine_name)
                                        true
                                    }
                                    R.id.russian_cuisine ->{
                                        categoryName.setText(R.string.russian_cuisine_name)
                                        true
                                    }
                                    R.id.mediterranean_cuisine ->{
                                        categoryName.setText(R.string.mediterranean_cuisine_name)
                                        true
                                    }
                                    else -> false
                                }
                            }
                        }.show()
                    }
                    authorName.setText(editedRecipe.author)
                    recipeStepsList.adapter = adapter
                    addRecipeButton.setOnClickListener {
                        viewModel.addRecipe(
                            recipeId,
                            recipeTitle.text.toString(),
                            authorName.text.toString(),
                            categoryName.text.toString(),
                            viewModel.data.value!!.first{it.id == recipeId}.favourite,
                            viewModel.currentSteps.value!!
                        )
                        findNavController().navigateUp()
                    }
                    editStepText.visibility = View.VISIBLE
                    addStepButton.visibility = View.VISIBLE
                    addStepButton.setOnClickListener{
                        viewModel.saveStep(Step(
                            if(viewModel.currentSteps.value.isNullOrEmpty()) 0 else viewModel.currentSteps.value!!.last().id+1,
                            editedRecipe.id,
                            binding.editStepText.text.toString(),
                            null))
                        AndroidUtils.hideKeyboard(requireView())
                        binding.editStepText.setText("")
                        binding.recipeStepsList.adapter?.notifyDataSetChanged()
                    }

                }
        }
        viewModel.currentSteps.observe(viewLifecycleOwner){steps ->
            adapter.submitList(steps)
        }
        return binding.root
    }
}