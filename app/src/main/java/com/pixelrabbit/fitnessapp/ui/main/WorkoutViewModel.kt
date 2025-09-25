package com.pixelrabbit.fitnessapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pixelrabbit.fitnessapp.data.model.Workout
import com.pixelrabbit.fitnessapp.data.repository.WorkoutRepository
import com.pixelrabbit.fitnessapp.utils.UiState
import kotlinx.coroutines.launch

class WorkoutViewModel : ViewModel() {

    private val repository = WorkoutRepository()

    private val _workouts = MutableLiveData<UiState<List<Workout>>>()
    val workouts: LiveData<UiState<List<Workout>>> get() = _workouts

    private var _allWorkouts: List<Workout> = emptyList()

    private var currentQuery: String = ""
    private var currentFilterType: Int = 0

    fun loadWorkouts() {
        viewModelScope.launch {
            _workouts.value = UiState.Loading
            try {
                val result = repository.getWorkouts()
                _allWorkouts = result
                applyFilters()
            } catch (e: Exception) {
                _workouts.value = UiState.Error("Ошибка загрузки: ${e.message}")
            }
        }
    }

    fun searchByTitle(query: String) {
        currentQuery = query
        applyFilters()
    }

    fun filterByType(type: Int) {
        currentFilterType = type
        applyFilters()
    }

    private fun applyFilters() {
        var filtered = _allWorkouts

        if (currentFilterType != 0) {
            filtered = filtered.filter { it.type == currentFilterType }
        }

        if (currentQuery.isNotBlank()) {
            filtered = filtered.filter {
                it.title.contains(currentQuery, ignoreCase = true)
            }
        }

        _workouts.value =
            if (filtered.isEmpty()) UiState.Empty else UiState.Success(filtered)
    }
}
