package com.example.diettracker.ui.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diettracker.models.FoodItem

@Composable
fun FoodItemCard(
    food: FoodItem,
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 🔹 Header Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = food.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = food.unit ?: "",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF999999),
                            fontSize = 13.sp
                        )
                    )
                }

                // 🔹 Action Buttons
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFE8F5E9), CircleShape),
                        colors = IconButtonDefaults.iconButtonColors(contentColor = Color(0xFF4CAF50))
                    ) {
                        Icon(Icons.Outlined.Edit, contentDescription = "Edit", modifier = Modifier.size(18.dp))
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFFFEBEE), CircleShape),
                        colors = IconButtonDefaults.iconButtonColors(contentColor = Color(0xFFF44336))
                    ) {
                        Icon(Icons.Outlined.Delete, contentDescription = "Delete", modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Row 1: Calories + Protein
// 🔹 Row 1: Calories + Protein
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NutrientInfoInline("Calories", food.calories, "kcal", Modifier.weight(1f))
                Spacer(modifier = Modifier.width(16.dp)) // 👈 small gap between left & right
                NutrientInfoInline("Protein", food.protein, "g", Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(8.dp))

// 🔹 Row 2: Carbs + Fats
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NutrientInfoInline("Carbs", food.carbs, "g", Modifier.weight(1f))
                Spacer(modifier = Modifier.width(16.dp)) // 👈 same gap between left & right
                NutrientInfoInline("Fats", food.fats, "g", Modifier.weight(1f))
            }

        }
    }
}

@Composable
fun NutrientInfoInline(
    name: String,
    value: Int,
    unit: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color(0xFF555555),
                fontSize = 13.sp
            )
        )
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                ) { append(value.toString()) }
                withStyle(
                    style = SpanStyle(
                        fontSize = 12.sp,
                        color = Color(0xFF777777)
                    )
                ) { append(" $unit") }
            }
        )
    }
}

