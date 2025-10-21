
package com.example.diettracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.diettracker.data.UserPreferencesManager
import com.example.diettracker.ui.components.buttons.AddMealFab
import com.example.diettracker.ui.components.cards.CalorieProgressCard
import com.example.diettracker.ui.components.cards.GreetingCard
import com.example.diettracker.ui.components.dialogs.MealDialog
import com.example.diettracker.ui.components.cards.MealInfo
import com.example.diettracker.ui.components.cards.NutrientsPreviewSection
import com.example.diettracker.ui.components.cards.TodaysFoodsSection
import java.text.SimpleDateFormat
import java.util.*
import com.example.diettracker.ui.components.headers.AppHeader

@Composable
fun HomeScreen(
    meals: MutableList<MealInfo>,
    onNavigateToAllNutrients: () -> Unit = {}
) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferencesManager(context) }
    val userName by userPreferences.userName.collectAsState(initial = "User")

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // ✅ Fixed Header at top
            AppHeader(title = "NutriTrack")

            // ✅ Scrollable content beneath header
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                GreetingCard(
                    greeting = greeting,
                    userName = userName,
                    currentDateTime = currentDateTime
                )

                CalorieProgressCard(
                    totalCalories = totalCalories,
                    calorieGoal = calorieGoal
                )

                NutrientsPreviewSection(
                    totalProtein = totalProtein,
                    totalCarbs = totalCarbs,
                    onNavigateToAllNutrients = onNavigateToAllNutrients
                )

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
        }

        // Floating Action Button on top
        AddMealFab(
            onClick = {
                editingMealIndex = null
                showMealDialog = true
            }
        )
    }

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

