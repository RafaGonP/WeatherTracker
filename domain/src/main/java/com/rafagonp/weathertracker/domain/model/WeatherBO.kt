package com.rafagonp.weathertracker.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

const val WEATHER_ICONS_URL_BASE = "https://openweathermap.org/img/wn/"
const val WEATHER_ICONS_URL_EXTENSION = "@2x.png"
@Parcelize
data class WeatherBO(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
) : Parcelable