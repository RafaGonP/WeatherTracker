package com.rafagonp.weathertracker.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.toJavaInstant

@Parcelize
data class DayBO(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val moonrise: Long,
    val moonset: Long,
    val moonPhase: Double,
    val summary: String,
    val temp: TempBO,
    val pressure: Int,
    val humidity: Int,
    val dewPoint: Double,
    val windSpeed: Double,
    val windDeg: Int,
    val windGust: Double,
    val weather: List<WeatherBO>,
    val clouds: Int,
    val pop: Double,
    val rain: Double,
    val uvi: Double
) : Parcelable

val dateFormatDate: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM")

val dateFormatDay: DateTimeFormatter = DateTimeFormatter.ofPattern("EEEE")

val dateFormatTime: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

fun Long.toDate(): String = toFormat(dateFormatDate)

fun Long.toDay(): String = toFormat(dateFormatDay)

fun Long.toTime(): String = toFormat(dateFormatTime)

@OptIn(ExperimentalTime::class)
fun Long.toFormat(format: DateTimeFormatter): String = LocalDateTime.ofInstant(
    Instant.fromEpochSeconds(this, 0).toJavaInstant(),
    TimeZone.getDefault().toZoneId()
).format(format)

fun String.myCapitalize(): String =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
