package com.pixelrabbit.fitnessapp.data.api

import com.pixelrabbit.fitnessapp.data.model.VideoWorkout
import com.pixelrabbit.fitnessapp.data.model.Workout
import retrofit2.HttpException
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.IOException

interface FitnessApi {

    @GET("get_workouts")
    suspend fun getWorkouts(): List<Workout>

    @GET("get_video")
    suspend fun getVideo(@Query("id") id: Int): VideoWorkout
}

sealed class Resource<out T> {
    object Loading : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
    object Empty : Resource<Nothing>()
}

suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T> {
    return try {
        val result = apiCall()
        if ((result as? Collection<*>)?.isEmpty() == true) {
            Resource.Empty
        } else {
            Resource.Success(result)
        }
    } catch (e: IOException) {
        Resource.Error("Network error: ${e.message}")
    } catch (e: HttpException) {
        Resource.Error("HTTP error: ${e.message}")
    } catch (e: Exception) {
        Resource.Error("Unexpected error: ${e.message}")
    }
}
