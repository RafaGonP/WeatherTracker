package com.rafagonp.weathertracker.data.remote.datasource

import arrow.core.Either
import com.rafagonp.weathertracker.data.remote.model.DaysListEntity
import com.rafagonp.weathertracker.domain.model.CustomError

interface WeatherRemoteDataSource {

    suspend fun getPredictionPerDays(
        exclude: String,
        units: String,
    ): Either<CustomError, DaysListEntity>
}