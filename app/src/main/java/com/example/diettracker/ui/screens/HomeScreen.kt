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
import com.example.diettracker.data.DietRepository
import com.example.diettracker.models.DietViewModel
import com.example.diettracker.models.FoodItem
import com.example.diettracker.models.UserViewModel
import com.example.diettracker.ui.components.buttons.AddMealFab
import com.example.diettracker.ui.components.cards.*
import com.example.diettracker.ui.components.dialogs.MealDialog
import com.example.diettracker.ui.components.headers.AppHeader
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
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

    val auth = remember { FirebaseAuth.getInstance() }
    val userId = auth.currentUser?.uid

    val user by userViewModel.user.collectAsState()
    val userFirstName =
        user?.fullName?.split(" ")?.firstOrNull()?.replaceFirstChar { it.uppercase() } ?: "User"

    val matchedAgeData = ageRangeData.find { it.range == user?.ageRange } ?: ageRangeData[1]
    val calorieGoal = matchedAgeData.calories
    val proteinGoal = matchedAgeData.nutrients.protein.value.removeSuffix("g").toInt()
    val carbsGoal = matchedAgeData.nutrients.carbohydrates.value.removeSuffix("g").toInt()
    val fatsGoal = matchedAgeData.nutrients.fats.value.removeSuffix("g").toInt()

    val todayDateString = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    // Get meals from DietViewModel which loads from DietRepository
    val days by dietViewModel.days.collectAsState()

    val todaysFoods = remember(days) {
        val todaysDay = days.find { it.date == todayDateString }
        todaysDay?.meals?.flatMap { it.foods } ?: emptyList()
    }

    val totalCalories by derivedStateOf { todaysFoods.sumOf { it.calories } }
    val totalProtein by derivedStateOf { todaysFoods.sumOf { it.protein } }
    val totalCarbs by derivedStateOf { todaysFoods.sumOf { it.carbs } }
    val totalFats by derivedStateOf { todaysFoods.sumOf { it.fats } }

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

    var showMealDialog by remember { mutableStateOf(false) }
    var editingFoodIndex by remember { mutableStateOf<Int?>(null) }
    val scope = rememberCoroutineScope()

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
                    meals = todaysFoods,
                    onEditMeal = { index ->
                        editingFoodIndex = index
                        showMealDialog = true
                    },
                    onDeleteMeal = { index ->
                        if (userId != null) {
                            val foodToDelete = todaysFoods[index]
                            Log.d("HomeScreen", "Deleting food: ${foodToDelete.name}")

                            scope.launch {
                                try {
                                    DietRepository.deleteFoodForUser(userId, foodToDelete)

                                    Log.d("HomeScreen", "Successfully deleted from Firebase")
                                } catch (e: Exception) {
                                    Log.e("HomeScreen", "Error deleting from Firebase", e)
                                }
                            }
                        }
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

    if (showMealDialog) {
        val editingMeal = editingFoodIndex?.let { index ->
            val food = todaysFoods[index]
            MealInfo(
                name = food.name,
                servings = 1,
                calories = food.calories,
                protein = food.protein,
                carbs = food.carbs,
                fat = food.fats,
                iron = 0.0,
                calcium = 0.0,
                vitamins = 0.0
            )
        }

        MealDialog(
            meal = editingMeal,
            onDismiss = {
                showMealDialog = false
                editingFoodIndex = null
            },
            onSave = { mealInfo ->
                if (userId != null) {
                    val newFood = FoodItem(
                        name = mealInfo.name,
                        calories = mealInfo.calories,
                        protein = mealInfo.protein,
                        carbs = mealInfo.carbs,
                        fats = mealInfo.fat
                    )

                    scope.launch {
                        try {
                            if (editingFoodIndex != null) {
                                // Delete old food
                                val oldFood = todaysFoods[editingFoodIndex!!]
                                DietRepository.deleteFoodForUser(userId, oldFood)
                            }

                            // Add new/updated food
                            DietRepository.addFoodForUser(userId, newFood)


                            Log.d("HomeScreen", "Successfully saved to Firebase")
                        } catch (e: Exception) {
                            Log.e("HomeScreen", "Error saving to Firebase", e)
                        }
                    }

                    showMealDialog = false
                    editingFoodIndex = null
                }
            }
        )
    }
}