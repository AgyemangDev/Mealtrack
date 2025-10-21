package com.example.diettracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.diettracker.ui.empty.EmptyState
import com.example.diettracker.ui.components.cards.FoodItemCard
import com.example.diettracker.ui.components.headers.AppHeader
import com.example.diettracker.R
import com.example.diettracker.models.DietViewModel

@Composable
fun AllFoodsScreen(
    navController: NavHostController,
    viewModel: DietViewModel = viewModel()
) {
    val days by viewModel.days.collectAsState()

    Scaffold(
        topBar = { AppHeader(title = "All Foods", onBackClick = { navController.popBackStack() }) }
    ) { paddingValues ->
        if (days.isEmpty()) {
            EmptyState(
                message = "No foods added yet!",
                imageRes = R.drawable.empty
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                days.forEach { day ->
                    item {
                        Text(
                            text = day.date,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    day.meals.forEach { meal ->
                        item {
                            Text(
                                text = meal.type.replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
                            )
                        }
                        items(meal.foods) { food ->
                            FoodItemCard(
                                food = food,

                                onDelete = { }
                            )
                        }
                    }
                }
            }
        }
    }
}
