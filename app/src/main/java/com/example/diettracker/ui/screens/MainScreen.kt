

package com.example.diettracker.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.diettracker.ui.navigation.BottomNavBar
import com.example.diettracker.ui.navigation.BottomNavItem

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            // Hide bottom bar on certain screens
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
            if (currentRoute != "all_nutrients") {
                BottomNavBar(navController = navController)
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavigationHost(navController = navController)
        }
    }
}


@Composable
fun NavigationHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route
    ) {
        composable(BottomNavItem.Home.route) {
            HomeScreen(
                onNavigateToAllNutrients = {
                    navController.navigate("all_nutrients")
                }
            )
        }

        composable(BottomNavItem.Nutrients.route) {
            AllNutrientsScreen(onBackClick = { navController.popBackStack() })
        }

        composable(BottomNavItem.Foods.route) {
            AllFoodsScreen(navController) // Food list
        }

        composable(BottomNavItem.AddFood.route) {
            AddFoodsScreen(navController) // Add new food
        }

        composable(BottomNavItem.Profile.route) {
            SettingScreen()
        }

        composable("all_nutrients") {
            AllNutrientsScreen(onBackClick = { navController.popBackStack() })
        }
    }
}


// Placeholder for Search - you can replace this later
@Composable
fun SearchPlaceholder() {
    Box(modifier = Modifier.fillMaxSize()) {
        androidx.compose.material3.Text("Search Screen")
    }
}