package com.rafagonp.weathertracker.data.remote.model

import com.google.gson.annotations.SerializedName

data class GetPredictionPerDaysResponseDTO(
    val lat: Double? = null,
    val long: Double? = null,
    val timezone: String? = null,
    @SerializedName("timezone_offset")
    val timezoneOffset: Int? = null,
    val daily: List<DayDTO>? = null
)
