package com.example.diettracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.example.diettracker.Interfaces.ApiClient
import com.example.diettracker.models.FoodItem
import com.example.diettracker.models.toFoodItem
import com.example.diettracker.ui.components.headers.AppHeader
import com.example.diettracker.ui.components.inputs.FoodSearchBar
import com.example.diettracker.ui.components.lists.FoodList
import com.example.diettracker.ui.components.sections.SelectedFoodSection
import com.example.diettracker.ui.components.buttons.CustomButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodScreen(navController: NavController? = null) {
    var searchQuery by remember { mutableStateOf("") }
    var foodList by remember { mutableStateOf(listOf<FoodItem>()) }
    var selectedFood by remember { mutableStateOf<FoodItem?>(null) }
    var quantity by remember { mutableStateOf("150") }

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            AppHeader(
                title = "Add Food",
                onBackClick = { navController?.popBackStack() }
            )
        },
        bottomBar = {
            // persistent button at bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CustomButton(
                    text = "Add Food",
                    onClick = { /* TODO: Integrate Add Food logic */ },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Search bar at top of scrollable content
            item {
                FoodSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearchClick = {
                        scope.launch {
                            try {
                                val response = ApiClient.api.searchRecipes(
                                    query = searchQuery,
                                    number = 6,
                                    addNutrition = true,
                                    apiKey = "3a69df17a1e94aae83558dcfd1afef0f"
                                )
                                foodList = response.results.map { it.toFoodItem() }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    },
                    searchIcon = Icons.Default.Search
                )
            }

            // Display fetched foods
            items(foodList) { food ->
                FoodList(
                    foods = listOf(food),
                    onFoodSelected = { selectedFood = it }
                )
            }

            // Show selected food section if applicable


            // Padding space so content doesn’t clash with bottom button
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddFoodScreenPreview() {
    AddFoodScreen()
}
