package com.example.diettracker.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(
    text: String,
    loading: Boolean = false,
    onClick: () -> Unit,
    icon: ImageVector? = null // ✅ icon is optional
) {
    Button(onClick = onClick, enabled = !loading) {
        if (loading) {
            // Show loading spinner
            Box(modifier = Modifier.size(24.dp)) {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(24.dp)
                )
            }
        } else {
            Row {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                }
                Text(text = text)
            }
        }
    }
}
