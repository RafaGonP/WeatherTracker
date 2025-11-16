package com.rafagonp.weathertracker.data.remote.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val CONNECTION_TIMEOUT_MINUTES = 2L
const val READ_TIMEOUT_MINUTES = 2L

const val BASE_URL = "https://api.openweathermap.org/data/3.0/"

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    //region OkHttp Clients and Retrofit
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
            .readTimeout(READ_TIMEOUT_MINUTES, TimeUnit.MINUTES)
            .connectTimeout(CONNECTION_TIMEOUT_MINUTES, TimeUnit.MINUTES)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(this::getContentTypeAndAcceptInterceptor)
            .build()

    }

    @Provides
    fun provideRetrofitClient(client: OkHttpClient): Retrofit =
        Retrofit.Builder().client(client).baseUrl(BASE_URL).addConverterFactory(
            GsonConverterFactory.create()
        ).build()
    //endregion

    private fun getContentTypeAndAcceptInterceptor(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .build()
        return chain.proceed(request)
    }
}