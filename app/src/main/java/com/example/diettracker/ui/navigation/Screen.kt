package com.example.diettracker.ui.navigation

sealed class Screen(val route: String) {
    // Authentication & Onboarding routes
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object AgeRange : Screen("ageRange/{fullName}/{email}/{password}") {
        fun createRoute(fullName: String, email: String, password: String) =
            "ageRange/$fullName/$email/$password"
    }
    object DailyUsage : Screen("dailyUsage/{fullName}/{email}/{password}/{ageRange}") {
        fun createRoute(fullName: String, email: String, password: String, ageRange: String) =
            "dailyUsage/$fullName/$email/$password/$ageRange"
    }


    object Main : Screen("main")
}