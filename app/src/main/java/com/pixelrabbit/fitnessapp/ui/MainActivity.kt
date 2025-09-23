package com.pixelrabbit.fitnessapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pixelrabbit.fitnessapp.R
import com.pixelrabbit.fitnessapp.databinding.ActivityMainBinding
import com.pixelrabbit.fitnessapp.ui.main.WorkoutListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, WorkoutListFragment())
                .commit()
        }
    }
}
