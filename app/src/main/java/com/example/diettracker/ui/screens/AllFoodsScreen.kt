package com.example.diettracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.diettracker.models.Food
import com.example.diettracker.models.FoodItem
import com.example.diettracker.ui.components.cards.FoodItemCard
import com.example.diettracker.ui.components.headers.AppHeader
import com.example.diettracker.viewmodel.FoodViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun AllFoodsScreen(navController: NavHostController, foodViewModel: FoodViewModel = viewModel()) {
    val foodItems by foodViewModel.foodItems.collectAsState()
    var showEditDialog by remember { mutableStateOf<Food?>(null) }
    var showDeleteDialog by remember { mutableStateOf<Food?>(null) }

    val foodItemsByDate = foodItems.groupBy {
        SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(it.date)
    }

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
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(foods) { food ->
                    FoodItemCard(
                        food = FoodItem(
                            name = food.name,
                            unit = "${food.amount.toInt()} ${food.unit}",
                            calories = food.calories.toInt(),
                            protein = food.protein.toInt(),
                            carbs = food.carbs.toInt(),
                            fats = food.fat.toInt()
                        ),
                        onEdit = { showEditDialog = food },
                        onDelete = { showDeleteDialog = food }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        showEditDialog?.let { foodToEdit ->
            EditFoodDialog(
                food = foodToEdit,
                onDismiss = { showEditDialog = null },
                onConfirm = { updatedFood ->
                    foodViewModel.updateFood(foodToEdit, updatedFood)
                    showEditDialog = null
                }
            )
        }

        showDeleteDialog?.let { foodToDelete ->
            DeleteConfirmationDialog(
                food = foodToDelete,
                onDismiss = { showDeleteDialog = null },
                onConfirm = {
                    foodViewModel.deleteFood(foodToDelete)
                    showDeleteDialog = null
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFoodDialog(food: Food, onDismiss: () -> Unit, onConfirm: (Food) -> Unit) {
    val baseCalories = if (food.amount > 0) food.calories / food.amount else 0.0
    val baseProtein = if (food.amount > 0) food.protein / food.amount else 0.0
    val baseCarbs = if (food.amount > 0) food.carbs / food.amount else 0.0
    val baseFat = if (food.amount > 0) food.fat / food.amount else 0.0

    var newAmountStr by remember { mutableStateOf(food.amount.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Food") },
        text = {
            Column {
                Text("Editing: ${food.name}", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))
                TextField(
                    value = newAmountStr,
                    onValueChange = { newAmountStr = it },
                    label = { Text("Quantity (${food.unit})") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val newAmount = newAmountStr.toDoubleOrNull() ?: food.amount
                val updatedFood = food.copy(
                    amount = newAmount,
                    calories = baseCalories * newAmount,
                    protein = baseProtein * newAmount,
                    carbs = baseCarbs * newAmount,
                    fat = baseFat * newAmount
                )
                onConfirm(updatedFood)
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun DeleteConfirmationDialog(food: Food, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Food") },
        text = { Text("Are you sure you want to delete ${food.name}?") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Delete")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview
@Composable
fun AllFoodsScreenPreview() {
    AllFoodsScreen(navController = rememberNavController())
}
