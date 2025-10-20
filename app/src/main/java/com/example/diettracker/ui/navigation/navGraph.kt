package com.example.diettracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.diettracker.ui.components.cards.MealInfo
import com.example.diettracker.ui.screens.*
import com.example.diettracker.viewmodel.FoodViewModel

@Composable
fun AppNavGraph(navController: NavHostController) {
    // Create a shared meals list that persists across navigation
    val meals = remember { mutableStateListOf<MealInfo>() }

    // Get shared ViewModel instance
    val foodViewModel: FoodViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route
    ) {
        composable(route = BottomNavItem.Home.route) {
            HomeScreen(
                meals = meals,
                onNavigateToAllNutrients = {
                    navController.navigate(BottomNavItem.Nutrients.route)
                }
            )
        }

        composable(route = BottomNavItem.Nutrients.route) {
            AllNutrientsScreen(
                meals = meals,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = BottomNavItem.Foods.route) {
            AllFoodsScreen(
                navController = navController,
                foodViewModel = foodViewModel
            )
        }

        composable(route = BottomNavItem.AddFood.route) {
            AddFoodScreen(foodViewModel = foodViewModel)
        }

        composable(route = BottomNavItem.Profile.route) {
            SettingScreen()
        }
    }
}