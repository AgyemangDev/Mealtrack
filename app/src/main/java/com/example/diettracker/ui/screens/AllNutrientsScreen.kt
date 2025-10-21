package com.example.diettracker.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diettracker.R
import com.example.diettracker.ui.components.cards.MealInfo

data class NutrientDetail(
    val name: String,
    val current: Int,
    val goal: Int,
    val unit: String,
    val imageRes: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllNutrientsScreen(
    meals: List<MealInfo>,
    onBackClick: () -> Unit = {}
) {
    val totalCalories = meals.sumOf { it.calories }
    val totalProtein = meals.sumOf { it.protein }
    val totalCarbs = meals.sumOf { it.carbs }
    val totalFat = meals.sumOf { it.fat }
    val totalCalcium = meals.sumOf { it.fat }
    val totalIron = meals.sumOf { it.fat }
    val totalVitamins = meals.sumOf { it.fat }


    val nutrients = listOf(
        NutrientDetail("Calories", totalCalories, 2200, "kcal", R.drawable.nuts),
        NutrientDetail("Protein", totalProtein, 150, "g", R.drawable.protein),
        NutrientDetail("Carbs", totalCarbs, 250, "g", R.drawable.carbs),
        NutrientDetail("Fats", totalFat, 70, "g", R.drawable.fats),
        NutrientDetail("Calcium", totalCalcium, 1000, "mg", R.drawable.calcium),
        NutrientDetail("Iron", totalIron, 30, "mg", R.drawable.iron),
        NutrientDetail("Vitamins", totalVitamins, 30, "g", R.drawable.fruitsandveggies)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "All Nutrients",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
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
                fontWeight = FontWeight.Bold
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