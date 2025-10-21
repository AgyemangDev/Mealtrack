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
import com.example.diettracker.models.UserViewModel
import com.example.diettracker.ui.components.cards.MealInfo
import com.example.diettracker.ui.components.headers.AppHeader

data class NutrientDetail(
    val name: String,
    val current: Int,
    val goal: Int,
    val unit: String,
    val imageRes: Int
)

@Composable
fun AllNutrientsScreen(
    meals: List<MealInfo>,
    navController: NavController,
    userViewModel: UserViewModel = viewModel()
) {
    // Disable swipe back gesture
    BackHandler(enabled = true) { /* Do nothing */ }

    // Observe user info
    val user by userViewModel.user.collectAsState()

    val matchedAgeData = ageRangeData.find { it.range == user?.ageRange } ?: ageRangeData[1]
    Log.d("AllNutrientsScreen", "User: ${user?.fullName}, AgeRange: ${user?.ageRange}")
    Log.d("AllNutrientsScreen", "Matched Age Data: ${matchedAgeData.label}, Calories: ${matchedAgeData.calories}")

    // Totals from meals
    val totalCalories = meals.sumOf { it.calories }
    val totalProtein = meals.sumOf { it.protein }
    val totalCarbs = meals.sumOf { it.carbs }
    val totalFat = meals.sumOf { it.fat }
    val totalCalcium = meals.sumOf { it.calcium.toInt() }
    val totalIron = meals.sumOf { it.iron.toInt() }
    val totalVitamins = meals.sumOf { it.vitamins.toInt() }

    // Goals from ageRangeData
    val calorieGoal = matchedAgeData.calories
    val proteinGoal = matchedAgeData.nutrients.protein.value.removeSuffix("g").toInt()
    val carbsGoal = matchedAgeData.nutrients.carbohydrates.value.removeSuffix("g").toInt()
    val fatsGoal = matchedAgeData.nutrients.fats.value.removeSuffix("g").toInt()

    val nutrients = listOf(
        NutrientDetail("Calories", totalCalories, calorieGoal, "kcal", R.drawable.nuts),
        NutrientDetail("Protein", totalProtein, proteinGoal, "g", R.drawable.protein),
        NutrientDetail("Carbs", totalCarbs, carbsGoal, "g", R.drawable.carbs),
        NutrientDetail("Fats", totalFat, fatsGoal, "g", R.drawable.fats),
        NutrientDetail("Calcium", totalCalcium, 1000, "mg", R.drawable.calcium),
        NutrientDetail("Iron", totalIron, 30, "mg", R.drawable.iron),
        NutrientDetail("Vitamins", totalVitamins, 30, "g", R.drawable.fruitsandveggies)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Sticky header with back button
            AppHeader(
                title = "All Nutrients",
                onBackClick = { navController.popBackStack() }
            )

            // Scrollable content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                nutrients.forEach { nutrient ->
                    NutrientDetailCard(nutrient = nutrient)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
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

            val progress = nutrient.current.toFloat() / nutrient.goal.toFloat()
            LinearProgressIndicator(
                progress = progress.coerceIn(0f, 1f),
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
