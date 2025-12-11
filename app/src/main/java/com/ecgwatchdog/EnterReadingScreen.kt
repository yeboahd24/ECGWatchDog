package com.ecgwatchdog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterReadingScreen(viewModel: ECGViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Enter Today's Meter Reading",
                    style = MaterialTheme.typography.headlineSmall
                )
                
                if (uiState.lastReading != null) {
                    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                    Text(
                        text = "Last reading: ${uiState.lastReading.readingKwh} kWh on ${dateFormat.format(Date(uiState.lastReading.timestamp))}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        OutlinedTextField(
            value = uiState.currentReading,
            onValueChange = viewModel::updateCurrentReading,
            label = { Text("Meter Reading (kWh)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        Button(
            onClick = viewModel::saveReading,
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading && uiState.currentReading.isNotBlank()
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text("Save Reading")
        }
        
        uiState.error?.let { error ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = viewModel::clearError) {
                        Text("Dismiss")
                    }
                }
            }
        }
        
        if (uiState.dailyUsage != null && uiState.costBreakdown != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (uiState.isSpike) 
                        MaterialTheme.colorScheme.errorContainer 
                    else 
                        MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = if (uiState.isSpike) "⚠️ High Usage Alert!" else "✅ Reading Saved",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text("Daily Usage: ${uiState.dailyUsage} kWh")
                    Text("Estimated Cost: ${CostCalculator.formatCurrency(uiState.costBreakdown.total)}")
                    Text("Category: ${uiState.usageCategory}")
                }
            }
        }
    }
}