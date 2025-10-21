package com.example.diettracker.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.diettracker.models.DietViewModel
import com.example.diettracker.models.UserViewModel
import com.example.diettracker.ui.navigation.BottomNavBar
import com.example.diettracker.ui.navigation.BottomNavItem

@Composable
fun MainScreen(rootNavController: NavController) {
    Log.d("MainScreen", "=== MainScreen Initialized ===")

    // ✅ Local navController for bottom navigation
    val bottomNavController = rememberNavController()

    val userViewModel: UserViewModel = viewModel()
    val dietViewModel: DietViewModel = viewModel()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavBar(navController = bottomNavController) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(
                navController = bottomNavController,
                startDestination = BottomNavItem.Home.route
            ) {
                composable(BottomNavItem.Home.route) {
                    HomeScreen(
                        dietViewModel = dietViewModel,
                        userViewModel = userViewModel,
                        onNavigateToAllNutrients = {
                            bottomNavController.navigate(BottomNavItem.Nutrients.route)
                        }
                    )
                }

                composable(BottomNavItem.Nutrients.route) {
                    AllNutrientsScreen(
                        navController = bottomNavController,
                        dietViewModel = dietViewModel,
                        userViewModel = userViewModel
                    )
                }

                composable(BottomNavItem.Foods.route) {
                    AllFoodsScreen(bottomNavController)
                }

                composable(BottomNavItem.AddFood.route) {
                    AddFoodScreen(bottomNavController)
                }

                composable(BottomNavItem.Profile.route) {
                    SettingScreen(
                        navController = rootNavController, // 👈 Important: Use ROOT navController for logout
                        userViewModel = userViewModel
                    )
                }
            }
        }
    }
}
