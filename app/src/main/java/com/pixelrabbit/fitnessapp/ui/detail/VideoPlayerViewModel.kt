package com.pixelrabbit.fitnessapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pixelrabbit.fitnessapp.data.model.VideoWorkout
import com.pixelrabbit.fitnessapp.data.repository.WorkoutRepository
import com.pixelrabbit.fitnessapp.utils.UiState
import kotlinx.coroutines.launch

class VideoPlayerViewModel(
    private val repository: WorkoutRepository = WorkoutRepository()
) : ViewModel() {

    private val _video = MutableLiveData<UiState<VideoWorkout>>()
    val video: LiveData<UiState<VideoWorkout>> get() = _video

    fun loadVideo(id: Int) {
        _video.value = UiState.Loading
        viewModelScope.launch {
            try {
                val result = repository.getVideo(id)
                _video.value = UiState.Success(result)
            } catch (e: Exception) {
                _video.value = UiState.Error(e.message ?: "Ошибка при загрузке видео")
            }
        }
    }
}
