package com.ecgwatchdog

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MeterReadingDao {
    @Insert
    suspend fun insertReading(reading: MeterReading)
    
    @Query("SELECT * FROM meter_readings ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastReading(): MeterReading?
    
    @Query("SELECT * FROM meter_readings ORDER BY timestamp DESC")
    fun getAllReadings(): Flow<List<MeterReading>>
    
    @Query("SELECT * FROM meter_readings ORDER BY timestamp DESC LIMIT 7")
    suspend fun getLastSevenReadings(): List<MeterReading>
}