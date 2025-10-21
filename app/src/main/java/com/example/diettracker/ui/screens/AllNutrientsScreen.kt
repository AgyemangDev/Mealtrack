package com.example.diettracker.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.diettracker.R
import com.example.diettracker.data.ageRangeData
import com.example.diettracker.models.DietViewModel
import com.example.diettracker.models.UserViewModel
import com.example.diettracker.ui.components.headers.AppHeader
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

data class NutrientDetail(
    val name: String,
    val current: Int,
    val goal: Int,
    val unit: String,
    val imageRes: Int
)

@Composable
fun AllNutrientsScreen(
    navController: NavController,
    dietViewModel: DietViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    BackHandler(enabled = true) {}

    Log.d("AllNutrientsScreen", "=== AllNutrientsScreen Started ===")

    // --- User Info ---
    val user by userViewModel.user.collectAsState()
    Log.d("AllNutrientsScreen", "User: ${user?.fullName}, AgeRange: ${user?.ageRange}, UserId: ${user?.uid}")

    // --- Nutrient Goals ---
    val matchedAgeData = ageRangeData.find { it.range == user?.ageRange } ?: ageRangeData[1]
    Log.d("AllNutrientsScreen", "Matched Age Data: ${matchedAgeData.label}, Calories: ${matchedAgeData.calories}")

    val calorieGoal = matchedAgeData.calories
    val proteinGoal = matchedAgeData.nutrients.protein.value.removeSuffix("g").toInt()
    val carbsGoal = matchedAgeData.nutrients.carbohydrates.value.removeSuffix("g").toInt()
    val fatsGoal = matchedAgeData.nutrients.fats.value.removeSuffix("g").toInt()

    Log.d("AllNutrientsScreen", "Goals - Calories: $calorieGoal, Protein: ${proteinGoal}g, Carbs: ${carbsGoal}g, Fats: ${fatsGoal}g")

    // --- Observe Diet Days ---
    val days by dietViewModel.days.collectAsState()
    Log.d("AllNutrientsScreen", "Total days in database: ${days.size}")

    // --- Today's Date ---
    val todayCalendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    val todayDate = todayCalendar.time
    val todayDateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(todayDate)
    Log.d("AllNutrientsScreen", "Today's date: $todayDateString")

    // Find  meals from database
    val todaysDay = days.find { day ->
        val dayDate = try {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(day.date)
        } catch (e: Exception) {
            Log.e("AllNutrientsScreen", "Failed to parse day.date=${day.date}", e)
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

    if (todaysDay == null) {
        Log.d("AllNutrientsScreen", "⚠️ No day entry found for today!")
    } else {
        Log.d("AllNutrientsScreen", "✅ Found today's day entry with ${todaysDay.meals.size} meals")
    }

    val todaysMeals = todaysDay?.meals ?: emptyList()

    if (todaysMeals.isEmpty()) {
        Log.d("AllNutrientsScreen", "⚠️ No meals found for today!")
    } else {
        todaysMeals.forEachIndexed { index, meal ->
            Log.d("AllNutrientsScreen", "Meal $index: ${meal.type}, foods count: ${meal.foods.size}")
            meal.foods.forEachIndexed { foodIndex, food ->
                Log.d("AllNutrientsScreen", "  Food $foodIndex: ${food.name}, Cal: ${food.calories}, P: ${food.protein}g, C: ${food.carbs}g, F: ${food.fats}g")
            }
        }
    }

    //
    val todaysFoods = remember(todaysMeals) {
        todaysMeals.flatMap { it.foods }
    }
    Log.d("AllNutrientsScreen", "Total foods for today: ${todaysFoods.size}")

    // --- Calculate Totals ---
    val totalCalories = todaysFoods.sumOf { it.calories }
    val totalProtein = todaysFoods.sumOf { it.protein }
    val totalCarbs = todaysFoods.sumOf { it.carbs }
    val totalFat = todaysFoods.sumOf { it.fats }

    Log.d("AllNutrientsScreen", "Totals - Calories: $totalCalories, Protein: ${totalProtein}g, Carbs: ${totalCarbs}g, Fats: ${totalFat}g")

    // Nutrients not in DB (generate based on calories)
    val scaleFactor = if (totalCalories > 0) totalCalories.toFloat() / calorieGoal else 0f
    val totalCalcium = (1000 * scaleFactor).roundToInt()
    val totalIron = (30 * scaleFactor).roundToInt()
    val totalVitamins = (30 * scaleFactor).roundToInt()

    Log.d("AllNutrientsScreen", "Scale Factor: $scaleFactor")
    Log.d("AllNutrientsScreen", "Calculated - Calcium: ${totalCalcium}mg, Iron: ${totalIron}mg, Vitamins: ${totalVitamins}g")

    val nutrients = listOf(
        NutrientDetail("Calories", totalCalories, calorieGoal, "kcal", R.drawable.nuts),
        NutrientDetail("Protein", totalProtein, proteinGoal, "g", R.drawable.protein),
        NutrientDetail("Carbs", totalCarbs, carbsGoal, "g", R.drawable.carbs),
        NutrientDetail("Fats", totalFat, fatsGoal, "g", R.drawable.fats),
        NutrientDetail("Calcium", totalCalcium, 1000, "mg", R.drawable.calcium),
        NutrientDetail("Iron", totalIron, 30, "mg", R.drawable.iron),
        NutrientDetail("Vitamins", totalVitamins, 30, "g", R.drawable.fruitsandveggies)
    )

    Log.d("AllNutrientsScreen", "Created ${nutrients.size} nutrient detail items")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AppHeader(
                title = "All Nutrients",
                onBackClick = {
                    Log.d("AllNutrientsScreen", "Back button clicked")
                    navController.popBackStack()
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                nutrients.forEachIndexed { index, nutrient ->
                    Log.d("AllNutrientsScreen", "Rendering nutrient card $index: ${nutrient.name}")
                    NutrientDetailCard(nutrient = nutrient)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    Log.d("AllNutrientsScreen", "=== AllNutrientsScreen Composition Complete ===")
}

@Composable
fun NutrientDetailCard(nutrient: NutrientDetail) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = nutrient.imageRes),
                    contentDescription = nutrient.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = nutrient.name,
                fontSize = 20.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${nutrient.current}${nutrient.unit} / ${nutrient.goal}${nutrient.unit}",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            val progress = if (nutrient.goal > 0) {
                nutrient.current.toFloat() / nutrient.goal.toFloat()
            } else {
                0f
            }

            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = getNutrientColor(nutrient.name),
                trackColor = Color(0xFFE0E0E0),
            )
        }
    }
}

fun getNutrientColor(nutrientName: String): Color {
    return when (nutrientName.lowercase()) {
        "calories" -> Color(0xFFFFA726)
        "protein" -> Color(0xFFEF5350)
        "carbs" -> Color(0xFFFFEE58)
        "fats" -> Color(0xFF66BB6A)
        "calcium" -> Color(0xFF42A5F5)
        "iron" -> Color(0xFFAB47BC)
        "vitamins" -> Color(0xFF4CAF50)
        else -> Color(0xFF4CAF50)
    }
}