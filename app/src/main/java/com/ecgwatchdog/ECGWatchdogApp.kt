package com.ecgwatchdog

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ECGWatchdogApp(viewModel: ECGViewModel) {
    val navController = rememberNavController()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ECG Watchdog") }
            )
        },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                    label = { Text("Enter Reading") },
                    selected = currentDestination?.hierarchy?.any { it.route == "enter_reading" } == true,
                    onClick = {
                        navController.navigate("enter_reading") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                    label = { Text("Summary") },
                    selected = currentDestination?.hierarchy?.any { it.route == "summary" } == true,
                    onClick = {
                        navController.navigate("summary") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.List, contentDescription = null) },
                    label = { Text("History") },
                    selected = currentDestination?.hierarchy?.any { it.route == "history" } == true,
                    onClick = {
                        navController.navigate("history") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "enter_reading",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("enter_reading") {
                EnterReadingScreen(viewModel)
            }
            composable("summary") {
                SummaryScreen(viewModel)
            }
            composable("history") {
                HistoryScreen(viewModel)
            }
        }
    }
}