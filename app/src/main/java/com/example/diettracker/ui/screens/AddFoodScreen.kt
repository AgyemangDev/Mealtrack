package com.example.diettracker.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

// Sample food data
val foodItems = listOf(
    FoodItem(name = "Broccoli Florets", imageUrl = R.drawable.ic_launcher_foreground, calories = 55, protein = 4, carbs = 11, fats = 1),
    FoodItem(name = "Brown Rice (cooked)", imageUrl = R.drawable.ic_launcher_foreground, calories = 110, protein = 3, carbs = 23, fats = 1),
    FoodItem(name = "Grilled Chicken Breast", imageUrl = R.drawable.ic_launcher_foreground, calories = 250, protein = 30, carbs = 0, fats = 15)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFood by remember { mutableStateOf<FoodItem?>(null) }
    var quantity by remember { mutableStateOf("150") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add Food") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search for a food...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(foodItems.filter { it.name.contains(searchQuery, ignoreCase = true) }) { food ->
                    FoodListItem(food) { selectedFood = it }
                }
            }

            selectedFood?.let { food ->
                SelectedFoodSection(food, quantity) { quantity = it }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* TODO: Add food logic */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Add Food")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodListItem(food: FoodItem, onFoodSelected: (FoodItem) -> Unit) {
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
                modifier = Modifier.size(50.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = food.name, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                Text(
                    text = "${food.calories} kcal ${food.protein}g P ${food.carbs}g C ${food.fats}g F",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedFoodSection(food: FoodItem, quantity: String, onQuantityChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Selected Food", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = food.imageUrl ?: R.drawable.ic_launcher_foreground),
                contentDescription = food.name,
                modifier = Modifier.size(50.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = food.name, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Quantity")
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(value = quantity, onValueChange = onQuantityChange, modifier = Modifier.width(100.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))) { Text("g") }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(onClick = {}) { Text("cups") }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(onClick = {}) { Text("servings") }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Nutritional Breakdown")
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Calories: ${food.calories} kcal")
            Text("Protein: ${food.protein} g")
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Carbs: ${food.carbs} g")
            Text("Fats: ${food.fats} g")
        }
    }
}
