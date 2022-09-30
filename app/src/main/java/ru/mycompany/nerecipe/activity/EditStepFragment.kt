package ru.mycompany.nerecipe.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.mycompany.nerecipe.R
import ru.mycompany.nerecipe.databinding.EditStepFragmentBinding
import ru.mycompany.nerecipe.dto.Step
import ru.mycompany.nerecipe.util.IntArg
import ru.mycompany.nerecipe.util.NumArg
import ru.mycompany.nerecipe.viewModel.RecipeViewModel

class EditStepFragment : Fragment() {

    companion object {
        var Bundle.intArg: Int by IntArg
        var Bundle.numArg: Long by NumArg
    }

    private val viewModel: RecipeViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = EditStepFragmentBinding.inflate(inflater, container, false)
        val stepId = arguments?.intArg
        val recipeId = arguments?.numArg
        if (stepId != -1) {
            for (step in viewModel.currentSteps.value!!) {
                if (step.id == stepId) {
                    binding.apply {
                        stepNumber.text = getString(R.string.edit_step_title)
                        editedStepDescription.setText(step.instruction)
                        if (step.illustration != null) {
                            deleteIllustrationButton.visibility = View.VISIBLE
                            stepIllustration.setImageURI(Uri.parse(step.illustration))
                            viewModel.currentPicture.value = step.illustration
                            addIllustrationButton.visibility = View.GONE
                        } else {
                            addIllustrationButton.visibility = View.VISIBLE
                            deleteIllustrationButton.visibility = View.GONE
                        }
                    }
                }
            }
        } else {
            binding.deleteIllustrationButton.visibility = View.GONE
            binding.stepIllustration.visibility = View.GONE
        }

        binding.apply {


            val image = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
                if (it != null) {
                    requireActivity().contentResolver.takePersistableUriPermission(
                        it,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    viewModel.currentPicture.value = it.toString()
                    stepIllustration.visibility = View.VISIBLE
                    stepIllustration.setImageURI(it)
                    deleteIllustrationButton.visibility = View.VISIBLE
                    addIllustrationButton.visibility = View.GONE
                }
            }

            addIllustrationButton.setOnClickListener {
                image.launch(arrayOf("image/*"))
            }
            deleteIllustrationButton.setOnClickListener {
                stepIllustration.visibility = View.GONE
                deleteIllustrationButton.visibility = View.GONE
                addIllustrationButton.visibility = View.VISIBLE
            }
            saveStepButton.setOnClickListener {
                viewModel.saveStep(
                    Step(
                        stepId!!,
                        recipeId!!,
                        editedStepDescription.text.toString(),
                        if (stepIllustration.visibility == View.VISIBLE) viewModel.currentPicture.value else null
                    )
                )
                viewModel.currentPicture.value = null
                findNavController().navigateUp()
            }
        }
        return binding.root
    }

}