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
import ru.mycompany.recipebook.databinding.NewRecipeFragmentBinding
import ru.mycompany.recipebook.dto.Step
import ru.mycompany.recipebook.util.AndroidUtils
import ru.mycompany.recipebook.viewModel.RecipeViewModel

class NewRecipeFragment : Fragment() {



    @SuppressLint("ResourceType", "NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)
        val binding = NewRecipeFragmentBinding.inflate(
            inflater, container, false)
        val adapter = StepAdapter(object : StepInteractionListener{
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
        binding.stepsList.adapter = adapter
        binding.apply {
            categoriesMenu.setOnClickListener {
                PopupMenu(context, categoriesMenu).apply{
                    inflate(R.menu.recipe_category_options)
                    setOnMenuItemClickListener { item->
                        when(item.itemId){
                            R.id.european_cuisine ->{
                                chooseCategoriesText.setText(R.string.european_cuisine_name)
                                true
                            }
                            R.id.asian_cuisine ->{
                                chooseCategoriesText.setText(R.string.asian_cuisine_name)
                                true
                            }
                            R.id.pan_asian_cuisine ->{
                                chooseCategoriesText.setText(R.string.pan_asian_cuisine_name)
                                true
                            }
                            R.id.oriental_cuisine ->{
                                chooseCategoriesText.setText(R.string.oriental_cuisine_name)
                                true
                            }
                            R.id.american_cuisine ->{
                                chooseCategoriesText.setText(R.string.american_cuisine_name)
                                true
                            }
                            R.id.russian_cuisine ->{
                                chooseCategoriesText.setText(R.string.russian_cuisine_name)
                                true
                            }
                            R.id.mediterranean_cuisine ->{
                                chooseCategoriesText.setText(R.string.mediterranean_cuisine_name)
                                true
                            }
                            else -> false
                        }
                    }
            }.show()
        }

            viewModel.currentSteps.observe(viewLifecycleOwner){steps ->
                adapter.submitList(steps)
            }

            binding.addStepButton.setOnClickListener{
                viewModel.saveStep(Step(
                    if(viewModel.currentSteps.value.isNullOrEmpty()) 0 else viewModel.currentSteps.value!!.last().id+1,
                    viewModel.data.value!!.size.toLong(),
                    binding.stepText.text.toString(),
                    null))
                AndroidUtils.hideKeyboard(requireView())
                binding.stepText.setText("")
                binding.stepsList.adapter?.notifyDataSetChanged()
            }

            binding.addRecipeButton.setOnClickListener{
                viewModel.addRecipe(
                    0L,
                    binding.recipeTitle.text.toString(),
                    binding.authorName.text.toString(),
                    binding.chooseCategoriesText.text.toString(),
                    false,
                    viewModel.currentSteps.value!!.toList()
                )
                binding.stepsList.adapter?.notifyDataSetChanged()
                findNavController().navigateUp()
            }
            return binding.root
        }
    }
}