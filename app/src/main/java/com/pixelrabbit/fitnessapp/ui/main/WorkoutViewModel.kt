package com.pixelrabbit.fitnessapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pixelrabbit.fitnessapp.data.model.Workout
import com.pixelrabbit.fitnessapp.utils.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WorkoutViewModel : ViewModel() {

    private val _workouts = MutableLiveData<UiState<List<Workout>>>()
    val workouts: LiveData<UiState<List<Workout>>> get() = _workouts

    private var allWorkouts: List<Workout> = emptyList()

    fun loadWorkouts() {
        _workouts.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Имитация запроса к API из твоего YAML
                delay(500) // имитация сети
                val response = listOf(
                    Workout(1, "Утренняя пробежка", "Идеальная пробежка для старта дня", 1, "30 минут"),
                    Workout(2, "Жиросжигающая тренировка", null, 1, "45 минут"),
                    Workout(3, "Прямой эфир с тренером", "Живой эфир с разбором техники", 2, "60 минут"),
                    Workout(4, "Силовой комплекс", "Упражнения с собственным весом", 3, "20 минут"),
                    Workout(5, "Йога для начинающих", "Поза лотоса и базовые асаны", 1, "40 минут")
                )
                allWorkouts = response
                _workouts.postValue(if (response.isEmpty()) UiState.Empty else UiState.Success(response))
            } catch (e: Exception) {
                _workouts.postValue(UiState.Error(e.message ?: "Ошибка загрузки"))
            }
        }
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
