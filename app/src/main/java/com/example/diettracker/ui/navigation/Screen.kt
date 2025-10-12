package com.example.diettracker.ui.navigation

sealed class Screen(val route:String){
    object Welcome: Screen("welcome")
    object Login : Screen("login")
    object Register: Screen("register")
    object Home: Screen("home")
    object Progress: Screen("progress")
    object LogMeal: Screen("logmeal")
    object Setting: Screen("setting")
}