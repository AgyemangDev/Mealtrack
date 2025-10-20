package com.example.diettracker.ui.components.lists

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diettracker.R
import com.example.diettracker.models.FoodItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodListItem(
    food: FoodItem,
    onFoodSelected: (FoodItem) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = { onFoodSelected(food) }
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = food.imageUrl ?: R.drawable.ic_launcher_foreground),
                contentDescription = food.name,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = food.name, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                Text(
                    text = "${food.calories} kcal • ${food.protein}g P • ${food.carbs}g C • ${food.fats}g F",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun FoodList(
    foods: List<FoodItem>,
    onFoodSelected: (FoodItem) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        items(foods) { food ->
            FoodListItem(food, onFoodSelected)
        }
    }
}
