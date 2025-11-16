package com.rafagonp.weathertracker.data.remote.model

import com.google.gson.annotations.SerializedName

data class DayDTO(
    val dt: Long? = null,
    val sunrise: Long? = null,
    val sunset: Long? = null,
    val moonrise: Long? = null,
    val moonset: Long? = null,
    @SerializedName("moon_phase")
    val moonPhase: Double? = null,
    val summary: String? = null,
    val temp: TempDTO? = null,
    val pressure: Int? = null,
    val humidity: Int? = null,
    @SerializedName("dew_point")
    val dewPoint: Double? = null,
    @SerializedName("wind_speed")
    val windSpeed: Double? = null,
    @SerializedName("wind_deg")
    val windDeg: Int? = null,
    @SerializedName("wind_gust")
    val windGust: Double? = null,
    val weather: List<WeatherDTO>? = null,
    val clouds: Int? = null,
    val pop: Double? = null,
    val rain: Double? = null,
    val uvi: Double? = null
)

