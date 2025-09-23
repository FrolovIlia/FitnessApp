package com.pixelrabbit.fitnessapp.data.repository

import com.pixelrabbit.fitnessapp.data.model.VideoWorkout
import com.pixelrabbit.fitnessapp.data.model.Workout
import com.pixelrabbit.fitnessapp.data.network.RetrofitInstance

class WorkoutRepository {

    suspend fun getWorkouts(): List<Workout> {
        return RetrofitInstance.api.getWorkouts()
    }

    suspend fun getVideo(id: Int): VideoWorkout {
        return RetrofitInstance.api.getVideo(id)
    }
}
