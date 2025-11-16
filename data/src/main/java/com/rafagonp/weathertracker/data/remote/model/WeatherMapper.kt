package com.rafagonp.weathertracker.data.remote.model

import com.rafagonp.weathertracker.domain.model.WeatherBO

fun List<WeatherDTO>?.toModel() : List<WeatherBO> = this?.map { it.toModel() } ?: throw UnsupportedOperationException("The list is null")

fun WeatherDTO.toModel() = WeatherBO(
    id = id ?: 0,
    main = main.orEmpty(),
    description = description.orEmpty(),
    icon = icon.orEmpty()
)