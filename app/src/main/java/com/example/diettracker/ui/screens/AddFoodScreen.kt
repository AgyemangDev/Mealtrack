package com.example.diettracker.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.diettracker.models.Food
import com.example.diettracker.viewmodel.FoodViewModel
import kotlinx.coroutines.launch

// Sample data for searching
private val searchableFoods = listOf(
    Food(name="Broccoli Florets", calories=55.0, protein=4.0, carbs=11.0, fat=1.0, amount=100.0, unit="g"),
    Food(name="Brown Rice (cooked)", calories=110.0, protein=3.0, carbs=23.0, fat=1.0, amount=150.0, unit="g"),
    Food(name="Grilled Chicken Breast", calories=250.0, protein=30.0, carbs=0.0, fat=15.0, amount=100.0, unit="g"),
    Food(name="Apple", calories=95.0, protein=0.0, carbs=25.0, fat=0.0, amount=1.0, unit="unit"),
    Food(name="Lentil Soup", calories=320.0, protein=18.0, carbs=40.0, fat=8.0, amount=350.0, unit="ml"),
    Food(name="Avocado Toast", calories=280.0, protein=7.0, carbs=30.0, fat=15.0, amount=1.0, unit="slice"),
    Food(name="Salmon Fillet", calories=200.0, protein=22.0, carbs=0.0, fat=17.0, amount=100.0, unit="g"),
    Food(name="Banana", calories=105.0, protein=1.0, carbs=26.0, fat=0.0, amount=1.0, unit="g")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodScreen(foodViewModel: FoodViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFood by remember { mutableStateOf<Food?>(null) }
    var quantity by remember { mutableStateOf("150") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add Food") },
            )
        },
        bottomBar = {

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

            if (searchQuery.isNotBlank()) {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 200.dp)
                ) {
                    items(searchableFoods.filter { it.name.contains(searchQuery, ignoreCase = true) }) { food ->
                        FoodSearchItem(food = food) {
                            selectedFood = food
                            searchQuery = ""
                        }
                    }
                }
            }

            selectedFood?.let { food ->
                SelectedFoodSection(food, quantity) { quantity = it }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    selectedFood?.let { food ->
                        foodViewModel.addFood(food.copy(amount = quantity.toDoubleOrNull() ?: 0.0))
                        scope.launch {
                            snackbarHostState.showSnackbar("Food added successfully")
                        }
                        selectedFood = null // Clear selection
                    }
                 },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                enabled = selectedFood != null
            ) {
                Text("Add Food")
            }
        }
    }
}

@Composable
fun FoodSearchItem(food: Food, onFoodSelected: (Food) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onFoodSelected(food) }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = food.name, fontWeight = FontWeight.Bold)
                Text(
                    text = "${food.calories.toInt()} kcal | P: ${food.protein.toInt()}g C: ${food.carbs.toInt()}g F: ${food.fat.toInt()}g",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedFoodSection(food: Food, quantity: String, onQuantityChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text("Selected Food", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = food.name, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 16.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Quantity")
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = quantity,
                onValueChange = onQuantityChange,
                modifier = Modifier.width(100.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))) {
                Text("g")
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(onClick = {}) {
                Text("cups")
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(onClick = {}) {
                Text("servings")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Nutritional Breakdown")
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
            Text("Fats: ${food.fat} g")
        }
    }
}
