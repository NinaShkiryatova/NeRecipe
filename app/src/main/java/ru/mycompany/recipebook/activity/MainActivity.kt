package ru.mycompany.recipebook.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import ru.mycompany.recipebook.R
import ru.mycompany.recipebook.activity.SingleRecipeFragment.Companion.numArg
import ru.mycompany.recipebook.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //handleIntent(intent)
    /*}

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?){
        intent?.let{
            if (it.action != Intent.ACTION_SEND){
                return@let
            }
            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            if(text.isNullOrBlank()){
                Snackbar.make(binding.root, "text", Snackbar.LENGTH_LONG)
                    .setAction(android.R.string.ok){
                        finish()
                    }.show()
                return@let
            }

            val fragment = supportFragmentManager.findFragmentById(R.id.nav_main_fragment) as NavHostFragment
            fragment.navController.navigate(
                R.id.action_recipeListFragment_to_singleRecipeFragment,
                Bundle().apply { numArg =  }
            )
        }*/
    }
}