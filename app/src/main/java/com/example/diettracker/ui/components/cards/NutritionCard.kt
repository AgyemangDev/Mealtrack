package com.example.diettracker.ui.components.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diettracker.data.NutrientDetail

@Composable
fun NutrientCard(
    title: String,
    nutrientDetail: NutrientDetail,
    backgroundColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Title and Value Row
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = nutrientDetail.icon,
                        fontSize = 24.sp // professional icon size
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black,
                        fontSize = 20.sp // professional title size
                    )
                }
                Text(
                    text = nutrientDetail.value,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF4A7C59),
                    fontSize = 18.sp // professional value size
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Examples Section: inline separated by a dot
            if (nutrientDetail.examples.isNotEmpty()) {
                Text(
                    text = nutrientDetail.examples.joinToString(" • "),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF616161),
                    fontSize = 16.sp,
                    lineHeight = 22.sp // better readability
                )
            }
        }
    }
}
