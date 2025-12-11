package com.ecgwatchdog

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(viewModel: ECGViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Usage History",
            style = MaterialTheme.typography.headlineMedium
        )
        
        if (uiState.readings.isNotEmpty()) {
            // Simple bar chart
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Daily Usage Chart",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val readings = uiState.readings.take(7).reversed()
                    val usages = mutableListOf<Double>()
                    
                    for (i in 0 until readings.size - 1) {
                        val usage = readings[i + 1].readingKwh - readings[i].readingKwh
                        if (usage > 0) usages.add(usage)
                    }
                    
                    if (usages.isNotEmpty()) {
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                        ) {
                            val maxUsage = usages.maxOrNull() ?: 1.0
                            val barWidth = size.width / usages.size
                            val maxHeight = size.height - 20.dp.toPx()
                            
                            usages.forEachIndexed { index, usage ->
                                val barHeight = (usage / maxUsage * maxHeight).toFloat()
                                val x = index * barWidth + barWidth * 0.2f
                                val barWidthActual = barWidth * 0.6f
                                
                                drawRect(
                                    color = Color.Blue,
                                    topLeft = Offset(x, size.height - barHeight),
                                    size = androidx.compose.ui.geometry.Size(barWidthActual, barHeight)
                                )
                            }
                        }
                    }
                }
            }
            
            // History list
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val readings = uiState.readings
                items(readings.size - 1) { index ->
                    val current = readings[index]
                    val previous = readings[index + 1]
                    val usage = current.readingKwh - previous.readingKwh
                    
                    if (usage > 0) {
                        val cost = CostCalculator.calculateDailyCost(usage)
                        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                        
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = dateFormat.format(Date(current.timestamp)),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = CostCalculator.formatCurrency(cost.total),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Usage: ${usage} kWh",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = CostCalculator.getUsageCategory(usage),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                
                                Text(
                                    text = "Reading: ${current.readingKwh} kWh",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
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
                        text = "No history available. Start by entering your meter readings.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}