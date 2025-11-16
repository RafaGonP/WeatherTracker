package com.rafagonp.weathertracker.data.remote.model

import com.rafagonp.weathertracker.domain.model.DaysListBO

fun DaysListEntity.toModel() : DaysListBO = DaysListBO(
    days = listOfDays.days
)