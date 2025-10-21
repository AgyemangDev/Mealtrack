package com.example.diettracker.ui.empty

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.example.diettracker.R

@Composable
fun EmptyState(
    message: String = "No items found",
    imageRes: Int = R.drawable.empty
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Empty State Image",
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .aspectRatio(1f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
    }
}
