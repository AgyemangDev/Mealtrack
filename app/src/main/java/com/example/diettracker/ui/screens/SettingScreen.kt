package com.example.diettracker.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.diettracker.data.ageRangeData
import com.example.diettracker.models.UserViewModel
import com.example.diettracker.ui.components.cards.ProfileCard
import com.example.diettracker.ui.components.buttons.LogoutButton
import com.example.diettracker.ui.components.dialogs.EditProfileDialog
import com.example.diettracker.ui.components.dialogs.LogoutDialog
import com.example.diettracker.viewmodel.SettingsViewModel
import androidx.navigation.NavController
import com.example.diettracker.ui.navigation.Screen

@Composable
fun SettingScreen(
    userViewModel: UserViewModel = viewModel(),
    navController: NavController
) {
    BackHandler(enabled = true) {}
    val settingsViewModel: SettingsViewModel = viewModel()
    val user by userViewModel.user.collectAsState()

    // --- Full Name ---
    val userFullName = user?.fullName?.split(" ")?.joinToString(" ") {
        it.replaceFirstChar { c -> c.uppercase() }
    } ?: "User"
    val userEmail = user?.email ?: "email@example.com"

    // --- Match age range data ---
    val matchedAgeData = ageRangeData.find { it.range == user?.ageRange } ?: ageRangeData[1]
    val calorieGoal = matchedAgeData.calories
    val proteinGoal = matchedAgeData.nutrients.protein.value.removeSuffix("g").toInt()
    val carbsGoal = matchedAgeData.nutrients.carbohydrates.value.removeSuffix("g").toInt()
    val fatsGoal = matchedAgeData.nutrients.fats.value.removeSuffix("g").toInt()

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // --- Profile Card ---
            ProfileCard(
                userName = userFullName,
                userEmail = userEmail,
                calorieGoal = calorieGoal,
                proteinGoal = proteinGoal,
                carbsGoal = carbsGoal,
                fatGoal = fatsGoal,
                userAgeRange = user?.ageRange ?: "18-25",
                userViewModel = userViewModel,
                onEditClick = { showEditProfileDialog = true }
            )

            Spacer(modifier = Modifier.height(32.dp))


            LogoutButton(onLogoutClick = { showLogoutDialog = true })
        }
    }

    // --- Dialogs ---
    if (showEditProfileDialog) {
        EditProfileDialog(
            currentName = userFullName,
            onDismiss = { showEditProfileDialog = false },
            onSave = { name ->
                settingsViewModel.updateProfile(name, userEmail)
                showEditProfileDialog = false
            }
        )
    }

    if (showLogoutDialog) {
        LogoutDialog(
            onDismiss = { showLogoutDialog = false },
            onLogoutComplete = {
                showLogoutDialog = false
                navController.navigate(Screen.Welcome.route) {

                    popUpTo(0) { inclusive = true } // clears the stack
                }
            }
        )
    }
}
