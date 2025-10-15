// NavGraph.kt
package com.example.diettracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.diettracker.ui.screens.*

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Welcome.route) {
        composable (Screen.Welcome.route){WelcomeScreen(navController)}
        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.Register.route) { RegisterScreen() }
        composable(Screen.AgeRange.route) { AgeRange() }
        composable(Screen.DailyUsage.route) { DailyUsage() }
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Progress.route) { ProgressScreen() }
        composable(Screen.Setting.route) { SettingScreen() }
        composable(Screen.LogMeal.route) { LogMealScreen() }
    }
}
