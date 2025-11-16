package com.rafagonp.weathertracker.domain.usecase

import com.rafagonp.weathertracker.domain.model.DaysListBO
import com.rafagonp.weathertracker.domain.model.Resource
import com.rafagonp.weathertracker.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

const val EXCLUDE = "current,minutely,hourly,alerts"
const val UNITS = "metric"

class GetPredictionPerDaysUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {
    operator fun invoke(): Flow<Resource<DaysListBO>> = weatherRepository.getPredictionPerDays(
        EXCLUDE,
        UNITS
    )

}