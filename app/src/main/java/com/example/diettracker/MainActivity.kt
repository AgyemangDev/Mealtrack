package com.example.diettracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.diettracker.ui.theme.DietTrackerTheme
import com.example.diettracker.ui.navigation.AppNavGraph


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DietTrackerTheme {
                AppNavGraph()
            }
        }
    }
}