package com.pixelrabbit.fitnessapp.data.repository

import com.pixelrabbit.fitnessapp.data.model.Workout
import com.pixelrabbit.fitnessapp.data.model.VideoWorkout
import com.pixelrabbit.fitnessapp.data.network.RetrofitInstance
import retrofit2.Response

class WorkoutRepository {
    suspend fun getWorkouts(): Response<List<Workout>> =
        RetrofitInstance.api.getWorkouts()

    suspend fun getVideo(id: Int): Response<VideoWorkout> =
        RetrofitInstance.api.getVideo(id)
}