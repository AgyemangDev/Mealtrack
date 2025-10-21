package com.example.diettracker.ui.screens

import HomeScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.diettracker.ui.components.cards.MealInfo
import com.example.diettracker.ui.navigation.BottomNavBar
import com.example.diettracker.ui.navigation.BottomNavItem
import com.example.diettracker.models.UserViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val meals = remember { mutableStateListOf<MealInfo>() }

    // ✅ Get lifecycle-aware ViewModel
    val userViewModel: UserViewModel = viewModel()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavBar(navController = navController)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Home.route
            ) {
                // Home Screen
                composable(BottomNavItem.Home.route) {
                    HomeScreen(
                        meals = meals,
                        userViewModel = userViewModel,
                        onNavigateToAllNutrients = {
                            navController.navigate(BottomNavItem.Nutrients.route)
                        }
                    )
                }

                // Nutrients Screen — pass meals
                composable(BottomNavItem.Nutrients.route) {
                    AllNutrientsScreen(meals = meals, navController, userViewModel = userViewModel,)
                }

                // Foods Screen
                composable(BottomNavItem.Foods.route) {
                    AllFoodsScreen(navController)
                }

                // Add Food Screen
                composable(BottomNavItem.AddFood.route) {
                    AddFoodScreen(navController)
                }

                // Profile / Settings Screen
                composable(BottomNavItem.Profile.route) {
                    SettingScreen()
                }
            }
        }
    }
}
