package com.example.diettracker.ui.components.dialogs

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController

@Composable
fun AccountCreatedDialog(
    navController: NavController,
    showDialog: Boolean,
    onNavigateToHome: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = {
                Text(
                    text = "Account Created",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("Your account has been created successfully!")
            },
            confirmButton = {
                Button(
                    onClick = onNavigateToHome,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4A7C59)
                    )
                ) {
                    Text("Go to Home")
                }
            }
        )
    }
}