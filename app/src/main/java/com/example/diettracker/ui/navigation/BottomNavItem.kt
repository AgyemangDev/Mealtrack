package com.example.diettracker.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )

    object Nutrients : BottomNavItem(
        route = "nutrients",
        title = "Nutrients",
        icon = Icons.Default.Favorite
    )

    object Foods : BottomNavItem(
        route = "foods",
        title = "Foods",
        icon = Icons.Default.Restaurant
    )

    object AddFood : BottomNavItem(
        route = "add_food",
        title = "Add Food",
        icon = Icons.Default.Add
    )

    object Profile : BottomNavItem(
        route = "profile",
        title = "Profile",
        icon = Icons.Default.Person
    )
}