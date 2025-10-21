package com.example.diettracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.diettracker.ui.screens.*

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }

        composable(Screen.Welcome.route) {
            WelcomeScreen(navController)
        }

        composable(Screen.Login.route) {
            LoginScreen(navController)
        }

        composable(Screen.Register.route) {
            RegisterScreen(navController)
        }

        composable(
            route = Screen.AgeRange.route,
            arguments = listOf(
                navArgument("fullName") { type = NavType.StringType },
                navArgument("email") { type = NavType.StringType },
                navArgument("password") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val fullName = backStackEntry.arguments?.getString("fullName") ?: ""
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val password = backStackEntry.arguments?.getString("password") ?: ""
            AgeRange(navController, fullName, email, password)
        }

        composable(
            route = Screen.DailyUsage.route,
            arguments = listOf(
                navArgument("fullName") { type = NavType.StringType },
                navArgument("email") { type = NavType.StringType },
                navArgument("password") { type = NavType.StringType },
                navArgument("ageRange") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val fullName = backStackEntry.arguments?.getString("fullName") ?: ""
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val password = backStackEntry.arguments?.getString("password") ?: ""
            val ageRange = backStackEntry.arguments?.getString("ageRange") ?: ""
            DailyUsage(navController, fullName, email, password, ageRange)
        }

        // ✅ Navigate to the bottom navigation container after login
        composable(Screen.Main.route) {
            MainScreen()
        }
    }
}
