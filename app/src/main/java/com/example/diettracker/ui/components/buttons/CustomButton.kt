package com.example.diettracker.ui.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    icon: ImageVector? = null,
    spinnerDelay: Long = 0L,
    backgroundColor: Color = Color(0xFF668405),
    contentColor: Color = Color.White,
    modifier: Modifier = Modifier
) {
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val isInverted = contentColor != Color.White
    val actualBackground = if (isInverted) Color.White else backgroundColor
    val actualContent = if (isInverted) contentColor else Color.White
    val borderStroke = if (isInverted) BorderStroke(2.dp, contentColor) else null

    Button(
        onClick = {
            if (!isLoading) {
                scope.launch {
                    isLoading = true
                    delay(spinnerDelay)
                    onClick()
                    isLoading = false
                }
            }
        },
        enabled = !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = actualBackground,
            contentColor = actualContent
        ),
        border = borderStroke,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                strokeWidth = 2.dp,
                modifier = Modifier.size(24.dp),
                color = actualContent
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = actualContent
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text,
                    color = actualContent,
                    fontSize = 18.sp,
                )
            }
        }
    }
}
