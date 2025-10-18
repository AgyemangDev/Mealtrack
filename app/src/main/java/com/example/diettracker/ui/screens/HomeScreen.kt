package com.example.diettracker.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.activity.compose.BackHandler

@Composable
fun HomeScreen(){
    BackHandler(enabled = true) {
        // Do nothing — disables going back
    }
    Text("Home Screen")
}