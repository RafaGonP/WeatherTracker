package com.rafagonp.weathertracker.data.remote.service

import com.rafagonp.weathertracker.data.remote.model.GetPredictionPerDaysResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("onecall")
    suspend fun getPredictionPerDays(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String,
        @Query("appid") appid: String,
        @Query("units") units: String,
    ): Response<GetPredictionPerDaysResponseDTO>
}