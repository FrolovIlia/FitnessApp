package com.pixelrabbit.fitnessapp.data.network

import com.pixelrabbit.fitnessapp.data.api.FitnessApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://ref.test.kolsa.ru/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: FitnessApi by lazy {
        retrofit.create(FitnessApi::class.java)
    }
}
