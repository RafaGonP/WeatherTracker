package com.rafagonp.weathertracker.data.remote.di

import com.rafagonp.weathertracker.data.remote.dispatcher.AppDispatcherProvider
import com.rafagonp.weathertracker.data.remote.dispatcher.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DispatchersModule {

    @Provides
    fun providesDispatcher(): DispatcherProvider = AppDispatcherProvider()
}
