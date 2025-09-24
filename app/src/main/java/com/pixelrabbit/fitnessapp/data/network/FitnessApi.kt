package com.pixelrabbit.fitnessapp.data.network

import com.pixelrabbit.fitnessapp.data.model.Workout
import com.pixelrabbit.fitnessapp.data.model.VideoWorkout
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FitnessApi {
    @GET("get_workouts")
    suspend fun getWorkouts(): Response<List<Workout>>

    @GET("get_video")
    suspend fun getVideo(@Query("id") id: Int): Response<VideoWorkout>
}
