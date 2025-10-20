package com.example.diettracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.diettracker.models.FoodItem
import com.example.diettracker.ui.components.headers.AppHeader
import com.example.diettracker.ui.components.inputs.FoodSearchBar
import com.example.diettracker.ui.components.lists.FoodList
import com.example.diettracker.ui.components.sections.SelectedFoodSection
import com.example.diettracker.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.navigation.NavController

// Sample data for preview and testing
val foodItemsSample = listOf(
    FoodItem(
        name = "Broccoli Florets",
        imageUrl = R.drawable.ic_launcher_foreground,
        calories = 55,
        protein = 4,
        carbs = 11,
        fats = 1
    ),
    FoodItem(
        name = "Brown Rice (cooked)",
        imageUrl = R.drawable.ic_launcher_foreground,
        calories = 110,
        protein = 3,
        carbs = 23,
        fats = 1
    ),
    FoodItem(
        name = "Grilled Chicken Breast",

        imageUrl = R.drawable.ic_launcher_foreground,
        calories = 250,
        protein = 30,
        carbs = 0,
        fats = 15
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodScreen(navController: NavController? = null) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFood by remember { mutableStateOf<FoodItem?>(null) }
    var quantity by remember { mutableStateOf("150") }
    var foodList by remember { mutableStateOf(foodItemsSample) }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AppHeader(
                title = "Add Food",
                onBackClick = { navController?.popBackStack() } // safe call
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                FoodSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearchClick = { /* TODO */ },
                    searchIcon = Icons.Default.Search
                )

                Spacer(modifier = Modifier.height(16.dp))

                FoodList(
                    foods = foodList.filter { it.name.contains(searchQuery, ignoreCase = true) },
                    onFoodSelected = { selectedFood = it }
                )

                selectedFood?.let {
                    SelectedFoodSection(it, quantity) { quantity = it }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Add Food")
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddFoodScreenPreview() {
    androidx.compose.material3.MaterialTheme {
        AddFoodScreen()
    }
}
