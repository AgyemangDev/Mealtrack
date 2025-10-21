package com.example.diettracker.ui.components.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


@Composable
fun LogoutDialog(
    onDismiss: () -> Unit,
    onLogoutComplete: () -> Unit
) {
    var loggingOut by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = { if (!loggingOut) onDismiss() },
        title = { Text("Logout") },
        text = {
            if (loggingOut) {
                Row(verticalAlignment = Alignment.CenterVertically) {
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
                onClick = {
                    loggingOut = true
                    scope.launch {
                        FirebaseAuth.getInstance().signOut()
                        loggingOut = false
                        onLogoutComplete()
                    }
                },
                enabled = !loggingOut,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Logout")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !loggingOut) {
                Text("Cancel")
            }
        }
    )
}
