package com.ecgwatchdog

import kotlinx.coroutines.flow.Flow

class UsageRepository(private val dao: MeterReadingDao) {
    
    suspend fun saveReading(readingKwh: Double) {
        dao.insertReading(MeterReading(readingKwh = readingKwh))
    }
    
    suspend fun getLastReading(): MeterReading? {
        return dao.getLastReading()
    }
    
    fun getAllReadings(): Flow<List<MeterReading>> {
        return dao.getAllReadings()
    }
    
    suspend fun calculateDailyUsage(currentReading: Double): Double? {
        val lastReading = getLastReading()
        return if (lastReading != null && currentReading >= lastReading.readingKwh) {
            currentReading - lastReading.readingKwh
        } else null
    }
    
    suspend fun detectSpike(currentUsage: Double): Boolean {
        val lastSevenReadings = dao.getLastSevenReadings()
        if (lastSevenReadings.size < 2) return false
        
        val usages = mutableListOf<Double>()
        for (i in 0 until lastSevenReadings.size - 1) {
            val usage = lastSevenReadings[i].readingKwh - lastSevenReadings[i + 1].readingKwh
            if (usage > 0) usages.add(usage)
        }
        
        if (usages.isEmpty()) return false
        
        val average = usages.average()
        return currentUsage > average * 1.5
    }
}