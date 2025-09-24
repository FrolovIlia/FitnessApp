package com.pixelrabbit.fitnessapp.data.model

data class Workout(
    val id: Int,
    val title: String,
    val description: String?,
    val type: Int,
    val duration: String
)

fun Workout.typeName(): String {
    return when (type) {
        1 -> "Тренировка"
        2 -> "Эфир"
        3 -> "Комплекс"
        else -> "Неизвестно"
    }
}
