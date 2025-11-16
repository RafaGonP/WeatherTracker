package com.rafagonp.weathertracker.data.remote.model

import com.rafagonp.weathertracker.domain.model.TempBO

fun TempDTO?.toModel() = this?.let {
    TempBO(
        day = day ?: 0.0,
        min = min ?: 0.0,
        max = max ?: 0.0,
        night = night ?: 0.0,
        eve = eve ?: 0.0,
        morn = morn ?: 0.0,
    )
} ?: throw IllegalArgumentException("TempDTO cannot be null")