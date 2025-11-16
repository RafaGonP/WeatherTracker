package com.rafagonp.weathertracker.data.local.di

import android.content.Context
import androidx.room.Room
import com.rafagonp.weathertracker.data.local.AppDatabase
import com.rafagonp.weathertracker.data.local.DaysListDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "appdatabase.db")
            .build()

    @Provides
    @Singleton
    fun provideDaysListDao(database: AppDatabase): DaysListDao = database.daysListDao()
}
