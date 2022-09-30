package ru.mycompany.nerecipe.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.mycompany.nerecipe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}