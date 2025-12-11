package com.ecgwatchdog

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meter_readings")
data class MeterReading(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val readingKwh: Double,
    val timestamp: Long = System.currentTimeMillis()
)