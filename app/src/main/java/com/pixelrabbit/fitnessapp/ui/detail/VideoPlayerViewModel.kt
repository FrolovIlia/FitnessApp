package com.pixelrabbit.fitnessapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pixelrabbit.fitnessapp.data.model.VideoWorkout
import com.pixelrabbit.fitnessapp.data.model.Workout
import com.pixelrabbit.fitnessapp.data.repository.WorkoutRepository
import com.pixelrabbit.fitnessapp.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VideoPlayerViewModel : ViewModel() {

    private val repository = WorkoutRepository()

    private val _video = MutableStateFlow<UiState<VideoWorkout>>(UiState.Empty)
    val video: StateFlow<UiState<VideoWorkout>> = _video

    fun loadVideo(workoutId: Int) {
        viewModelScope.launch {
            _video.value = UiState.Loading
            try {
                val videoWorkout = repository.getVideo(workoutId)
                _video.value = UiState.Success(videoWorkout)
            } catch (e: Exception) {
                _video.value = UiState.Error("Ошибка загрузки видео")
            }
        }
    }

    suspend fun getWorkoutById(workoutId: Int): Workout? {
        return try {
            repository.getWorkouts().find { it.id == workoutId }
        } catch (e: Exception) {
            null
        }
    }
}
