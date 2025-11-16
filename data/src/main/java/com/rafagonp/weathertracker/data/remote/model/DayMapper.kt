package com.rafagonp.weathertracker.data.remote.model

import com.rafagonp.weathertracker.domain.model.DayBO

fun GetPredictionPerDaysResponseDTO.toModel() = DaysListEntity(
    listOfDays = ListOfDays(daily?.map { it.toModel() } ?: emptyList())
)

fun DayDTO.toModel() = DayBO(
    dt = dt ?: 0,
    sunrise = sunrise ?: 0,
    sunset = sunset ?: 0,
    moonrise = moonrise ?: 0,
    moonset = moonset ?: 0,
    moonPhase = moonPhase ?: 0.0,
    summary = summary.orEmpty(),
    temp = temp.toModel(),
    pressure = pressure ?: 0,
    humidity = humidity ?: 0,
    dewPoint = dewPoint ?: 0.0,
    windSpeed = windSpeed ?: 0.0,
    windDeg = windDeg ?: 0,
    windGust = windGust ?: 0.0,
    weather = weather.toModel(),
    clouds = clouds ?: 0,
    pop = pop ?: 0.0,
    rain = rain ?: 0.0,
    uvi = uvi ?: 0.0
)
