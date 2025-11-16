package com.rafagonp.weathertracker.data.remote.datasource.impl

import arrow.core.Either
import com.rafagonp.weathertracker.data.local.OpenWeatherAPIKey
import com.rafagonp.weathertracker.data.remote.datasource.WeatherRemoteDataSource
import com.rafagonp.weathertracker.data.remote.manager.RemoteErrorManagement
import com.rafagonp.weathertracker.data.remote.model.DaysListEntity
import com.rafagonp.weathertracker.data.remote.model.toModel
import com.rafagonp.weathertracker.data.remote.service.WeatherService
import com.rafagonp.weathertracker.data.session.datasource.LAT
import com.rafagonp.weathertracker.data.session.datasource.LONG
import com.rafagonp.weathertracker.domain.model.CustomError
import javax.inject.Inject

class WeatherRemoteDataSourceImpl @Inject constructor(
    private val weatherService: WeatherService,
    private val openWeatherAPIKey: OpenWeatherAPIKey
) : WeatherRemoteDataSource {

    override suspend fun getPredictionPerDays(
        exclude: String,
        units: String,
    ): Either<CustomError, DaysListEntity> =
        RemoteErrorManagement.safeCall(action = {
            weatherService.getPredictionPerDays(
                LAT,
                LONG,
                exclude,
                openWeatherAPIKey.apiKey,
                units
            )
        }, mapperToDomainLayer = { it.toModel() })

}