package com.example.diettracker.ui.components.lists

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.diettracker.data.AgeRangeInfo
import com.example.diettracker.ui.components.cards.NutrientCard

@Composable
fun NutrientCardsList(
    ageRangeInfo: AgeRangeInfo,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            NutrientCard(
                title = "Protein",
                nutrientDetail = ageRangeInfo.nutrients.protein,
                backgroundColor = Color(0xFFF0F4FF)
            )
        }

        item {
            NutrientCard(
                title = "Carbohydrates",
                nutrientDetail = ageRangeInfo.nutrients.carbohydrates,
                backgroundColor = Color(0xFFE8F5E9)
            )
        }

        item {
            NutrientCard(
                title = "Fats",
                nutrientDetail = ageRangeInfo.nutrients.fats,
                backgroundColor = Color(0xFFF0F9FF)
            )
        }

        item {
            NutrientCard(
                title = "Vitamins & Minerals",
                nutrientDetail = ageRangeInfo.nutrients.vitaminsAndMinerals,
                backgroundColor = Color(0xFFFFF3E0)
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}