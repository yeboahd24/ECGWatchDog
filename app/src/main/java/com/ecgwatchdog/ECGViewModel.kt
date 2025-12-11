package com.ecgwatchdog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ECGUiState(
    val currentReading: String = "",
    val lastReading: MeterReading? = null,
    val dailyUsage: Double? = null,
    val costBreakdown: CostBreakdown? = null,
    val usageCategory: String = "",
    val isSpike: Boolean = false,
    val readings: List<MeterReading> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class ECGViewModel(private val repository: UsageRepository) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ECGUiState())
    val uiState: StateFlow<ECGUiState> = _uiState.asStateFlow()
    
    init {
        loadData()
    }
    
    fun updateCurrentReading(reading: String) {
        _uiState.value = _uiState.value.copy(currentReading = reading)
    }
    
    fun saveReading() {
        val reading = _uiState.value.currentReading.toDoubleOrNull()
        if (reading == null || reading < 0) {
            _uiState.value = _uiState.value.copy(error = "Please enter a valid reading")
            return
        }
        
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                
                val dailyUsage = repository.calculateDailyUsage(reading)
                val isSpike = if (dailyUsage != null) {
                    repository.detectSpike(dailyUsage)
                } else false
                
                repository.saveReading(reading)
                
                val costBreakdown = if (dailyUsage != null) {
                    CostCalculator.calculateDailyCost(dailyUsage)
                } else null
                
                val usageCategory = if (dailyUsage != null) {
                    CostCalculator.getUsageCategory(dailyUsage)
                } else ""
                
                _uiState.value = _uiState.value.copy(
                    currentReading = "",
                    dailyUsage = dailyUsage,
                    costBreakdown = costBreakdown,
                    usageCategory = usageCategory,
                    isSpike = isSpike,
                    isLoading = false
                )
                
                loadData()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to save reading: ${e.message}"
                )
            }
        }
    }
    
    private fun loadData() {
        viewModelScope.launch {
            try {
                val lastReading = repository.getLastReading()
                _uiState.value = _uiState.value.copy(lastReading = lastReading)
                
                repository.getAllReadings().collect { readings ->
                    _uiState.value = _uiState.value.copy(readings = readings)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to load data: ${e.message}")
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}