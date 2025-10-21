package com.example.diettracker.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.example.diettracker.Interfaces.ApiClient
import com.example.diettracker.R
import com.example.diettracker.models.FoodItem
import com.example.diettracker.models.toFoodItem
import com.example.diettracker.ui.components.headers.AppHeader
import com.example.diettracker.ui.components.inputs.FoodSearchBar
import com.example.diettracker.ui.components.lists.FoodListItem
import com.example.diettracker.ui.components.buttons.CustomButton
import com.example.diettracker.data.DietRepository
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodScreen(navController: NavController? = null) {
    var searchQuery by remember { mutableStateOf("") }
    var foodList by remember { mutableStateOf(listOf<FoodItem>()) }
    var selectedFood by remember { mutableStateOf<FoodItem?>(null) }
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
                    text = if (isLoading) "Adding..." else "Add Food",
                    onClick = {
                        scope.launch {
                            val userId = FirebaseAuth.getInstance().currentUser?.uid

                            if (userId == null) {
                                errorMessage = "⚠️ You must be signed in to add food."
                                return@launch
                            }

                            if (selectedFood == null) {
                                errorMessage = "⚠️ Please select a food to add."
                                return@launch
                            }

                            try {
                                isLoading = true
                                errorMessage = null

                                DietRepository.addFoodForUser(
                                    userId = userId,
                                    food = selectedFood!!
                                )

                                errorMessage = "✅ Food successfully added!"
                                selectedFood = null
                            } catch (e: Exception) {
                                errorMessage = "❌ Failed to add food: ${e.message}"
                                e.printStackTrace()
                            } finally {
                                isLoading = false
                            }
                        }
                    },
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
            // 🔍 Search bar
            item {
                FoodSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearchClick = {
                        if (searchQuery.isBlank()) {
                            errorMessage = "⚠️ Please enter a search term."
                            return@FoodSearchBar
                        }

                        scope.launch {
                            isLoading = true
                            errorMessage = null
                            foodList = emptyList()

                            try {
                                val response = ApiClient.api.searchRecipes(
                                    query = searchQuery,
                                    number = 3,
                                    addNutrition = true,
                                    apiKey = "3a69df17a1e94aae83558dcfd1afef0f"
                                )

                                foodList = response.results.map { it.toFoodItem() }

                                if (foodList.isEmpty()) {
                                    errorMessage = "⚠️ No results found for '$searchQuery'."
                                }
                            } catch (e: Exception) {
                                errorMessage = "❌ Error: ${e.message}"
                                e.printStackTrace()
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    searchIcon = Icons.Default.Search
                )
            }

            // ⏳ Loading indicator
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

            // ⚠️ Error or success message
            if (errorMessage != null) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (errorMessage!!.startsWith("✅")) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                        )
                    ) {
                        Text(
                            text = errorMessage ?: "",
                            color = if (errorMessage!!.startsWith("✅")) Color(0xFF2E7D32) else Color(0xFFC62828),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            // 🧾 Search results or placeholder
            if (foodList.isEmpty() && !isLoading) {
                item { NoFoodPlaceholder() }
            } else {
                items(foodList) { food ->
                    FoodListItem(
                        food = food,
                        isSelected = selectedFood == food,
                        onFoodSelected = { selectedFood = it }
                    )
                }
            }

            // Extra space for bottom button
            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}

@Composable
fun NoFoodPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()  // take all available vertical space
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.TopCenter // start from top and move down manually
    ) {
        Column(
            modifier = Modifier
                .padding(top = 120.dp), // push it down from the top
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.eat),
                contentDescription = "Empty plate",
                modifier = Modifier
                    .size(200.dp) // increased size
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "What are you eating today?",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray
            )
        }
    }
}
