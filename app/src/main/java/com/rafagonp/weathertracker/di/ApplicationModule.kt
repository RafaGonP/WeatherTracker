package com.rafagonp.weathertracker.di

import android.content.Context
import com.rafagonp.weathertracker.data.R

import com.rafagonp.weathertracker.data.local.OpenWeatherAPIKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideOpenWeatherApiKey(
        @ApplicationContext context: Context,
    ) = OpenWeatherAPIKey(
        apiKey = context.getString(R.string.openWeatherApiKey)
    )
}