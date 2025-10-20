package com.example.diettracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.diettracker.models.FoodItem
import com.example.diettracker.models.toFoodItem
import com.example.diettracker.ui.components.headers.AppHeader
import com.example.diettracker.ui.components.inputs.FoodSearchBar
import com.example.diettracker.ui.components.lists.FoodList
import com.example.diettracker.ui.components.sections.SelectedFoodSection
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.navigation.NavController
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch
import com.example.diettracker.Interfaces.ApiClient
import com.example.diettracker.ui.components.buttons.CustomButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodScreen(navController: NavController? = null) {
    var searchQuery by remember { mutableStateOf("") }
    var foodList by remember { mutableStateOf(listOf<FoodItem>()) }
    var selectedFood by remember { mutableStateOf<FoodItem?>(null) }
    var quantity by remember { mutableStateOf("150") }

    val scope = rememberCoroutineScope()

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Scrollable content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 80.dp) // Add bottom padding to avoid overlap with button
            ) {
                AppHeader(
                    title = "Add Food",
                    onBackClick = { navController?.popBackStack() }
                )

                Spacer(modifier = Modifier.height(16.dp))

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

                Spacer(modifier = Modifier.height(16.dp))

                FoodList(
                    foods = foodList,
                    onFoodSelected = { selectedFood = it }
                )

                selectedFood?.let {
                    SelectedFoodSection(it, quantity) { quantity = it }
                }
            }

            // Fixed bottom button
            CustomButton(
                text = "Add Food",
                onClick = { /* TODO: Add food logic */ },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddFoodScreenPreview() {
    AddFoodScreen()
}
