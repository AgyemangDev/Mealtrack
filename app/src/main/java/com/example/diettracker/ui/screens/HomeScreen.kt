package com.example.diettracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diettracker.ui.components.buttons.AddMealFab
import com.example.diettracker.ui.components.cards.CalorieProgressCard
import com.example.diettracker.ui.components.cards.GreetingCard
import com.example.diettracker.ui.components.dialogs.MealDialog
import com.example.diettracker.ui.components.cards.MealInfo
import com.example.diettracker.ui.components.cards.NutrientsPreviewSection
import com.example.diettracker.ui.components.cards.TodaysFoodsSection
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    meals: MutableList<MealInfo>,
    onNavigateToAllNutrients: () -> Unit = {}
) {
    var showMealDialog by remember { mutableStateOf(false) }
    var editingMealIndex by remember { mutableStateOf<Int?>(null) }

    val totalCalories = meals.sumOf { it.calories }
    val totalProtein = meals.sumOf { it.protein }
    val totalCarbs = meals.sumOf { it.carbs }
    val totalFat = meals.sumOf { it.fat }
    val totalIron = meals.sumOf { it.iron }
    val totalCalcium = meals.sumOf { it.calcium }
    val totalVitamins = meals.sumOf { it.vitamins }
    val calorieGoal = 2000

    val currentDateTime = remember {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEEE, MMM dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val date = dateFormat.format(calendar.time)
        val time = timeFormat.format(calendar.time)
        "$date • $time"
    }

    val greeting = remember {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when (hour) {
            in 0..11 -> "Good morning"
            in 12..16 -> "Good afternoon"
            else -> "Good evening"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "NutriTrack",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Greeting Card
        GreetingCard(
            greeting = greeting,
            userName = "John",
            currentDateTime = currentDateTime
        )

        // Calorie Progress Card
        CalorieProgressCard(
            totalCalories = totalCalories,
            calorieGoal = calorieGoal
        )

        // Nutrients Preview Section
        NutrientsPreviewSection(
            totalProtein = totalProtein,
            totalCarbs = totalCarbs,
            onNavigateToAllNutrients = onNavigateToAllNutrients
        )

        // Today's Foods Section
        TodaysFoodsSection(
            meals = meals,
            onEditMeal = { index ->
                editingMealIndex = index
                showMealDialog = true
            },
            onDeleteMeal = { index ->
                meals.removeAt(index)
            }
        )

        Spacer(modifier = Modifier.height(80.dp))
    }

    // Floating Action Button
    AddMealFab(
        onClick = {
            editingMealIndex = null
            showMealDialog = true
        }
    )

    // Meal Dialog
    if (showMealDialog) {
        MealDialog(
            meal = editingMealIndex?.let { meals[it] },
            onDismiss = {
                showMealDialog = false
                editingMealIndex = null
            },
            onSave = { mealInfo ->
                if (editingMealIndex != null) {
                    meals[editingMealIndex!!] = mealInfo
                } else {
                    meals.add(mealInfo)
                }
                showMealDialog = false
                editingMealIndex = null
            }
        )
    }
}