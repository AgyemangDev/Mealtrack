package com.example.diettracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.diettracker.models.FoodItem
import com.example.diettracker.ui.components.cards.FoodItemCard
import com.example.diettracker.ui.components.headers.AppHeader

@Composable
fun AllFoodsScreen(navController: NavHostController) {
    val foodItemsByDate = mapOf(
        "Today" to listOf(
            FoodItem(name = "Apple", unit = "1 unit", calories = 95, protein = 0, carbs = 25, fats = 0)
        ),
        "October 18, 2023" to listOf(
            FoodItem(name = "Lentil Soup", unit = "350ml", calories = 320, protein = 18, carbs = 40, fats = 8),
            FoodItem(name = "Avocado Toast", unit = "1 slice", calories = 280, protein = 7, carbs = 30, fats = 15)
        )
    )

    Scaffold(
        topBar = {
            AppHeader(
                title = "All Foods",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }
            foodItemsByDate.forEach { (date, foods) ->
                item {
                    Text(
                        text = date,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(foods) { food ->
                    FoodItemCard(food)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Preview
@Composable
fun AllFoodsScreenPreview() {
    AllFoodsScreen(navController = rememberNavController())
}
