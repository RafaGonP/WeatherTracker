package com.rafagonp.weathertracker.domain.usecase

import com.rafagonp.weathertracker.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object WeatherUseCasesModule {

    @Provides
    fun provideGetPredictionPerDaysUseCase(
        weatherRepository: WeatherRepository,
    ): GetPredictionPerDaysUseCase =
        GetPredictionPerDaysUseCase(weatherRepository)
}