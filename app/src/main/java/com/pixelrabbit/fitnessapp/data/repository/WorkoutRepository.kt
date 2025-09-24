package com.pixelrabbit.fitnessapp.data.repository

import com.pixelrabbit.fitnessapp.data.model.VideoWorkout
import com.pixelrabbit.fitnessapp.data.model.Workout

class WorkoutRepository {

    private val workouts = listOf(
        Workout(1, "Утренняя пробежка", "Идеальная пробежка для старта дня", 1, "30 минут"),
        Workout(2, "Жиросжигающая тренировка", null, 1, "45 минут"),
        Workout(3, "Прямой эфир с тренером", "Живой эфир с разбором техники", 2, "60 минут"),
        Workout(4, "Силовой комплекс", "Упражнения с собственным весом", 3, "20 минут"),
        Workout(5, "Йога для начинающих", "Поза лотоса и базовые асаны", 1, "40 минут")
    )

    private val videos = mapOf(
        1 to VideoWorkout(1, "30 минут", "https://archive.org/download/BigBuckBunny_124/BigBuckBunny_512kb.mp4"),
        2 to VideoWorkout(2, "45 минут", "https://archive.org/download/BigBuckBunny_124/BigBuckBunny_512kb.mp4"),
        3 to VideoWorkout(3, "60 минут", "https://archive.org/download/BigBuckBunny_124/BigBuckBunny_512kb.mp4"),
        4 to VideoWorkout(4, "20 минут", "https://archive.org/download/BigBuckBunny_124/BigBuckBunny_512kb.mp4"),
        5 to VideoWorkout(5, "40 минут", "https://archive.org/download/BigBuckBunny_124/BigBuckBunny_512kb.mp4")
    )

    suspend fun getWorkouts(): List<Workout> = workouts

    suspend fun getVideo(id: Int): VideoWorkout = videos[id] ?: videos[1]!!
}
