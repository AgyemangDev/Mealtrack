package com.example.diettracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.diettracker.models.FoodItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllFoodsScreen(navController: NavHostController) {
    val foodItemsByDate = mapOf(
        "Today" to listOf(
            FoodItem(name = "Apple", unit = "1 unit", calories = 95, protein = 0, carbs = 25, fats = 0)
        ),
        "October 18, 2023" to listOf(
            FoodItem(name = "Lentil Soup", unit = "350ml", calories = 320, protein = 18, carbs = 40, fats = 8),
            FoodItem(name = "Avocado Toast", unit = "1 slice", calories = 280, protein = 7, carbs = 30, fats = 15)
        )
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("All Foods", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
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
                        modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
                    )
                }
                items(foods) { food ->
                    FoodItemCard(food = food)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun FoodItemCard(food: FoodItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
            Text(text = food.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = food.unit ?: "", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))

            Row(Modifier.fillMaxWidth()) {
                NutrientInfo(name = "Calories", value = food.calories, unit = "kcal", modifier = Modifier.weight(1f))
                NutrientInfo(name = "Protein", value = food.protein, unit = "g", modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth()) {
                NutrientInfo(name = "Carbs", value = food.carbs, unit = "g", modifier = Modifier.weight(1f))
                NutrientInfo(name = "Fats", value = food.fats, unit = "g", modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { /* TODO: Implement edit */ },
                    modifier = Modifier.size(36.dp).background(Color(0xFFE8F5E9), CircleShape)
                ) {
                    Icon(Icons.Outlined.Edit, "Edit", tint = Color(0xFF4CAF50))
                }
                Spacer(modifier = Modifier.width(16.dp))
                IconButton(
                    onClick = { /* TODO: Implement delete */ },
                    modifier = Modifier.size(36.dp).background(Color(0xFFFFEBEE), CircleShape)
                ) {
                    Icon(Icons.Outlined.Delete, "Delete", tint = Color(0xFFF44336))
                }
            }
        }
    }
}

@Composable
fun NutrientInfo(name: String, value: Int, unit: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(name, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)) {
                    append(value.toString())
                }
                withStyle(style = SpanStyle(fontSize = 14.sp, color = Color.Gray)) {
                    append(" $unit")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AllFoodsScreenPreview() {
    AllFoodsScreen(navController = rememberNavController())
}
