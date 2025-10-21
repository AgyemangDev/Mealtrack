package com.example.diettracker.ui.screens

import CalorieProgressCard
import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.diettracker.data.ageRangeData
import com.example.diettracker.models.DietViewModel
import com.example.diettracker.models.FoodItem
import com.example.diettracker.models.UserViewModel
import com.example.diettracker.ui.components.buttons.AddMealFab
import com.example.diettracker.ui.components.cards.*
import com.example.diettracker.ui.components.headers.AppHeader
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("UnrememberedMutableState")
@Composable
fun HomeScreen(
    dietViewModel: DietViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(),
    onNavigateToAllNutrients: () -> Unit = {}
) {
    BackHandler(enabled = true) {}

    // --- User Info ---
    val user by userViewModel.user.collectAsState()
    val userFirstName =
        user?.fullName?.split(" ")?.firstOrNull()?.replaceFirstChar { it.uppercase() } ?: "User"

    // --- Nutrient Goals ---
    val matchedAgeData = ageRangeData.find { it.range == user?.ageRange } ?: ageRangeData[1]
    val calorieGoal = matchedAgeData.calories
    val proteinGoal = matchedAgeData.nutrients.protein.value.removeSuffix("g").toInt()
    val carbsGoal = matchedAgeData.nutrients.carbohydrates.value.removeSuffix("g").toInt()
    val fatsGoal = matchedAgeData.nutrients.fats.value.removeSuffix("g").toInt()

    // --- Observe Diet Days ---
    val days by dietViewModel.days.collectAsState()

    // --- Today's Date ---
    val todayCalendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    val todayDate = todayCalendar.time

    // --- Find today's meals from database ---
    val todaysDay = days.find { day ->
        val dayDate = try {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(day.date)
        } catch (e: Exception) {
            Log.e("HomeScreen", "Failed to parse day.date=${day.date}", e)
            null
        }

        dayDate?.let {
            val cal = Calendar.getInstance().apply { time = it }
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            cal.time == todayDate
        } ?: false
    }

    val todaysMeals = todaysDay?.meals ?: emptyList()

    if (todaysMeals.isEmpty()) Log.d("HomeScreen", "⚠️ No meals found for today!")
    else {
        todaysMeals.forEach { meal ->
            Log.d("HomeScreen", "Meal: ${meal.type}, foods: ${meal.foods}")
        }
    }

    // --- Flatten foods for today's meals ---
    val todaysFoods = remember { mutableStateListOf<FoodItem>() }
    LaunchedEffect(todaysMeals) {
        todaysFoods.clear()
        todaysFoods.addAll(todaysMeals.flatMap { it.foods })
        Log.d("HomeScreen", "Total foods for today: ${todaysFoods.size}")
    }

    // --- Totals ---
    val totalCalories by derivedStateOf { todaysFoods.sumOf { it.calories } }
    val totalProtein by derivedStateOf { todaysFoods.sumOf { it.protein } }
    val totalCarbs by derivedStateOf { todaysFoods.sumOf { it.carbs } }
    val totalFats by derivedStateOf { todaysFoods.sumOf { it.fats } }

    // --- Greeting & Time ---
    val currentDateTime = remember {
        val cal = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEEE, MMM dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        "${dateFormat.format(cal.time)} • ${timeFormat.format(cal.time)}"
    }

    val greeting = remember {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when (hour) {
            in 5..10 -> "Ready for breakfast?"
            in 11..14 -> "Lunch time! Enjoy your meal"
            in 15..17 -> "Grab some healthy snack"
            in 18..21 -> "Keep Dinner light"
            else -> "Late night cravings?"
        }
    }

    // --- Meal Dialog State ---
    var showMealDialog by remember { mutableStateOf(false) }
    var editingFoodIndex by remember { mutableStateOf<Int?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AppHeader(title = "NutriTrack")

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                GreetingCard(
                    greeting = greeting,
                    userName = userFirstName,
                    currentDateTime = currentDateTime
                )

                CalorieProgressCard(
                    totalCalories = totalCalories,
                    calorieGoal = calorieGoal
                )

                NutrientsPreviewSection(
                    totalProtein = totalProtein,
                    totalCarbs = totalCarbs,
                    totalFats = totalFats,
                    proteinGoal = proteinGoal,
                    carbsGoal = carbsGoal,
                    fatsGoal = fatsGoal,
                    onNavigateToAllNutrients = onNavigateToAllNutrients
                )

                TodaysFoodsSection(
                    meals = todaysFoods, // FoodItem list
                    onEditMeal = { index ->
                        editingFoodIndex = index
                        showMealDialog = true
                    },
                    onDeleteMeal = { index ->
                        Log.d("HomeScreen", "Deleting food at index $index: ${todaysFoods[index]}")
                        todaysFoods.removeAt(index)
                    }
                )

                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        AddMealFab(onClick = {
            editingFoodIndex = null
            showMealDialog = true
        })
    }

    // --- Meal Dialog ---
}
