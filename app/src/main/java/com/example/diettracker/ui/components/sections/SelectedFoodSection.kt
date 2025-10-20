package com.example.diettracker.ui.components.sections

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.diettracker.models.FoodItem

@Composable
fun SelectedFoodSection(
    food: FoodItem,
    quantity: String,
    onQuantityChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Selected Food", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = food.imageUrl ?: "",
                contentDescription = food.name,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = food.name, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Quantity")
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = quantity,
                onValueChange = onQuantityChange,
                modifier = Modifier.width(100.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) { Text("g") }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(onClick = {}) { Text("cups") }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(onClick = {}) { Text("servings") }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Nutritional Breakdown", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Calories: ${food.calories} kcal")
            Text("Protein: ${food.protein} g")
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Carbs: ${food.carbs} g")
            Text("Fats: ${food.fats} g")
        }
    }
}
