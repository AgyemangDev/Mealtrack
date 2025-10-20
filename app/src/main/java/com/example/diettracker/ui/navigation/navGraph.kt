
package com.example.diettracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.diettracker.ui.screens.HomeScreen
import com.example.diettracker.ui.screens.AllNutrientsScreen
import com.example.diettracker.ui.screens.AllFoodsScreen
import com.example.diettracker.ui.screens.AddFoodScreen
import com.example.diettracker.ui.screens.DailyUsage
import com.example.diettracker.ui.components.cards.MealInfo

@Composable
fun AppNavGraph(navController: NavHostController) {
    val meals = remember { mutableStateListOf<MealInfo>() }

    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route
    ) {
        composable(BottomNavItem.Home.route) {
            HomeScreen(
                meals = meals,
                onNavigateToAllNutrients = {
                    navController.navigate(BottomNavItem.Nutrients.route)
                }
            )
        }

        composable(BottomNavItem.Nutrients.route) {
            AllNutrientsScreen(
                meals = meals,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(BottomNavItem.Foods.route) {
            AllFoodsScreen(navController = navController)
        }

        composable(BottomNavItem.AddFood.route) {
            AddFoodScreen(navController = navController)
        }

        composable(BottomNavItem.Profile.route) {
            DailyUsage(navController = navController)
        }
    }
}