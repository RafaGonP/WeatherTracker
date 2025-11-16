package com.rafagonp.weathertracker.domain.repository

import com.rafagonp.weathertracker.domain.model.DaysListBO
import com.rafagonp.weathertracker.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun getPredictionPerDays(
        exclude: String,
        units: String,
    ): Flow<Resource<DaysListBO>>
}