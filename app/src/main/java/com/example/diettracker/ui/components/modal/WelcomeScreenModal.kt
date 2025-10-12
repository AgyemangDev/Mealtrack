package com.example.diettracker.ui.components.modal

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.diettracker.ui.components.buttons.CustomButton
import com.example.diettracker.ui.navigation.Screen

@Composable
fun WelcomeScreenModal(navController: NavController) {
    CustomButton(
        text = "Login To Account",
        icon = Icons.Default.Mail,
        onClick = {
            navController.navigate(Screen.Login.route)
        }
    )
}
