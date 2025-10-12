package com.example.diettracker.ui.components.buttons

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CustomButton(
    text: String,
    onClick: suspend () -> Unit,
    icon: ImageVector? = null,
    spinnerDelay: Long = 200L,
    backgroundColor: Color = Color(0xFF00A86B),  // default green
    contentColor: Color = Color.White             // default white for text & icon
) {
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Button(
        onClick = {
            scope.launch {
                var showSpinner = false
                val spinnerJob = launch {
                    delay(spinnerDelay)
                    showSpinner = true
                    isLoading = true
                }

                // Run the actual operation
                onClick()

                // Cancel spinner if function finished early
                spinnerJob.cancel()
                if (showSpinner) {
                    isLoading = false
                }
            }
        },
        enabled = !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,   // background
            contentColor = contentColor         // icon/text color
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                strokeWidth = 2.dp,
                modifier = Modifier.size(24.dp),
                color = contentColor
            )
        } else {
            Row {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = contentColor
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                }
                Text(
                    text = text,
                    color = contentColor
                )
            }
        }
    }
}
