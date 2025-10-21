package com.example.diettracker.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.diettracker.models.DietViewModel
import com.example.diettracker.models.UserViewModel
import com.example.diettracker.ui.navigation.BottomNavBar
import com.example.diettracker.ui.navigation.BottomNavItem

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Log.d("MainScreen", "=== MainScreen Initialized ===")

    // ✅ Create shared ViewModels at the MainScreen level
    val userViewModel: UserViewModel = viewModel()
    val dietViewModel: DietViewModel = viewModel()

    Log.d("MainScreen", "ViewModels created - UserViewModel: $userViewModel, DietViewModel: $dietViewModel")

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavBar(navController = navController) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Home.route
            ) {
                // Home Screen
                composable(BottomNavItem.Home.route) {
                    Log.d("MainScreen", "Navigating to Home Screen")
                    HomeScreen(
                        dietViewModel = dietViewModel,
                        userViewModel = userViewModel,
                        onNavigateToAllNutrients = {
                            Log.d("MainScreen", "Navigating to All Nutrients Screen")
                            navController.navigate(BottomNavItem.Nutrients.route)
                        }
                    )
                }

                // Nutrients Screen - ✅ Removed meals parameter, pass ViewModels
                composable(BottomNavItem.Nutrients.route) {
                    Log.d("MainScreen", "Navigating to All Nutrients Screen")
                    AllNutrientsScreen(
                        navController = navController,
                        dietViewModel = dietViewModel,
                        userViewModel = userViewModel
                    )
                }

                // Foods Screen
                composable(BottomNavItem.Foods.route) {
                    Log.d("MainScreen", "Navigating to All Foods Screen")
                    AllFoodsScreen(navController)
                }

                // Add Food Screen
                composable(BottomNavItem.AddFood.route) {
                    Log.d("MainScreen", "Navigating to Add Food Screen")
                    AddFoodScreen(navController)
                }

                // Profile / Settings Screen
                composable(BottomNavItem.Profile.route) {
                    Log.d("MainScreen", "Navigating to Settings Screen")
                    SettingScreen()
                }
            }
        }
    }
}