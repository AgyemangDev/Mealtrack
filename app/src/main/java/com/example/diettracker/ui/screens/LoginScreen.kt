package com.example.diettracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.diettracker.ui.components.buttons.CustomButton
import com.example.diettracker.ui.navigation.Screen
import kotlinx.coroutines.delay
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun LoginScreen(navController: NavController) {
    var isLoading by remember { mutableStateOf(false) }

    // Trigger side-effect when isLoading becomes true


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CustomButton(
            text = "Log in Successful",
            onClick = { isLoading = true } // just set state
        )
    }
}
