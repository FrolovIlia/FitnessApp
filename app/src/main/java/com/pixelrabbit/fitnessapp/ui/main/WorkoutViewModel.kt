package com.pixelrabbit.fitnessapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pixelrabbit.fitnessapp.data.model.Workout
import com.pixelrabbit.fitnessapp.data.repository.WorkoutRepository
import com.pixelrabbit.fitnessapp.utils.UiState
import kotlinx.coroutines.launch

class WorkoutViewModel(
    private val repository: WorkoutRepository = WorkoutRepository()
) : ViewModel() {

    private val _workouts = MutableLiveData<UiState<List<Workout>>>()
    val workouts: LiveData<UiState<List<Workout>>> get() = _workouts

    private var allWorkouts: List<Workout> = emptyList()

    fun loadWorkouts() {
        _workouts.value = UiState.Loading
        viewModelScope.launch {
            try {
                allWorkouts = repository.getWorkouts()
                if (allWorkouts.isEmpty()) {
                    _workouts.value = UiState.Empty
                } else {
                    _workouts.value = UiState.Success(allWorkouts)
                }
            } catch (e: Exception) {
                _workouts.value = UiState.Error(e.message ?: "Не удалось загрузить данные")
            }
        }
    }

    fun filterByType(type: Int) {
        val filtered = if (type == 0) allWorkouts else allWorkouts.filter { it.type == type }
        _workouts.value = if (filtered.isEmpty()) UiState.Empty else UiState.Success(filtered)
    }

    fun searchByTitle(query: String) {
        val searched = allWorkouts.filter { it.title.contains(query, ignoreCase = true) }
        _workouts.value = if (searched.isEmpty()) UiState.Empty else UiState.Success(searched)
    }
}
