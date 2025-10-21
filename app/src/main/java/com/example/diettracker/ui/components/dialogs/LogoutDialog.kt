package com.example.diettracker.ui.components.dialogs
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LogoutDialog(
    isLoggingOut: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { if (!isLoggingOut) onDismiss() },
        title = { Text("Logout") },
        text = {
            if (isLoggingOut) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Logging out...")
                }
            } else {
                Text("Are you sure you want to log out?")
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isLoggingOut,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Logout")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isLoggingOut
            ) {
                Text("Cancel")
            }
        }
    )
}