package com.example.diettracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.diettracker.data.ageRangeData
import com.example.diettracker.ui.components.buttons.CustomButton
import com.example.diettracker.ui.components.cards.AgeRangeCard
import com.example.diettracker.ui.components.dialogs.AppAlertDialog
import com.example.diettracker.ui.components.headers.AppHeader
import com.example.diettracker.ui.navigation.Screen


@Composable
fun AgeRange(
    navController: NavController,
    fullName: String,
    email: String,
    password: String
) {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var showAlert by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .background(Color(0xFFF6FAF5)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AppHeader(
            title = "Select Your Age Range",
            onBackClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        ageRangeData.forEachIndexed { index, range ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                AgeRangeCard(
                    data = range,
                    isSelected = selectedIndex == index,
                    onClick = { selectedIndex = index }
                )
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        CustomButton(
            text = "Continue",
            onClick = {
                if (selectedIndex == null) {
                    showAlert = true
                } else {
                    val selectedAgeRangeLabel = ageRangeData[selectedIndex!!].label

                    println("DEBUG: Navigating with:")
                    println("  fullName = '$fullName'")
                    println("  email = '$email'")
                    println("  password = '$password'")
                    println("  ageRange = '$selectedAgeRangeLabel'")

                    // Pass parameters directly - let the NavHost handle encoding
                    // Don't encode here!
                    navController.navigate(
                        Screen.DailyUsage.createRoute(fullName, email, password, selectedAgeRangeLabel)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(vertical = 20.dp)
        )
    }

    if (showAlert) {
        AppAlertDialog(
            title = "Selection Required",
            message = "Please select your age range before continuing.",
            showDialog = showAlert,
            onDismiss = { showAlert = false }
        )
    }
}
