package com.example.diettracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.diettracker.data.ageRangeData
import com.example.diettracker.ui.components.lists.NutrientCardsList
import com.example.diettracker.ui.components.headers.AppHeader
import com.example.diettracker.ui.components.buttons.FinishButton
import com.example.diettracker.ui.components.cards.AgeRangeSummaryCard
import com.example.diettracker.ui.navigation.Screen

@Composable
fun DailyUsage(
    navController: NavController? = null,
    fullName: String = "",
    email: String = "",
    password: String = "",
    ageRange: String = ""
) {
    var showSuccess by remember { mutableStateOf(false) }

    // Find the age range info by matching the label
    val ageRangeInfo = ageRangeData.find { it.label == ageRange }

    // Debug logging
    LaunchedEffect(ageRange) {
        println("DEBUG: DailyUsage received ageRange = '$ageRange'")
        println("DEBUG: Available labels: ${ageRangeData.map { it.label }}")
        println("DEBUG: Match found: ${ageRangeInfo != null}")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6FAF5))
            .statusBarsPadding()
    ) {
        // Header
        if (navController != null) {
            AppHeader(
                title = "Your Daily Nutrition Plan",
                onBackClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Content or Error State
        if (ageRangeInfo != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // Summary Card
                AgeRangeSummaryCard(
                    ageRangeInfo = ageRangeInfo
                )

                // Nutrient Cards List
                NutrientCardsList(
                    ageRangeInfo = ageRangeInfo,
                    modifier = Modifier.weight(1f)
                )
            }

            // Finish Button
            if (navController != null) {
                FinishButton(
                    navController = navController,
                    fullName = fullName,
                    email = email,
                    password = password,
                    ageRangeInfo = ageRangeInfo,
                    onSuccess = { showSuccess = true }
                )
            }
        } else {
            // Error State
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No age range data found",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Received: '$ageRange'",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}