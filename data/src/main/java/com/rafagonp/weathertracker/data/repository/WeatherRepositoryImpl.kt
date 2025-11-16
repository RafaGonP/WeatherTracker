package com.rafagonp.weathertracker.data.repository

import com.rafagonp.weathertracker.data.local.DaysListDao
import com.rafagonp.weathertracker.data.remote.datasource.WeatherRemoteDataSource
import com.rafagonp.weathertracker.data.remote.dispatcher.DispatcherProvider
import com.rafagonp.weathertracker.data.remote.model.toModel
import com.rafagonp.weathertracker.domain.model.CustomError
import com.rafagonp.weathertracker.domain.model.DaysListBO
import com.rafagonp.weathertracker.domain.model.Resource
import com.rafagonp.weathertracker.domain.repository.WeatherRepository
import com.rafagonp.weathertracker.utils.TimeHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
    private val daysListDao: DaysListDao,
    private val dispatcherProvider: DispatcherProvider
) : WeatherRepository {

    private val CACHE_DURATION = TimeUnit.MINUTES.toMillis(10)

    override fun getPredictionPerDays(
        exclude: String,
        units: String,
    ): Flow<Resource<DaysListBO>> =
        daysListDao.getDaysList()
            .transform { cachedDaysList ->
                try {
                    cachedDaysList?.let { emit(Resource.success(cachedDaysList.toModel())) }
                    if (cachedDaysList == null || isCacheExpired(cachedDaysList.lastUpdated)) {
                        refreshDaysList(exclude, units)
                    }
                } catch (e: Exception) {
                    emit(Resource.error(e.message, null))
                }
            }
            .flowOn(dispatcherProvider.io)

    suspend fun refreshDaysList(exclude: String, units: String) {
        withContext(dispatcherProvider.io) {
            weatherRemoteDataSource.getPredictionPerDays(
                exclude,
                units
            ).fold(
                ifLeft = {
                    when (it) {
                        is CustomError.Connectivity -> throw Exception()
                        is CustomError.ServerError -> throw Exception(it.message)
                        is CustomError.Unknown -> throw Exception(it.message)
                    }
                },
                ifRight = {
                    daysListDao.insertDaysList(it.copy(lastUpdated = TimeHelper.getNow()))
                }
            )
        }
    }

    private fun isCacheExpired(timestamp: Long): Boolean {
        return TimeHelper.getNow() - timestamp > CACHE_DURATION
    }

}