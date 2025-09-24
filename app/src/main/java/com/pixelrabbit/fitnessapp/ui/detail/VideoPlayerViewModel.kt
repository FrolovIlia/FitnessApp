package com.pixelrabbit.fitnessapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pixelrabbit.fitnessapp.data.model.VideoWorkout
import com.pixelrabbit.fitnessapp.data.repository.WorkoutRepository
import com.pixelrabbit.fitnessapp.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VideoPlayerViewModel : ViewModel() {

    private val repository = WorkoutRepository()

    private val _video = MutableStateFlow<UiState<VideoWorkout>>(UiState.Empty)
    val video: StateFlow<UiState<VideoWorkout>> = _video

    fun loadVideo(id: Int) {
        viewModelScope.launch {
            try {
                _video.value = UiState.Loading
                val videoData = repository.getVideo(id)
                _video.value = UiState.Success(videoData)
            } catch (e: Exception) {
                _video.value = UiState.Error(e.message ?: "Ошибка")
            }
        }
    }
}
