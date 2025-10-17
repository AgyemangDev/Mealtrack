package com.example.diettracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.diettracker.data.ageRangeData
import com.example.diettracker.ui.components.buttons.CustomButton
import com.example.diettracker.ui.components.cards.NutrientCard
import com.example.diettracker.ui.components.headers.AppHeader
import com.example.diettracker.ui.navigation.Screen

@Composable
fun DailyUsage(
    navController: NavController? = null,
    fullName: String = "",
    email: String = "",
    password: String = "",
    ageRange: String = ""
) {
    var isLoading by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }

    // Find the age range info by matching the label
    // The ageRange parameter should already be decoded by the NavHost
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
        if (navController != null) {
            AppHeader(
                title = "Your Daily Nutrition Plan",
                onBackClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (ageRangeInfo != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Age Range and Total Calories Summary
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${ageRangeInfo.label} • Total: ${ageRangeInfo.calories} kcal",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF4A7C59),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Protein Card
                item {
                    NutrientCard(
                        title = "Protein",
                        nutrientDetail = ageRangeInfo.nutrients.protein,
                        backgroundColor = Color(0xFFF0F4FF)
                    )
                }

                // Carbohydrates Card
                item {
                    NutrientCard(
                        title = "Carbohydrates",
                        nutrientDetail = ageRangeInfo.nutrients.carbohydrates,
                        backgroundColor = Color(0xFFE8F5E9)
                    )
                }

                // Fats Card
                item {
                    NutrientCard(
                        title = "Fats",
                        nutrientDetail = ageRangeInfo.nutrients.fats,
                        backgroundColor = Color(0xFFF0F9FF)
                    )
                }

                // Vitamins & Minerals Card
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

            // Finish Button at Bottom
            if (navController != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    CustomButton(
                        text = if (isLoading) "Creating Account..." else "Finish",
                        onClick = {
                            isLoading = true
                            // TODO: Call your API endpoint here with:
                            // - fullName
                            // - email
                            // - password
                            // - ageRangeInfo
                            // Example:
                            // apiService.createUser(
                            //     fullName = fullName,
                            //     email = email,
                            //     password = password,
                            //     ageRange = ageRangeInfo.label,
                            //     calories = ageRangeInfo.calories
                            // ).onSuccess {
                            //     navController.navigate(Screen.Home.route) {
                            //         popUpTo(Screen.Register.route) { inclusive = true }
                            //     }
                            // }.onFailure {
                            //     isLoading = false
                            // }
                            showSuccess = true
                            isLoading = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    )
                }
            }
        } else {
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

    // Success Dialog
    if (showSuccess) {
        AlertDialog(
            onDismissRequest = { },
            title = {
                Text(
                    text = "Account Created",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("Your account has been created successfully!")
            },
            confirmButton = {
                Button(
                    onClick = {
                        navController?.navigate(Screen.Home.route) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4A7C59)
                    )
                ) {
                    Text("Go to Home")
                }
            }
        )
    }
}