package com.pixelrabbit.fitnessapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pixelrabbit.fitnessapp.data.model.VideoWorkout
import com.pixelrabbit.fitnessapp.data.repository.WorkoutRepository
import com.pixelrabbit.fitnessapp.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VideoPlayerViewModel(private val repository: WorkoutRepository = WorkoutRepository()) : ViewModel() {

    private val _video = MutableStateFlow<UiState<VideoWorkout>>(UiState.Loading)
    val video: StateFlow<UiState<VideoWorkout>> = _video

    fun loadVideo(id: Int) {
        viewModelScope.launch {
            _video.value = UiState.Loading
            try {
                val response = repository.getVideo(id)
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) _video.value = UiState.Success(data)
                    else _video.value = UiState.Empty
                } else {
                    _video.value = UiState.Error("Ошибка сервера")
                }
            } catch (e: Exception) {
                _video.value = UiState.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }
}
