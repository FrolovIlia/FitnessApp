package com.pixelrabbit.fitnessapp.data.model

data class Workout(
    val id: Int,
    val title: String,
    val description: String?,
    val type: Int,       // 1 - тренировка, 2 - эфир, 3 - комплекс
    val duration: String
)
