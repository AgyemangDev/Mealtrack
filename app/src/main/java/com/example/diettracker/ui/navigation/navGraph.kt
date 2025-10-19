package com.example.diettracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.diettracker.ui.screens.*
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Welcome.route) {
        // Authentication & Onboarding Screens (No Bottom Nav)
        composable(Screen.Welcome.route) { WelcomeScreen(navController) }
        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.Register.route) { RegisterScreen(navController) }

        composable(Screen.AgeRange.route) { backStackEntry ->
            val fullName = URLDecoder.decode(
                backStackEntry.arguments?.getString("fullName") ?: "",
                StandardCharsets.UTF_8.toString()
            )
            val email = URLDecoder.decode(
                backStackEntry.arguments?.getString("email") ?: "",
                StandardCharsets.UTF_8.toString()
            )
            val password = URLDecoder.decode(
                backStackEntry.arguments?.getString("password") ?: "",
                StandardCharsets.UTF_8.toString()
            )

            AgeRange(
                navController = navController,
                fullName = fullName,
                email = email,
                password = password
            )
        }

        composable(Screen.DailyUsage.route) { backStackEntry ->
            val fullName = URLDecoder.decode(
                backStackEntry.arguments?.getString("fullName") ?: "",
                StandardCharsets.UTF_8.toString()
            )
            val email = URLDecoder.decode(
                backStackEntry.arguments?.getString("email") ?: "",
                StandardCharsets.UTF_8.toString()
            )
            val password = URLDecoder.decode(
                backStackEntry.arguments?.getString("password") ?: "",
                StandardCharsets.UTF_8.toString()
            )
            val ageRange = URLDecoder.decode(
                backStackEntry.arguments?.getString("ageRange") ?: "",
                StandardCharsets.UTF_8.toString()
            )

            println("DEBUG: Received ageRange = '$ageRange'")

            DailyUsage(
                navController = navController,
                fullName = fullName,
                email = email,
                password = password,
                ageRange = ageRange
            )
        }

        composable(Screen.Main.route) {
            MainScreen()
        }

        // 🏠 Main App Screens (with Bottom Navigation elsewhere)
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.AllFoodsScreen.route) { AllFoodsScreen(navController) }
        composable(Screen.Setting.route) { SettingScreen() }

        // 🍱 Food-related Screens
        composable("AllFoods") { AllFoodsScreen(navController) }
        composable("addfood") { AddFoodScreen() }
    }
}