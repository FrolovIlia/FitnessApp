package com.pixelrabbit.fitnessapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.pixelrabbit.fitnessapp.R
import com.pixelrabbit.fitnessapp.ui.detail.WorkoutDetailFragment
import com.pixelrabbit.fitnessapp.ui.main.WorkoutListFragment

class MainActivity : AppCompatActivity(), WorkoutListFragment.WorkoutSelectionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Показываем список тренировок при запуске
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, WorkoutListFragment())
            }
        }
    }

    // Метод интерфейса для передачи выбранной тренировки
    override fun onWorkoutSelected(workoutId: Int) {
        val fragment = WorkoutDetailFragment().apply {
            arguments = Bundle().apply {
                putInt("workout_id", workoutId)
            }
        }
        supportFragmentManager.commit {
            replace(R.id.fragment_container, fragment)
            addToBackStack(null)
        }
    }
}
