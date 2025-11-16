package com.rafagonp.weathertracker.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rafagonp.weathertracker.data.remote.model.DaysListEntity
import com.rafagonp.weathertracker.data.remote.model.DaysTypeConverters
import kotlinx.coroutines.flow.Flow

@Dao
interface DaysListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDaysList(daysList: DaysListEntity)

    @Query("SELECT * FROM DaysListEntity")
    fun getDaysList(): Flow<DaysListEntity?>
}
@TypeConverters(value = [DaysTypeConverters::class])
@Database(entities = [DaysListEntity::class], exportSchema = false, version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun daysListDao(): DaysListDao
}