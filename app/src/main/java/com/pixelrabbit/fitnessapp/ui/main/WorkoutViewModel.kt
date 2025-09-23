package com.pixelrabbit.fitnessapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pixelrabbit.fitnessapp.data.model.Workout
import com.pixelrabbit.fitnessapp.utils.UiState

class WorkoutViewModel : ViewModel() {

    private val _workouts = MutableLiveData<UiState<List<Workout>>>()
    val workouts: LiveData<UiState<List<Workout>>> = _workouts

    private val allWorkouts = listOf(
        Workout(1, "Утренняя пробежка", "Идеальная пробежка для старта дня", 1, "30 минут"),
        Workout(2, "Жиросжигающая тренировка", null, 1, "45 минут"),
        Workout(3, "Прямой эфир с тренером", "Разбор техники", 2, "60 минут"),
        Workout(4, "Силовой комплекс", "Упражнения с весом", 3, "20 минут"),
        Workout(5, "Йога для начинающих", "Базовые асаны", 1, "40 минут")
    )

    fun loadWorkouts() {
        _workouts.value = UiState.Loading
        _workouts.value = UiState.Success(allWorkouts)
    }

    fun searchByTitle(query: String) {
        val filtered = allWorkouts.filter { it.title.contains(query, ignoreCase = true) }
        _workouts.value = if (filtered.isEmpty()) UiState.Empty else UiState.Success(filtered)
    }

    fun filterByType(type: Int) {
        val filtered = if (type == 0) allWorkouts else allWorkouts.filter { it.type == type }
        _workouts.value = if (filtered.isEmpty()) UiState.Empty else UiState.Success(filtered)
    }
}
