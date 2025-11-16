package com.rafagonp.weathertracker.data.repository.di

import com.rafagonp.weathertracker.data.repository.WeatherRepositoryImpl
import com.rafagonp.weathertracker.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindWeatherRepository(
        weatherRepository: WeatherRepositoryImpl
    ): WeatherRepository

}