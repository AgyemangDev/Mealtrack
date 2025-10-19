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

// Data class for nutrient details
data class NutrientDetail(
    val name: String,
    val current: Int,
    val goal: Int,
    val unit: String,
    val imageRes: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllNutrientsScreen(onBackClick: () -> Unit = {}) {
    val nutrients = listOf(
        NutrientDetail("Calories", 1850, 2200, "kcal", R.drawable.ic_launcher_foreground),
        NutrientDetail("Protein", 120, 150, "g", R.drawable.ic_launcher_foreground),
        NutrientDetail("Carbs", 180, 250, "g", R.drawable.ic_launcher_foreground),
        NutrientDetail("Fats", 55, 70, "g", R.drawable.ic_launcher_foreground),
        NutrientDetail("Calcium", 800, 1000, "mg", R.drawable.ic_launcher_foreground),
        NutrientDetail("Fiber", 20, 30, "g", R.drawable.ic_launcher_foreground)
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
            // Nutrient Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {

            }

            Spacer(modifier = Modifier.height(12.dp))

            // Nutrient Name
            Text(
                text = nutrient.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Current / Goal
            Text(
                text = "${nutrient.current}${nutrient.unit} / ${nutrient.goal}${nutrient.unit}",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Progress Bar
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
        "fiber" -> Color(0xFFAB47BC)
        else -> Color(0xFF4CAF50)
    }
}