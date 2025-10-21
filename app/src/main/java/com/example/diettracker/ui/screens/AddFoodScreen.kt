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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.example.diettracker.Interfaces.ApiClient
import com.example.diettracker.models.FoodItem
import com.example.diettracker.models.toFoodItem
import com.example.diettracker.ui.components.headers.AppHeader
import com.example.diettracker.ui.components.inputs.FoodSearchBar
import com.example.diettracker.ui.components.lists.FoodListItem
import com.example.diettracker.ui.components.buttons.CustomButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodScreen(navController: NavController? = null) {
    var searchQuery by remember { mutableStateOf("") }
    var foodList by remember { mutableStateOf(listOf<FoodItem>()) }
    var selectedFood by remember { mutableStateOf<FoodItem?>(null) }
    var quantity by remember { mutableStateOf("150") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            AppHeader(
                title = "Add Food",
                onBackClick = { navController?.popBackStack() }
            )
        },
        bottomBar = {
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
            // 1️⃣ Search bar at top
            item {
                FoodSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearchClick = {
                        println("🔍 Search clicked with query: '$searchQuery'")

                        if (searchQuery.isBlank()) {
                            errorMessage = "Please enter a search term"
                            println("❌ Empty search query")
                            return@FoodSearchBar
                        }

                        scope.launch {
                            isLoading = true
                            errorMessage = null
                            foodList = emptyList()

                            try {
                                println("📡 Making API call...")
                                val response = ApiClient.api.searchRecipes(
                                    query = searchQuery,
                                    number = 6,
                                    addNutrition = true,
                                    apiKey = "3a69df17a1e94aae83558dcfd1afef0f"
                                )

                                println("✅ API Response received")
                                println("📊 Total results: ${response.results.size}")

                                // Log each result
                                response.results.forEachIndexed { index, result ->
                                    println("  Item $index: ${result.title}")
                                }

                                // Convert to FoodItem
                                foodList = response.results.map {
                                    val foodItem = it.toFoodItem()
                                    println("🍎 Converted: ${foodItem.name} - ${foodItem.calories} kcal")
                                    foodItem
                                }

                                println("✨ Final foodList size: ${foodList.size}")

                                if (foodList.isEmpty()) {
                                    errorMessage = "No results found for '$searchQuery'"
                                    println("⚠️ No results after conversion")
                                }

                            } catch (e: Exception) {
                                errorMessage = "Error: ${e.message}"
                                println("❌ Exception occurred: ${e.message}")
                                e.printStackTrace()
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    searchIcon = Icons.Default.Search
                )
            }

            // 2️⃣ Loading indicator
            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            // 3️⃣ Error message
            if (errorMessage != null) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFEBEE)
                        )
                    ) {
                        Text(
                            text = errorMessage ?: "",
                            color = Color(0xFFC62828),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            // 4️⃣ Debug info
            if (!isLoading) {
                item {
                    Text(
                        text = "Results: ${foodList.size} items",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

            // 5️⃣ Display fetched foods dynamically
            items(foodList) { food ->
                println("🎨 Rendering FoodListItem: ${food.name}")
                FoodListItem(food = food, onFoodSelected = { selectedFood = it })
            }

            // 6️⃣ Spacer so content doesn't clash with bottom button
            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}