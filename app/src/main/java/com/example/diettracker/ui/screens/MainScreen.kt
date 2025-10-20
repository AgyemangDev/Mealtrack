package com.example.diettracker.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.diettracker.ui.navigation.AppNavGraph
import com.example.diettracker.ui.navigation.BottomNavBar
import com.example.diettracker.ui.navigation.BottomNavItem

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            // Hide bottom bar on certain screens if needed
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
            // You can add conditions here to hide the bottom bar on specific routes
            BottomNavBar(navController = navController)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            AppNavGraph(navController = navController)
        }
    }
}