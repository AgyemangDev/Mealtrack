package com.example.diettracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.diettracker.ui.components.cards.ProfileCard
import com.example.diettracker.ui.components.buttons.LogoutButton
import com.example.diettracker.ui.components.dialogs.LogoutDialog
import com.example.diettracker.ui.components.dialogs.EditProfileDialog
import com.example.diettracker.ui.components.sections.SettingsSection
import com.example.diettracker.ui.components.lists.SettingsItem
import com.example.diettracker.ui.components.lists.SettingsSwitchItem
import com.example.diettracker.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    settingsViewModel: SettingsViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(), // ✅ Add UserViewModel
    onLogoutComplete: () -> Unit = {}
) {
    // Observe the full user object
    val user by userViewModel.user.collectAsState()

    // Get full name like in HomeScreen
    val userFullName = user?.fullName?.split(" ")?.joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
        ?: "User"

    val userEmail = user?.email ?: "email@example.com" // fallback
    val isLoggingOut by settingsViewModel.isLoggingOut.collectAsState()

    // Dialog states
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showGoalsDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            ProfileCard(
                userName = userFullName, // ✅ Use full name
                userEmail = userEmail,
                onEditClick = { showEditProfileDialog = true }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    DialogHandlers(
        showEditProfileDialog = showEditProfileDialog,
        showGoalsDialog = showGoalsDialog,
        showLogoutDialog = showLogoutDialog,
        userName = userFullName, // ✅ Use full name here too
        userEmail = userEmail,
        isLoggingOut = isLoggingOut,
        settingsViewModel = settingsViewModel,
        onDismissEditProfile = { showEditProfileDialog = false },
        onDismissGoals = { showGoalsDialog = false },
        onDismissLogout = { showLogoutDialog = false },
        onLogoutComplete = onLogoutComplete,
        snackbarHostState = snackbarHostState
    )
}

@Composable
private fun GoalsSection(
    calorieGoal: Int,
    proteinGoal: Int,
    carbsGoal: Int,
    fatGoal: Int,
    onGoalsClick: () -> Unit
) {
    SettingsSection(title = "Goals") {
        SettingsItem(
            icon = Icons.Default.Info,
            title = "Daily Nutrition Goals",
            subtitle = "$calorieGoal kcal • ${proteinGoal}g protein • ${carbsGoal}g carbs • ${fatGoal}g fat",
            onClick = onGoalsClick
        )
    }
}

@Composable
private fun PreferencesSection(
    unitSystem: String,
    onUnitSystemClick: () -> Unit
) {
    SettingsSection(title = "Preferences") {
        SettingsItem(
            icon = Icons.Default.Speed,
            title = "Unit System",
            subtitle = unitSystem,
            onClick = onUnitSystemClick
        )
    }
}

@Composable
private fun DialogHandlers(
    showEditProfileDialog: Boolean,
    showGoalsDialog: Boolean,
    showLogoutDialog: Boolean,
    userName: String,
    userEmail: String,
    calorieGoal: Int,
    proteinGoal: Int,
    carbsGoal: Int,
    fatGoal: Int,
    isLoggingOut: Boolean,
    settingsViewModel: SettingsViewModel,
    onDismissEditProfile: () -> Unit,
    onDismissGoals: () -> Unit,
    onDismissLogout: () -> Unit,
    onLogoutComplete: () -> Unit,
    snackbarHostState: SnackbarHostState,
    scope: kotlinx.coroutines.CoroutineScope
) {
    if (showEditProfileDialog) {
        EditProfileDialog(
            currentName = userName,
            onDismiss = onDismissEditProfile,
            onSave = { name ->
                settingsViewModel.updateProfile(name, userEmail)
                onDismissEditProfile()
            }
        )
    }


    if (showLogoutDialog) {
        LogoutDialog(
            isLoggingOut = isLoggingOut,
            onDismiss = onDismissLogout,
            onConfirm = {
                scope.launch {
                    try {
                        settingsViewModel.logoutSuspend()
                        onDismissLogout()
                        onLogoutComplete()
                    } catch (e: Exception) {
                        snackbarHostState.showSnackbar("Logout failed: ${e.message}")
                        onDismissLogout()
                    }
                }
            }
        )
    }
}