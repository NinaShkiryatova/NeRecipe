package ru.mycompany.nerecipe.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.mycompany.nerecipe.R
import ru.mycompany.nerecipe.activity.EditStepFragment.Companion.intArg
import ru.mycompany.nerecipe.adapters.StepAdapter
import ru.mycompany.nerecipe.adapters.StepInteractionListener
import ru.mycompany.nerecipe.databinding.EditRecipeFragmentBinding
import ru.mycompany.nerecipe.dto.Step
import ru.mycompany.nerecipe.util.NumArg
import ru.mycompany.nerecipe.viewModel.RecipeViewModel
import java.util.*

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

        val adapter = StepAdapter(object : StepInteractionListener {

            override fun deleteStep(stepId: Int) {
                viewModel.removeStepById(stepId)
            }

            override fun editStep(step: Step) {
                findNavController().navigate(
                    R.id.action_editRecipeFragment_to_editStepFragment,
                    Bundle().apply {
                        intArg = step.id
                        numArg = step.recipeId
                    }
                )
            }
        })

        val binding = EditRecipeFragmentBinding.inflate(inflater, container, false)

        val recipeId = arguments?.numArg
        for (editedRecipe in viewModel.recipeData.value!!) {
            if (editedRecipe.recipe.id == recipeId)
                binding.apply {
                    recipeTitle.setText(editedRecipe.recipe.title)
                    author.setText(R.string.author)
                    title.setText(R.string.recipe_title)
                    category.setText(R.string.category)
                    categoryName.text = editedRecipe.recipe.category
                    chooseCategoryMenu.setOnClickListener {
                        PopupMenu(context, chooseCategoryMenu).apply {
                            inflate(R.menu.recipe_category_options)
                            setOnMenuItemClickListener { item ->
                                when (item.itemId) {
                                    R.id.european_cuisine -> {
                                        categoryName.setText(R.string.european_cuisine_name)
                                        true
                                    }
                                    R.id.asian_cuisine -> {
                                        categoryName.setText(R.string.asian_cuisine_name)
                                        true
                                    }
                                    R.id.pan_asian_cuisine -> {
                                        categoryName.setText(R.string.pan_asian_cuisine_name)
                                        true
                                    }
                                    R.id.oriental_cuisine -> {
                                        categoryName.setText(R.string.oriental_cuisine_name)
                                        true
                                    }
                                    R.id.american_cuisine -> {
                                        categoryName.setText(R.string.american_cuisine_name)
                                        true
                                    }
                                    R.id.russian_cuisine -> {
                                        categoryName.setText(R.string.russian_cuisine_name)
                                        true
                                    }
                                    R.id.mediterranean_cuisine -> {
                                        categoryName.setText(R.string.mediterranean_cuisine_name)
                                        true
                                    }
                                    else -> false
                                }
                            }
                        }.show()
                    }
                    authorName.setText(editedRecipe.recipe.author)
                    recipeStepsList.adapter = adapter

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
                            Collections.swap(
                                viewModel.currentSteps.value!!,
                                fromPosition,
                                toPosition
                            )

                            adapter.notifyItemMoved(fromPosition, toPosition)

                            return false
                        }

                        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        }
                    }

                    val itemTouchHelper = ItemTouchHelper(simpleCallback)
                    itemTouchHelper.attachToRecyclerView(recipeStepsList)

                    viewModel.currentSteps.observe(viewLifecycleOwner) { steps ->
                        adapter.submitList(steps)
                    }
                    addRecipeButton.setOnClickListener {
                        if (binding.recipeTitle.text.isNotEmpty() && binding.authorName.text.isNotEmpty() && !viewModel.currentSteps.value.isNullOrEmpty()) {
                            viewModel.removeById(editedRecipe.recipe.id)
                            viewModel.addRecipe(
                                recipeId,
                                recipeTitle.text.toString(),
                                authorName.text.toString(),
                                categoryName.text.toString(),
                                viewModel.recipeData.value!!.first { it.recipe.id == recipeId }.recipe.favourite,
                                viewModel.currentSteps.value!!.toList()
                            )
                            findNavController().navigate(R.id.action_editRecipeFragment_to_recipeListFragment)
                        } else {
                            val text: String = if (binding.recipeTitle.text.isEmpty()) {
                                "Fill the title field"
                            } else if (binding.authorName.text.isEmpty()) {
                                "Fill the author name field"
                            } else {
                                "Add at least one step"
                            }
                            Toast.makeText(context, text, Toast.LENGTH_LONG).show()
                        }
                    }
                    addStepButton.visibility = View.VISIBLE
                    addStepButton.setOnClickListener {
                        findNavController().navigate(
                            R.id.action_editRecipeFragment_to_editStepFragment,
                            Bundle().apply {
                                intArg = 1
                                numArg = editedRecipe.recipe.id
                            }
                        )
                    }

                }
        }
        return binding.root
    }
}