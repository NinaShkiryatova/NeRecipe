package ru.mycompany.nerecipe.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.mycompany.nerecipe.R
import ru.mycompany.nerecipe.activity.SingleRecipeFragment.Companion.textArg
import ru.mycompany.nerecipe.databinding.FiltersFragmentBinding

class FiltersFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FiltersFragmentBinding.inflate(
            inflater, container, false
        )
        binding.apply {
            applyFiltersButton.setOnClickListener {
                    if (!europeanCheckBox.isChecked && !asianCheckBox.isChecked && !panAsianCheckBox.isChecked && !orientalCheckBox.isChecked && !americanCheckBox.isChecked && !russianCheckBox.isChecked && !mediterraneanCheckBox.isChecked) {
                        Toast.makeText(context, "Choose at least one category", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        val categoriesChosen = mutableListOf<String>()

                        if (europeanCheckBox.isChecked) categoriesChosen.add(europeanCheckBox.text.toString())
                        if (asianCheckBox.isChecked) categoriesChosen.add(asianCheckBox.text.toString())
                        if (panAsianCheckBox.isChecked) categoriesChosen.add(panAsianCheckBox.text.toString())
                        if (orientalCheckBox.isChecked) categoriesChosen.add(orientalCheckBox.text.toString())
                        if (americanCheckBox.isChecked) categoriesChosen.add(americanCheckBox.text.toString())
                        if (russianCheckBox.isChecked) categoriesChosen.add(russianCheckBox.text.toString())
                        if (mediterraneanCheckBox.isChecked) categoriesChosen.add(mediterraneanCheckBox.text.toString())

                        findNavController().navigate(
                            R.id.action_filtersFragment_to_recipeListFragment,
                            Bundle().apply { textArg = categoriesChosen.joinToString() }
                        )
                    }
            }
        }


        return binding.root
    }
}