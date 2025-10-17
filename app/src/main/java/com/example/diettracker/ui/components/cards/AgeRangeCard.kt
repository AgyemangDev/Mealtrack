package com.example.diettracker.ui.components.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diettracker.data.AgeRangeInfo

@Composable
fun AgeRangeCard(
    data: AgeRangeInfo,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // Determine icon based on label or type
    val icon = when (data.label) {
        "13–18" -> Icons.Default.LocalPizza       // Example: 🍕 for teens
        "19–30" -> Icons.Default.SetMeal         // Example: balanced meal
        "31–50" -> Icons.Default.RamenDining     // Example: general nutrition
        "51+" -> Icons.Default.LocalDining       // Example: softer meals
        else -> Icons.Default.Restaurant
    }

    val iconTint = if (isSelected) Color.White else Color.Gray

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF4CAF50) else Color.White
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {

                Column {
                    Text(
                        text = data.label,
                        fontSize = 18.sp,
                        color = if (isSelected) Color.White else Color.Black,
                        style = if (isSelected) MaterialTheme.typography.titleMedium else MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = data.description,
                        fontSize = 14.sp,
                        color = if (isSelected) Color(0xFFE8F5E9) else Color.Gray
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Next",
                tint = iconTint
            )
        }
    }
}
