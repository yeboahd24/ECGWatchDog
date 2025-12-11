package com.ecgwatchdog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SummaryScreen(viewModel: ECGViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Daily Summary",
            style = MaterialTheme.typography.headlineMedium
        )
        
        if (uiState.readings.isNotEmpty()) {
            val readings = uiState.readings
            val todayUsage = if (readings.size >= 2) {
                readings[0].readingKwh - readings[1].readingKwh
            } else null
            
            val yesterdayUsage = if (readings.size >= 3) {
                readings[1].readingKwh - readings[2].readingKwh
            } else null
            
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Usage Comparison",
                        style = MaterialTheme.typography.titleLarge
                    )
                    
                    if (todayUsage != null && todayUsage > 0) {
                        val costBreakdown = CostCalculator.calculateDailyCost(todayUsage)
                        val category = CostCalculator.getUsageCategory(todayUsage)
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Today's Usage:")
                            Text("${todayUsage} kWh")
                        }
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Estimated Cost:")
                            Text(CostCalculator.formatCurrency(costBreakdown.total))
                        }
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Category:")
                            Text(category)
                        }
                        
                        Divider()
                    }
                    
                    if (yesterdayUsage != null && yesterdayUsage > 0) {
                        val yesterdayCost = CostCalculator.calculateDailyCost(yesterdayUsage)
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Yesterday's Usage:")
                            Text("${yesterdayUsage} kWh")
                        }
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Yesterday's Cost:")
                            Text(CostCalculator.formatCurrency(yesterdayCost.total))
                        }
                        
                        if (todayUsage != null && todayUsage > 0) {
                            val difference = todayUsage - yesterdayUsage
                            val percentChange = (difference / yesterdayUsage) * 100
                            
                            Divider()
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Change:")
                                Text(
                                    text = "${if (difference > 0) "+" else ""}${String.format("%.1f", difference)} kWh (${String.format("%.1f", percentChange)}%)",
                                    color = if (difference > 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
            
            // Monthly projection
            if (todayUsage != null && todayUsage > 0) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Monthly Projection",
                            style = MaterialTheme.typography.titleLarge
                        )
                        
                        val monthlyUsage = todayUsage * 30
                        val monthlyCost = CostCalculator.calculateDailyCost(todayUsage).total * 30
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Projected Usage:")
                            Text("${String.format("%.1f", monthlyUsage)} kWh")
                        }
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Projected Cost:")
                            Text(CostCalculator.formatCurrency(monthlyCost))
                        }
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp)
                ) {
                    Text(
                        text = "No readings available. Please enter your first meter reading.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}