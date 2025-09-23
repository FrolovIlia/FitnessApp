package com.pixelrabbit.fitnessapp.utils

// Обертка для состояния экрана (loading, success, error, empty)
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
    object Empty : UiState<Nothing>()
}
