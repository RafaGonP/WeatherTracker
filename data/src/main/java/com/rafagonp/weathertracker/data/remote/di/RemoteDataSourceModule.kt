package com.rafagonp.weathertracker.data.remote.di

import com.rafagonp.weathertracker.data.remote.datasource.WeatherRemoteDataSource
import com.rafagonp.weathertracker.data.remote.datasource.impl.WeatherRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceModule {

    @Binds
    abstract fun bindWeatherRemoteDataSource(
        remoteDataSource: WeatherRemoteDataSourceImpl
    ): WeatherRemoteDataSource


}