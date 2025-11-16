package com.rafagonp.weathertracker.data.remote.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.rafagonp.weathertracker.domain.model.DayBO
import com.rafagonp.weathertracker.utils.TimeHelper

@Entity
data class DaysListEntity(
    @PrimaryKey
    val id : Int = 0,
    val listOfDays: ListOfDays,
    val lastUpdated: Long = TimeHelper.getNow()
)

data class ListOfDays(
    var days : List<DayBO>
)

class DaysTypeConverters{
    @TypeConverter
    fun fromDaysList(daysList: ListOfDays): String {
        return Gson().toJson(daysList)
    }
    @TypeConverter
    fun toDaysList(daysListString: String): ListOfDays {
        return Gson().fromJson(daysListString, ListOfDays::class.java)
    }
}
