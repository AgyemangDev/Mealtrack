package com.example.diettracker.ui.navigation

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")

    object AgeRange : Screen("agerange/{fullName}/{email}/{password}") {

        fun createRoute(fullName: String, email: String, password: String) =
            "agerange/$fullName/$email/$password"
    }

    object DailyUsage : Screen("dailyusage/{fullName}/{email}/{password}/{ageRange}") {
        fun createRoute(fullName: String, email: String, password: String, ageRange: String) =
            "dailyusage/$fullName/$email/$password/$ageRange"
    }


    object Main : Screen("main")


    object Home : Screen("home")
    object AllFoodsScreen : Screen("allfood")
    object AddFood : Screen("addfood")
    object LogMeal : Screen("logmeal")

    object Setting : Screen("setting")
}